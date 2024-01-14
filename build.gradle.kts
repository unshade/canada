plugins {
    id("java")
    id("application")
    id ("io.freefair.aspectj.post-compile-weaving") version "8.4"
    id ("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "org.trad.pcl"
version = "1.0-SNAPSHOT"

// mettre l'encodage en utf-8
tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}


application {
    mainClass = "org.trad.pcl.Main"
}

dependencies {
    implementation("com.diogonunes:JColor:5.5.1")
    implementation("org.aspectj:aspectjrt:1.9.21")
    implementation("org.aspectj:aspectjweaver:1.9.21")

    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}