package com.example.androiddemo;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {DataModel.class}, version = 1, exportSchema = false)
public abstract class DataModelDatabase extends RoomDatabase
{
  public abstract DataModelDao dataModelDao();
}
