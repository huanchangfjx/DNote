package com.brcorner.dnote.android.adapter;

import java.util.List;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.brcorner.dnote.android.R;
import com.brcorner.dnote.android.data.ConstantData;
import com.brcorner.dnote.android.model.NoteModel;

public class NoteAdapter extends ArrayAdapter<NoteModel>{
	
	
	private int resourceId;

	public NoteAdapter(Context context, int resource,
			List<NoteModel> objects) {
		super(context, resource, objects);
		// TODO Auto-generated constructor stub
		resourceId = resource;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub		
		return super.getCount();
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		NoteModel noteModel = getItem(position);
		View view;
		ViewHolder viewHolder;
		if(convertView == null)
		{
			view = LayoutInflater.from(getContext()).inflate(resourceId, null);
			viewHolder = new ViewHolder();
			viewHolder.ago_text = (TextView) view.findViewById(R.id.ago_text);
			viewHolder.time_text = (TextView) view.findViewById(R.id.time_text);
			viewHolder.summary_text = (TextView) view.findViewById(R.id.summary_text);
			viewHolder.fav_image = (ImageView) view.findViewById(R.id.fav_image);
			viewHolder.clip_image = (ImageView) view.findViewById(R.id.clip_image);
			view.setTag(viewHolder);
		}
		else
		{
			view = convertView;
			viewHolder = (ViewHolder) view.getTag();
		}
		viewHolder.ago_text.setText("一天前");
		viewHolder.time_text.setText(noteModel.getNoteTime());
		if(noteModel.getNoteContent().length() > ConstantData.TITLE_LENGTH)
		{
			viewHolder.summary_text.setText(noteModel.getNoteContent().substring(0, ConstantData.TITLE_LENGTH));
		}
		else
		{
			viewHolder.summary_text.setText(noteModel.getNoteContent());
		}
		if(noteModel.isFav())
		{
			viewHolder.fav_image.setVisibility(View.VISIBLE);
		}
		else
		{
			viewHolder.fav_image.setVisibility(View.INVISIBLE);
		}
		return view;
	}
	
	class ViewHolder{
		TextView ago_text;
		TextView time_text;
		ImageView fav_image;
		TextView summary_text;
		ImageView clip_image;
	}
	
	
}
