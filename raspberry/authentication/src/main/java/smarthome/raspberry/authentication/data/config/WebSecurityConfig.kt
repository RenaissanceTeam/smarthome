package smarthome.raspberry.authentication.data.config

import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.password.NoOpPasswordEncoder
import smarthome.raspberry.authentication.domain.entity.Roles
import javax.sql.DataSource

@Configuration
@EnableWebSecurity
open class WebSecurityConfig(private val dataSource: DataSource) : WebSecurityConfigurerAdapter() {
    
    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http
            .csrf().disable()
            .authorizeRequests()
                .antMatchers("/login", "/signup").permitAll()
                .antMatchers("/signup/admin").hasRole(Roles.ADMIN.name)
                .antMatchers("/api/**").authenticated()
            .and().logout().permitAll()
    }
    
    override fun configure(auth: AuthenticationManagerBuilder?) {
        auth ?: return
        
        auth.jdbcAuthentication()
            .dataSource(dataSource)
            .passwordEncoder(NoOpPasswordEncoder.getInstance())
            .usersByUsernameQuery("select username, password, enabled from users where username=?")
            .authoritiesByUsernameQuery("select username, role from user_roles where username=?")
    }
}