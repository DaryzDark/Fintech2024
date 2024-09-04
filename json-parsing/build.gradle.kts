plugins {
    id("java")
}

group = "org.fintech2024"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // Lombok
    compileOnly("org.projectlombok:lombok:1.18.34")
    annotationProcessor("org.projectlombok:lombok:1.18.34")

    // Jackson Databind
    implementation("com.fasterxml.jackson.core:jackson-databind:2.17.2")

    // Jackson XML
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:2.17.2")

    // Logger
    implementation("org.slf4j:slf4j-api:2.0.7")
    implementation("ch.qos.logback:logback-classic:1.5.7")

    // Test
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.11.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.11.0")
}

tasks.test {
    useJUnitPlatform()
}