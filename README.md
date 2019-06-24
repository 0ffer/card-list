# card-list
Simple card list application api

[JSON API](https://jsonapi.org) experiment - try with [crnk project](http://www.crnk.io)...

### docker compose

You can run project with an only docker.
Go into 'run-from-sources-docker' folder and run the next command:
* __start__: docker-compose -p cards up -d 
* __stop__: docker-compose -p cards down
* __delete-all-data__: docker-compose -p cards down --rmi local --volumes

Application can be accessed on 8080 port.

### test data

You can create some test data on startup. 

For this need to activate spring profile __init-test-data__

If some data already exist in database, generation will interrupted.

### security

In application used Oauth2 + JWT security.

Now only single static user available.

To get token use next request:

Basic auth credentials: _ClientId:secret_

```
   POST http://localhost:8080//oauth/token?grant_type=password&username=admin&password=admin HTTP/1.1
   Accept: application/vnd.api+json
   Content-Type: application/vnd.api+json; charset=utf-8
   Authorization: Basic Q2xpZW50SWQ6c2VjcmV0
  ```
  
  To disable authentication you may use property __security.enabled__. 
  The property disable auth checks on _/api/**_ endpoints.

### documentation

The documentation was generated when project builds.

Before build project - run task "copyDocumentationToResources"

Docs will be included to static resourses and available on the root app path.

### docker hub

Application exists on dockerhub - [app page](https://hub.docker.com/r/0ffer/card-list)

As well application may be run in complete [docker-compose file](/docker-compose.yml)
