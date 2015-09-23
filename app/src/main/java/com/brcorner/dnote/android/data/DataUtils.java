package com.brcorner.dnote.android.data;

import com.brcorner.dnote.android.model.NoteModel;

import java.util.ArrayList;
import java.util.List;


public class DataUtils {
	public static List<NoteModel> getExampleList()
	{
		List<NoteModel> lst = new ArrayList<NoteModel>();

		NoteModel noteModel = new NoteModel();
		noteModel.setFav(false);
		noteModel.setNoteTime("15:13 5月13日");
		noteModel.setNoteContent("啦啦啦啦，fuck，you，up！");
		lst.add(noteModel);

		NoteModel noteModel2 = new NoteModel();
		noteModel2.setFav(true);
		noteModel2.setNoteTime("5:13 5月13日");
		noteModel2.setNoteContent("啦啦啦啦，fuck，you，up,to,sky！");
		lst.add(noteModel2);

		NoteModel noteModel3 = new NoteModel();
		noteModel3.setFav(false);
		noteModel3.setNoteTime("15:13 5月13日");
		noteModel3.setNoteContent("啦啦啦啦，fuck，you，up！");
		lst.add(noteModel3);

		NoteModel noteModel4 = new NoteModel();
		noteModel4.setFav(false);
		noteModel4.setNoteTime("15:13 5月13日");
		noteModel4.setNoteContent("啦啦啦啦，fuck，you，up！");
		lst.add(noteModel4);

		NoteModel noteModel5 = new NoteModel();
		noteModel5.setFav(false);
		noteModel5.setNoteTime("6:13 5月13日");
		noteModel5.setNoteContent("我爱我的小老婆。");
		lst.add(noteModel5);

		NoteModel noteModel6 = new NoteModel();
		noteModel6.setFav(false);
		noteModel6.setNoteTime("7:13 5月13日");
		noteModel6.setNoteContent("我爱我的小老婆婆");
		lst.add(noteModel6);
		return lst;
	}
}
