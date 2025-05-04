
all: clean build

try: clean-build build run

build-all: docs build

build:
	mvn package

run:
	java -jar target/lightbulbgame.jar

clean: clean-build clean-docs clean-pack

clean-build:
	mvn clean

clean-docs:
	rm -rf docs

clean-pack:
	rm -rf xkocio00 xkocio00.zip

pack: clean-pack
	mkdir xkocio00
	cp requirements.pdf xkocio00/requirements.pdf
	cp pom.xml xkocio00/pom.xml
	cp readme.txt xkocio00/readme.txt
	cp Makefile xkocio00/Makefile
	cp -r src xkocio00/src
	cp -r data xkocio00/data
	cp -r lib xkocio00/lib
	zip xkocio00.zip -r xkocio00
	rm -rf xkocio00

docs: clean-docs
	mkdir -p docs
	mvn javadoc:javadoc

.PHONY: build run clean try all pack docs clean-pack clean-docs clean-build