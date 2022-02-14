package edu.byu.cs.tweeter.client.presenter;

import android.widget.EditText;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.LoginService;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.UserEntryNotificationObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class LoginPresenter extends UserPresenter{
    private final LoginService loginService;

    public LoginPresenter(UserPresenter.View view) {
        super(view);
        loginService = new LoginService();
    }

    @Override
    String getDescription() {
        return "login";
    }

    public void login(EditText alias, EditText password) {
        loginService.login(alias.getText().toString(), password.getText().toString(), new UserObserver());
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

}
