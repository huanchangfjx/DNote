package com.brcorner.dnote.android.activity;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract.CommonDataKinds.Note;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnLongClickListener;
import android.view.ViewStub;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.brcorner.dnote.android.R;
import com.brcorner.dnote.android.adapter.NoteAdapter;
import com.brcorner.dnote.android.data.ConstantData;
import com.brcorner.dnote.android.data.DNoteDB;
import com.brcorner.dnote.android.listener.MyAnimationListener;
import com.brcorner.dnote.android.listener.MyHideAnimationListener;
import com.brcorner.dnote.android.model.NoteModel;
import com.brcorner.dnote.android.utils.CommonUtils;
import com.brcorner.drag_sort_listview_lib.DragSortController;
import com.brcorner.drag_sort_listview_lib.DragSortListView;


public class MainActivity extends Activity {

    /**
     * 列表页面
     **/
    private ImageView info_image;
    private TextView title_text;
    private ImageView add_image;

    /**
     * 新增页面
     **/
    private Button back_btn;
    private TextView complete_text;
    // 完成按钮为button_add

    /**
     * 完成详情页面
     **/
    private ImageView delete_image;
    private ImageView send_image;

    private ViewStub viewstub_about;

    private Animation bottom_in_anim, bottom_out_anim, fade_in, fade_in_300, fade_out,
            left_in, left_out, right_in, right_out, zoom_translate,scale;
    private AnimationDrawable animationDrawable;
    private FrameLayout about_fl;
    private View about_bg;
    private DNoteDB dNoteDB;

    private RelativeLayout send_rl;
    private RelativeLayout delete_rl;
    private DragSortListView dragSortListView;
    private DragSortController mController;

    private NoteAdapter noteAdapter;

    private RelativeLayout empty_note_view;
    private RelativeLayout layout_info;

    private FrameLayout list_fl, edit_fl;

    private EditText edittext_note;
    private CheckBox fav_checkBox;
    private TextView time_text;
    private TableLayout layout_share;

    /***
     * 搜索栏
     ***/
    private EditText edittext_search;
    private ImageView imageview_delete;
    /***
     * 搜索栏
     ***/


    // 保存的值
    private NoteModel noteModel;
    private List<NoteModel> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        this.initView();
        listPage();
    }

    private void initView() {
        dNoteDB = DNoteDB.getInstance(this);
        info_image = (ImageView) findViewById(R.id.info_image);
        title_text = (TextView) findViewById(R.id.title_text);
        add_image = (ImageView) findViewById(R.id.add_image);
        back_btn = (Button) findViewById(R.id.back_btn);
        complete_text = (TextView) findViewById(R.id.complete_text);
        delete_image = (ImageView) findViewById(R.id.delete_image);
        send_image = (ImageView) findViewById(R.id.send_image);
        viewstub_about = (ViewStub) findViewById(R.id.viewstub_about);
        viewstub_about.inflate();
        about_fl = (FrameLayout) findViewById(R.id.about_fl);
        layout_share = (TableLayout) findViewById(R.id.layout_share);
        layout_info = (RelativeLayout) findViewById(R.id.layout_info);
        about_bg = findViewById(R.id.about_bg);

        send_rl = (RelativeLayout) View.inflate(this, R.layout.send_dialog,
                null);
        delete_rl = (RelativeLayout) View.inflate(this, R.layout.delete_dialog,
                null);
        empty_note_view = (RelativeLayout) findViewById(R.id.empty_note_view);
        list_fl = (FrameLayout) findViewById(R.id.list_fl);
        edit_fl = (FrameLayout) findViewById(R.id.edit_fl);
        edittext_note = (EditText) findViewById(R.id.edittext_note);
        edittext_note.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                if (edittext_note.isShown()) {
                    if (hasFocus) {
                        noteEditState();
                    } else {
                        noteFinishState();
                    }
                }

            }
        });
        fav_checkBox = (CheckBox) findViewById(R.id.fav_checkBox);
        time_text = (TextView) findViewById(R.id.time_text);
        fav_checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                // TODO Auto-generated method stub
                noteModel.setFav(isChecked);
            }
        });
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT);

        this.addContentView(send_rl, params);
        this.addContentView(delete_rl, params);

        bottom_in_anim = AnimationUtils.loadAnimation(this, R.anim.bottom_in);
        bottom_out_anim = AnimationUtils.loadAnimation(this, R.anim.bottom_out);
        fade_in = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        fade_out = AnimationUtils.loadAnimation(this, R.anim.fade_out);
        left_in = AnimationUtils.loadAnimation(this, R.anim.left_in);
        left_out = AnimationUtils.loadAnimation(this, R.anim.left_out);
        right_out = AnimationUtils.loadAnimation(this, R.anim.right_out);
        right_in = AnimationUtils.loadAnimation(this, R.anim.right_in);
        fade_in_300 = AnimationUtils.loadAnimation(this, R.anim.fade_in_100);
        zoom_translate = AnimationUtils.loadAnimation(this,
                R.anim.zoom_translate);
        scale = AnimationUtils.loadAnimation(this,R.anim.scale);

        dragSortListView = (DragSortListView) findViewById(R.id.listview_notes);

        dataList = dNoteDB.loadNotes();
        if (dataList != null && dataList.size() > 0) {
            empty_note_view.setVisibility(View.INVISIBLE);
        } else {
            empty_note_view.setVisibility(View.VISIBLE);
        }
        noteAdapter = new NoteAdapter(this, R.layout.list_notes_item, dataList);
        mController = buildController(dragSortListView);
        dragSortListView.setFloatViewManager(mController);
        dragSortListView.setOnTouchListener(mController);
        dragSortListView.setDragEnabled(true);


        //搜索框
        LinearLayout search = (LinearLayout) View.inflate(this,
                R.layout.search, null);
        edittext_search = (EditText) search.findViewById(R.id.edittext_search);
        imageview_delete = (ImageView) search.findViewById(R.id.imageview_delete);
        imageview_delete.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                edittext_search.setText("");
            }
        });
        edittext_search.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                String searchStr = s.toString();
                if (s != null && s.length() > 0) {
                    imageview_delete.setVisibility(View.VISIBLE);
                } else {
                    imageview_delete.setVisibility(View.GONE);
                }
                List<NoteModel> newList = dNoteDB.searchNotesByStr(searchStr);
                dataList.clear();
                dataList.addAll(newList);
                if (noteAdapter != null) {
                    noteAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });

        dragSortListView.addHeaderView(search);
        dragSortListView.setAdapter(noteAdapter);

        dragSortListView
                .setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1,
                                            int arg2, long arg3) {
                        noteModel = dataList.get(arg2 - 1);
                        showFinish();
                        noteFinishState();
                    }
                });
    }

    public void setUp()
    {
        for (NoteModel model : dataList) {
            model.setIsUp(true);
        }
    }

    public void setDown()
    {
        for (NoteModel model : dataList) {
            model.setIsUp(false);
        }
    }

    public DragSortController buildController(DragSortListView dslv) {
        DragSortController controller = new DragSortController(dslv);
        controller.setDragHandleId(R.id.drag_handle);
        controller.setClickRemoveId(R.id.click_remove);
        controller.setRemoveEnabled(true);
        controller.setSortEnabled(true);
        controller.setDragInitMode(DragSortController.ON_LONG_PRESS);
        controller.setRemoveMode(DragSortController.CLICK_REMOVE);

        return controller;
    }

    public void listPage() {
        info_image.setVisibility(View.VISIBLE);
        title_text.setVisibility(View.VISIBLE);
        add_image.setVisibility(View.VISIBLE);

        back_btn.setVisibility(View.INVISIBLE);
        complete_text.setVisibility(View.INVISIBLE);
        delete_image.setVisibility(View.INVISIBLE);
        send_image.setVisibility(View.INVISIBLE);

    }

    // 进入info
    public void showInfoDialog(View view) {
        viewstub_about.setVisibility(View.VISIBLE);
        bottom_in_anim.setAnimationListener(new MyAnimationListener(about_fl));
        about_fl.startAnimation(bottom_in_anim);
        about_fl.setVisibility(View.VISIBLE);

        fade_in.setAnimationListener(new MyAnimationListener(about_bg));
        about_bg.startAnimation(fade_in);
        about_bg.setVisibility(View.VISIBLE);
        about_bg.setClickable(true);

        CommonUtils.stack.push(ConstantData.INFOR_DIALOG);
    }

    public void hideInfoDialog(View view) {

        bottom_out_anim.setAnimationListener(new MyHideAnimationListener(new View[]{about_fl}));
        about_fl.startAnimation(bottom_out_anim);
        about_bg.setClickable(false);
        fade_out.setAnimationListener(new MyHideAnimationListener(new View[]{viewstub_about, about_bg}));
        about_bg.startAnimation(fade_out);
        layout_share.setVisibility(View.GONE);
        layout_info.setVisibility(View.VISIBLE);

        CommonUtils.stack.pop();
    }

    public void share(View view) {
        fade_in_300.setAnimationListener(new MyAnimationListener(layout_share));
        layout_share.startAnimation(fade_in_300);
        layout_share.setVisibility(View.VISIBLE);
        layout_info.setVisibility(View.GONE);
    }

    public void showFinish() {
        showEditAnim();
        initFinishData();
    }

    public void showEditAnim() {
        edittext_note.setVisibility(View.VISIBLE);
        left_out.setAnimationListener(new MyAnimationListener(list_fl));
        list_fl.startAnimation(left_out);
        list_fl.setVisibility(View.INVISIBLE);

        edit_fl.setScaleX(0.95f);
        edit_fl.setScaleY(0.95f);
        scale.setAnimationListener(new MyAnimationListener(edit_fl));
        right_in.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                edit_fl.startAnimation(scale);
                edit_fl.setScaleX(1.0f);
                edit_fl.setScaleY(1.0f);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        edit_fl.startAnimation(right_in);


        edit_fl.setVisibility(View.VISIBLE);
        CommonUtils.stack.push(ConstantData.EDIT_STATE);
    }

    // 跳转编辑页面
    @SuppressWarnings("unchecked")
    public void showEdit(View view) {
        showEditAnim();
        edittext_note.setFocusable(true);
        edittext_note.requestFocus();

        //初始化note值 时间 是否喜欢
        noteModel = new NoteModel();
        noteModel.setFav(false);
        noteModel.setNoteTime(CommonUtils.getDate());

        initFinishData();
    }

    public void initFinishData() {
        time_text.setText(noteModel.getNoteTime());
        fav_checkBox.setChecked(noteModel.isFav());
        edittext_note.setText(noteModel.getNoteContent());
    }

    // 返回列表
    public void backToList(View view) {
        listPage();
        edittext_note.setVisibility(View.GONE);
        right_out.setAnimationListener(new MyAnimationListener(edit_fl));
        edit_fl.startAnimation(right_out);
        edit_fl.setVisibility(View.INVISIBLE);

        left_in.setAnimationListener(new MyAnimationListener(list_fl));
        list_fl.startAnimation(left_in);
        list_fl.setVisibility(View.VISIBLE);
        CommonUtils.stack.pop();
        dataList.clear();

        List<NoteModel> newList = dNoteDB.loadNotes();
        dataList.addAll(newList);
        if (noteAdapter != null) {
            noteAdapter.notifyDataSetChanged();
        }

        if (dataList != null && dataList.size() > 0) {
            empty_note_view.setVisibility(View.INVISIBLE);
        } else {
            empty_note_view.setVisibility(View.VISIBLE);
        }
    }

    // 显示删除列表
    public void showDeleteDialog(View view) {
        bottom_in_anim.setAnimationListener(new MyAnimationListener(delete_rl));
        delete_rl.startAnimation(bottom_in_anim);
        delete_rl.setVisibility(View.VISIBLE);

        fade_in.setAnimationListener(new MyAnimationListener(about_bg));
        about_bg.startAnimation(fade_in);
        about_bg.setVisibility(View.VISIBLE);
        about_bg.setClickable(true);
        CommonUtils.stack.push(ConstantData.DELETE_DIALOG);
    }

    // 隐藏删除弹窗
    public void hideDeleteDialog(View view) {
        about_bg.setClickable(false);
        bottom_out_anim
                .setAnimationListener(new MyAnimationListener(delete_rl));
        delete_rl.startAnimation(bottom_out_anim);
        delete_rl.setVisibility(View.GONE);

        fade_out.setAnimationListener(new MyAnimationListener(about_bg));
        about_bg.startAnimation(fade_out);
        about_bg.setVisibility(View.GONE);
        CommonUtils.stack.pop();
    }

    // 显示发送弹窗
    public void showSendDialog(View view) {
        bottom_in_anim.setAnimationListener(new MyAnimationListener(send_rl));
        send_rl.startAnimation(bottom_in_anim);
        send_rl.setVisibility(View.VISIBLE);
        about_bg.setClickable(false);
        fade_in.setAnimationListener(new MyAnimationListener(about_bg));
        about_bg.startAnimation(fade_in);
        about_bg.setVisibility(View.VISIBLE);
        CommonUtils.stack.push(ConstantData.SEND_DIALOG);
    }

    // 隐藏发送弹窗
    public void hideSendDialog(View view) {
        about_bg.setClickable(false);
        bottom_out_anim.setAnimationListener(new MyAnimationListener(send_rl));
        send_rl.startAnimation(bottom_out_anim);
        send_rl.setVisibility(View.GONE);

        fade_out.setAnimationListener(new MyAnimationListener(about_bg));
        about_bg.startAnimation(fade_out);
        about_bg.setVisibility(View.GONE);
        CommonUtils.stack.pop();
    }

    // 点击删除按钮
    public void doDelete(View view) {
        this.hideDeleteDialog(null);

        delete_image.setImageResource(R.anim.del_btn_anim);
        animationDrawable = (AnimationDrawable) delete_image.getDrawable();
        animationDrawable.start();
        int duration = 0;
        for (int i = 0; i < animationDrawable.getNumberOfFrames(); i++) {
            duration += animationDrawable.getDuration(i);
        }
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                // 此处调用第二个动画播放方法
                edit_fl.startAnimation(zoom_translate);
                zoom_translate.setAnimationListener(new AnimationListener() {

                    @Override
                    public void onAnimationStart(Animation animation) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        // TODO Auto-generated method stub
                        delete_image
                                .setImageResource(R.drawable.batch_delete_back);
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                        dNoteDB.deleteNote(noteModel.getNoteId());

                        backToList(null);


                        delete_image.clearAnimation();
                    }
                });
            }
        }, duration);
    }

    private void noteFinishState() {
        back_btn.setVisibility(View.VISIBLE);
        delete_image.setVisibility(View.VISIBLE);
        send_image.setVisibility(View.VISIBLE);

        info_image.setVisibility(View.INVISIBLE);
        title_text.setVisibility(View.INVISIBLE);
        add_image.setVisibility(View.INVISIBLE);
        complete_text.setVisibility(View.INVISIBLE);
    }

    private void noteEditState() {
        back_btn.setVisibility(View.VISIBLE);
        complete_text.setVisibility(View.VISIBLE);

        info_image.setVisibility(View.INVISIBLE);
        title_text.setVisibility(View.INVISIBLE);
        add_image.setVisibility(View.INVISIBLE);
        delete_image.setVisibility(View.INVISIBLE);
        send_image.setVisibility(View.INVISIBLE);
    }

    // 完成
    public void doFinish(View view) {
        // 修改头部
        String noteContent = edittext_note.getText().toString();
        if (noteContent != null && noteContent.length() > 0) {
            edittext_note.clearFocus();

            noteModel.setNoteContent(noteContent);
            int noteId = dNoteDB.saveNote(noteModel);
            noteModel.setNoteId(noteId);
        } else {
            backToList(null);
        }

    }

    public void doClickBg(View view) {
        CommonUtils.doFinish(this);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            CommonUtils.doFinish(this);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
