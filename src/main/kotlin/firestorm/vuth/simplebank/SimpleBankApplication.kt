package firestorm.vuth.simplebank

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class SimpleBankApplication

fun main(args: Array<String>) {
    runApplication<SimpleBankApplication>(*args)
}
