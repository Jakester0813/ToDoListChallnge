package com.jakester.todolistchallenge.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.jakester.todolistchallenge.entities.UserSettings;


/**
 * Created by Jake on 8/21/2017.
 */

public class ImageUtil {
    private static ImageUtil mInstance;
    private Context mContext;

    public ImageUtil(Context pContext){
        this.mContext = pContext;
    }

    //Made this a singleton to make it a single instance shared across the app
    public static ImageUtil getInstance(Context pContext){
        return mInstance != null ? mInstance : new ImageUtil(pContext);
    }




    public Drawable getImage(String mImageName){
        Drawable drawable = mContext.getResources().getDrawable(mContext.getResources()
                .getIdentifier(mImageName, "drawable", mContext.getPackageName()));

        return drawable;
    }

    //Used when passing imageName is not possible
    public Drawable getImage(){
        String imageName = UserSettings.getInstance(mContext).getImageName();
        Drawable drawable = mContext.getResources().getDrawable(mContext.getResources()
                .getIdentifier(imageName, "drawable", mContext.getPackageName()));

        return drawable;
    }

    //Used to make the background image static and not resized by keyboard image
    public int getImageInt(){
        String imageName = UserSettings.getInstance(mContext).getImageName();
        int resource = mContext.getResources().getIdentifier(imageName, "drawable", mContext.getPackageName());
        return resource;
    }
}
