package com.example.androiddemo;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity
{
    public static final int GRAPHICS_OK = 69;
    private EditText mEditText;

    private Button mBtnLower;
    private Button mBtnClear;
    private Spinner mSpinBox;
    private ArrayList<DataModel> mRVData;
    private RecyclerView mRVList;
    private RecyclerView.Adapter mAdapter;
    private ImageView mImageView;

    private DataModelDatabase mDB;
    private DataModelDao mDataModelDao;
    private ExecutorService mExecutor;
    private TextView mDBView;

    private ActivityResultLauncher<Intent> mActivityLauncher;

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
        mRVList = findViewById(R.id.recyclerView);
        mRVList.setLayoutManager(new LinearLayoutManager(this));
        mDBView = findViewById(R.id.databaseView);
        mImageView = findViewById (R.id.imageView);

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

        /*Intent intent = getIntent();
        if(null != intent)
        {
            DataStore myData;
            mEditText.setText(intent.getStringExtra(Intent.EXTRA_TEXT));
            myData = intent.getParcelableExtra("DATA");
            if(myData != null)
            {
                mEditText.setText(myData.toString());

                if(myData.getImage() != null)
                {
                    Bitmap bmp = BitmapFactory.decodeByteArray(
                        myData.getImage(), 0,
                        myData.getImage().length);

                    mImageView.setImageBitmap(bmp);
                }
            }
        }*/

        mExecutor = Executors.newSingleThreadExecutor ();
        mExecutor.execute(() -> {
           mDB = Room.databaseBuilder (getApplicationContext (),
               DataModelDatabase.class, "DataModel-db").build();

           mDataModelDao = mDB.dataModelDao ();
           if(0 == mDataModelDao.getSize())
           {
               for(DataModel dm : mRVData)
               {
                   mDataModelDao.insert(dm);
               }
           }
        });

        mActivityLauncher = registerForActivityResult (
            new ActivityResultContracts.StartActivityForResult (),
            new ActivityResultCallback<ActivityResult>()
            {
                @Override
                public void onActivityResult (ActivityResult result)
                {
                    if(result.getResultCode() == Activity.RESULT_OK)
                    {
                        Intent intent = getIntent();
                        if(null != intent)
                        {
                            DataStore myData;
                            mEditText.setText(intent.getStringExtra(Intent.EXTRA_TEXT));
                            myData = intent.getParcelableExtra("DATA");
                            if(myData != null)
                            {
                                mEditText.setText(myData.toString());

                                if(myData.getImage() != null)
                                {
                                    Bitmap bmp = BitmapFactory.decodeByteArray(
                                        myData.getImage(), 0,
                                        myData.getImage().length);

                                    mImageView.setImageBitmap(bmp);
                                }
                            }
                        }
                    }
                }
            });
    }

    public void onClick(View view)
    {
        mEditText.setText ((String) mSpinBox.getSelectedItem ());
        mRVData.add (new DataModel (mEditText.getText ().toString (),
            mEditText.getText ().toString ()));
        mAdapter.notifyDataSetChanged ();

        mExecutor.execute (() -> {
            mDataModelDao.insert (
                new DataModel (mEditText.getText ().toString (), "INSERTED"));
            List<DataModel> theData = mDataModelDao.getAll ();

            view.post (() -> mDBView.setText (""));
            for (DataModel dm : theData)
            {
                //Sends work to UI thread
                view.post (() -> mDBView.append (
                    dm.getTitle () + " : " + dm.getData () + "\n"));
            }
        });
    }

    public void onClickGo(View view)
    {
        Log.d("EVENT", "GO BUTTON");
        Intent intent = new Intent(this, MainActivity2.class);

        //startActivity(intent);
        mActivityLauncher.launch(intent);
    }

    public void onClickGraphics(View view)
    {
        Log.d("EVENT", "GRAPHICS BUTTON");
        Intent intent = new Intent(this, Graphics_Activity.class);

        //startActivity(intent);
        mActivityLauncher.launch(intent);
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