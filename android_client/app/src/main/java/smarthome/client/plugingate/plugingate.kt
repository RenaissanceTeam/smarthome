package smarthome.client.plugingate

import org.koin.core.qualifier.named
import org.koin.dsl.module
import smarthome.client.arduino.scripts.entity.action.ArduinoActionFromBlockResolver
import smarthome.client.arduino.scripts.entity.block.ArduinoBlockNameResolver
import smarthome.client.arduino.scripts.entity.block.ArduinoControllerBlockResolver
import smarthome.client.arduino.scripts.entity.condition.ArduinoConditionFromBlockResolver
import smarthome.client.arduino.scripts.presentation.ArduinoActionModelResolver
import smarthome.client.arduino.scripts.presentation.ArduinoConditionModelResolver
import smarthome.client.domain.api.scripts.resolver.ActionFromBlockResolver
import smarthome.client.domain.api.scripts.resolver.BlockNameResolver
import smarthome.client.domain.api.scripts.resolver.ConditionFromBlockResolver
import smarthome.client.domain.api.scripts.resolver.ControllerBlockResolver
import smarthome.client.domain.scripts.blocks.notification.NotificationActionFromBlockResolver
import smarthome.client.presentation.scripts.resolver.ActionModelResolver
import smarthome.client.presentation.scripts.resolver.ConditionModelResolver
import smarthome.client.presentation.scripts.setup.dependency.action.notification.SendNotificationActionModelResolver
import smarthome.client.presentation.scripts.setup.graph.blockviews.notifications.NotificationBlockNameResolver

val plugingate = module {
    factory<ConditionFromBlockResolver> {
        ConditionFromBlockResolverImpl(listOf(
                get(named<ArduinoConditionFromBlockResolver>()
                ))
        )
    }

    factory<ControllerBlockResolver> {
        ControllerBlockResolverImpl(listOf(
                get(named<ArduinoControllerBlockResolver>())
        ))
    }

    factory<ConditionModelResolver> {
        ConditionModelResolverImpl(listOf(
                get(named<ArduinoConditionModelResolver>())
        ))
    }

    factory<ActionFromBlockResolver> {
        ActionFromBlockResolverImpl(listOf(
                get(named<ArduinoActionFromBlockResolver>()),
                get(named<NotificationActionFromBlockResolver>())
        ))
    }

    factory<ActionModelResolver> {
        ActionModelResolverImpl(listOf(
                get(named<ArduinoActionModelResolver>()),
                get(named<SendNotificationActionModelResolver>())
        ))
    }
    factory<BlockNameResolver> {
        BlockNameResolverImpl(listOf(
                get(named<ArduinoBlockNameResolver>()),
                get(named<NotificationBlockNameResolver>())
        ))
    }
}