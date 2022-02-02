package edu.byu.cs.tweeter.client.presenter;

import android.widget.EditText;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.LoginService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class LoginPresenter {
    private final View view;
    private final LoginService loginService;
    public LoginPresenter(View view) {
        this.view = view;
        loginService = new LoginService();
    }

    public void login(EditText alias, EditText password) {
        loginService.login(alias.getText().toString(), password.getText().toString(), new LoginObserver());
    }

    public void validateLogin(EditText alias, EditText password) throws IllegalArgumentException {
        if (alias.getText().charAt(0) != '@') {
            throw new IllegalArgumentException("Alias must begin with @.");
        }
        if (alias.getText().length() < 2) {
            throw new IllegalArgumentException("Alias must contain 1 or more characters after the @.");
        }
        if (password.getText().length() == 0) {
            throw new IllegalArgumentException("Password cannot be empty.");
        }
    }

    public interface View {
        void displayErrorMessage(String message);

        void loggedIn(User user);
    }

    private class LoginObserver implements LoginService.LoginObserver {

        @Override
        public void handleSuccess(User loggedInUser, AuthToken authToken) {
            // Cache user session information
            Cache.getInstance().setCurrUser(loggedInUser);
            Cache.getInstance().setCurrUserAuthToken(authToken);
            view.loggedIn(loggedInUser);
        }

        @Override
        public void handleFailure(String message) {
            view.displayErrorMessage("Failed to login: " + message);
        }

        @Override
        public void handleException(Exception exception) {
            view.displayErrorMessage("Failed to login because of exception: " + exception.getMessage());
        }
    }
}
