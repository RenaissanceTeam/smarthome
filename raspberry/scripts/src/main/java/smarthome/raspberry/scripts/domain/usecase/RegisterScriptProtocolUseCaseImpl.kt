package smarthome.raspberry.scripts.domain.usecase

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import org.springframework.stereotype.Component
import smarthome.raspberry.entity.script.*
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
        private val actionRunners: Map<String, ActionRunner>
) : RegisterScriptProtocolUseCase {
    private val disposable = CompositeDisposable()

    override fun execute(script: Script): Disposable {
        val topDependencies = findTopDependencies(script)

        topDependencies.map { dependency ->
            val validators = dependency.conditions.map { condition ->
                val validator = conditionValidators[composeValidatorName(condition)]
                        ?: throw IllegalStateException("No condition validator for $condition")

                condition to validator
            }

            val runners = dependency.actions.map { action ->
                val runner = actionRunners[composeRunnerName(action)]
                        ?: throw IllegalStateException("No action runner for $action")

                action to runner
            }.toMap()

            val blockObserver = blockObservers[composeObserverName(dependency.start)]
                    ?: throw IllegalStateException("No block observer for ${dependency.start}")

            dependency.start.id.let(blockObserver::execute)
                    .map { state -> validators.all { it.second.validate(it.first, state) } }
                    .distinct()
                    .filter { it }
                    .doOnNext {
                        dependency.actions.forEach { action ->
                            runners[action]?.runCatching { runAction(action, dependency.end.id) }
                        }
                    }
        }.map {
            it.subscribe({}, {})
        }.forEach { disposable.add(it) }

        return disposable
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
