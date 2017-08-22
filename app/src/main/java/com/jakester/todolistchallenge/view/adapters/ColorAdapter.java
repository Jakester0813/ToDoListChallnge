package com.jakester.todolistchallenge.view.adapters;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.jakester.todolistchallenge.R;
import com.jakester.todolistchallenge.model.objects.ToDoColor;
import com.jakester.todolistchallenge.view.listeners.RecyclerItemClickListener;

import java.util.ArrayList;

/**
 * Created by Jake on 8/21/2017.
 */

public class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.ColorViewHolder> implements RecyclerItemClickListener.OnItemClickListener{

    private ArrayList<ToDoColor> mColors;
    public int selectedItem = -1;

    public ColorAdapter(ArrayList<ToDoColor> pColors){
        this.mColors = pColors;
    }

    @Override
    public ColorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.color_row, parent, false);
        return new ColorViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(ColorViewHolder holder, int position) {
        holder.mColorView.setBackgroundColor(Color.parseColor(mColors.get(position).getBaseColor()));
        holder.mCheck.setVisibility(selectedItem == position ? View.VISIBLE : View.GONE);

    }

    @Override
    public int getItemCount() {
        return mColors.size();
    }

    public ToDoColor getSelectedColor(int pos){
        return mColors.get(pos);
    }

    public void setSelectedItem(int pos){
        this.selectedItem = pos;
    }

    @Override
    public void onItemClick(View view, int position) {
        view.setSelected(true);
        selectedItem = position;
    }

    @Override
    public void onLongItemClick(View view, int position) {

    }

    public class ColorViewHolder extends RecyclerView.ViewHolder {

        public View mColorView;
        public ImageView mCheck;

        public ColorViewHolder(View colorView) {
            super(colorView);
            this.mColorView = colorView.findViewById(R.id.color_view);
            this.mCheck = (ImageView) colorView.findViewById(R.id.iv_check);
        }

    }

}
