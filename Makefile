.PHONY: build

default:
	# java 11
	java --version

build:
	./gradlew shadowJar
