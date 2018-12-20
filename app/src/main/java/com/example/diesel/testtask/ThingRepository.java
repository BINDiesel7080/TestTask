package com.example.diesel.testtask;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.example.diesel.testtask.db.AppDatabase;
import com.example.diesel.testtask.db.Thing;
import com.example.diesel.testtask.db.ThingDAO;

import java.util.List;

public class ThingRepository {
    private ThingDAO mThingDao;
    private LiveData<List<Thing>> mAllThings;

    ThingRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        mThingDao = db.thingDAO();
        mAllThings = mThingDao.getAll();
    }

    LiveData<List<Thing>> getAllThings() {
        return mAllThings;
    }

    public void insert(Thing thing) {
        new insertAsyncTask(mThingDao).execute(thing);
    }

    private static class insertAsyncTask
            extends AsyncTask<Thing, Void, Void> {

        private ThingDAO mAsyncTaskDao;

        insertAsyncTask(ThingDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Thing... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    public void update(Thing thing) {
        new updateAsyncTask(mThingDao).execute(thing);
    }

    private static class updateAsyncTask
            extends AsyncTask<Thing, Void, Void> {

        private ThingDAO mAsyncTaskDao;

        updateAsyncTask(ThingDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Thing... params) {
            mAsyncTaskDao.update(params[0]);
            return null;
        }
    }

    public void delete(Thing thing) {
        new deleteAsyncTask(mThingDao).execute(thing);
    }

    private static class deleteAsyncTask
            extends AsyncTask<Thing, Void, Void> {

        private ThingDAO mAsyncTaskDao;

        deleteAsyncTask(ThingDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Thing... params) {
            mAsyncTaskDao.delete(params[0]);
            return null;
        }
    }

}
