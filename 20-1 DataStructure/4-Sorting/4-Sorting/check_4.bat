@echo off

rem comfile
javac -encoding UTF-8 SortingTest.java

rem make folder
MKDIR my_output

echo "-execute your program-"

echo Start Measure %Time%

rem make output
for /l %%i in (1, 1, 50) do (
	java SortingTest < testset\input\%%i.txt > my_output\%%i.txt
)

echo Stop Measure %Time% 

echo "-print wrong answer-"

rem compare output
for /l %%i in (1, 1, 50) do (
	fc my_output\%%i.txt testset\output\%%i.txt
)


pause