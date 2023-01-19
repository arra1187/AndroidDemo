package com.example.androiddemo;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class DataModel
{
    @PrimaryKey(autoGenerate = true)
    private int nid;

    @ColumnInfo(name = "title")
    private String mTitle;
    @ColumnInfo(name = "data")
    private String mData;

    public DataModel(String title, String data)
    {
        mTitle = title;
        mData = data;
    }

    public String getTitle()
    {
        return mTitle;
    }

    public String getData()
    {
        return mData;
    }

    public int getNid() { return nid; }

    public void setNid(int value) { nid = value; }

}
