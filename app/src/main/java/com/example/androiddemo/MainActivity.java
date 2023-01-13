package com.example.androiddemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {

    private EditText mEditText;

    private Button mBtnLower;
    private Button mBtnClear;
    private Spinner mSpinBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        String[] choiceArray = new String[] {"Strain", "Price", "Pacific"};
        ArrayAdapter<String> choiceAdapter
                = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,
                choiceArray);
        choiceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEditText = findViewById(R.id.editTextEntryBox);
        mBtnLower = findViewById(R.id.btnLower);
        mBtnClear = findViewById(R.id.clear_button);
        mSpinBox = findViewById(R.id.spinbox);
        mSpinBox.setAdapter(choiceAdapter);

        mBtnLower.setOnClickListener(
                new View.OnClickListener()
                {
                    public void onClick(View view)
                    {
                        Log.d("UIDEMO", "LOWER BUTTON");
                        mEditText.setText(mEditText.getText().toString().toLowerCase());
                    }
                }
        );

        mBtnClear.setOnClickListener(
                new View.OnClickListener()
                {
                    public void onClick(View view)
                    {
                        Log.d("UIDEMO", "CLEAR BUTTON");
                        mEditText.setText("");
                    }
                }
        );

        Intent intent = getIntent();
        if(null != intent)
        {
            DataStore myData;
            mEditText.setText(intent.getStringExtra(Intent.EXTRA_TEXT));
            myData = intent.getParcelableExtra("DATA");
            if(myData != null)
            {
                mEditText.setText(myData.toString());
            }
        }
    }

    public void onClick(View view)
    {
        mEditText.setText((String) mSpinBox.getSelectedItem());
    }

    public void onClickGo(View view)
    {
        Log.d("EVEMT", "GO BUTTON");
        Intent intent = new Intent(this, MainActivity2.class);

        startActivity(intent);
    }


}