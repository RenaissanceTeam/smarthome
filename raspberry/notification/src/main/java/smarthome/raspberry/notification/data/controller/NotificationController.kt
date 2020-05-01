package smarthome.raspberry.notification.data.controller

import org.springframework.web.bind.annotation.*
import smarthome.raspberry.authentication.api.domain.usecase.GetCallingUserUseCase
import smarthome.raspberry.notification.NotificationTokenRepository
import smarthome.raspberry.notification.api.domain.usecase.GetNotificationTokenForUserUseCase
import smarthome.raspberry.notification.api.domain.usecase.SaveNotificationTokenUseCase
import smarthome.raspberry.notification.data.dto.TokenDto

@RestController
@RequestMapping("api/")
class NotificationController(
        private val saveNotificationTokenUseCase: SaveNotificationTokenUseCase,
        private val getNotificationTokenForUserUseCase: GetNotificationTokenForUserUseCase,
        private val getCallingUserUseCase: GetCallingUserUseCase

) {

    @PostMapping("notifications")
    fun save(@RequestBody tokenDto: TokenDto) {
        saveNotificationTokenUseCase.execute(tokenDto.token)
    }

    @GetMapping("notifications/token")
    fun getToken(): TokenDto {

        return TokenDto(getNotificationTokenForUserUseCase.execute(getCallingUserUseCase.execute()).token)
    }
}