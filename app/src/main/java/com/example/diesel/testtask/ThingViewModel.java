package com.example.diesel.testtask;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.example.diesel.testtask.db.Thing;

import java.util.List;

public class ThingViewModel extends AndroidViewModel {
    private ThingRepository mRepository;
    private LiveData<List<Thing>> mAllThings;

    public ThingViewModel(Application application) {
        super(application);
        mRepository = new ThingRepository(application);
        mAllThings = mRepository.getAllThings();
    }

    LiveData<List<Thing>> getAllThings() {
        return mAllThings;
    }

    public void insert(Thing thing) {
        mRepository.insert(thing);
    }

    public void update(Thing thing) {
        mRepository.update(thing);
    }

    public void delete(Thing thing) {
        mRepository.delete(thing);
    }
}


