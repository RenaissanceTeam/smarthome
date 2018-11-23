# Smart home server
## Build from source
use bootjar gradle task

## Deployment 
copy and edit application.properties to out dir and run jar with following command
```
java -Dspring.config.location=/application.properties -jar target.jar
```

