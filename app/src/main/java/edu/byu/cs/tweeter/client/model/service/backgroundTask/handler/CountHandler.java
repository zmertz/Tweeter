package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetCountTask;
import edu.byu.cs.tweeter.client.model.service.observer.CountObserver;

public abstract class CountHandler<T extends CountObserver> extends BackgroundTaskHandler<T> {
    public CountHandler(T observer) {
        super(observer);
    }

    @Override
    protected void handleSuccessMessage(T observer, Bundle bundle) {
        observer.handleSuccess(bundle.getInt(GetCountTask.COUNT_KEY));
    }
}
