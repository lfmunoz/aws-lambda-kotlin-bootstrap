#!/usr/bin/env bash

source "$HOME/.sdkman/bin/sdkman-init.sh"
source "$HOME/.nvm/nvm.sh"
nvm use use v18.16.0
sdk use java 11.0.14-zulu

# ________________________________________________________________________________
# VARIABLES
# ________________________________________________________________________________
repositoryUrl=https://github.com/lfmunoz/vertx-kt-rocket.git
# ________________________________________________________________________________
# BUILD STAGES
# ________________________________________________________________________________
stage-info() {
	echo "# ----------------------------------------------"
	echo "# STAGE: info"
	echo "# ----------------------------------------------"
	echo "pwd            =" $(pwd)
	echo "whoami         =" $(whoami)
	echo "repositoryUrl  =" ${repositoryUrl}
	echo
	echo "java --version"
	java --version
	node --version
}

stage-git-clone() {
	echo "# ----------------------------------------------"
	echo "# STAGE: git clone"
	echo "# ----------------------------------------------"
	echo "git clone ${repositoryUrl}"
#	git clone ${repositoryUrl} --depth 1 /root/workingDir/repository
}

stage-test() {
	echo "# ----------------------------------------------"
	echo "# STAGE: test"
	echo "# ----------------------------------------------"
#	cd repository ; ./gradlew test
}

stage-all() {
	echo "# ----------------------------------------------"
	echo "# STAGE: all"
	echo "# ----------------------------------------------"
	./gradlew lambda:shadowJar
	./gradlew stack:shadowJar
	cd stack
	cdk deploy
}

stage-package() {
	echo "# ----------------------------------------------"
	echo "# STAGE: package"
	echo "# ----------------------------------------------"
	./gradlew stack:shadowJar
}

stage-publish() {
	echo "# ----------------------------------------------"
	echo "# STAGE: publish"
	echo "# ----------------------------------------------"
	echo "N/A"
}

stage-deploy() {
	echo "# ----------------------------------------------"
	echo "# STAGE: deploy"
	echo "# ----------------------------------------------"
	echo "N/A"
	cd stack
	cdk deploy
}

# ________________________________________________________________________________
# EXEC
# ________________________________________________________________________________
# Check if the function exists (bash specific)
if declare -f "$1" > /dev/null
then
    # call arguments verbatim
    "$@"
else
    # Show a helpful error
    echo "'$1' is not a known function name" >&2
    exit 1
fi

