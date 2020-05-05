package smarthome.client.presentation.scripts.setup.dependency.condition.location

import com.airbnb.epoxy.EpoxyModel
import smarthome.client.domain.api.scripts.blocks.location.HomeGeofenceCondition
import smarthome.client.domain.api.scripts.usecases.dependency.ChangeSetupDependencyConditionUseCase
import smarthome.client.entity.script.dependency.condition.Condition
import smarthome.client.presentation.scripts.resolver.ConditionModelResolver

class LocationConditionResolver(
        private val changeSetupDependencyConditionUseCase: ChangeSetupDependencyConditionUseCase
) : ConditionModelResolver {
    override fun canResolve(item: Condition): Boolean {
        return item is HomeGeofenceCondition
    }

    override fun resolve(item: Condition): EpoxyModel<*> {
        return when (item) {
            is HomeGeofenceCondition -> HomeGeofenceConditionViewModel_().apply {
                id(item.id.hashCode())

                inside(item.inside)
                onChangeInside { newInside ->
                    changeSetupDependencyConditionUseCase.execute(item.id) {
                        (it as? HomeGeofenceCondition?)?.copy(inside = newInside) ?: it
                    }
                }
            }
            else -> throw IllegalArgumentException("Can't create model for $item")
        }
    }
}