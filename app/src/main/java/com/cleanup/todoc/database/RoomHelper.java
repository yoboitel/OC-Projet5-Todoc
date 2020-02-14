package com.cleanup.todoc.database;

import android.os.AsyncTask;
import com.cleanup.todoc.model.Task;
import com.cleanup.todoc.ui.MainActivity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class RoomHelper {

    //ASYNCTASK TO ADD TASK
    public static class AddASync extends AsyncTask<Task, Void, Void> {
        private WeakReference<MainActivity> activityReference;

        public AddASync(MainActivity mainActivity){
            activityReference = new WeakReference<>(mainActivity);
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            MainActivity activity = activityReference.get();
            if (activity != null && !activity.isFinishing()) {
                for (Task task : tasks) {
                    task.setId(MainActivity.roomDatabase.myDao().addTask(task));
                    activity.tasks.add(task);
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            MainActivity activity = activityReference.get();
            if (activity != null && !activity.isFinishing()) {
                activity.updateTasks();
            }
        }
    }

    //ASYNCTASK TO DELETE TASK
    public static class DeleteTaskASync extends AsyncTask<Task, Void, Void> {
        private WeakReference<MainActivity> activityReference;

        public DeleteTaskASync(MainActivity mainActivity) {
            activityReference = new WeakReference<>(mainActivity);
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            MainActivity activity = activityReference.get();
            if (activity != null && !activity.isFinishing()) {
                for (Task task : tasks) {
                    MainActivity.roomDatabase.myDao().deleteTask(task);
                    activity.tasks.remove(task);
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            MainActivity activity = activityReference.get();
            if (activity != null && !activity.isFinishing()) {
                activity.updateTasks();
            }
        }
    }

    //ASYNCTASK TO GET TASKS
    public static class GetTasksAsync extends AsyncTask<Void, Void, Void> {
        private WeakReference<MainActivity> activityReference;

        public GetTasksAsync(MainActivity mainActivity) {
            activityReference = new WeakReference<>(mainActivity);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            MainActivity activity = activityReference.get();
            if (activity != null && !activity.isFinishing()) {
                activity.tasks = (ArrayList<Task>) MainActivity.roomDatabase.myDao().getTasks();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            MainActivity activity = activityReference.get();
            if (activity != null && !activity.isFinishing()) {
                activity.updateTasks();
            }
        }
    }
}
