package servustech.moballusers.model;

/**
 * Created by Claudiu on 5/23/2016.
 */
public class ResultAutentification {
    private String userUUID;
    private Token token;

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public String getUserUUID() {
        return userUUID;
    }

    public void setUserUUID(String userUUID) {
        this.userUUID = userUUID;
    }


}
