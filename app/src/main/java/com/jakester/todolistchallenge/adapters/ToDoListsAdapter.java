package com.jakester.todolistchallenge.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakester.todolistchallenge.R;
import com.jakester.todolistchallenge.entities.Item;
import com.jakester.todolistchallenge.entities.UserList;

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

    public ToDoListsAdapter(Context pContext, ArrayList<UserList> lists){
        this.mContext = pContext;
        if(lists != null) {
            this.mUserLists = lists;
        }
        else{
            this.mUserLists = new ArrayList<UserList>();
        }
    }

    //Adds a new created List to the list
    public void addList(UserList pList){
        mUserLists.add(pList);
        notifyDataSetChanged();
    }

    public ArrayList<UserList> getList(){
        return mUserLists;
    }

    public void updateList(int pos, UserList updatedList){
        mUserLists.remove(pos);
        mUserLists.add(pos, updatedList);
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
        return mUserLists != null ? this.mUserLists.size() + 1 : 1;
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
