package edu.byu.cs.tweeter.server.dao;

import edu.byu.cs.tweeter.server.dao.interfaces.IAuthDAO;
import edu.byu.cs.tweeter.server.dao.interfaces.IDAOFactory;
import edu.byu.cs.tweeter.server.dao.interfaces.IFollowDAO;
import edu.byu.cs.tweeter.server.dao.interfaces.IStatusDAO;
import edu.byu.cs.tweeter.server.dao.interfaces.IUserDAO;

public class DAOFactory implements IDAOFactory {
    @Override
    public IStatusDAO getStatusDAO() {
        return new StatusDAO();
    }

    @Override
    public IFollowDAO getFollowDAO() {
        return new FollowDAO();
    }

    @Override
    public IUserDAO getUserDAO() {
        return new UserDAO();
    }

    @Override
    public IAuthDAO getAuthDAO() {
        return new AuthDAO();
    }

}
