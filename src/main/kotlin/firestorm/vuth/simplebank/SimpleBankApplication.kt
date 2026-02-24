package firestorm.vuth.simplebank

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableAsync

@SpringBootApplication
@ConfigurationPropertiesScan
@EnableAsync
class SimpleBankApplication

fun main(args: Array<String>) {
    runApplication<SimpleBankApplication>(*args)
}
