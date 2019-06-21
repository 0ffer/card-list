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

### documentation

The documentation was generated when project builds.

Before build project - run task "copyDocumentationToResources"

Docs will be included to static resourses and available on the root app path.

### docker hub

Application exists on dockerhub - [app page](https://hub.docker.com/r/0ffer/card-list)

As well application may be run in complete [docker-compose file](/docker-compose.yml)
