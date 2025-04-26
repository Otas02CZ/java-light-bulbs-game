JUNIT="junit-platform-console-standalone-1.11.4.jar"

# Build a spusteni testu 1
test1:
	javac -cp .:ijatool.jar:${JUNIT} -d build ija/ija2024/homework2/Homework1Test.java
	java -ea -jar ./${JUNIT} execute --cp build:ijatool.jar --scan-class-path

# Build a spusteni testu 2
test2:
	javac -cp .:ijatool.jar:${JUNIT} -d build ija/ija2024/homework2/Homework2Test.java
	java -ea -jar ./${JUNIT} execute --cp build:ijatool.jar --scan-class-path

# Prelozi a spusti aplikaci s vizualizerem
visual:
	javac -cp .:ijatool.jar -d build ija/ija2024/homework2/Homework2.java
	java -cp build:ijatool.jar ija.ija2024.homework2.Homework2

.PHONY:
	clean visual test build pack

clean:
	rm -rf build xkocio00.zip

pack:
	zip -r xkocio00.zip xkocio00.pdf ija