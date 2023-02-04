package edu.byu.cs.tweeter.client.presenter;

import android.view.View;

import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.observer.ServiceObserver;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class Presenter {
    View view;

    public Presenter (View view) {
        this.view = view;
    }

    public View getView() {
        return view;
    }

    public interface View {
        void goToUser(User user);
        void displayInfo(String message);
    }

    public StatusService getStatusService() {
        return new StatusService();
    }

    public abstract class BaseObserver implements ServiceObserver {
        @Override
        public void handleFailure(String message) {
            view.displayInfo(message);
        }

        @Override
        public void handleException(Exception ex) {
            view.displayInfo(ex.getMessage());
        }
    }




}
