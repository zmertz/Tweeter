package edu.byu.cs.tweeter.server.dao.interfaces;

public interface IDAOFactory {
    IStatusDAO getStatusDAO();
    IFollowDAO getFollowDAO();
    IUserDAO getUserDAO();
    IAuthDAO getAuthDAO();
}
