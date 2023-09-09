.PHONY: default all

default:
	# java 11
	java --version

all:
	bash build.bash stage-all

# LAMBDA
build-lambda:
	./gradlew lambda:shadowJar

# CDK STACK
ci-deploy:
	bash build.bash stage-deploy

ci-package:
	bash build.bash stage-package

