package io.github.siyual_park.controller

import com.jcabi.manifests.Manifests
import io.github.siyual_park.model.Version
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class VersionController {

    @GetMapping("/version")
    fun ping(): ResponseEntity<Version> {
        val version = Version(
            javaClass.`package`.specificationVersion,
            javaClass.`package`.implementationVersion,
            Manifests.DEFAULT["Build-Time"]
        )
        return ResponseEntity.ok(version)
    }
}
