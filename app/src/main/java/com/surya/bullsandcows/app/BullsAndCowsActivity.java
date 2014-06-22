package com.surya.bullsandcows.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;


public class BullsAndCowsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    /** Called when the user clicks the Start button */
    public void gotToGamePage(View view) {
        // Do something in response to button
        Intent intent = new Intent(getApplicationContext(), GamePlay.class);
        startActivity(intent);
    }

}
