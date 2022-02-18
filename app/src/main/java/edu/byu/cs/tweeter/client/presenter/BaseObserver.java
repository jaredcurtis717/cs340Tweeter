package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.ServiceObserver;

public abstract class BaseObserver implements ServiceObserver {
    Presenter.View baseView;

    public BaseObserver(Presenter.View view){
        baseView = view;
    }

    abstract String getDescription();

    @Override
    public void handleFailure(String message) {
        baseView.clearInfoMessage();
        baseView.displayErrorMessage("Failed to get " + getDescription() + ": " + message);
    }

    @Override
    public void handleException(Exception exception) {
        baseView.clearInfoMessage();
        baseView.displayErrorMessage("Failed to get " + getDescription() + " because of exception: " + exception.getMessage());
    }

}
