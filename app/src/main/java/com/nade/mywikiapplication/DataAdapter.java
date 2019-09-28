package com.nade.mywikiapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nade.mywikiapplication.models.Random;
import com.nade.mywikiapplication.utils.SharedPref;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {

    private List<Random> randomList;
    int[] myImageList = new int[]{R.drawable.demo_image_one, R.drawable.demo_image_two, R.drawable.demo_image_three, R.drawable.demo_image_four,
            R.drawable.demo_image_five, R.drawable.demo_image_six, R.drawable.demo_image_seven,
            R.drawable.demo_image_eight, R.drawable.demo_image_nine, R.drawable.demo_image_ten};

    public DataAdapter(List<Random> randomList) {
        this.randomList = randomList;
    }

    @NonNull
    @Override
    public DataAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_row, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DataAdapter.ViewHolder viewHolder, int i) {
        viewHolder.tv_title.setText(randomList.get(i).getTitle());
        viewHolder.wikiImage.setImageResource(myImageList[i]);
    }

    @Override
    public int getItemCount() {
        return randomList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView tv_title;
        private ImageView wikiImage;
        public ViewHolder(View view) {
            super(view);
            tv_title = (TextView)view.findViewById(R.id.tv_title);
            wikiImage = (ImageView)view.findViewById(R.id.wiki_image);
        }
    }
}
