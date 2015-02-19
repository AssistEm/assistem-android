package seniorproject.caretakers.caretakersapp.objects;

public abstract class User {

    long mUserId;
    String mUserName;
    Community mCommunity;

    public User(long userId, String userName, Community community) {
        mUserId = userId;
        mUserName = userName;
        mCommunity = community;
    }

    public long getUserId() {
        return mUserId;
    }

    public String getUserName() {
        return mUserName;
    }

    public Community getCommunity() {
        return mCommunity;
    }
}
