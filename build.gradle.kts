plugins {
    java
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "org.example.Main"
    }
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("org.jmonkeyengine:jme3-core:3.4.0-stable")
    implementation("org.jmonkeyengine:jme3-desktop:3.4.0-stable")
    implementation("org.jmonkeyengine:jme3-lwjgl:3.4.0-stable")
    implementation("org.jmonkeyengine:jme3-plugins:3.4.0-stable")
}

tasks.test {
    useJUnitPlatform()
}
