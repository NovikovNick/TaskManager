## Running List

Check actual version: https://runninglist.ru

#### Development 

1. Don't forget to enable annotation processing in IDE and install lombok plugin
2. Use gradle wrapper
3. provide env variables for:
    - DATASOURCE_URL=jdbc:postgresql://localhost:5432/runninglist
    - DATASOURCE_USERNAME=metalheart
    - DATASOURCE_PASSWORD=metalheart
    - OAUTH2_GOOGLE_CLIENT_ID=blablablaclientid
    - OAUTH2_GOOGLE_CLIENT_SECRET=blablablaclientsecret
    - SMTP_USERNAME=your@domain.net
    - SMTP_PASSWORD=yourpassword
    - DEFAULT_ADMIN_USERNAME=admin
    - DEFAULT_ADMIN_PASSWORD=admin
    - BACK_URL=http://localhost:8080
    - FRONT_URL=http://localhost:3000
    - LOG_DIR=./runninglist-logs

```.env

// start db
docker-compose up

// build and start front dev server
cd ./frontend
npm install
npm run start

// backend
run from ./app/src/main/java/com/metalheart/App.java
```
 
 ```.env
 tail -f ./runninglist-logs/runninglist.log | jq '.'
 tail -f ./runninglist-logs/runninglist.log | jq '. | select(.context.OPERATION_ID != null) | select(.context.OPERATION_ID | contains("551100ce-7fd5-47c3-ac46-4ef9719b6b7c"))'
 ```

 #### Production
 
 ```.env
 
 // build front
 cd ./frontend
 npm run build
 docker build -t nicknovikov/running-list-front:latest .
 cd ../
 
 // build back
 ./gradlew dockerBuildImage
 
 // push to repository
 docker push nicknovikov/running-list-front:latest
 docker push nicknovikov/running-list-server:latest

tail -f ./runninglist/logs/runninglist.log | jq '.'
 ```
 