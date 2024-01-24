import requests
import time

class Route:
  def __init__(self, name, methods, hosts, paths):
    self.name = name
    self.methods = methods
    self.hosts = hosts
    self.paths = paths

r = requests.Response()
r.status_code = 400

# service list item format:
# 
# "service_name|url"

services = [
    "PeoplePerson-Get|http://microservice:8080/person/"
    # "TestService|https://www.google.com/"
]

# routes var is a List of Lists of Routes
# each element in routes should match a service defined in 
# a corresponding services var element 

routes = [
    [
        Route(  "PeoplePerson-GetRoute",
                ["GET"],
                ["localhost"],
                ["/person/"]
        )
    ]
    # [
    #     Route(  "TestRoute1",
    #             ["GET"],
    #             ["localhost"],
    #             ["/google1/"]
    #     ),
    #     Route(  "TestRoute2",
    #             ["GET"],
    #             ["localhost"],
    #             ["/google2/"]
    #     )
    # ]
]

while r.status_code != 200 :
    
    print("Attempting connection to KongAdmin API...")
    try:
        r = requests.get("http://kong-gateway:8001/")
    except: 
        print("Connection Failed, retrying...")
        time.sleep(1)

print("Connection successful!")


if len(services) != len(routes): 
   print("WARNING: number of services and routes defined do not match")
   print("Verify that you have set all the services and routes you correctly, then redeploy")
   print("Program will continue in 10 secs")
   time.sleep(10)
   print("Continuing program...")

print("Setting Services...")
print("------")


for i in range(0, len(services)) :
    service_info = services[i].split('|')

    print("Sending request to create service " + service_info[0] + " with url: " + service_info[1] )

    service_req_payload = {'name': service_info[0], 'url': service_info[1]}
    service_req = requests.post(url="http://kong-gateway:8001/services/", data=service_req_payload)
    
    service_check = requests.get(url="http://kong-gateway:8001/services/" + service_info[0])

    while(service_check.status_code != 200):
       service_check = requests.get(url="http://kong-gateway:8001/services/" + service_info[0])
      
    print("Service created")
    for route in routes[i]:
        print("Sending request to create route " + route.name)
        route_req_payload = {
        'name': route.name,
        'methods': route.methods,
        'hosts': route.hosts,
        'paths': route.paths
        }
        content_header = {'Content-Type': 'application/json'}
        req_url = "http://kong-gateway:8001/services/" + service_info[0] + "/routes/"
        print(req_url)
        route_req = requests.post(url=req_url, 
                                data=route_req_payload)
            
print("Set-up Finished")

