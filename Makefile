
build-gui:
	mvn clean package -Pgui

build-cli:
	mvn clean package -Pcli

run-gui:
	java -jar target/lightbulb-gui-jar-with-dependencies.jar

run-cli:
	java -jar target/lightbulb-cli-jar-with-dependencies.jar
