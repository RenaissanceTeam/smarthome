package smarthome.client.domain.api.scripts.resolver

import smarthome.client.entity.Controller
import smarthome.client.entity.script.block.Block
import smarthome.client.entity.script.controller.ControllerBlock
import smarthome.client.util.Position

interface ControllerBlockResolver: Resolver<Controller, ControllerBlock>