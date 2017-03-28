After putting my code through Better Code Hub I received the following analysis:

![Grade by Better Code Hub](/betterCodeHubGrade.png)

So my main concern is minimizing duplicated code. Besides that, 
there are some methods which are more than 15 lines, which I will make shorter.



This resulted in the following tasklist:


Part 1: Write Code Once:

- 14 lines occurring 2 times in multiple files: and in RoutesListActivity.java and in ShowRouteActivity.java
- 10 lines occurring 2 times in multiple files: in MainActivity.java and in RegisterLoginActivity.java
- 10 lines occurring 2 times in multiple files in MainActivity.java and in RegisterLoginActivity.java
- 10 lines occurring 2 times in multiple files in MainActivity.java and in RegisterLoginActivity.java
      
      
 Part 2: Write Short Units of Code:
 
 - RegisterLoginActivity.onCreate(Bundle)
 - MainActivity.onCreate(Bundle)
 - AddRouteActivity.addToDB(View)
 - RegisterLoginActivity.login(View)
 - RegisterLoginActivity.createUser()
 - ShowRouteActivity.makeTopRouteNmsList(User)
 - ShowRouteActivity.onCreate(Bundle)
 - ShowRouteActivity.getUserInfoFromDB(String)
 - ShowRouteActivity.setListener()
 - RoutesListActivity.getFromDB()

