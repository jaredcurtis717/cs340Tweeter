package edu.byu.cs.tweeter.client.presenter;

import android.content.Intent;
import android.widget.EditText;
import android.widget.Toast;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.LoginService;
import edu.byu.cs.tweeter.client.view.main.MainActivity;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class LoginPresenter {
    public interface View{
        void displayErrorMessage(String message);
        void loggedIn(User user);
    }
    private View view;
    private LoginService loginService;

    public LoginPresenter(View view){
        this.view = view;
        loginService = new LoginService();
    }

    public void login(EditText alias, EditText password){
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

    private class LoginObserver implements LoginService.LoginObserver{

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
