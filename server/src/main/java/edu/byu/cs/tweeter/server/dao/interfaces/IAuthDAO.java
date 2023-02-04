package edu.byu.cs.tweeter.server.dao.interfaces;

public interface IAuthDAO {
    void addToken(String token, String timeStamp);
    String validateToken(String token);
}
