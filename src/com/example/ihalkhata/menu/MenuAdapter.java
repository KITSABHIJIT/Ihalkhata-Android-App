package com.example.ihalkhata.menu;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ihalkhata.R;

public class MenuAdapter extends BaseAdapter {
	private ArrayList<MenuItem> menuItems;
	private LayoutInflater inflater;

	public MenuAdapter(Context c, ArrayList<MenuItem> m) {
		inflater = LayoutInflater.from(c);
		menuItems = m;
	}

	@Override
	public int getCount() {

		return menuItems.size();
	}

	@Override
	public Object getItem(int position) {

		return null;
	}

	@Override
	public long getItemId(int position) {

		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		final ViewHolder holder;

		if (convertView == null || convertView instanceof TextView) {
			convertView = inflater.inflate(R.layout.rbm_item, null);

			holder = new ViewHolder();
			holder.image = (ImageView) convertView
					.findViewById(R.id.rbm_item_icon);
			holder.text = (TextView) convertView
					.findViewById(R.id.rbm_item_text);

			convertView.setTag(holder);

		} else {

			holder = (ViewHolder) convertView.getTag();
		}

		holder.image.setImageResource(menuItems.get(position).icon);
		holder.text.setText(menuItems.get(position).text);

		return convertView;
	}

}
