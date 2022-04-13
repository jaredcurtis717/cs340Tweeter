package edu.byu.cs.tweeter.server.dao.interfaces;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;
import edu.byu.cs.tweeter.util.DataAccessException;

public interface UserDAO {
    User getUser(String userHandle) throws DataAccessException;
    User registerUser(RegisterRequest request) throws DataAccessException;
    User login(LoginRequest request) throws DataAccessException;
    void addUserBatch(List<User> users);
}
