package com.example.tuptr.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.tuptr.Model.departments;
import com.example.tuptr.R;

import java.util.List;

public class DepartmentAdapter extends BaseAdapter {
    Context context;
    List<departments> valueList;
    SwipeRefreshLayout swiper;

    public DepartmentAdapter(Context context, List<departments> valueList, SwipeRefreshLayout swiper) {
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
        deptViewItem viewItem = null;

        if(convertView == null)
        {
            viewItem = new deptViewItem();

            LayoutInflater layoutInflater = (LayoutInflater)this.context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            convertView = layoutInflater.inflate(R.layout.layout_items, null);

            viewItem.TextViewdeptName = (TextView)convertView.findViewById(R.id.textView1);

            convertView.setTag(viewItem);
        }
        else
        {
            viewItem = (deptViewItem) convertView.getTag();
        }

        viewItem.TextViewdeptName.setText(valueList.get(position).deptName);

        return convertView;
    }
}

class deptViewItem
{

    TextView TextViewdeptName;

}
