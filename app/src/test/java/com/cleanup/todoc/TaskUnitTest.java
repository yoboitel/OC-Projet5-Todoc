package com.cleanup.todoc;

import android.content.Context;
import androidx.test.core.app.ApplicationProvider;

import androidx.room.Room;
import com.cleanup.todoc.database.RoomDatabase;
import com.cleanup.todoc.model.Task;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for tasks
 *
 * @author Gaëtan HERFRAY
 */
@Config(manifest=Config.NONE)
@RunWith(RobolectricTestRunner.class)
public class TaskUnitTest {

    private RoomDatabase roomDatabase;

    //CREATE THE DATABASE TO USE IT
    @Before
    public void createRoomDatabase() {
        Context context = ApplicationProvider.getApplicationContext();
        roomDatabase = Room.inMemoryDatabaseBuilder(context, RoomDatabase.class).allowMainThreadQueries().build();
    }

    @After
    public void closeRoomDatabase() {
        roomDatabase.close();
    }

    @Test
    public void test_projects() {
        final Task task1 = new Task(1, 1, "task 1", new Date().getTime());
        final Task task2 = new Task(2, 2, "task 2", new Date().getTime());
        final Task task3 = new Task(3, 3, "task 3", new Date().getTime());
        final Task task4 = new Task(4, 4, "task 4", new Date().getTime());

        assertEquals("Projet Tartampion", task1.getProject().getName());
        assertEquals("Projet Lucidia", task2.getProject().getName());
        assertEquals("Projet Circus", task3.getProject().getName());
        assertNull(task4.getProject());
    }

    @Test
    public void test_az_comparator() {
        final Task task1 = new Task(1, 1, "aaa", 123);
        final Task task2 = new Task(2, 2, "zzz", 124);
        final Task task3 = new Task(3, 3, "hhh", 125);

        final ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(task1);
        tasks.add(task2);
        tasks.add(task3);
        Collections.sort(tasks, new Task.TaskAZComparator());

        assertSame(tasks.get(0), task1);
        assertSame(tasks.get(1), task3);
        assertSame(tasks.get(2), task2);
    }

    @Test
    public void test_za_comparator() {
        final Task task1 = new Task(1, 1, "aaa", 123);
        final Task task2 = new Task(2, 2, "zzz", 124);
        final Task task3 = new Task(3, 3, "hhh", 125);

        final ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(task1);
        tasks.add(task2);
        tasks.add(task3);
        Collections.sort(tasks, new Task.TaskZAComparator());

        assertSame(tasks.get(0), task2);
        assertSame(tasks.get(1), task3);
        assertSame(tasks.get(2), task1);
    }

    @Test
    public void test_recent_comparator() {
        final Task task1 = new Task(1, 1, "aaa", 123);
        final Task task2 = new Task(2, 2, "zzz", 124);
        final Task task3 = new Task(3, 3, "hhh", 125);

        final ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(task1);
        tasks.add(task2);
        tasks.add(task3);
        Collections.sort(tasks, new Task.TaskRecentComparator());

        assertSame(tasks.get(0), task3);
        assertSame(tasks.get(1), task2);
        assertSame(tasks.get(2), task1);
    }

    @Test
    public void test_old_comparator() {
        final Task task1 = new Task(1, 1, "aaa", 123);
        final Task task2 = new Task(2, 2, "zzz", 124);
        final Task task3 = new Task(3, 3, "hhh", 125);

        final ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(task1);
        tasks.add(task2);
        tasks.add(task3);
        Collections.sort(tasks, new Task.TaskOldComparator());

        assertSame(tasks.get(0), task1);
        assertSame(tasks.get(1), task2);
        assertSame(tasks.get(2), task3);
    }

    //-------------------
    //ROOM DATABASE TESTS
    //-------------------

    @Test
    public void test_task_creation(){
        final Task task1 = new Task(1, 1, "aaa", 123);
        final Task task2 = new Task(2, 2, "zzz", 124);
        final Task task3 = new Task(3, 3, "hhh", 125);

        //CREATE LIST AND ADD THE TASKS IN IT
        final ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(task1);
        tasks.add(task2);
        tasks.add(task3);

        //ADD SAME TASKS IN OUR ROOM DB
        roomDatabase.myDao().addTask(task1);
        roomDatabase.myDao().addTask(task2);
        roomDatabase.myDao().addTask(task3);
        //THEN RETRIEVE THEM IN ANOTHER LIST
        List<Task> tasksFromDB = roomDatabase.myDao().getTasks();

        //VERIFY EACH TASKS IN BOTH LIST ARE THE SAME
        assertThat(tasksFromDB.get(0).getId(), equalTo(tasks.get(0).getId()));
        assertThat(tasksFromDB.get(1).getId(), equalTo(tasks.get(1).getId()));
        assertThat(tasksFromDB.get(2).getId(), equalTo(tasks.get(2).getId()));

        //DELETE TASKS FROM OUR ROOM DB AND LIST
        tasks.remove(task1);
        tasks.remove(task2);
        tasks.remove(task3);

        roomDatabase.myDao().deleteTask(task1);
        roomDatabase.myDao().deleteTask(task2);
        roomDatabase.myDao().deleteTask(task3);
    }

    @Test
    public void test_task_deletion(){
        //REUSE THE ABOVE TEST TO CREATE AND DELETE TASKS IN OUR ROOM DB
        test_task_creation();

        //RETRIEVE THE LIST OF TASKS IN OUR ROOM DB
        List<Task> tasksFromDB = roomDatabase.myDao().getTasks();

        //VERIFIES THERE IS NO TASKS IN OUR ROOM DB
        assertTrue(tasksFromDB.isEmpty());
    }

}