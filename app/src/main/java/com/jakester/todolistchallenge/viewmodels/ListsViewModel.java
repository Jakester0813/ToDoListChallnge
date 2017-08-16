package com.jakester.todolistchallenge.viewmodels;

import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;

import com.jakester.todolistchallenge.database.ToDoListsDatabaseHelper;
import com.jakester.todolistchallenge.model.objects.UserList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jake on 8/2/2017.
 * This acts as a bridge between business logic (models) and activities (views)
 * Good for having asynctasks, set observables, and get methods for minor UI things (Progress Bar, Error, etc)
 */

public class ListsViewModel extends ViewModel{

    private ArrayList<UserList> lists;
    private Context mContext;



    public ListsViewModel(Context pContext){
        this.mContext = pContext;
    }

    //gets lists
    public ArrayList<UserList> getLists(){
        if(lists == null){
            new getDBListsTask(mContext).execute();
        }

        return lists;
    }

    public void addOrUpdateList(UserList pList){
        ToDoListsDatabaseHelper.getInstance(mContext).addOrUpdateList(pList);
    }

    public int setVisibility(View view) {
        if(view.getVisibility() == View.VISIBLE){
            return View.GONE;
        }
        else{
            return View.VISIBLE;
        }
    }


    private class getDBListsTask extends AsyncTask<Void, Void, List<UserList>> {

        private Context context;

        public getDBListsTask(Context pContext) {
            this.context = pContext;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<UserList> doInBackground(Void... params) {
            return ToDoListsDatabaseHelper.getInstance(context).getListsFromDataBase();
        }



        @Override
        protected void onPostExecute(List<UserList> userLists) {
            super.onPostExecute(userLists);
            lists.addAll(userLists);
        }
    }
}
