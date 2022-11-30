set /p cookie=< aoc_cookie

curl  --cookie "%cookie%" https://adventofcode.com/2021/day/%1/input > src/main/kotlin/day%1/input