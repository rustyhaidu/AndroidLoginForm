package servustech.moballusers.model;

import java.util.ArrayList;

/**
 * Created by Claudiu on 5/20/2016.
 */
public class ResultGson {

    private String pageNumber;
    private String pageSize;
    private String size;
    private ArrayList<Item> items;

    public String getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(String pageNumber) {
        this.pageNumber = pageNumber;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return items.toString();
    }
}
