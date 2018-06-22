# tinygram - a project by Quentin Lexert, from Université de Nantes, students in MIAGE.

Introduction : 
Tinygram is a student project that has for purpose to show more about the interactions that an app can have with the datastore and to show the scalability of such environment.
Used technologies are the following : 
- Java 
- Google App Engine
- Google Cloud Endpoints (Unused here)
- JavaScript (Unused here)

Tinygram is supposedly to reproduce the behaviour of Instagram, the famous SNS app to share photos. 
Due to some constraints and problems, I have been late in the project (due by April 21, delivered on June 21). And thus I want to apologize again to my teacher, Mr Molli that waited for this project without any news about it.

Since I had no knowledge of web programing or any skill in Cloud (I only had theory), and due to the hard time I had to setup my dev environment, I skipped the part about the API.
I couldn't manage to make work Google Cloud Endpoints as I wanted. I decided then to focus on the servlet part. 

In the following, I will explain my technical choices toward  our problem, and a check up of all that have been conceived.

First of all, here is the URL of the project :

Project URL : http://1-dot-woven-amulet-194010.appspot.com/

About my project :
My Tinygram is conceived this way : since I don't want to rely too much on the datastore, I decided to play some fo the instructions with Java, so no need for write/read too much. But as we will see, this
approach had limits. 
BigIndex is a servlet that contains an ID, and two maps. And that's all ! 
Map<String, LinkedHashSet<Long>> mapMessages
This map's role is to map the ID of the message created with the user. In that way, each user is a key in this map, and the set associated with is the list that contains all the ID of the messages.
Map<String, LinkedHashSet<String>> mapFollowers;
This map's role is to keep a track of the followers of one user. It works the same way as mapMessages, except that the LinkedHashSet contains user id ("name").

The point is that with that index, we just have to read once the BigIndex, and then we can play with it, then store it again in the datastore. For some operations, it seems like having great values.
However, for instance, we reach a limit with the LinkedHashSet and Map. From what I found out, it seems they are restrained by the server, or have a maximum capability which limitate what we can do with them. 
All the servlets then come in support to the BigIndex.Each servlet as a function, with its own methods. They all refer to BigIndex to make link between them, and so to ensure the data is complete in each case. 
I decided to use objectify so that I can store my own java object in the datastore. Furthermore, I'm really at ease with Java, and it seemed way easier than to use the embedded method given by google. 
I didn't have enough skills with web technologies to produce a complete app, but with that, we can say that the backend is ready. 

Tips : we use the node @Cache which allow the datastore not to be sollicitated every time, if the data is in the cache.

API commands / URL links  :
It is possible to access to the API I was trying to root throught this URL : https://1-dot-woven-amulet-194010.appspot.com/_ah/api/explorer
(Calling it with HTTPS is important for the API to behave well). I would not recommend to use this API, because it is not maintained and I'm not sure of the success of the commands.

For these commands, I used CocoaRestClient for MAC. I will give an exemple for each, and comment them.

- GET http://1-dot-woven-amulet-194010.appspot.com/initialisation
Probably the most important command. It is necessary to do it every time you install the program on a new environment:
It will setup the BigIndex (with key = "BIGINDEX"). It is really important to call it only once because it will be used as global index for message and users.
As discussed precedently, it can cause scalability problems on the long term. 

- GET http://1-dot-woven-amulet-194010.appspot.com/user?name=TOTO
Allow you to get the informations about the user. "name" is the key/id for entity user. 

- POST http://1-dot-woven-amulet-194010.appspot.com/user?name=TOTO
Create a new user in the datastore.

- GET http://1-dot-woven-amulet-194010.appspot.com/timeline?name=TOTO&max=10
Give you the last messages posted by the user (with key/id "name") with a maximum of "max".

- POST http://1-dot-woven-amulet-194010.appspot.com/message?name=TOTO&message=Coucou%20TOTO%20!&image=AnotherURL
Create a new message associated in BigIndexn by "name" and "id". It can store a message and an URL.

- GET http://1-dot-woven-amulet-194010.appspot.com/message?id=488500393848
Allow you to get a specific message with it's ID. 

- GET http://1-dot-woven-amulet-194010.appspot.com/follow?name=TOTO
Give you the user's follower list. 

- POST http://1-dot-woven-amulet-194010.appspot.com/follow?name=TOTO&follower=TUTU
Add a follower do user's follower list. "name" is the one who is followed by the "follower".

- POST http://1-dot-woven-amulet-194010.appspot.com/test?nb=25
Special command for test. Got inactivated for the sake of the good use. Uncomment lines 50 to 57 if you wish to use it fully. 
In this state, create a list of friends for the user HeraChat+nb (argument "nb").

Results :

<table>
   <tr>
       <td>Message posted with</td>
       <td>100 followers</td>
       <td>500 followers</td>
       <td>1000 followers</td>
   </tr>
   <tr>
       <td>Mean</td>
       <td>277 ms</td>
       <td>259 ms</td>
       <td>239 ms</td>
   </tr>
   <tr>
       <td>Variance</td>
       <td>15885,22 ms</td>
       <td>13096,21 ms</td>
       <td>5671,7 ms</td>
   </tr>
</table>

<table>
   <tr>
       <td>Timeline posted with</td>
       <td>10 messages</td>
       <td>50 messages</td>
       <td>100 messages</td>
   </tr>
   <tr>
       <td>Mean</td>
       <td>154,5. ms</td>
       <td>375,63 ms</td>
       <td>720,75 ms</td>
   </tr>
   <tr>
       <td>Variance</td>
       <td>3791,67 ms</td>
       <td>23162,82 ms</td>
       <td>162837,02 ms</td>
   </tr>
</table>



Conclusion :
We can sense a limit with my test : for instance, we cannot make it to a timeline of 1000. We can explain these surprisingly high results for the timeline by the fact that we are sorting is quite heavy for all those.
We need to manage differently the sort by date. However, for the write we remain, which show clearly the scalability of the app.
