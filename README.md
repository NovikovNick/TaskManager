## Task manager

tail -f ./taskmanager.log | jq '.'
tail -f ./taskmanager.log | jq '. | select(.level == "ERROR")'


```$xslt

./gradlew clean build

docker build -t running-list-front:latest ./frontend
docker exec -it /taskmanager_running-list-front_1 sh

```