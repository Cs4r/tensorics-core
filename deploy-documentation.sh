#!/bin/bash
set -e # exit with nonzero exit code if anything fails

mkdir -p build/documentation

cp -r build/asciidoc/* build/documentation/
cp -r build/docs/* build/documentation/

cd build/documentation

git init
git config user.name "Travis CI"
git config user.email "${E_MAIL}"

git add .
git commit -m "Automatically generated GitHub Pages and Javadocs"

# Force push from the current repo's master branch to the remote
# repo's gh-pages branch. (All previous history on the gh-pages branch
# will be lost, since we are overwriting it.) We redirect any output to
# /dev/null to hide any sensitive credential data that might otherwise be exposed.
git push --force --quiet "https://${GH_TOKEN}@${GH_REF}" master:gh-pages  > /dev/null 2>&1
