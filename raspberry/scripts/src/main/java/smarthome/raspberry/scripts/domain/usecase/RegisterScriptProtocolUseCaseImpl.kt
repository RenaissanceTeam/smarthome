package smarthome.raspberry.scripts.domain.usecase

import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import org.springframework.stereotype.Component
import smarthome.raspberry.entity.script.*
import smarthome.raspberry.scripts.api.data.RegisteredProtocolsRepository
import smarthome.raspberry.scripts.api.domain.ActionRunner
import smarthome.raspberry.scripts.api.domain.BlockObserver
import smarthome.raspberry.scripts.api.domain.ConditionValidator
import smarthome.raspberry.scripts.api.domain.usecase.RegisterScriptProtocolUseCase
import javax.transaction.Transactional

@Component
@Transactional
open class RegisterScriptProtocolUseCaseImpl(
        private val blockObservers: Map<String, BlockObserver<*>>,
        private val conditionValidators: Map<String, ConditionValidator>,
        private val actionRunners: Map<String, ActionRunner>,
        private val repo: RegisteredProtocolsRepository
) : RegisterScriptProtocolUseCase {

    override fun execute(script: Script) {
        if (repo.isRegistered(script.id)) repo.unregister(script.id)

        val disposable = CompositeDisposable()

        findTopDependencies(script)
                .map { dependency ->
                    val blockObserver = blockObservers[composeObserverName(dependency.start)]
                            ?: throw IllegalStateException("No block observer for ${dependency.start}")

                    runActionOnValidCondition(dependency, blockObserver)
                }
                .map { it.subscribe({}, {}) }
                .forEach { disposable.add(it) }


        repo.register(script.id, disposable)
    }

    private fun runActionOnValidCondition(
            dependency: Dependency,
            blockObserver: BlockObserver<*>
    ): Observable<Boolean> {
        return dependency.start.id.let(blockObserver::execute)
                .distinct()
                .map { state -> findValidators(dependency).entries.all { it.value.validate(it.key, state) } }
                .filter { it }
                .doOnNext {
                    dependency.actions.forEach { action ->
                        findActionRunners(dependency)[action]
                                ?.runCatching { runAction(action, dependency.end.id) }
                    }
                }
    }

    private fun findActionRunners(dependency: Dependency): Map<Action, ActionRunner> {
        return dependency.actions.map { action ->
            val runner = actionRunners[composeRunnerName(action)]
                    ?: throw IllegalStateException("No action runner for $action")

            action to runner
        }.toMap()
    }

    private fun findValidators(dependency: Dependency): Map<Condition, ConditionValidator> {
        return dependency.conditions.map { condition ->
            val validator = conditionValidators[composeValidatorName(condition)]
                    ?: throw IllegalStateException("No condition validator for $condition")

            condition to validator
        }.toMap()
    }

    private fun composeObserverName(block: Block) = "${block::class.simpleName!!.decapitalize()}Observer"
    private fun composeValidatorName(condition: Condition) = "${condition::class.simpleName!!.decapitalize()}Validator"
    private fun composeRunnerName(action: Action) = "${action::class.simpleName!!.decapitalize()}Runner"

    private fun findTopDependencies(script: Script): List<Dependency> {
        val topBlocks = script.blocks
                .filter { !script.dependencies.map { it.end.id }.contains(it.id) }
        return topBlocks.flatMap { findBlockDependencies(script, it) }
    }

    private fun findBlockDependencies(script: Script, block: Block): List<Dependency> {
        return script.dependencies.filter { it.start.id == block.id }
    }
}
