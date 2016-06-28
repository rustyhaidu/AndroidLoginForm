package servustech.moballusers.model;

/**
 * Created by Claudiu on 5/20/2016.
 */
public class Token {

    private String username;
    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


}
