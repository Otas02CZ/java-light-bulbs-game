
build-gui:
	mvn clean package -Pgui

build-cli:
	mvn clean package -Pcli

run-gui:
	java -jar target/lightbulbgame-gui-jar-with-dependencies.jar

run-cli:
	java -jar target/lightbulbgame-cli-jar-with-dependencies.jar
