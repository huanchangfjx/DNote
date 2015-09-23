package com.brcorner.dnote.android.data;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.brcorner.dnote.android.model.NoteModel;


public class DNoteDB {

	/**
	 * 数据库名
	 */
	public static final String DB_NAME = "dnote";

	/**
	 * 数据库版本
	 */
	public static final int VERSION = 1;

	private static DNoteDB dnoteDB;

	private SQLiteDatabase db;

	private DNoteDB(Context context)
	{
		DNoteOpenHelper dbHelper = new DNoteOpenHelper(context, DB_NAME, null, VERSION);
		db = dbHelper.getWritableDatabase();
	}

	public synchronized static DNoteDB getInstance(Context context)
	{
		if(dnoteDB == null)
		{
			dnoteDB = new DNoteDB(context);
		}
		return dnoteDB;
	}

	public int saveNote(NoteModel noteModel)
	{
		if(noteModel != null)
		{
			ContentValues values = new ContentValues();
			values.put(DNoteOpenHelper.NOTE_TIME, noteModel.getNoteTime());
			values.put(DNoteOpenHelper.NOTE_CONTENT, noteModel.getNoteContent());
			if(noteModel.isFav())
			{
				values.put(DNoteOpenHelper.NOTE_ISFAV, 1);
			}
			else
			{
				values.put(DNoteOpenHelper.NOTE_ISFAV, 0);
			}

			return (int) db.insert(DNoteOpenHelper.TAB_NAME, null, values);
		}
		return 0;
	}

	public void updateNote(NoteModel noteModel)
	{
		if(noteModel.isFav())
		{
			db.execSQL("update " + DNoteOpenHelper.TAB_NAME + " set note_content = ? and note_isfav = 1 where id = ?",
					new Object[]{noteModel.getNoteContent(),String.valueOf(noteModel.getNoteId())});
		}
		else
		{
			db.execSQL("update " + DNoteOpenHelper.TAB_NAME + " set note_content = ? and note_isfav = 0 where id = ?",
					new Object[]{noteModel.getNoteContent(),String.valueOf(noteModel.getNoteId())});
		}
	}

	public void changeNoteRanking(int fromIndex,int toIndex)
	{
		db.execSQL("update " + DNoteOpenHelper.TAB_NAME + " set id = 0 where id = ?",
				new Object[]{String.valueOf(fromIndex)});
		db.execSQL("update " + DNoteOpenHelper.TAB_NAME + " set id = id - 1 where ? > id > ?",
				new Object[]{String.valueOf(toIndex),String.valueOf(fromIndex)});
		db.execSQL("update " + DNoteOpenHelper.TAB_NAME + " set id = ? where id = 0",
				new Object[]{String.valueOf(toIndex)});
	}

	public void deleteNote(int noteId)
	{
		db.execSQL("delete from " + DNoteOpenHelper.TAB_NAME + " where id = ?", new Object[]{String.valueOf(noteId)});
	}

	public List<NoteModel> searchNotesByStr(String str)
	{
		List<NoteModel> list = new ArrayList<NoteModel>();
		Cursor cursor = null;
		if(str != null && str.length() > 0)
		{
			cursor = db.rawQuery("select * from Dnote where note_content like '%"+ str +"%'",null);
		}
		else
		{
			cursor = db.query(DNoteOpenHelper.TAB_NAME, null, null, null, null, null, null);
		}
		if(cursor.moveToFirst())
		{
			do{
				NoteModel noteModel = new NoteModel();
				noteModel.setNoteId(cursor.getInt(cursor.getColumnIndex("id")));
				noteModel.setNoteContent(cursor.getString(cursor.getColumnIndex(DNoteOpenHelper.NOTE_CONTENT)));
				noteModel.setNoteTime(cursor.getString(cursor.getColumnIndex(DNoteOpenHelper.NOTE_TIME)));
				int b =  cursor.getInt(cursor.getColumnIndex(DNoteOpenHelper.NOTE_ISFAV));
				if(b == 1)
				{
					noteModel.setFav(true);
				}
				else
				{
					noteModel.setFav(false);
				}
				list.add(noteModel);
			}while(cursor.moveToNext());
		}
		return list;
	}

	public List<NoteModel> loadNotes()
	{
		List<NoteModel> list = new ArrayList<NoteModel>();
//		Cursor cursor = db.rawQuery("select * from Dnote order by note_rankId",null);
		Cursor cursor = db.query(DNoteOpenHelper.TAB_NAME, null, null, null, null, null, null);
		if(cursor.moveToFirst())
		{
			do{
				NoteModel noteModel = new NoteModel();
				noteModel.setNoteId(cursor.getInt(cursor.getColumnIndex("id")));
				noteModel.setNoteContent(cursor.getString(cursor.getColumnIndex(DNoteOpenHelper.NOTE_CONTENT)));
				noteModel.setNoteTime(cursor.getString(cursor.getColumnIndex(DNoteOpenHelper.NOTE_TIME)));
				int b =  cursor.getInt(cursor.getColumnIndex(DNoteOpenHelper.NOTE_ISFAV));
				if(b == 1)
				{
					noteModel.setFav(true);
				}
				else
				{
					noteModel.setFav(false);
				}
				list.add(noteModel);
			}while(cursor.moveToNext());
		}
		return list;
	}
}
