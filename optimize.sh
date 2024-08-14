#!/bin/bash
# Variables for easier access
# filename=recipeviewer-1.0-SNAPSHOT-jar-with-dependencies.jar
proguard=$(realpath ./proguard-7.5.0)
# mainclass="ru.morozovit.recipeviewer.Main"
projectdir="."

mvnGetProperty() {
  # shellcheck disable=SC2046
  return $(mvn help:evaluate -Dexpression=$1 -q -DforceStdout)
}

parseDescriptorRef() {
  # shellcheck disable=SC2046
  return $(python3 - << EOF
  import re
  regex = r"\s*<descriptorRefs>\s*<descriptorRef>(.*)<\/descriptorRef>\s*<\/descriptorRefs>"

  test_str = ("$1")

  subst = "\$1"

  # You can manually specify the number of replacements by changing the 4th argument
  result = re.sub(regex, subst, test_str, 0, re.MULTILINE)

  if result:
      print (result)
EOF
)
}

# shellcheck disable=SC2237
if ! [ -z $1 ]; then
  projectdir=$1
fi

# shellcheck disable=SC2237
if ! [ -z $2 ]; then
  mainclass=$2
fi

if ! [ -e "$proguard/bin/proguard.sh" ]; then
  echo "ProGuard script not found." >&2
  exit 1
elif ! [ -e "$projectdir/src/main/java" ]; then
  echo "Source folder not found." >&2
  exit 1
elif ! [ -e "$projectdir/src/main/java/$(echo $mainclass | sed -E "s/\./\//gm;t").java" ]; then
  echo "Main class not found." >&2
  exit 1
fi

# Resolve jar file name
version=$(mvn help:evaluate -Dexpression=project.version -q -DforceStdout)
artifactId=$(mvn help:evaluate -Dexpression=project.artifactId -q -DforceStdout)
descriptorRef=$(python3 - << EOF
import re
regex = r"\s*<descriptorRefs>\s*<descriptorRef>(.*)<\/descriptorRef>\s*<\/descriptorRefs>"

test_str = ("$(cat $projectdir/pom.xml | grep --color=auto -A 1 -B 1 "<descriptorRef>" | xargs)")

subst = r"\1"

# You can manually specify the number of replacements by changing the 4th argument
result = re.sub(regex, subst, test_str, 0, re.MULTILINE)

if result:
    print (result)
EOF
              )

# Install the root project to the local repository
mvn -non-recursive install

# shellcheck disable=SC2154
filename="${artifactId}-${version}-${descriptorRef}.jar"

prevDir=$(pwd)

# shellcheck disable=SC2164
cd $projectdir

# Compile and move the jar
# shellcheck disable=SC2046
if [ $(realpath $prevDir) == $(realpath .) ]; then
  cmd="mvn -non-recursive package"
else
  cmd="mvn package"
fi

echo "Command: $cmd"

if ! $cmd; then
  echo "Compilation failed." >&2
  exit 1
fi
mv ./target/$filename $proguard/bin/$filename

# Optimize it with ProGuard
$proguard/bin/proguard.sh \
-injars \
$proguard/bin/$filename \
-outjars \
recipeviewer.jar \
-dontwarn \
-dontobfuscate \
-keep \
"public class $mainclass {public static void main(java.lang.String[]);}"

# Move the jar to bin folder
mkdir -p ./bin
mv ./recipeviewer.jar ./bin/recipeviewer.jar

# Clean up
rm ./target/*.jar 2>/dev/null
rm $proguard/bin/recipeviewer*.jar 2>/dev/null

# shellcheck disable=SC2164
cd $prevDir