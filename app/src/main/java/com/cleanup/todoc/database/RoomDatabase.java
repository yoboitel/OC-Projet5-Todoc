package com.cleanup.todoc.database;

import androidx.room.Database;

import com.cleanup.todoc.model.Task;

@Database(entities = {Task.class}, version = 1, exportSchema = false)

public abstract class RoomDatabase extends androidx.room.RoomDatabase {

    public  abstract RoomDao myDao();

}
