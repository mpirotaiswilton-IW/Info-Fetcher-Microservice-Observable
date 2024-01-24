#!/bin/sh
result=0
correct=200

while [ "$result" -ne "$correct" ]
do
    printf "attempting connection to Kong Admin API... \n\n"
    result=`curl -s -o /dev/null -w "%{http_code}" http://kong-gateway:8001/`
    sleep 1
done

printf "Connection to Kong Gateway established \n"

printf "\nSetting up PeopleGet service...\n\n"

curl -i -X POST \
--url http://kong-gateway:8001/services/ \
--data name=PeoplePerson-Get \
--data 'url=http://microservice:8080/person/'

printf "\n\nSetting up PeopleGet service routes...\n\n"

curl -i -X POST \
--url http://kong-gateway:8001/services/PeoplePerson-Get/routes/ \
    -H "Content-Type: application/json" \
    -d '{"name": "PeoplePerson-GetRoute", "methods":["GET"], "hosts":["localhost"], "paths":["/person"]}'

printf "\n\nSet-up complete\n"