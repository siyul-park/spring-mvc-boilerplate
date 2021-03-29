package io.github.siyual_park.confg

import io.swagger.annotations.Api
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import java.time.Instant

@Configuration
class SpringFoxConfig {
    @Bean
    fun api(): Docket {
        return Docket(DocumentationType.SWAGGER_2).apply {
            directModelSubstitute(Instant::class.java, Long::class.java)
        }
            .select()
            .apis(RequestHandlerSelectors.withClassAnnotation(Api::class.java))
            .paths(PathSelectors.any())
            .build()
    }
}
