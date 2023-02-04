package edu.byu.cs.tweeter.model.net.response;

public class IsFollowingResponse extends Response{
    private boolean isFollowing;
    IsFollowingResponse(boolean success) {
        super(success);
        this.isFollowing = success;
    }

    public IsFollowingResponse(boolean success, String message) {
        super(success, message);
    }

    public void setIsFollowing(boolean bool){
        isFollowing = bool;
    }

    public boolean getIsFollowing(){
        return isFollowing;
    }
}
