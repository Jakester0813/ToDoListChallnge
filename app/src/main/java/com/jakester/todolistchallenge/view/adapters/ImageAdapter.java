package com.jakester.todolistchallenge.view.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.jakester.todolistchallenge.R;
import com.jakester.todolistchallenge.utils.ImageUtil;
import com.jakester.todolistchallenge.view.listeners.RecyclerItemClickListener;

import java.util.ArrayList;

/**
 * Created by Jake on 8/21/2017.
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> implements RecyclerItemClickListener.OnItemClickListener{


    private ArrayList<String> mImages;
    public int selectedItem = -1;
    public Context mContext;

    public ImageAdapter(Context pContext){
        this.mImages = new ArrayList<String>();
        populateList();
        mContext = pContext;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_row, parent, false);
        return new ImageViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        holder.mImageView.setBackgroundDrawable(ImageUtil.getInstance(mContext).getImage(mImages.get(position)));
        holder.mCheck.setVisibility(selectedItem == position ? View.VISIBLE : View.GONE);

    }

    @Override
    public int getItemCount() {
        return mImages.size();
    }

    private void populateList(){
        mImages.add("beach");
        mImages.add("city");
        mImages.add("desert");
        mImages.add("flowers");
        mImages.add("gaming");
        mImages.add("kittens");
        mImages.add("puppies");
        mImages.add("snow");
        mImages.add("waterfall");
    }

    public String getSelectedImage(int pos){
        return mImages.get(pos);
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

    public class ImageViewHolder extends RecyclerView.ViewHolder {

        public View mImageView;
        public ImageView mCheck;

        public ImageViewHolder(View imageView) {
            super(imageView);
            this.mImageView = imageView.findViewById(R.id.image_view);
            this.mCheck = (ImageView) imageView.findViewById(R.id.iv_check);
        }

    }

}
