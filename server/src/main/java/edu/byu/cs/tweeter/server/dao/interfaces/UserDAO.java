package edu.byu.cs.tweeter.server.dao.interfaces;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;
import edu.byu.cs.tweeter.util.DataAccessException;

public interface UserDAO {
    User getUser(String userHandle) throws DataAccessException;
    User registerUser(RegisterRequest request) throws DataAccessException;
}
