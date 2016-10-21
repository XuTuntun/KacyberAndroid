package com.kacyber.adapter;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.kacyber.R;

public class SortAdapter extends BaseAdapter implements SectionIndexer {
    private List<SortModelCities> list = null;
    private Context mContext;

    public SortAdapter(Context mContext, List<SortModelCities> list) {
        this.mContext = mContext;
        this.list = list;
    }


    public void updateListView(List<SortModelCities> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public int getCount() {
        return this.list.size();
    }

    public Object getItem(int position) {
        return list.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup arg2) {
        ViewHolder viewHolder = null;
        final SortModelCities mContent = list.get(position);
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.item, null);
            viewHolder.tvTitle = (TextView) view.findViewById(R.id.title);
            viewHolder.tvLetter = (TextView) view.findViewById(R.id.catalog);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        int section = getSectionForPosition(position);


//		if(position == getPositionForSection(section)){
//
//			viewHolder.tvLetter.setVisibility(View.VISIBLE);
//			viewHolder.tvLetter.setText(mContent.getSortLetters());
//		}else{
//
//			viewHolder.tvLetter.setVisibility(View.GONE);
//		}

        if (showLetters(position)) {
            viewHolder.tvLetter.setVisibility(View.VISIBLE);
            viewHolder.tvLetter.setText(mContent.getSortLetters().toUpperCase());
        } else {
			viewHolder.tvLetter.setVisibility(View.GONE);
        }

        viewHolder.tvTitle.setText(this.list.get(position).getName());

        return view;

    }

    private boolean showLetters(int position) {
        if (position == 0) {
            return true;
        } else {
            if (list.get(position).getSortLetters().toUpperCase().equals(list.get(position-1).getSortLetters().toUpperCase())) {
                return false;
            } else {
                return true;
            }
        }
    }


    final static class ViewHolder {
        TextView tvLetter;
        TextView tvTitle;
    }


    public int getSectionForPosition(int position) {
        return list.get(position).getSortLetters().charAt(0);
    }

    public int getPositionForSection(int section) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = list.get(i).getSortLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            Log.e("=========", "section is " + section + " firstChar is " + firstChar);
            if (firstChar == section) {
                return i;
            }
        }

        return -1;
    }


    private String getAlpha(String str) {
        String sortStr = str.trim().substring(0, 1).toUpperCase();
        if (sortStr.matches("[A-Z]")) {
            return sortStr;
        } else {
            return "#";
        }
    }

    @Override
    public Object[] getSections() {
        return null;
    }
}