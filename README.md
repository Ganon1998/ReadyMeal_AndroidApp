# ReadyMeal_AndroidApp
An app developed on Android devices (Java) which will generate a meal plan for the day based on the user's food preference as well as their caloric goals.

With the help of the USDA's FoodData Central API, the app requests information from said API using Android Studio's Volley library to perform a JSON GET request which has the url with an appended SQL query (based on the food preferences) and parse the requested data to present it to the user.
