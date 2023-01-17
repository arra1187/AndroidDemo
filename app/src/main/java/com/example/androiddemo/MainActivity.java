package com.example.androiddemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText mEditText;

    private Button mBtnLower;
    private Button mBtnClear;
    private Spinner mSpinBox;
    private ArrayList<DataModel> mRVData;
    private RecyclerView mRVList;
    private RecyclerView.Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
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
        mRVList = findViewById(R.id.recyclerLayout);
        mRVList.setLayoutManager(new LinearLayoutManager(this));

        mRVData = new ArrayList<>();
        mRVData.add(new DataModel("CS260", "Winter"));
        mRVData.add(new DataModel("CS380", "Spring"));
        mRVData.add(new DataModel("CS485", "Spring"));
        mRVData.add(new DataModel("CS300", "Fall"));

        mSpinBox.setAdapter(choiceAdapter);

        mBtnLower.setOnClickListener(
                new View.OnClickListener()
                {
                    public void onClick(View view)
                    {
                        Log.d("UIDEMO", "LOWER BUTTON");
                        mEditText.setText(mEditText.getText().toString().toLowerCase());
                        mRVData.remove(0);
                        mAdapter.notifyDataSetChanged();
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
        mRVData.add(new DataModel(mEditText.getText().toString(), mEditText.getText().toString()));
        mAdapter.notifyDataSetChanged();
    }

    public void onClickGo(View view)
    {
        Log.d("EVEMT", "GO BUTTON");
        Intent intent = new Intent(this, MainActivity2.class);

        startActivity(intent);
    }

    public void onResume()
    {
        super.onResume();
        mAdapter = new DataModelRecyclerViewAdapter(mRVData);

        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mRVList.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
            }
        });
    }

}