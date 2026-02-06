package firestorm.vuth.simplebank.utils

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate

object AccountNumberGenerator {
    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    fun nextVal(): Long {
        return jdbcTemplate.queryForObject("SELECT nextval('account_number_seq')", Long::class.java)!!
    }
}