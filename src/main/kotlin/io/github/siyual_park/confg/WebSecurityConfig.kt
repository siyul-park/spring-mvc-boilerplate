package io.github.siyual_park.confg

import io.github.siyual_park.domain.security.AuthenticationFactoryManager
import io.github.siyual_park.domain.security.BearerAuthenticationFactory
import io.github.siyual_park.filter.AuthenticationFilter
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class WebSecurityConfig(
    private val authenticationFilter: AuthenticationFilter,
    authenticationFactoryManager: AuthenticationFactoryManager,
    bearerAuthenticationFactory: BearerAuthenticationFactory
) : WebSecurityConfigurerAdapter() {

    init {
        authenticationFactoryManager.register(bearerAuthenticationFactory)
    }

    override fun configure(http: HttpSecurity) {
        http.csrf().disable().authorizeRequests()
            .anyRequest().permitAll()
            .and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .formLogin()
            .disable()

        http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
    }
}
