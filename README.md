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


npm WARN react-scripts@3.3.1 requires a peer of typescript@^3.2.1 but none is installed. You must install peer dependencies yourself.
npm WARN sass-loader@8.0.2 requires a peer of sass@^1.3.0 but none is installed. You must install peer dependencies yourself.
npm WARN sass-loader@8.0.2 requires a peer of fibers@>= 3.1.0 but none is installed. You must install peer dependencies yourself.
npm WARN eslint-config-react-app@5.2.0 requires a peer of eslint-plugin-flowtype@3.x but none is installed. You must install peer dependencies yourself.
npm WARN tsutils@3.17.1 requires a peer of typescript@>=2.8.0 || >= 3.2.0-dev || >= 3.3.0-dev || >= 3.4.0-dev || >= 3.5.0-dev || >= 3.6.0-dev || >= 3.6.0-beta || >= 3.7.0-dev || >= 3.7.0-beta but none is installed. You must install peer dependencies yourself.

npm install eslint-plugin-flowtype