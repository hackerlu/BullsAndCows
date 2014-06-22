package com.surya.bullsandcows.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Surya Prakash on 21/6/14.
 */
public class GamePlay extends Activity {

    private String theWord = "";
    private int noOfTry = 1;
    private TableLayout resultData;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gameplay);
        theWordSelection();
    }

    //Helper Method to select a random word from the Jason File
    private void theWordSelection(){
        List <String> wordList = new ArrayList<String>();
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
                    wordList.add(jsonWordsArray.get(i).toString());
            }
            if(!wordList.isEmpty()){
                this.theWord =  wordList.get((int)Math.floor(Math.random() * wordList.size()));
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

        //Dummy blank data... for alignment.. need to look into this
//        TextView dummyValue = new TextView(this);
//        dummyValue.setText(" ");
//        dummyValue.setTextSize((float)25.0);
//        dummyValue.setGravity(Gravity.CENTER);
//        resultRow.addView(dummyValue);

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
}
