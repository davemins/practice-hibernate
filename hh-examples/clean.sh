#!/usr/bin/env zsh

# don't throw errors when file globs don't match anything
setopt NULL_GLOB

rm -rf  */{.gradle,.settings,bin,build,.vscode}
rm -f */{.project,.classpath}

# for dir in 0*
# do
#     echo $dir;
#     pushd $dir
#     gradle clean
#     popd
# done

# rm -rf */gradle
# rm -f */{gradlew,gradlew.bat}

