After putting my code through Better Code Hub I received the following analysis:

![Grade by Better Code Hub](/BetterCodeHubGrade.png)

So my main concern is minimizing duplicated code. Besides that, 
there are some methods which are more than 15 lines, which I will make shorter.

This resulted in the following tasklist:
Write Code Once:
  — 14 lines occurring 2 times in multiple files
      starts at line 89 in RoutesListActivity.java
      starts at from line 75 in ShowRouteActivity.java
  — 10 lines occurring 2 times in multiple files
      starts at line 43 in MainActivity.java
      starts at line 39 in RegisterLoginActivity.java
  — 10 lines occurring 2 times in multiple files
      starts at line 25 in MainActivity.java
      starts at line 21 in RegisterLoginActivity.java
  — 10 lines occurring 2 times in multiple files
      starts at line 65 in MainActivity.java
      starts at line 63 in RegisterLoginActivity.java
      
 Write Short Units of Code:
  — RegisterLoginActivity.onCreate(Bundle)
  — MainActivity.onCreate(Bundle)
  — AddRouteActivity.addToDB(View)
  — RegisterLoginActivity.login(View)
  — RegisterLoginActivity.createUser()
  — ShowRouteActivity.makeTopRouteNmsList(User)
  — ShowRouteActivity.onCreate(Bundle)
  — ShowRouteActivity.getUserInfoFromDB(String)
  — ShowRouteActivity.setListener()
  — RoutesListActivity.getFromDB()

