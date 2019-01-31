plugins {
    id("org.springframework.boot") version "2.1.2.RELEASE"
    id("com.bmuschko.docker-spring-boot-application") version "4.3.0"
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-webflux")
}

docker {
    springBootApplication {
        baseImage.set("openjdk:11-jre")
        ports.set(listOf(8080))
        tag.set("itembase/http-request-bin:$version")
    }
}
