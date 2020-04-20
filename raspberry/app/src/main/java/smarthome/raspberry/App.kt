package smarthome.raspberry

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import smarthome.raspberry.core.ObservableJpaRepository

@SpringBootApplication
@EnableJpaRepositories(repositoryBaseClass = ObservableJpaRepository::class)
open class RaspberryApplication

fun main(args: Array<String>) {
    ObjectMapper().registerModule(KotlinModule())
    runApplication<RaspberryApplication>(*args)
}
