package smarthome.raspberry.controllers_api.domain

import smarthome.library.common.BaseController
import smarthome.library.common.Id

interface GetControllerByIdUseCase {
    suspend fun execute(id: Id): BaseController
}