val kotlin_version: String by project
val jcabi_manifests_version: String by project
val springfox_version: String by project
val guava_version: String by project
val jjwt_version: String by project

buildscript {
    val klint_version: String by project

    repositories {
        maven { url = uri("https://plugins.gradle.org/m2/") }
        jcenter()
    }
    dependencies {
        classpath("org.jlleitschuh.gradle:ktlint-gradle:$klint_version")
    }
}

plugins {
    application

    id("org.springframework.boot")
    id("io.spring.dependency-management")

    kotlin("jvm")
    kotlin("plugin.spring")
    kotlin("plugin.jpa")

    id("org.jlleitschuh.gradle.ktlint")
}

group = "io.github.siyual_park"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenLocal()
    jcenter()
    maven { url = uri("https://kotlin.bintray.com/ktor") }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")

    implementation("org.springframework.boot:spring-boot-starter-security")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    implementation("com.jcabi:jcabi-manifests:$jcabi_manifests_version")

    implementation("io.springfox:springfox-boot-starter:$springfox_version")
    implementation("io.springfox:springfox-swagger-ui:$springfox_version")

    implementation("com.google.guava:guava:$guava_version")

    implementation("com.google.guava:guava:$guava_version")

    implementation("io.jsonwebtoken:jjwt-api:$jjwt_version")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-security")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:$jjwt_version")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:$jjwt_version")

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    developmentOnly("org.springframework.boot:spring-boot-devtools")
    runtimeOnly("com.h2database:h2")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
}

kotlin.sourceSets["main"].kotlin.srcDirs("src/main")
kotlin.sourceSets["test"].kotlin.srcDirs("src/test")

sourceSets["main"].resources.srcDirs("src/main/resources")
sourceSets["test"].resources.srcDirs("src/test/resources")

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
