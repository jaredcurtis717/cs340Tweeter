package edu.byu.cs.tweeter.server.dao.interfaces;

import edu.byu.cs.tweeter.model.domain.User;

public interface UserDAO {
    User getUser(String userHandle);
}
