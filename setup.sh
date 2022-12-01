#/bin/bash
# Copies the template for a new day
# Takes in the day as the first parm
# For example: To setup day 8 use `./setup.sh 8`

package=day$1
mkdir -p src/main/kotlin/$package
mkdir -p src/test/kotlin/$package

cp src/main/kotlin/template/DayT.kt src/main/kotlin/$package/Day$1.kt
cp src/test/kotlin/template/DayTKtTest.kt src/test/kotlin/$package/Day$1KtTest.kt

sed -i "s/package template/package $package/" src/*/kotlin/$package/*.kt
sed -i "s/val day = 0/val day = $1/" src/test/kotlin/$package/*.kt
sed -i "s/DayTKtTest/Day$1KtTest/" src/test/kotlin/$package/*.kt
sed -i "s/@Ignore \/\/Ignore Class//" src/test/kotlin/$package/*.kt