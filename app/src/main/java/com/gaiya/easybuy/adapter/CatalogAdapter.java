
package com.gaiya.easybuy.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gaiya.easybuy.R;
import com.gaiya.easybuy.model.CataLogModel;
import com.gaiya.easybuy.util.ViewUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dengt on 15-10-8.
 */
public class CatalogAdapter extends BaseAdapter {
    List<CataLogModel> items;
    private Activity context;

    public CatalogAdapter(List<CataLogModel> cataLogModels, Activity activity) {
        if (items == null) {
            items = new ArrayList<CataLogModel>();
        }
        this.items = cataLogModels;
        this.context = activity;
    }

    public void updateListView(List<CataLogModel> list) {
        this.items = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return items == null ? 0 : items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_my_spinner,
                    parent, false);
        }
        TextView name=ViewUtil.findViewById(convertView,android.R.id.text1);
        name.setText(items.get(position).getName());
        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_spinner_dropdown,
                    parent, false);
            holder = new Holder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        holder.name.setText(items.get(position).getName());
        return convertView;
    }

    class Holder {
        TextView name;

        public Holder(View view) {
            name = ViewUtil.findViewById(view, android.R.id.text1);
        }
    }
}
