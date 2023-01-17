package com.example.androiddemo;

public class DataModel
{
    private String mTitle;
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
}
