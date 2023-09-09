.PHONY: build

default:
	# java 11
	java --version

build:
	./gradlew shadowJar


build-lambda:
	./gradlew lambda:shadowJar

build-stack:
	# nvm use v18.16.0
	#cd tmp ; maven package &&  cdk deploy
	./gradlew stack:shadowJar && cd stack cdk deploy

