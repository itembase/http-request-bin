subprojects {
    apply(plugin = "java")

    group = "com.itembase"
    version = "0.0.3"

    repositories {
        jcenter()
        mavenCentral()
    }

    dependencies {
        val springVersion = "2.1.2.RELEASE"

        "implementation"(platform("org.springframework.boot:spring-boot-dependencies:$springVersion"))
    }
}
