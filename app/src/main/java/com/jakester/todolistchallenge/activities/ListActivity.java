package com.jakester.todolistchallenge.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jakester.todolistchallenge.R;
import com.jakester.todolistchallenge.adapters.ListAdapter;
import com.jakester.todolistchallenge.application.ToDoApplication;
import com.jakester.todolistchallenge.constants.ToDoConstants;
import com.jakester.todolistchallenge.database.DatabaseManager;
import com.jakester.todolistchallenge.database.ToDoListsDatabaseHelper;
import com.jakester.todolistchallenge.entities.Item;
import com.jakester.todolistchallenge.entities.UserList;
import com.jakester.todolistchallenge.entities.UserSettings;
import com.jakester.todolistchallenge.utils.ColorUtil;
import com.jakester.todolistchallenge.utils.ImageUtil;
import com.jakester.todolistchallenge.utils.UtilFunctions;


public class ListActivity extends AppCompatActivity {

    Toolbar mToolbar;
    RecyclerView mCurrentItemsRecycler, mDoneItemsRecycler;
    LinearLayoutManager mCurrentManager, mDoneManager;
    ListAdapter mCurrentItemsAdapter, mDoneItemsAdapter;
    LinearLayout mCompletedToDosLinear, mBackgroundLinear, mAddItemLinear;
    TextView mListText, mCompletedToDoText;
    EditText mListEdit, mNewItemEdit;
    UserList mUserList;
    int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        DatabaseManager.initializeInstance(ToDoListsDatabaseHelper.getInstance(this));
        if(getIntent().hasExtra(ToDoConstants.USERLIST_KEY)){
            mUserList = getIntent().getParcelableExtra(ToDoConstants.USERLIST_KEY);
            position = getIntent().getIntExtra(ToDoConstants.POSITION_KEY, -1);
        }
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(ToDoConstants.EMPTY_STRING);
        mListText = (TextView) findViewById(R.id.toolbar_title);
        mListEdit = (EditText) findViewById(R.id.et_list_name);
        mListText.setText(mUserList.getName());
        mBackgroundLinear = (LinearLayout) findViewById(R.id.ll_background);
        mAddItemLinear = (LinearLayout) findViewById(R.id.ll_add_item);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mToolbar.showOverflowMenu();
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mUserList.getUpdated()){
                    Intent renamedList = new Intent();
                    renamedList.putExtra(ToDoConstants.UPDATED_LIST_KEY,mUserList);
                    renamedList.putExtra(ToDoConstants.POSITION_KEY, position);
                    setResult(ToDoConstants.EDITED_LIST_RESULT, renamedList);
                    finish();
                }
                else {
                    finish();
                }
            }
        });
        mListEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    mListText.setText(v.getText());
                    mUserList.setName(v.getText().toString());
                    mListEdit.setVisibility(View.GONE);
                    mListText.setVisibility(View.VISIBLE);
                    InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    mgr.hideSoftInputFromWindow(mListEdit.getWindowToken(), 0);
                    mUserList.setUpdated(true);
                    return true;
                }
                return false;
            }
        });
        mCompletedToDoText = (TextView) findViewById(R.id.tv_completed_to_do_text);
        mCompletedToDoText.setText(mUserList.getShowCompleted() ?
                ToDoConstants.HIDE_COMPLETED : ToDoConstants.SHOW_COMPLETED);
        mCompletedToDosLinear = (LinearLayout) findViewById(R.id.ll_completed_to_do_items);
        mCompletedToDosLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCompletedToDoText.setText(mUserList.getShowCompleted() ?
                        ToDoConstants.SHOW_COMPLETED : ToDoConstants.HIDE_COMPLETED);
                mDoneItemsRecycler.setVisibility(mUserList.getShowCompleted() ? View.GONE : View.VISIBLE);
                mUserList.setShowCompleted(mUserList.getShowCompleted() ? false : true);
            }
        });

        mNewItemEdit = (EditText) findViewById(R.id.et_new_item);
        mNewItemEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    String itemName = v.getText().toString();
                    mNewItemEdit.setText("");
                    Item item = new Item(itemName);
                    mCurrentItemsAdapter.addItem(item);
                    if(mCurrentItemsRecycler.getVisibility() == View.GONE){
                        mCurrentItemsRecycler.setVisibility(View.VISIBLE);
                    }
                    InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    mgr.hideSoftInputFromWindow(mNewItemEdit.getWindowToken(), 0);
                    mUserList.setUpdated(true);
                    return true;
                }
                return false;
            }
        });
        mCurrentItemsRecycler = (RecyclerView) findViewById(R.id.rv_list);
        if(mUserList.getCompletedItems().size() > 0){
            mCurrentItemsRecycler.setVisibility(View.VISIBLE);
        }
        mDoneItemsRecycler = (RecyclerView) findViewById(R.id.rv_completed_list);
        if(mUserList.getShowCompleted()){
            mDoneItemsRecycler.setVisibility(View.VISIBLE);
        }
        mCurrentManager = new LinearLayoutManager(this);
        mDoneManager = new LinearLayoutManager(this);
        mCurrentItemsRecycler.setLayoutManager(mCurrentManager);
        mDoneItemsRecycler.setLayoutManager(mDoneManager);
        mCurrentItemsAdapter = new ListAdapter(mUserList.getCurrentItems(),
        new ListAdapter.OnClickListener() {
            @Override
            public void mClick(View v, int position) {
                Item item = mCurrentItemsAdapter.getItems().get(position);
                if(v.getId() == R.id.cb_completed){
                    item.setCompleted(true);
                    mCurrentItemsAdapter.removeItem(position);
                    mDoneItemsAdapter.addItem(item);
                }
                else{
                    Intent itemIntent = new Intent (ListActivity.this, ItemActivity.class);
                    itemIntent.putExtra(ToDoConstants.ITEM_KEY, item);
                    itemIntent.putExtra(ToDoConstants.LIST_NAME_KEY, mUserList.getName());
                    itemIntent.putExtra(ToDoConstants.POSITION_KEY, position);
                    startActivityForResult(itemIntent, ToDoConstants.ITEM_REQUEST_CODE);
                }
            }
        });
        mDoneItemsAdapter = new ListAdapter(mUserList.getCompletedItems(),
                new ListAdapter.OnClickListener() {
                    @Override
                    public void mClick(View v, int position) {
                        Item item = mDoneItemsAdapter.getItems().get(position);
                        if(v.getId() == R.id.cb_completed){
                            item.setCompleted(false);
                            mDoneItemsAdapter.removeItem(position);
                            mCurrentItemsAdapter.addItem(item);
                        }
                        else{
                            Intent itemIntent = new Intent (ListActivity.this, ItemActivity.class);
                            itemIntent.putExtra(ToDoConstants.ITEM_KEY, item);
                            itemIntent.putExtra(ToDoConstants.COMPLETED_KEY, true);
                            itemIntent.putExtra(ToDoConstants.POSITION_KEY, position);
                            startActivityForResult(itemIntent, ToDoConstants.ITEM_REQUEST_CODE);
                        }
                    }
                });
        mCurrentItemsRecycler.setAdapter(mCurrentItemsAdapter);
        mDoneItemsRecycler.setAdapter(mDoneItemsAdapter);
    }

    @Override
    public void onStart(){
        super.onStart();
        setUserUI();
        showCompletedList();
    }

    @Override
    public void onStop(){
        super.onStop();
        if(mUserList.getUpdated())
            DatabaseManager.getInstance().updateList(mUserList);
    }

    @Override
    protected void onDestroy() {
        ToDoListsDatabaseHelper.getInstance(ListActivity.this).close();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_settings:
                goToSettings();
                return true;
            case R.id.rename_item_menu:
                renameList();
                return true;
            case R.id.delete_list_menu:
                deleteList();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void renameList(){
        mListText.setVisibility(View.GONE);
        mListEdit.setVisibility(View.VISIBLE);
        mListEdit.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(mListEdit, InputMethodManager.SHOW_IMPLICIT);
    }

    public void deleteList(){
        Intent deletedList = new Intent();
        deletedList.putExtra(ToDoConstants.POSITION_KEY, position);
        deletedList.putExtra(ToDoConstants.UPDATED_LIST_KEY, mUserList);
        setResult(ToDoConstants.DELETE_LIST_RESULT, deletedList);
        finish();
    }

    public void goToSettings(){
        Intent intent = new Intent(this, SettingsActivity.class);
        this.startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == ToDoConstants.ITEM_REQUEST_CODE) {
            // Make sure the request was successful
            int listPos;
            switch (resultCode){
                case ToDoConstants.EDITED_ITEM_RESULT:
                    listPos = data.getIntExtra(ToDoConstants.POSITION_KEY, -1);
                    Item item = data.getParcelableExtra(ToDoConstants.ITEM_KEY);
                    if(!item.getCompleted()) {

                        if (!item.getPriority().equals(mCurrentItemsAdapter.getItems().get(listPos).getPriority())) {
                            mCurrentItemsAdapter.removeItem(listPos);
                            listPos = mCurrentItemsAdapter.getPositionFromPriority(item);
                            mCurrentItemsAdapter.getItems().add(listPos,item);
                        }
                        else{
                            mCurrentItemsAdapter.removeItem(listPos);
                            mCurrentItemsAdapter.addItem(item);
                        }


                    }
                    else{
                        mDoneItemsAdapter.removeItem(listPos);
                        mDoneItemsAdapter.addItem(item);
                    }
                    break;
                case ToDoConstants.DELETE_ITEM_RESULT:
                    Item datItem = data.getParcelableExtra(ToDoConstants.ITEM_KEY);
                    listPos = data.getIntExtra(ToDoConstants.POSITION_KEY, -1);
                    if(!datItem.getCompleted())
                        mCurrentItemsAdapter.removeItem(listPos);
                    else{
                        mDoneItemsAdapter.removeItem(listPos);
                    }
                    break;

            }
            DatabaseManager.getInstance().updateList(mUserList);
            mUserList.setUpdated(true);
        }
    }

    public void setUserUI() {
        ColorUtil.getInstance().setStatusBarColor(this);
        getWindow().setBackgroundDrawableResource(ImageUtil.getInstance(ListActivity.this).getImageInt());
        mCompletedToDosLinear.setBackgroundColor(Color.parseColor(UserSettings.getInstance(this).getLightColor()));
        mAddItemLinear.setBackgroundColor(Color.parseColor(UserSettings.getInstance(this).getLightColor()));
        mToolbar.setBackgroundColor(Color.parseColor(UserSettings.getInstance(this).getBaseColor()));
    }

    public void showCompletedList(){
        mCompletedToDoText.setText(mUserList.getShowCompleted() ?
                ToDoConstants.HIDE_COMPLETED : ToDoConstants.SHOW_COMPLETED);
        mDoneItemsRecycler.setVisibility(mUserList.getShowCompleted() ? View.VISIBLE : View.GONE);
    }
}
