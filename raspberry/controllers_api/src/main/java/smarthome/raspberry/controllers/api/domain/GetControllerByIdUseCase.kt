package smarthome.raspberry.controllers.api.domain

import smarthome.library.common.BaseController
import smarthome.library.common.Id

interface GetControllerByIdUseCase {
    suspend fun execute(id: Id): BaseController
}