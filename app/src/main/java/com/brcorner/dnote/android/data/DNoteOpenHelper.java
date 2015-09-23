package com.brcorner.dnote.android.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DNoteOpenHelper extends SQLiteOpenHelper{

	public static final String TAB_NAME = "Dnote";
//	public static final String NOTE_RANKID = "note_rankId";
	public static final String NOTE_TIME = "note_time";
	public static final String NOTE_CONTENT = "note_content";
	public static final String NOTE_ISFAV = "note_isfav";
	
	public static final String CREATE_NOTE = "create table " + TAB_NAME + " ("
			+ "id integer primary key autoincrement,"
			+ NOTE_TIME + " text,"
			+ NOTE_CONTENT + " text,"
			+ NOTE_ISFAV + " integer)";
	
	public DNoteOpenHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(CREATE_NOTE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

}
