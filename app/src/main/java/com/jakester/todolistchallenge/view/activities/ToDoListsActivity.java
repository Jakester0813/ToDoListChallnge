package com.jakester.todolistchallenge.view.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import com.jakester.todolistchallenge.R;
import com.jakester.todolistchallenge.constants.ToDoConstants;
import com.jakester.todolistchallenge.model.objects.UserList;
import com.jakester.todolistchallenge.view.adapters.ToDoListsAdapter;
import com.jakester.todolistchallenge.view.listeners.RecyclerItemClickListener;
import com.jakester.todolistchallenge.viewmodels.ListsViewModel;

public class ToDoListsActivity extends AppCompatActivity {

    Toolbar mToolbar;
    RecyclerView mListsRecycler;
    LinearLayoutManager mManager;
    ToDoListsAdapter mToDoListsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_lists);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mListsRecycler = (RecyclerView) findViewById(R.id.rv_lists);
        mManager = new LinearLayoutManager(this);
        mListsRecycler.setLayoutManager(mManager);
        mToDoListsAdapter = new ToDoListsAdapter(this);
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
                            listIntent.putExtra("UserList", mToDoListsAdapter.getList().get(position));
                            listIntent.putExtra("Position", position);
                            startActivityForResult(listIntent, ToDoConstants.LIST_REQUEST_CODE);
                        }
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                })
        );

    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_settings:
                goToSettings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void addNewListItem(String pListName){
        mToDoListsAdapter.addNewList(new UserList(mToDoListsAdapter.getItemCount(), pListName));
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

        dialogBuilder.setTitle("Add a new list");
        dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                if(listEdit.getText().length() > 0) {
                    addNewListItem(listEdit.getText().toString());
                }
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == ToDoConstants.LIST_REQUEST_CODE) {
            // Make sure the request was successful
            int listPos;
            switch (resultCode){
                case ToDoConstants.EDITED_LIST_RESULT:
                    listPos = data.getIntExtra("listPos", -1);
                    mToDoListsAdapter.updateList(listPos, (UserList) data.getParcelableExtra("updatedList"));
                    break;
                case ToDoConstants.DELETE_LIST_RESULT:
                    listPos = data.getIntExtra("listPos", -1);
                    mToDoListsAdapter.removeList(listPos);
                    break;

                // Do something with the contact here (bigger example below)
            }
        }
    }
}
