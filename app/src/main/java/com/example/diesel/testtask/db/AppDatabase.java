package com.example.diesel.testtask.db;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

@Database(entities = {Thing.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ThingDAO thingDAO();

    private static AppDatabase instance;

    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    AppDatabase.class,
                    "database").addCallback(sCallback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback sCallback
            = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsync(instance).execute();

        }
    };

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {
        private final ThingDAO mDao;

        PopulateDbAsync(AppDatabase db) {
            mDao = db.thingDAO();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Thing thing = new Thing(1,"new", false);
            mDao.insert(thing);
            thing = new Thing(2,"thing", true);
            mDao.insert(thing);
            return null;
        }
    }

    public static void destroyInstance() {
        instance = null;
    }
}
