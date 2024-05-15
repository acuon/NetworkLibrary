apply(plugin = "maven-publish")

configure<PublishingExtension> {
    publications.create<MavenPublication>("simplenetworklibrary") {
        groupId = "com.acuon.networklibrary"
        artifactId = "simplenetworklibrary"
        version = "1.0.0"
        pom.packaging = "aar"
        artifact("$buildDir/outputs/aar/simplenetworklibrary-release.aar")
    }
    repositories {
        mavenLocal()
    }
}
