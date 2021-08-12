package com.example.tuptr.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.tuptr.Model.apps;
import com.example.tuptr.R;

import java.util.List;

public class AppAdapter extends BaseAdapter {
    Context context;
    List<apps> valueList;
    SwipeRefreshLayout swiper;

    public AppAdapter(Context context, List<apps> valueList, SwipeRefreshLayout swiper) {
        this.context = context;
        this.valueList = valueList;
        this.swiper = swiper;
    }
    @Override
    public int getCount()
    {
        return this.valueList.size();
    }

    @Override
    public Object getItem(int position)
    {
        return this.valueList.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        appViewItem viewItem = null;

        if(convertView == null)
        {
            viewItem = new appViewItem();

            LayoutInflater layoutInflater = (LayoutInflater)this.context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            convertView = layoutInflater.inflate(R.layout.layout_items, null);

            viewItem.TextViewappName = (TextView)convertView.findViewById(R.id.textView1);

            convertView.setTag(viewItem);
        }
        else
        {
            viewItem = (appViewItem) convertView.getTag();
        }

        viewItem.TextViewappName.setText(valueList.get(position).applists);

        return convertView;
    }
}

class appViewItem
{

    TextView TextViewappName;
}
