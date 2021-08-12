package com.example.tuptr.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tuptr.Model.progress;
import com.example.tuptr.R;

import java.util.List;

public class ProgressAdapter extends BaseAdapter {
    Context context;
    List<progress> valueList;
    SwipeRefreshLayout swiper;

    public ProgressAdapter(Context context, List<progress> valueList, SwipeRefreshLayout swiper) {
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
        progViewItem viewItem = null;

        if(convertView == null)
        {
            viewItem = new progViewItem();

            LayoutInflater layoutInflater = (LayoutInflater)this.context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            convertView = layoutInflater.inflate(R.layout.layout_custom, null);

            viewItem.TextViewprogName = (TextView)convertView.findViewById(R.id.items);
            viewItem.ImageViewprogName = (ImageView)convertView.findViewById(R.id.imagex);

            convertView.setTag(viewItem);
        }
        else
        {
            viewItem = (progViewItem) convertView.getTag();
        }
        viewItem.TextViewprogName.setText(valueList.get(position).proglists);
        return convertView;
    }
}

class progViewItem
{

    TextView TextViewprogName;
    ImageView ImageViewprogName;
}
