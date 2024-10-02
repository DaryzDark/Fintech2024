plugins {
	java
	id("org.springframework.boot") version "3.3.4"
	id("io.spring.dependency-management") version "1.1.6"
	id("jacoco")
}

group = "org.fintech2024"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}
jacoco {
	toolVersion = "0.8.12"
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-aop:3.3.4")
	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.springframework.boot:spring-boot-starter-validation:3.3.4")
	implementation(project(":LogExecutionTimeStarter"))

	implementation("org.slf4j:slf4j-api:2.0.16")
	implementation("ch.qos.logback:logback-classic:1.5.8")

	compileOnly("org.projectlombok:lombok:1.18.34")
	annotationProcessor("org.projectlombok:lombok:1.18.34")

	implementation("org.eclipse.jetty:jetty-server:11.0.24")

	testImplementation("org.wiremock:wiremock:3.9.1")
	testImplementation("org.testcontainers:junit-jupiter:1.20.2")
	implementation("org.wiremock.integrations.testcontainers:wiremock-testcontainers-module:1.0-alpha-14")
	testImplementation("org.mockito:mockito-core:5.14.1")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
	useJUnitPlatform()
	finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
	reports {
		xml.required.set(true)
		html.required.set(true)
		csv.required.set(false)
	}
		classDirectories.setFrom(
			files(classDirectories.files.map {
				fileTree(it).exclude(
					"**/model/**",
				)
			})
		)
}