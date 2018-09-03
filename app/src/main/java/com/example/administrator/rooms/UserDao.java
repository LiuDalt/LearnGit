package com.example.administrator.rooms;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(User user);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<User> userList);

    @Delete
    int delete(User user);

    @Delete
    void deleteList(List<User> userList);

    @Update
    int update(User user);

    @Update
    void updateList(List<User> userList);

    @Query("select * from users_tb where user_age= :age")
    int queryByAge(int age);

    @Query("select * from users_tb")
    List<User> queryAll();

    @Query("select user_name, user_age from users_tb")
    List<SimpleInfo> querySimpleInfo();
}
