
all: clean build

build:
	mvn package

run:
	java -jar target/lightbulbgame.jar

clean:
	mvn clean

.PHONY: build run clean