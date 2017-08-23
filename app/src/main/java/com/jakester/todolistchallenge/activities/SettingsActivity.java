package com.jakester.todolistchallenge.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jakester.todolistchallenge.R;
import com.jakester.todolistchallenge.adapters.ColorAdapter;
import com.jakester.todolistchallenge.adapters.ImageAdapter;
import com.jakester.todolistchallenge.application.ToDoApplication;
import com.jakester.todolistchallenge.listeners.RecyclerItemClickListener;
import com.jakester.todolistchallenge.entities.UserSettings;
import com.jakester.todolistchallenge.utils.ColorUtil;
import com.jakester.todolistchallenge.utils.ImageUtil;
import com.jakester.todolistchallenge.utils.UtilFunctions;


public class SettingsActivity extends AppCompatActivity {

    Toolbar mToolbar;
    TextView mColorText, mImageText;
    LinearLayout mBackgroundLinear;
    RecyclerView mColorRecycler, mImageRecycler;
    RecyclerView.LayoutManager mColorManager, mImageManager;
    ColorAdapter mColorAdapter;
    ImageAdapter mImageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("Settings");
        setSupportActionBar(mToolbar);
        mBackgroundLinear = (LinearLayout) findViewById(R.id.ll_background);
        mColorText = (TextView) findViewById(R.id.tv_colors_text);
        mColorRecycler = (RecyclerView) findViewById(R.id.rv_colors);
        mColorManager = new LinearLayoutManager(SettingsActivity.this, LinearLayoutManager.HORIZONTAL, false);
        mColorRecycler.setLayoutManager(mColorManager);
        mColorAdapter = new ColorAdapter(ColorUtil.getInstance().getColors());
        if(UserSettings.getInstance(SettingsActivity.this).getColorPosition() > -1){
            mColorAdapter.setSelectedItem(UserSettings.getInstance(SettingsActivity.this).getColorPosition());
            mColorAdapter.notifyDataSetChanged();
        }
        mColorRecycler.setAdapter(mColorAdapter);
        mColorRecycler.addOnItemTouchListener(
                new RecyclerItemClickListener(this, mColorRecycler, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        mColorAdapter.setSelectedItem(position);
                        mColorAdapter.notifyDataSetChanged();
                        UserSettings.getInstance(SettingsActivity.this)
                                .setUserColor(position, mColorAdapter.getSelectedColor(position));
                        mToolbar.setBackgroundColor(Color.parseColor(UserSettings.getInstance(SettingsActivity.this).getBaseColor()));
                        UtilFunctions.getInstance(SettingsActivity.this).setStatusBarColor(SettingsActivity.this);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                })
        );

        mImageText = (TextView)findViewById(R.id.tv_images_text);
        mImageRecycler = (RecyclerView) findViewById(R.id.rv_images);
        mImageManager = new LinearLayoutManager(SettingsActivity.this, LinearLayoutManager.HORIZONTAL, false);
        mImageRecycler.setLayoutManager(mImageManager);
        mImageAdapter = new ImageAdapter(SettingsActivity.this);
        if(UserSettings.getInstance(SettingsActivity.this).getImagePosition() > -1){
            mImageAdapter.setSelectedItem(UserSettings.getInstance(SettingsActivity.this).getImagePosition());
            mImageAdapter.notifyDataSetChanged();
        }
        mImageRecycler.setAdapter(mImageAdapter);
        mImageRecycler.addOnItemTouchListener(
                new RecyclerItemClickListener(this, mImageRecycler, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        mImageAdapter.setSelectedItem(position);
                        mImageAdapter.notifyDataSetChanged();
                        UserSettings.getInstance(SettingsActivity.this)
                                .setImageName(position, mImageAdapter.getSelectedImage(position));
                        String mName = mImageAdapter.getSelectedImage(position);
                        mBackgroundLinear.setBackground(ImageUtil.getInstance(SettingsActivity.this).getImage(mName));
                        UtilFunctions.getInstance(SettingsActivity.this).setStatusBarColor(SettingsActivity.this);
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

        setUserUI();
    }

    public void setUserUI() {
        UtilFunctions.getInstance(this).setStatusBarColor(this);
        mBackgroundLinear.setBackground(ImageUtil.getInstance(this).getImage());
        mToolbar.setBackgroundColor(Color.parseColor(UserSettings.getInstance(this).getBaseColor()));
    }

}
