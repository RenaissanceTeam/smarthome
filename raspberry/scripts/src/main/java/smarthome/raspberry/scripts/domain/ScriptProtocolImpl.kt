package smarthome.raspberry.scripts.domain

import io.reactivex.disposables.CompositeDisposable
import smarthome.raspberry.entity.script.Dependency
import smarthome.raspberry.entity.script.Script
import smarthome.raspberry.scripts.api.domain.ConditionValidator
import smarthome.raspberry.scripts.api.domain.ObserveBlockStatesUseCase
import smarthome.raspberry.scripts.api.domain.RunScriptActionUseCase
import smarthome.raspberry.scripts.api.domain.ScriptProtocol

class ScriptProtocolImpl(
        private val observeBlockStatesUseCase: ObserveBlockStatesUseCase,
        private val conditionValidators: Map<String, ConditionValidator>,
        private val runScriptActionUseCase: RunScriptActionUseCase

) : ScriptProtocol {
    private val disposable = CompositeDisposable()

    override fun register(script: Script) {
        val topDependencies = findTopDependencies()

        val s = topDependencies.map { dependency ->
            val validators = dependency.conditions.map { condition ->
                val validator = conditionValidators[dependency::class.simpleName!!]
                        ?: throw IllegalStateException("No condition validator for $dependency")

                condition to validator
            }

            dependency.start.id.let(observeBlockStatesUseCase::execute)
                    .map { block -> validators.all { it.second.validate(it.first, block) } }
                    .distinct()
                    .doOnNext { if (it) dependency.actions.forEach { runScriptActionUseCase.execute(dependency.end, it) } }
        }

    }

    private fun findTopDependencies(): List<Dependency> {
        TODO()
    }
}
