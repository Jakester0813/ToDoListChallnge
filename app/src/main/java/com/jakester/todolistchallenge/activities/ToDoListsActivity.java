package com.jakester.todolistchallenge.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.jakester.todolistchallenge.R;
import com.jakester.todolistchallenge.adapters.ToDoListsAdapter;
import com.jakester.todolistchallenge.application.ToDoApplication;
import com.jakester.todolistchallenge.constants.ToDoConstants;
import com.jakester.todolistchallenge.database.DatabaseManager;
import com.jakester.todolistchallenge.database.ToDoListsDatabaseHelper;
import com.jakester.todolistchallenge.listeners.RecyclerItemClickListener;
import com.jakester.todolistchallenge.entities.ToDoColor;
import com.jakester.todolistchallenge.entities.UserList;
import com.jakester.todolistchallenge.entities.UserSettings;
import com.jakester.todolistchallenge.utils.ColorUtil;
import com.jakester.todolistchallenge.utils.ImageUtil;
import com.jakester.todolistchallenge.utils.UtilFunctions;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ToDoListsActivity extends AppCompatActivity {

    Toolbar mToolbar;
    LinearLayout mBackgroundLinear;
    RecyclerView mListsRecycler;
    LinearLayoutManager mManager;
    ToDoListsAdapter mToDoListsAdapter;
    int rowID;
    GetColorsFromJSONTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_lists);
        DatabaseManager.initializeInstance(ToDoListsDatabaseHelper.getInstance(this));
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(ToDoConstants.YOUR_TO_DO_LISTS);
        rowID = UtilFunctions.getInstance(this).getLastRowID();
        setSupportActionBar(mToolbar);
        mBackgroundLinear = (LinearLayout) findViewById(R.id.ll_background);
        mListsRecycler = (RecyclerView) findViewById(R.id.rv_lists);
        mManager = new LinearLayoutManager(this);
        mListsRecycler.setLayoutManager(mManager);
        if(UtilFunctions.getInstance(this).getFirstAppLaunch()) {
            mToDoListsAdapter = new ToDoListsAdapter(this);
            UtilFunctions.getInstance(this).setFirstAppLaunchCommited();
        }
        else{
            mToDoListsAdapter = new ToDoListsAdapter(this, DatabaseManager.getInstance().getLists());
        }
        mListsRecycler.setAdapter(mToDoListsAdapter);
        mListsRecycler.addOnItemTouchListener(
                new RecyclerItemClickListener(this, mListsRecycler, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        if(view.getId() == R.id.ll_add_new_list){
                            showNewListDialog();
                        }
                        else {
                            Intent listIntent = new Intent(ToDoListsActivity.this, ListActivity.class);
                            listIntent.putExtra(ToDoConstants.USERLIST_KEY, mToDoListsAdapter.getList().get(position));
                            listIntent.putExtra(ToDoConstants.POSITION_KEY, position);
                            startActivityForResult(listIntent, ToDoConstants.LIST_REQUEST_CODE);
                        }
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                })
        );
        task = new GetColorsFromJSONTask();


    }

    @Override
    public void onStart(){
        super.onStart();
        setUserUI();
        if(ColorUtil.getInstance().getColors() == null){
            task.execute();
        }
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onStop(){
        super.onStop();
        DatabaseManager.getInstance().updateLists(mToDoListsAdapter.getList());
    }

    @Override
    protected void onDestroy() {
        ToDoListsDatabaseHelper.getInstance(ToDoListsActivity.this).close();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                goToSettings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void addNewListItem(String pListName){
        UserList list = new UserList(rowID, pListName);
        mToDoListsAdapter.addList(list);
        DatabaseManager.getInstance().addList(list);
        rowID++;
        UtilFunctions.getInstance(ToDoListsActivity.this).setLastRowID(rowID);

    }

    public void goToSettings(){
        Intent intent = new Intent(this, SettingsActivity.class);
        this.startActivity(intent);
    }

    public void showNewListDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.new_list_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText listEdit = (EditText) dialogView.findViewById(R.id.et_new_list_name);

        dialogBuilder.setTitle(ToDoConstants.NEW_LIST_TITLE);
        dialogBuilder.setPositiveButton(ToDoConstants.DONE, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                if(listEdit.getText().length() > 0) {
                    addNewListItem(listEdit.getText().toString());
                }
            }
        });
        dialogBuilder.setNegativeButton(ToDoConstants.CANCEL, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ToDoConstants.LIST_REQUEST_CODE) {
            int listPos;
            switch (resultCode){
                case ToDoConstants.EDITED_LIST_RESULT:
                    UserList list = (UserList) data.getParcelableExtra(ToDoConstants.UPDATED_LIST_KEY);
                    listPos = data.getIntExtra(ToDoConstants.POSITION_KEY, -1);
                    mToDoListsAdapter.removeList(listPos);
                    mToDoListsAdapter.addList(list);
                    DatabaseManager.getInstance().updateList((UserList) data.getParcelableExtra(ToDoConstants.UPDATED_LIST_KEY));
                    break;
                case ToDoConstants.DELETE_LIST_RESULT:
                    listPos = data.getIntExtra(ToDoConstants.POSITION_KEY, -1);
                    mToDoListsAdapter.removeList(listPos);
                    DatabaseManager.getInstance().deleteList((UserList) data.getParcelableExtra(ToDoConstants.UPDATED_LIST_KEY));
                    break;

            }
        }
    }

    private class GetColorsFromJSONTask extends AsyncTask<Void,Void,ArrayList<ToDoColor>> {
        @Override
        protected ArrayList<ToDoColor> doInBackground(Void... voids) {
            Type listType = new TypeToken<ArrayList<ToDoColor>>(){}.getType();
            Gson gson = new GsonBuilder().serializeNulls().create();
            ArrayList<ToDoColor> colors = gson.fromJson(ColorUtil.getInstance().loadJSONFromAsset(ToDoListsActivity.this), listType);
            return colors;
        }
        @Override
        protected void onPostExecute(ArrayList<ToDoColor> colors) {
            super.onPostExecute(colors);
            ColorUtil.getInstance().setColors(colors);
        }
    }

    public void setUserUI() {
        ColorUtil.getInstance().setStatusBarColor(this);
        //Added this line to prevent background image from getting squished by adding each to do list item.
        getWindow().setBackgroundDrawableResource(ImageUtil.getInstance(ToDoListsActivity.this).getImageInt());
        mBackgroundLinear.setBackground(ImageUtil.getInstance(this).getImage());
        mToolbar.setBackgroundColor(Color.parseColor(UserSettings.getInstance(this).getBaseColor()));
    }
}
