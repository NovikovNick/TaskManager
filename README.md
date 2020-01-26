## Task manager

tail -f ./taskmanager.log | jq '.'
tail -f ./taskmanager.log | jq '. | select(.level == "ERROR")'


```$xslt

./gradlew clean build


docker exec -it /taskmanager_running-list-front_1 sh

docker tag running-list-front:latest nicknovikov/running-list-server:latest
docker push nicknovikov/running-list-server:latest

docker rmi $(docker images -a -q)

docker build -t nicknovikov/running-list-front:latest ./frontend
docker push nicknovikov/running-list-front:latest
docker push nicknovikov/running-list-server:latest

```