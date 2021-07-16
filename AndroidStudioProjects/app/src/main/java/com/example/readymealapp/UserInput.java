package com.example.readymealapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.readymealapp.ui.main.CalRoom;
import com.example.readymealapp.ui.main.Meals;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.Object;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class UserInput extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_input);


        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.sex, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        Spinner spinner2 = findViewById(R.id.spinner2);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.activity, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);
        spinner2.setOnItemSelectedListener(this);


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    public void goToHomePage(View v){
        int userAge = 0;
        int userHeight = 0;
        int userWeight = 0;

        //-----------------------------------------------------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------User input to Database----------------------------------------------------------------
        //-----------------------------------------------------------------------------------------------------------------------------------------------------------------

        //----------------------------------------------------------------retrieving data from textview/spinner to convert to string---------------------------------------
        //Creating a variable for first name
        EditText fNameEditText = findViewById(R.id.fName);
        //save first name to a variable
        String userfName = fNameEditText.getText().toString();

        //Last Name
        EditText lNameEditText = findViewById(R.id.lName);
        String userlName = lNameEditText.getText().toString();

        //Age
        EditText TextAge = findViewById(R.id.editTextAge);
        String StringAge = TextAge.getText().toString();

        //Height
        EditText heightFeet = findViewById(R.id.heightFeet);
        String heightFt = heightFeet.getText().toString();
        EditText heightInches = findViewById(R.id.heightInches);
        String heightIn = heightInches.getText().toString();

        //Age
        EditText weight = findViewById(R.id.weight);
        String weightLb = weight.getText().toString();

        //Favorite Food
        EditText faveFood = findViewById(R.id.favoriteFood);
        String favFood = faveFood.getText().toString();

        //Sex
        Spinner userSex = (Spinner) findViewById(R.id.spinner);
        String sex = userSex.getSelectedItem().toString();

        //User Desired Calories
        EditText calories = findViewById(R.id.caloricGoal);
        String caloricGoal = calories.getText().toString();

        //Physical Activity
        Spinner userPhysicalActivity = (Spinner) findViewById(R.id.spinner2);
        String userActivity = userPhysicalActivity.getSelectedItem().toString();

        //-----------------------------------------------------------------Check if the input is empty or null--------------------------------------------
        //if First Name is empty
        if (userfName.matches("")) {
            Toast.makeText(this, "First Name is Empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (userlName.matches("")) {
            Toast.makeText(this, "Last Name is Empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (StringAge.isEmpty() || StringAge.equals("0"))
        {
            Toast.makeText(this, "Age is Invalid", Toast.LENGTH_SHORT).show();
            return;
        }

        if (heightFt.isEmpty() || heightIn.isEmpty() || heightFt.equals("0"))
        {
            Toast.makeText(this, "Height is Empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (weightLb.isEmpty() || weightLb.equals("0"))
        {
            Toast.makeText(this, "Weight is Empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (favFood.matches("")) {
            Toast.makeText(this, "Favorite Food is Empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (caloricGoal.isEmpty() || caloricGoal.equals("0"))
        {
            Toast.makeText(this, "Calories is Empty", Toast.LENGTH_SHORT).show();
            return;
        }

        //-------------------------------------------------save the user data to the database--------------------------------------------------------

            //instantiating the database
            final AppDatabase Local_db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "User_db").build();
            //creating the user
            User me = new User();
            CalRoom cal = new CalRoom();
            cal.CalAch = Meals.TotalCalories();

            //------name------
            //add the user first name into the database
            me.FName = userfName;
            me.LName = userlName;

            //------age--------
            //convert age to int
            userAge = Integer.parseInt(StringAge);
            me.UserAge = userAge;


//---------------------------BMI---------------------------
            //-----height------
            //convert feet to inches and added them and save them to height, if the inches = 0, only calculate feet
            if(heightIn.equals("0"))
                {
                    userHeight = (0 + Integer.parseInt(heightFt)*12);
                }
            else
                {
                    userHeight = (Integer.parseInt(heightIn) + Integer.parseInt(heightFt)*12);
                }

            //------weight------
            //convert weightLb to integer
            userWeight = Integer.parseInt(weightLb);

            //-------BMI--------
            //calculate BMI and save to database
            me.UserBMI = (((703)*(userWeight))/(userHeight*userHeight));
//---------------------------BMI---------------------------

            //------Favorite Food------
            //add the user's favorite food into the database
            me.FoodFav = favFood;
            Meals.UserFoodPref = favFood;

            //--------Sex--------
            //add the user's favorite food into the database
            me.UserSex = sex;

            //--------Caloric Goal--------
            //add the caloric goal into the database
        if(Integer.parseInt(caloricGoal)<800) // 1200
            {
                me.DesiredCalories = 800; // 1200
                Meals.UserCalories = 800; // 1200
                Toast.makeText(this, "Calories lower than 800, has been set to 800", Toast.LENGTH_SHORT).show();
            }
        else
            {
                me.DesiredCalories = Integer.parseInt(caloricGoal);
                Meals.UserCalories = Integer.parseInt(caloricGoal);
            }
            //Meals.UserCalories = Integer.parseInt(caloricGoal);
            //initialize current calories to 0
            me.CurrentCalories = 0;
            int CalUnder = 0;
            CalUnder = Integer.parseInt(caloricGoal);

            //-----Physical Activity-----
            //add the user's favorite food into the database
            me.UserActivity = userActivity;


            //-----------------------------Save the data to the user Database------------------------------
            //Save to database
            Executor myExecutor = Executors.newSingleThreadExecutor();
            myExecutor.execute(() -> {
                Local_db.userDao().insertUser(me);
            });

            Local_db.close();

            // GET request call
            GETRequest(CalUnder);
        //-----------------------------------------------------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------User input to Database----------------------------------------------------------------
        //-----------------------------------------------------------------------------------------------------------------------------------------------------------------



        //Go to home from user input, code for the button
        Intent HomePage = new Intent (this, HomePage.class);
            startActivity(HomePage);



    }

    // GET Request is here
    private void GETRequest(int CalUnder)
    {

        Map<String, Integer> breakfastItems = new HashMap<String, Integer>();
        breakfastItems.put("Waffles", 82);
        breakfastItems.put("Pancakes", 64);
        breakfastItems.put("Sausages", 391);
        breakfastItems.put("Grits", 143);
        breakfastItems.put("Cereal", 307);

        int i =  1 + (int)(Math.random() * ((5 - 1) + 1));

        Iterator<Map.Entry<String, Integer>> itr = breakfastItems.entrySet().iterator();

        while(itr.hasNext() && i != 0)
        {
            Map.Entry<String, Integer> entry = itr.next();
            Meals.breakfast = entry.getKey();
            Meals.breakCal = entry.getValue();
            i--;
        }


        String APIurl = "https://api.nal.usda.gov/fdc/v1/foods/search?api_key=mOYUdPGUOJOJQJxoKffVm7buXQNzz5oKj7oqEBnX&query=" + Meals.UserFoodPref + "s";

        //User me = new User();
        //User me = new User();
        //Local_db.userDao().insertUser(me);

        /////// code for using a GET request for JSON object from API ///////
        ////////////////////////////////////////////////////////////////////


        JsonObjectRequest ObjReq = new JsonObjectRequest(
                Request.Method.GET,
                APIurl,
                null,
                response -> {
                    try {
                        // get a JSON array of foods
                        JSONArray jsonArray = response.getJSONArray("foods");

                        // get random index so that a new meal plan is generated each time
                        int index = 4 + (int)(Math.random() * ((jsonArray.length() - 4) + 1));

                        // get json object from index in array
                        JSONObject foodFav = jsonArray.getJSONObject(index);

                        // get another JSON array but from Json object this time
                        JSONArray TempJsonObj = foodFav.getJSONArray("foodNutrients");

                        // get another JSON object but from the new array
                        JSONObject JSONCal = (JSONObject) TempJsonObj.get(3);

                        // loop through until you get an index where the JSON values have calories
                        while (JSONCal.getInt("value") == 0.0 || JSONCal.getInt("value") <= 10.0)
                        {
                            if (index++ == jsonArray.length())
                                index = 0;
                            else
                                index++;
                        }
                        Meals.Lunch = foodFav.getString("lowercaseDescription");
                        Meals.mainCalLunch = JSONCal.getInt("value");

                        if (Meals.TotalCalories() > CalUnder){
                            Meals.Lunch = " ";
                            Meals.mainCalLunch = 0;
                        }

                        // do all of this again
                        index = 4 + (int)(Math.random() * ((jsonArray.length() - 4) + 1));

                        foodFav = jsonArray.getJSONObject(index);
                        TempJsonObj = foodFav.getJSONArray("foodNutrients");
                        JSONCal = (JSONObject) TempJsonObj.get(3);
                        while (JSONCal.getInt("value") == 0.0 || JSONCal.getInt("value") <= 10.0)
                        {
                            if (index++ >= jsonArray.length())
                                index = 0;
                            else
                                index++;
                        }
                        Meals.Dinner = foodFav.getString("lowercaseDescription");
                        Meals.mainCalDinner = JSONCal.getInt("value");

                        if (Meals.TotalCalories() > CalUnder){
                            Meals.Dinner = " ";
                            Meals.mainCalDinner = 0;
                        }

                    }
                    // end of try in case JSON Request is invalid or something
                    catch(JSONException e) {
                        e.printStackTrace();
                    }
                }, // end of API listener description
                error -> error.printStackTrace()
        );

        //  |
        //  V  this actually does the GET Request
        Volley.newRequestQueue(getApplicationContext()).add(ObjReq);
        //ReqQ.add(ObjReq);
    }
}
