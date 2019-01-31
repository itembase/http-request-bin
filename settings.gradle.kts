rootProject.name = "http-request-bin"

include("http-request-bin-service")

rootProject.children.forEach {
    it.buildFileName = "${it.name}.gradle.kts"
}
