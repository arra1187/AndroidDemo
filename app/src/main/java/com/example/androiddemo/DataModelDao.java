package com.example.androiddemo;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DataModelDao
{
  @Query ("Select * from DataModel")
  List<DataModel> getAll();

  @Query("Select * FROM DataModel WHERE nid = :id")
  DataModel findByID(int id);

  @Insert
  void insert(DataModel data);

  @Insert
  void delete(DataModel ... data);

  @Delete
  void delete(DataModel data);

  @Query("Delete FROM DataModel WHERE nid = :id")
  void deleteByID(int id);

  @Query("Delete from DataModel")
  void deleteAll();

  @Query("Select count(*) from DataModel")
  int getSize();

  @Query("Select * from DataMOdel WHERE title like :str")
  List<DataModel> getAllContains(String str);
}
