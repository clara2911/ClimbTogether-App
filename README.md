This is an app for indoor climbers who want to see which climbing routes are available, and add any new climbing routes.
It is possible to register / login with an email and a password. 
When the register / login is complete, the list of routes will become visible (see the middle screenshot below).
One can then long click on routes to view their info, or add extra routes to the list.

<img src="/doc/screenshot_home.png" alt="Initial screen" width="150"> <img src="/doc/screenshot1.png" alt="List of Routes" width="150"> <img src="/doc/screenshot2.png" alt="Route's Info" width="150">

The code is structured as follows:

Mainactivity.java: the initial screen, the user can click continue and will be redirected to the List of Routes if they are already logged in, or to the Register/Login-page otherwise.

RegisterLoginActivity.java: here one can Register or Log in.

RoutesListActivity.java: The list of routes is displayed here.

AddRouteActivity.java: A new route can be added here.

ShowRouteActivity.java: Here the info of a single route is displayed.

Route.java: the model class for routes.

User.java: the model class for users.

[![BCH compliance](https://bettercodehub.com/edge/badge/clara2911/MyOwn)](https://bettercodehub.com/)
