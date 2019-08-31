package com.androhungers.rssnewsreader.customViews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androhungers.rssnewsreader.R;

public class CustomUserDetailsView extends RelativeLayout {
    private RelativeLayout mContainer;
    private TextView tvUser,tvName,tvAge;

    public CustomUserDetailsView(Context context) {
        super(context);
        initView();
    }

    public CustomUserDetailsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public CustomUserDetailsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public CustomUserDetailsView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    private void initView(){
        LayoutInflater mInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mInflater.inflate(R.layout.layout_user_view, this, true);

        this.mContainer = (RelativeLayout)findViewById(R.id.container);
        this.tvUser = findViewById(R.id.tv_user_name);
        this.tvAge = findViewById(R.id.tv_age);
        this.tvName = findViewById(R.id.tv_name);
    }

    public void setItem(String userName, String name, String age){
        this.tvName.setText(name);
        this.tvUser.setText("User Name : "+userName);
        this.tvAge.setText("Age : "+age);
    }
}
