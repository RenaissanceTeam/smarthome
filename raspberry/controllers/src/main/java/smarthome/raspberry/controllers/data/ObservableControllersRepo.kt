package smarthome.raspberry.controllers.data

import org.springframework.stereotype.Component
import smarthome.raspberry.core.ObservableJpaRepository
import smarthome.raspberry.entity.controller.Controller

@Component
class ObservableControllersRepo(jpa: ControllersRepo) : ObservableJpaRepository<Controller, Long>(jpa) {
    override fun getEntityId(entity: Controller) = entity.id
    fun findById(id: Long) = jpaRepository.findById(id)
}
