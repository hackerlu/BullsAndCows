package com.surya.bullsandcows.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

/**
 * Created by Surya Prakash on 22/6/14.
 */
public class WinClass extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.win);
    }
    /** Called when the user clicks the Start button */
    public void reloadGame(View view) {
        // Do something in response to button
        Intent intent = new Intent(getApplicationContext(), GamePlay.class);
        startActivity(intent);
    }
}

