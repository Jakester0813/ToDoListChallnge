package com.jakester.todolistchallenge.view.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.jakester.todolistchallenge.R;
import com.jakester.todolistchallenge.model.objects.Item;
import com.jakester.todolistchallenge.model.objects.UserList;

import java.util.ArrayList;

/**
 * Created by Jake on 8/15/2017.
 */

public class ToDoListsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements RecyclerView.OnItemTouchListener {

    Context mContext;
    ArrayList<UserList> mUserLists;
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;

    public ToDoListsAdapter(Context pContext){
        this.mContext = pContext;
        this.mUserLists = new ArrayList<UserList>();
    }

    //Adds a new created List to the list
    public void addNewList(UserList pList){
        mUserLists.add(pList);
        notifyDataSetChanged();
    }

    public ArrayList<UserList> getList(){
        return mUserLists;
    }

    public void updateList(int pos, UserList updatedList){
        UserList userList = mUserLists.get(pos);
        if(!userList.getName().equals(updatedList.getName())){
            userList.setName(updatedList.getName());
        }
        if(userList.getCompletedItems() != updatedList.getCompletedItems()){
            ArrayList<Item> completedItems = userList.getCompletedItems();
            completedItems.clear();
            completedItems.addAll(updatedList.getCompletedItems());
        }

        if(userList.getCurrentItems() != updatedList.getCurrentItems()){
            ArrayList<Item> currentItems = userList.getCurrentItems();
            currentItems.clear();
            currentItems.addAll(updatedList.getCurrentItems());
        }
        notifyDataSetChanged();
    }

    public void renameList(int pos, String name){
        mUserLists.get(pos).setName(name);
        notifyDataSetChanged();
    }

    public void removeList(int pos){
        mUserLists.remove(pos);
        notifyDataSetChanged();
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = null;
        if(viewType == TYPE_ITEM) {
            layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row, parent, false);
            return new ListViewHolder(layoutView);
        }
        else if (viewType == TYPE_FOOTER){
            layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_footer_row, parent, false);
            return new AddListViewHolder(layoutView);
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == mUserLists.size())
            return TYPE_FOOTER;

        return TYPE_ITEM;
    }



    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ListViewHolder) {
            ((ListViewHolder) holder).mListName.setText(mUserLists.get(position).getName());
        }
    }

    @Override
    public int getItemCount() {
        return this.mUserLists.size() + 1;
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    public static class ListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView mListName;


        public ListViewHolder(View itemView) {
            super(itemView);
            this.mListName = (TextView) itemView.findViewById(R.id.tv_list_name);
        }

        @Override
        public void onClick(View view) {
            getAdapterPosition();
        }
    }

    public static class AddListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView mAddListImage;
        public TextView mAddList;


        public AddListViewHolder(View itemView) {
            super(itemView);
            this.mAddListImage = (ImageView) itemView.findViewById(R.id.add_new_list_image);
            this.mAddList = (TextView) itemView.findViewById(R.id.tv_add_new_list);
        }

        @Override
        public void onClick(View view) {
            getAdapterPosition();
        }
    }
}
