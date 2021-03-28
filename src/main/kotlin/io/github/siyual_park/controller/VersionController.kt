package io.github.siyual_park.controller

import com.jcabi.manifests.Manifests
import io.github.siyual_park.model.Version
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
class VersionController {

    @GetMapping("/version")
    @ResponseStatus(HttpStatus.OK)
    fun ping() = Version(
        javaClass.`package`.specificationVersion,
        javaClass.`package`.implementationVersion,
        Manifests.DEFAULT["Build-Time"]
    )
}
