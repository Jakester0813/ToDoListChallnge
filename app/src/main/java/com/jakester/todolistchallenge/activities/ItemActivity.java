package com.jakester.todolistchallenge.activities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jakester.todolistchallenge.R;
import com.jakester.todolistchallenge.application.ToDoApplication;
import com.jakester.todolistchallenge.constants.ToDoConstants;
import com.jakester.todolistchallenge.entities.Item;
import com.jakester.todolistchallenge.utils.ColorUtil;
import com.jakester.todolistchallenge.utils.UtilFunctions;

import java.util.Calendar;

public class ItemActivity extends AppCompatActivity {

    Toolbar mToolbar;
    LinearLayout mNoteLinear, mPriorityLinear;
    TextView mAddedDateText, mNoteText, mDueDate, mPriorityText, mPriority, mNameText;
    EditText mNoteEdit, mNameEdit;
    Item mItem = null;
    String mListName;
    boolean updated = true;
    boolean mCompleted;
    int position = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if(getIntent().hasExtra("Item")){
            mItem = getIntent().getParcelableExtra("Item");
            mListName = getIntent().getStringExtra("ListName");
            mCompleted = getIntent().hasExtra("Completed");
            position = getIntent().getIntExtra("Position", -1);
        }
        else{
            mItem = new Item("Dat Item");
        }
        mToolbar.setTitle(ToDoConstants.EMPTY_STRING);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(updated){
                    Intent renamedItem = new Intent();
                    renamedItem.putExtra("item",mItem);
                    renamedItem.putExtra("listPos", position);
                    setResult(ToDoConstants.EDITED_ITEM_RESULT, renamedItem);
                    finish();
                }
                else {
                    finish();
                }
            }
        });
        mNameEdit = (EditText) findViewById(R.id.et_item_name);
        mNameEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    mNameText.setText(v.getText());
                    mItem.setName(v.getText().toString());
                    mNameEdit.setVisibility(View.GONE);
                    mNameText.setVisibility(View.VISIBLE);
                    InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    mgr.hideSoftInputFromWindow(mNameEdit.getWindowToken(), 0);
                    setItemUpdated();
                    return true;
                }
                return false;
            }
        });
        mNoteLinear = (LinearLayout) findViewById(R.id.ll_note);
        mPriorityLinear = (LinearLayout) findViewById(R.id.ll_priority);
        mAddedDateText = (TextView) findViewById(R.id.tv_date_added);
        mNameText = (TextView) findViewById(R.id.toolbar_title);
        mNameText.setText(mItem.getName());
        mNoteText = (TextView) findViewById(R.id.tv_note);
        mNoteEdit = (EditText) findViewById(R.id.et_note);
        mDueDate = (TextView) findViewById(R.id.tv_set_due_date);
        mPriorityText = (TextView) findViewById(R.id.tv_priority_text);
        mPriority = (TextView) findViewById(R.id.tv_priority);
        mNoteLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNoteText.setVisibility(View.GONE);
                mNoteEdit.setVisibility(View.VISIBLE);
                if(!mNameText.getText().toString().equals(ToDoConstants.NOTE_HINT))
                    mNoteEdit.setText(mNoteText.getText());
                mNoteEdit.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(mNoteEdit, InputMethodManager.SHOW_IMPLICIT);
            }
        });
        mNoteEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    mItem.setNote(mNoteEdit.getText().toString());
                    mNoteText.setTextColor(Color.BLACK);
                    mNoteText.setText(mNoteEdit.getText().toString());
                    mNoteEdit.setText("");
                    mNoteEdit.setVisibility(View.GONE);
                    mNoteText.setVisibility(View.VISIBLE);
                    setItemUpdated();
                    InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    mgr.hideSoftInputFromWindow(mNoteEdit.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });

        if(!mCompleted) {
            mDueDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setDueDate();
                }
            });
            mPriorityLinear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Resources resources = ItemActivity.this.getResources();
                    AlertDialog.Builder builder = new AlertDialog.Builder(ItemActivity.this);
                    builder.setTitle("Select the priority level for this item:");
                    final String PRIORITY_STRINGS[] = {"Low", "Medium", "High"};
                    final int[] priorityColors = {resources.getColor(R.color.green), resources.getColor(R.color.yellow), resources.getColor(R.color.red)};
                    builder.setItems(PRIORITY_STRINGS, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mPriorityText.setText("Priority: ");
                            mPriorityText.setTextColor(Color.BLACK);
                            mPriority.setVisibility(View.VISIBLE);
                            mPriority.setText(PRIORITY_STRINGS[which]);
                            mPriority.setTextColor(priorityColors[which]);
                            setItemUpdated();
                            mItem.setPriorityColor(priorityColors[which]);
                            mItem.setPriority(PRIORITY_STRINGS[which]);

                        }
                    });
                    builder.show();
                }
            });
        }


    }

    public void setDueDate(){
        final Calendar c = Calendar.getInstance();
        final StringBuilder dateBuilder = new StringBuilder("Due on: ");
        final int year = c.get(Calendar.YEAR);
        final int month = c.get(Calendar.MONTH);
        final int day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(ItemActivity.this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        dateBuilder.append(month+1).append("/").append(dayOfMonth).append("/").append(year);
                        mDueDate.setText(dateBuilder.toString());
                        mDueDate.setTextColor(Color.BLACK);
                        mItem.setDueDate(mDueDate.getText().toString());
                        setItemUpdated();
                        addDateToCalendarDialog(monthOfYear, dayOfMonth, year);
                    }
                }, year, month, day);
        datePickerDialog.show();
    }

    public void addDateToCalendarDialog(final int month, final int day, final int year) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        dialogBuilder.setTitle("Add the due date to your calendar?");
        dialogBuilder.setPositiveButton("Sure!", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                addDateToCalendar(month, day, year);
                dialog.dismiss();
            }
        });
        dialogBuilder.setNegativeButton("Naw, I'm good", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }

    public void addDateToCalendar(int month, int day, int year){
        Calendar beginTime = Calendar.getInstance();
        beginTime.set(year, month, day, 0,0);
        Calendar endTime = Calendar.getInstance();
        endTime.set(year, month, day, 23, 59);
        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis())
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis())
                .putExtra(CalendarContract.Events.TITLE, mItem.getName())
                .putExtra(CalendarContract.Events.DESCRIPTION, "Complete \'" + mItem.getName() + "\' from your \'" + mListName + "\' list");
        startActivity(intent);
    }

    private void setItemUpdated(){
        if(!updated){
            updated = true;
        }
    }

    @Override
    public void onStart(){
        super.onStart();
        setUserUI();
        populateFields();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.item_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.rename_item_menu:
                renameItem();
                return true;
            case R.id.delete_item_menu:
                deleteItem();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void renameItem(){
        mNameText.setVisibility(View.GONE);
        mNameEdit.setVisibility(View.VISIBLE);
        mNameEdit.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(mNameEdit, InputMethodManager.SHOW_IMPLICIT);
    }

    public void deleteItem(){
        Intent deletedList = new Intent();
        deletedList.putExtra("listPos", position);
        deletedList.putExtra("item", mItem);
        setResult(ToDoConstants.DELETE_ITEM_RESULT, deletedList);
        finish();
    }

    public void populateFields(){
        mAddedDateText.setText(mItem.getAddedDate());
        if(!mItem.getDueDate().equals("")&& !mItem.getDueDate().equals(null)) {
            mDueDate.setText(mItem.getDueDate());
            mDueDate.setTextColor(Color.BLACK);
        }
        if(mItem.getPriorityColor() != 0 && (!mItem.getPriority().equals("")&& !mItem.getPriority().equals(null))) {
            mPriorityText.setText("Priority: ");
            mPriorityText.setTextColor(Color.BLACK);
            mPriority.setVisibility(View.VISIBLE);
            mPriority.setText(mItem.getPriority());
            mPriority.setTextColor(mItem.getPriorityColor());
        }
        if(!mItem.getNote().equals("") && !mItem.getNote().equals(null)) {
            mNoteText.setTextColor(Color.BLACK);
            mNoteText.setText(mItem.getNote());
        }
    }

    public void setUserUI() {
        ColorUtil.getInstance().setStatusBarColor(this);
    }

}
