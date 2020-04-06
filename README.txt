#What the app does
When you initally load the page, you'll be presented with an empty cart. You can click on
the "items" page and add items to your cart.

Items will only be presisted locally while a user is not logged in. To save the items to
the DB, either login or "register" a user(enter a username and password). Once logged in,
all updates to your cart will be saved to the backend.

You can try register, logging in, and logging out with different users. Notice how the
cart is saved across sessions, and is different for different users. Awesome!


#Some design considerations:

1) I used React for the frontend, since you guys are a react shop. This is my first time
developing with this specific frontend, and I had a lot of fun!
2) I used Spring Boot for the backend as I have expierence with java and it. I wanted to
showcase some of my backend skills, so I built a custom authenticator and password saver.
Obviously there were easier ways to do this(using built in frameworks), but again, wanted
to showcase my coding skills :D
3) I used mongoDB for the DB. This scales better than SQL DBs, and our data is not complex
nor has complex queries(and probably wont be in the future) so no need for SQL.

#How to run
-go to the top level ShoppingApplication directory and run:
docker-compose up
-this should bring up all 3 containers(frontend, backend, db). I had some problems
with configuring the frontend to point to the backend server; if it's not configured
correctly, please check the ip of the docker box using:
docker-machine ip
and then set line 27 in the docker-compose.yml to that ip:
#      REACT_APP_BACKEND_URL: http://<newIp>:8080


You can access the application on your dockerIP @ port 3000
i.e.:
http://192.168.99.100:8080