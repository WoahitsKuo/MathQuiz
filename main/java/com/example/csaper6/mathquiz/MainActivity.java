package com.example.csaper6.mathquiz;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity {

    private Button answerA,answerB,answerC,answerD;
    private TextView question,score,time;
    private String equation, type;
    private Integer buttonWithAnswer;
    private Integer marginError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialize what the operation is and what is the equation
        type = "simplify";
        equation = "5*8";
        marginError = 10;

        //Wiring
        answerA = (Button) findViewById(R.id.answerA);
        answerB = (Button) findViewById(R.id.answerB);
        answerC = (Button) findViewById(R.id.answerC);
        answerD = (Button) findViewById(R.id.answerD);

        question = (TextView) findViewById(R.id.question);
        score = (TextView) findViewById(R.id.score);
        time = (TextView) findViewById(R.id.timer);

        //Set question text
        question.setText(type.substring(0,1).toUpperCase() + type.substring(1,type.length()) + " " + equation + ".");

        //Set the button with answer (Button with the answer is in )
        getAnswer(type, equation);

    }

    private void getAnswer(String endpoint, String equation) {
        String baseUrl = "https://newton.now.sh/";
        String type = endpoint;
        String search = equation;
        String fullUrl = baseUrl + type + "/" + Uri.encode(search);
        new MathSolver().execute(fullUrl);
    }

    private class MathSolver extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            //make a new URL object
            String jsonString = "";
            String answer = "";
            try {
                URL url = new URL(urls[0]);
                URLConnection connection = url.openConnection();

                InputStream inputStream = connection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                //build a string just like you did in the code from Thursday
                while ((line = bufferedReader.readLine()) != null) {
                    jsonString += line;
                }
                //Log the built string and try it out
                //Log.d(TAG, "onCreate: " + jsonString);


            } catch (Exception e) {
                e.printStackTrace();
            }

            //create a json object from the string, dive into the json and access just the name
            JSONObject jsonData = null;
            try{
                jsonData = new JSONObject(jsonString);
                if(jsonData != null){
                    answer = jsonData.optString("result");
                    return answer;
                }
                else
                    return null;

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return answer;
        }

        @Override
        protected void onPostExecute(String answer){ //return from doInBackground
            super.onPostExecute(answer);
            buttonWithAnswer = (int) ((Math.random()*4)+1);
            if (buttonWithAnswer == 1)
                answerA.setText(answer);
            else if (buttonWithAnswer == 2)
                answerB.setText(answer);
            else if (buttonWithAnswer == 3)
                answerC.setText(answer);
            else if (buttonWithAnswer == 4)
                answerD.setText(answer);

            int answerN = Integer.parseInt(answer);
            int displayAnswer;

            if (buttonWithAnswer != 1) {
                displayAnswer = answerN + (int) (Math.random() * marginError +1);
                answerA.setText("" + displayAnswer);
            }

            if (buttonWithAnswer != 2) {
                displayAnswer = answerN - (int) (Math.random() * marginError +1);
                answerB.setText("" + displayAnswer);
            }

            if (buttonWithAnswer != 3) {
                displayAnswer = answerN + (int) (Math.random() * marginError +1);
                answerC.setText("" + displayAnswer);
            }

            if (buttonWithAnswer != 4) {
                displayAnswer = answerN - (int) (Math.random() * marginError +1);
                answerD.setText("" + displayAnswer);
            }
        }
    }

}


