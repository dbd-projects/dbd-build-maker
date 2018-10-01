# Character Service
The backend service for creating, reading, updating, and deleting Characters, Items, Addons, and Perks. This service 
exposes internal endpoints that another service in the environment can hit. 
[Play Framework](https://www.playframework.com/) is the one of choice for this service because it is an easy-to-use 
framework with high readability, reliability, and scalability. Also it is just better than Spring and I have other 
plans for [RESTEasy](https://resteasy.github.io/).

This application is also designed in a way that will allow it to be broken into separate projects that all perform 
similar functions for the *different* data used in it.

## Internal Endpoints
FYI, this service has no external endpoints.

### Characters

### Items

### Addons

### Perks