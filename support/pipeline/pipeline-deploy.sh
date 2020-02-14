#!/bin/bash

#### Init
cd "${TRAVIS_BUILD_DIR}" || exit

# Publish package
printf "Publish package to GitHub Packages"
./gradlew publish || exit
echo "[OK]"