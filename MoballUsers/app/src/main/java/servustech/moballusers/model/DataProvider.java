package servustech.moballusers.model;

/**
 * Created by Claudiu on 5/19/2016.
 */
public class DataProvider {

    private String name;
    private String value;

    public DataProvider(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String surname) {
        this.value = surname;
    }


}