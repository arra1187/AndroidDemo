package com.example.androiddemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class MainActivity2 extends AppCompatActivity
{

    private EditText mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        mData = findViewById(R.id.editTextData);
    }

    public void onClickGo(View view)
    {
        DataStore myData = new DataStore("hi", "CS260");
        Log.d("EVENT", "GO BUTTON PRESSED");

        Intent intent = new Intent(this, MainActivity.class);
        String message = mData.getText().toString();

        intent.putExtra(Intent.EXTRA_TEXT, message);

        startActivity(intent);
    }
}