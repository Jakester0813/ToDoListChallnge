package com.jakester.todolistchallenge.view.adapters;

import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.jakester.todolistchallenge.R;
import com.jakester.todolistchallenge.model.objects.Item;

import java.util.ArrayList;

/**
 * Created by Jake on 8/16/2017.
 */

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ItemViewHolder>{

    public interface OnClickListener{
        void mClick(View v, int position);
    }

    ArrayList<Item> mItemsList;
    private final OnClickListener listener;

    public ListAdapter(ArrayList<Item> pItems, OnClickListener listener){
        this.mItemsList = pItems;
        this.listener = listener;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row, parent, false);
        return new ItemViewHolder(v);

    }

    public ArrayList<Item> getItems(){
        return mItemsList;
    }

    public void addItem(Item item){
        mItemsList.add(item);
        notifyDataSetChanged();
    }

    public void removeItem(int pos){
        mItemsList.remove(pos);
        notifyDataSetChanged();
    }

    public int getPositionFromPriority(Item item){
        if(item.getPriority().equals("High") || mItemsList.get(0).getPriority().equals("")){
            return 0;
        }
        int pos = 0;
        switch (item.getPriority()){
            case "Low":
                pos = mItemsList.size() - 1;
                while((mItemsList.get(pos).getPriority().equals("Low") ||
                        mItemsList.get(pos).getPriority().equals("")) && pos > 0){
                    pos--;
                }
                if (pos == -1 || !mItemsList.get(pos).getPriority().equals("Low"))
                    pos++;
                break;
            case "Medium":
                while(mItemsList.get(pos).getPriority().equals("High") && pos < mItemsList.size()){
                    pos++;
                }
                if(pos == mItemsList.size())
                    pos--;
                break;
        }
        return pos;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        holder.bind(mItemsList.get(position), listener, position);
    }

    @Override
    public int getItemCount() {
        return mItemsList.size();
    }


    public static class ItemViewHolder extends RecyclerView.ViewHolder{

        public TextView mItemName;
        public TextView mPriorityText;
        public CheckBox mCompleted;

        public ItemViewHolder(View itemView) {
            super(itemView);
            this.mItemName = (TextView) itemView.findViewById(R.id.tv_item_name);
            this.mPriorityText = (TextView) itemView.findViewById(R.id.tv_item_priority);
            this.mCompleted = (CheckBox) itemView.findViewById(R.id.cb_completed);
        }

        public void bind(final Item item, final OnClickListener listener, final int position){
            if(!item.getPriority().equals("")) {
                StringBuilder sb = new StringBuilder(item.getPriority());
                sb.append(" Priority");
                mPriorityText.setText(sb.toString());
            }
            mItemName.setText(item.getName());
            if(item.getCompleted())
                mItemName.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            mPriorityText.setTextColor(item.getPriorityColor());
            mCompleted.setChecked(item.getCompleted());
            mCompleted.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.mClick(v, position);
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.mClick(v, position);
                }
            });
        }
    }
}
