package com.diogoaltoe.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import com.diogoaltoe.R;
import com.diogoaltoe.model.Customer;

public class CustomerAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<Customer> list;
    private Context context;

    public CustomerAdapter(ArrayList<Customer> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int pos) {
        return list.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return list.get(pos).getId();
        //just return 0 if your list items do not have an Id variable.
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.customer_list_item, null);
        }

        //Handle TextView and display string from your list
        TextView textViewId = (TextView)view.findViewById(R.id.textViewId);
        textViewId.setText(Integer.toString(list.get(position).getId()));

        TextView textViewFirstName = (TextView)view.findViewById(R.id.textViewFirstName);
        textViewFirstName.setText(list.get(position).getFirstName());

        TextView textViewLastName = (TextView)view.findViewById(R.id.textViewLastName);
        textViewLastName.setText(list.get(position).getLastName());

        return view;
    }
}