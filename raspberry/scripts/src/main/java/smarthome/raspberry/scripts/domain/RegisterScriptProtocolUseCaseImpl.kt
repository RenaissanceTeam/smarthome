package smarthome.raspberry.scripts.domain

import io.reactivex.disposables.CompositeDisposable
import org.springframework.stereotype.Component
import smarthome.raspberry.entity.script.Block
import smarthome.raspberry.entity.script.Dependency
import smarthome.raspberry.entity.script.Script
import smarthome.raspberry.scripts.api.domain.ConditionValidator
import smarthome.raspberry.scripts.api.domain.ObserveBlockStatesUseCase
import smarthome.raspberry.scripts.api.domain.RunScriptActionUseCase
import smarthome.raspberry.scripts.api.domain.RegisterScriptProtocolUseCase

@Component
class RegisterScriptProtocolUseCaseImpl(
        private val observeBlockStatesUseCase: ObserveBlockStatesUseCase,
        private val conditionValidators: Map<String, ConditionValidator>,
        private val runScriptActionUseCase: RunScriptActionUseCase

) : RegisterScriptProtocolUseCase {
    private val disposable = CompositeDisposable()

    override fun execute(script: Script) {
        val topDependencies = findTopDependencies(script)

        topDependencies.map { dependency ->
            val validators = dependency.conditions.map { condition ->
                val validator = conditionValidators["${condition::class.simpleName!!}Validator"]
                        ?: throw IllegalStateException("No condition validator for $dependency")

                condition to validator
            }

            dependency.start.id.let(observeBlockStatesUseCase::execute)
                    .map { block -> validators.all { it.second.validate(it.first, block) } }
                    .distinct()
                    .doOnNext { if (it) dependency.actions.forEach { runScriptActionUseCase.execute(dependency.end, it) } }
        }.forEach { it.subscribe() }

    }

    private fun findTopDependencies(script: Script): List<Dependency> {
        val topBlocks = script.blocks.filter { !script.dependencies.map { it.end }.contains(it) }
        return topBlocks.flatMap { findBlockDependencies(script, it) }
    }

    private fun findBlockDependencies(script: Script, block: Block): List<Dependency> {
        return script.dependencies.filter { it.start == block }
    }
}
