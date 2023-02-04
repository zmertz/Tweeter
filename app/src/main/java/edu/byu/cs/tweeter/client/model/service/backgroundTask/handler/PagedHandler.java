package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import android.os.Bundle;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.PagedTask;
import edu.byu.cs.tweeter.client.model.service.observer.PagedInterfaceObserver;

public abstract class PagedHandler<T extends PagedInterfaceObserver<U>, U> extends BackgroundTaskHandler<T> {
    public PagedHandler(T observer) {
        super(observer);
    }
    @Override
    protected void handleSuccessMessage(T observer, Bundle bundle) {
        List<U> items = (List<U>) bundle.getSerializable(PagedTask.ITEMS_KEY);
        boolean hasMorePages = bundle.getBoolean(PagedTask.MORE_PAGES_KEY);
        observer.handleSuccess(items, hasMorePages);
    }
}
