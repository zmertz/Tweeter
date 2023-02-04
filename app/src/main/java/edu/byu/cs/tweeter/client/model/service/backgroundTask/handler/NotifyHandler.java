package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.model.service.observer.NotifyObserver;

public abstract class NotifyHandler<T extends NotifyObserver> extends BackgroundTaskHandler<T> {

    public NotifyHandler(T observer) {
        super(observer);
    }

    @Override
    protected void handleSuccessMessage(T observer, Bundle bundle) {
        observer.handleSuccess();
    }
}
