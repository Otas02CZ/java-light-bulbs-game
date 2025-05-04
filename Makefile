
all: clean build

try: clean build run

build:
	mvn package

run:
	java -jar target/lightbulbgame.jar

clean:
	mvn clean
	rm -rf docs

pack:
	zip xkocio00.zip -r src data lib Makefile pom.xml readme.txt requirements.pdf

docs: clean
	mkdir -p docs
	mvn javadoc:javadoc

.PHONY: build run clean try all pack docs