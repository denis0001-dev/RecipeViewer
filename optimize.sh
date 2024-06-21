#!/bin/bash
filename=recipeviewer-1.0-SNAPSHOT-jar-with-dependencies.jar
proguard="./proguard-7.5.0"

mvn package
mv ./target/$filename $proguard/bin/$filename
$proguard/bin/proguard.sh \
-injars \
$proguard/bin/recipeviewer-1.0-SNAPSHOT-jar-with-dependencies.jar \
-outjars \
recipeviewer.jar \
-printusage \
-dontwarn \
-dontobfuscate \
-keep \
"public class ru.morozovit.recipeviewer.Main {public static void main(java.lang.String[]);}"
mkdir -p ./bin
mv ./recipeviewer.jar ./bin/recipeviewer.jar
rm ./target/*.jar
rm $proguard/bin/recipeviewer*.jar