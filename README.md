# card-list
simple card list application api

[JSON API](https://jsonapi.org) experiment - try with [crnk project](http://www.crnk.io)...

### docker compose

You can run project with an only docker.
Go into 'docker' folder and run the next command:
* __start__: docker-compose -p cards up -d 
* __stop__: docker-compose -p cards down
* __delete-all-data__: docker-compose -p cards down --rmi local --volumes

Application can be accessed on 8080 port.