package com.cleanup.todoc.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;

import java.util.List;

@Dao
public interface RoomDao {

    //TODO : Task DAO

    @Insert
    long addTask(Task task);

    @Delete
    void deleteTask(Task task);

    @Query("SELECT * FROM task")
    List<Task> getTasks();


    //TODO : Project DAO

    @Insert
    long addProject(Project project);

    @Delete
    void deleteProject(Project project);

    @Query("SELECT * FROM project")
    List<Project> getProjects();

}
