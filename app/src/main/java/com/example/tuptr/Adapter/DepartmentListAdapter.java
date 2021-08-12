package com.example.tuptr.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.tuptr.Model.courses;
import com.example.tuptr.Model.departments;
import com.example.tuptr.R;

import java.util.HashMap;
import java.util.List;

public class DepartmentListAdapter extends BaseExpandableListAdapter {
    Context context;
    List<departments> parent;
    HashMap<String, List<courses>> child;
    SwipeRefreshLayout swiper;

    public DepartmentListAdapter(Context context, List<departments> parent, HashMap<String, List<courses>> child, SwipeRefreshLayout swiper) {
        this.context = context;
        this.parent = parent;
        this.child = child;
        this.swiper = swiper;
    }


    @Override
    public int getGroupCount() {
        return parent.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return child.get(this.parent.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.parent.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        String childTitle = (String) getGroup(childPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item, null);
        }

        TextView lblListChild = (TextView) convertView
                .findViewById(R.id.lblListItem);
        lblListChild.setTypeface(null, Typeface.BOLD);
        lblListChild.setText(childTitle);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
