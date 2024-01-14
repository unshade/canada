plugins {
    id("java")
    id("application")
}

group = "org.trad.pcl"
version = "1.0-SNAPSHOT"

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

repositories {
    mavenCentral()
}

application {
    mainClass = "org.trad.pcl.Main"
}

dependencies {
    implementation("com.diogonunes:JColor:5.5.1")
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}