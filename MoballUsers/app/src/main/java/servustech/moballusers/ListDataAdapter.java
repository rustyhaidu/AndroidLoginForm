package servustech.moballusers;

/**
 * Created by Claudiu on 5/19/2016.
 */
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import servustech.moballusers.model.DataProvider;

/**
 * Created by claudiu.haidu on 7/24/2015.
 */

public class ListDataAdapter extends ArrayAdapter {
    DataProvider dataProvider;
    List list = new ArrayList();

    public ListDataAdapter(Context context, int resource) {
        super(context, resource);
    }

    public void add(Object object) {
        super.add(object);
        list.add(object);
    }

    public int getCount() {
        return list.size();
    }

    public Object getItem(int position) {
        return list.get(position);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        LayoutHandler layoutHandler;
        if (row == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.rowlayout, parent, false);
            layoutHandler = new LayoutHandler();
            layoutHandler.exchangeName = (TextView) row.findViewById(R.id.tvExchangeName);
            layoutHandler.exchangeValue = (TextView) row.findViewById(R.id.tvExchangeValue);
            row.setTag(layoutHandler);
        } else {
            layoutHandler = (LayoutHandler) row.getTag();
        }
        dataProvider = (DataProvider) this.getItem(position);
        layoutHandler.exchangeName.setText(dataProvider.getName());
        layoutHandler.exchangeValue.setText(dataProvider.getValue());

        return row;
    }

    static class LayoutHandler {
        TextView exchangeName, exchangeValue;
    }

}