package com.surya.bullsandcows.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by Surya Prakash on 21/6/14.
 */
public class GamePlay extends Activity {

    private String theWord = "";
    private int noOfTry = 1;
    private TableLayout resultData;
    List <String> wordList = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gameplay);
        theWordSelection();
    }

    //To select a random word from the Jason File
    private void theWordSelection(){
        this.wordList = new ArrayList<String>();
        String wordsString = "";
        String line = "";
        JSONArray jsonWordsArray = null;
        try {
        BufferedReader stream =
                new BufferedReader(new InputStreamReader(getResources().openRawResource(R.raw.words)));
            while ((line = stream.readLine()) != null) {
                wordsString += line;
                jsonWordsArray = new JSONArray(wordsString);
            }
            for (int i = 0; i < jsonWordsArray.length(); i++) {
                    this.wordList.add(jsonWordsArray.get(i).toString());
            }
            if(!wordList.isEmpty()){
                this.theWord =  this.wordList.get((int)Math.floor(Math.random() * this.wordList.size()));
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    /** Called when the user clicks the Enter button */
    public void verifyWord(View view) {
        // Do something in response to button
        int bullCount = 0;
        int cowCount = 0;
        EditText editText   = (EditText)findViewById(R.id.editText);
        String enteredWord = editText.getText().toString().trim();
        boolean wordBool = true;
        wordBool = validateWord(enteredWord);

        if(wordBool){
        // To make the results lay out visible
            LinearLayout layoutVisibility = (LinearLayout)findViewById(R.id.gameLayout);
            if(layoutVisibility.getVisibility() != View.VISIBLE)
            {
                layoutVisibility.setVisibility(View.VISIBLE);
            }
            //The Bull comparission
            String word1 = this.theWord;
            String word2 = enteredWord;
            char[] first  = word1.toLowerCase().toCharArray();
            char[] second = word2.toLowerCase().toCharArray();
            int wordLength = word1.length();

            //Method call to get the number of Bulls and Cows
            bullCount = this.theBullCounter(first,second,wordLength);
            cowCount = this.theCowCounter(first,second,wordLength);

            //Getting the existing Table Layout
            this.resultData = (TableLayout) findViewById(R.id.tableLayout2);
            //Creating the object for the new Row
            TableRow resultRow = new TableRow(this);
            //Setting the number of Tries in the row
            TextView attemptCnt = new TextView(this);
            attemptCnt.setText(Integer.toString(this.noOfTry++));
            attemptCnt.setTextSize((float) 25.0);
            attemptCnt.setGravity(Gravity.CENTER);
            resultRow.addView(attemptCnt);

            //Setting the word entered in the row
            TextView enteredWord2 = new TextView(this);
            enteredWord2.setText(enteredWord);
            enteredWord2.setTextSize((float)25.0);
            enteredWord2.setGravity(Gravity.LEFT);
            resultRow.addView(enteredWord2);
            //Setting the Bull Count in the row
            TextView bulls = new TextView(this);
            bulls.setText(Integer.toString(bullCount));
            bulls.setTextSize((float)25.0);
            bulls.setGravity(Gravity.CENTER);
            resultRow.addView(bulls);

            //Setting the Cow Count in the row
            TextView cows = new TextView(this);
            cows.setText(Integer.toString(cowCount));
            cows.setTextSize((float)25.0);
            cows.setGravity(Gravity.CENTER);
            resultRow.addView(cows);

            //Finally Setting the Row in Table Layout fetched
            //(resultRow, 1) Helps in adding the values to the top of the Table layout... useful!!
            this.resultData.addView(resultRow, 1);
            //Clearing Text after result is displayed
            editText.setText("", TextView.BufferType.EDITABLE);

            //The Winning moment
            if(this.theWord.equalsIgnoreCase(enteredWord)){
                Intent intent = new Intent(getApplicationContext(), WinClass.class);
                startActivity(intent);
            }
        }
    }

    private int theBullCounter(char[] first,char[] second,int wordLength){
        int bullCounter = 0;
        for(int i = 0; i < wordLength; i++)
        {
            if (first[i] == second[i])
            {
                bullCounter++;
            }
        }
        return bullCounter;
    }

    private int theCowCounter(char[] first,char[] second,int wordLength){
        int cowCounter = 0;
        for(int i = 0; i < wordLength; i++){
            for(int j = 0; j < wordLength; j++){
                if(first[i] == second[j] && i!=j){
                    cowCounter++;
                }
            }
        }
        return cowCounter;
    }

    // To Validate the entered word
    private boolean validateWord(String word){
        boolean containsUnique = false;
        EditText editText   = (EditText)findViewById(R.id.editText);
        if(word.length() < 4){
            showPopup(GamePlay.this);
            editText.setText("");
            return false;
        }
        HashSet<Character> m = new HashSet<Character>();
        for(char c : word.toLowerCase().toCharArray()){
            m.add(c);
        }
        if (m.size() < 4)
        {
            showPopup(GamePlay.this);
            editText.setText("");
            return false;
        }

        if(!wordList.contains(word.toLowerCase())){
            showPopup2(GamePlay.this);
            editText.setText("");
            return false;
        }
        return true;
    }

        //To display the error pop up and its size informations
        private void showPopup(final Activity context) {
            int popupWidth = 800;
            int popupHeight = 350;

            // Inflate the fourletterscheck.xml
            RelativeLayout viewGroup = (RelativeLayout) context.findViewById(R.layout.fourletterscheck);
            LayoutInflater layoutInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = layoutInflater.inflate(R.layout.fourletterscheck, viewGroup);

            // Creating the PopupWindow
            final PopupWindow popup = new PopupWindow(context);
            popup.setContentView(layout);
            popup.setWidth(popupWidth);
            popup.setHeight(popupHeight);
            popup.setFocusable(true);

            // Some offset to align the popup a bit to the right, and a bit down, relative to button's position.
            int OFFSET_X = 0;
            int OFFSET_Y = 0;
            // Displaying the popup at the specified location, + offsets.
            Point p = new Point();
            popup.showAtLocation(layout, Gravity.NO_GRAVITY,  OFFSET_X, OFFSET_Y);
            // Getting a reference to Close button, and close the popup when clicked.
            Button close = (Button) layout.findViewById(R.id.buttonOk);
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popup.dismiss();
                }
            });
    }


    //To display the wrong word pop up
    private void showPopup2(final Activity context) {
        int popupWidth = 800;
        int popupHeight = 350;

        // Inflate the noword_popup.xml.xml
        RelativeLayout viewGroup = (RelativeLayout) context.findViewById(R.layout.noword_popup);
        LayoutInflater layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.noword_popup, viewGroup);

        // Creating the PopupWindow
        final PopupWindow popup = new PopupWindow(context);
        popup.setContentView(layout);
        popup.setWidth(popupWidth);
        popup.setHeight(popupHeight);
        popup.setFocusable(true);

        // Some offset to align the popup a bit to the right, and a bit down, relative to button's position.
        int OFFSET_X = 0;
        int OFFSET_Y = 60;
        // Displaying the popup at the specified location, + offsets.
        Point p = new Point();
        popup.showAtLocation(layout, Gravity.NO_GRAVITY,  OFFSET_X, OFFSET_Y);
        // Getting a reference to Close button, and close the popup when clicked.
        Button close = (Button) layout.findViewById(R.id.buttonOk2);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.dismiss();
            }
        });
    }
}

