package com.markjmind.jwtools.util;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;

/**
 * <br>捲土重來<br>
 *
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 * @since 2016-06-13
 */
public class RecycleHolder extends RecyclerView.ViewHolder{
    public HashMap<Integer, View> views = new HashMap();
    private Finder finder;

    public RecycleHolder(View itemView) {
        super(itemView);
        initFinder(itemView);
    }

    public RecycleHolder add(int... id){
        if(id!=null) {
            for (int i = 0; i < id.length; i++) {
                views.put(id[i], finder.findViewById(id[i]));
            }
        }
        return this;
    }

    private void initFinder(final View finder){
        this.finder = new Finder() {
            @Override
            public View findViewById(int id) {
                return finder.findViewById(id);
            }
        };
    }

    public View get(int id){
        return views.get(id);
    }

    public TextView getTextView(int id){
        return (TextView)views.get(id);
    }

    public ImageView getImageView(int id){
        return (ImageView)views.get(id);
    }

    public EditText getEditText(int id){
        return (EditText)views.get(id);
    }

    public Button getButton(int id){
        return (Button)views.get(id);
    }

    public ViewGroup getViewGroup(int id){
        return (ViewGroup)views.get(id);
    }

    public LinearLayout getLinearLayout(int id){
        return (LinearLayout)views.get(id);
    }

    public FrameLayout getFrameLayout(int id){
        return (FrameLayout)views.get(id);
    }

    interface Finder{
        View findViewById(int id);
    }
}
