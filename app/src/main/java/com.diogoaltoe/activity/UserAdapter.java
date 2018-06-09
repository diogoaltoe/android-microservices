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
import com.diogoaltoe.model.User;

public class UserAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<User> list;
    private Context context;

    public UserAdapter(ArrayList<User> list, Context context) {
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
        return 0;
        //just return 0 if your list items do not have an Id variable.
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.user_list_item, null);
        }

        //Handle TextView and display string from your list
        TextView textViewName = (TextView)view.findViewById(R.id.textViewName);
        textViewName.setText(list.get(position).getName());

        TextView textViewEmail = (TextView)view.findViewById(R.id.textViewEmail);
        textViewEmail.setText(list.get(position).getEmail());

        return view;
    }
}