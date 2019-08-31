package com.androhungers.rssnewsreader.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.androhungers.rssnewsreader.R;
import com.androhungers.rssnewsreader.activities.LoginActivity;
import com.androhungers.rssnewsreader.activities.RssFeedActivity;
import com.androhungers.rssnewsreader.adapters.RssPagerAdapter;
import com.androhungers.rssnewsreader.common.Common;
import com.androhungers.rssnewsreader.common.Constants;
import com.androhungers.rssnewsreader.common.PreferenceHelper;
import com.androhungers.rssnewsreader.customViews.CustomUserDetailsView;
import com.androhungers.rssnewsreader.model.signin.DataModel;
import com.androhungers.rssnewsreader.viewModel.RssFeedViewModel;
import com.gigamole.navigationtabstrip.NavigationTabStrip;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.mrapp.android.dialog.MaterialDialog;


public class RssFeedFragment extends Fragment {

    @BindView(R.id.nav)
    NavigationTabStrip navigationTabStrip;

    @BindView(R.id.view_pager)
    ViewPager viewPager;

    @BindView(R.id.img_log_out)
    ImageView imgLogOut;

    public static String tag = RssFeedFragment.class.toString();
    private RssFeedActivity rssFeedActivity = new RssFeedActivity();
    ViewModel viewModel;
    RssPagerAdapter viewPagerAdater;


    public RssFeedFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rss_feed, container, false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        rssFeedActivity = (RssFeedActivity) getActivity();
        viewModel = ViewModelProviders.of(getActivity()).get(RssFeedViewModel.class);

        setUpUI();

        Log.i("::>>","Frag Activity Created");
    }

    private void setUpUI(){
        viewPagerAdater = new RssPagerAdapter(getChildFragmentManager());
        viewPager.removeAllViews();
        viewPager.setAdapter(viewPagerAdater);

        navigationTabStrip.setTitles("Home","My Rss");
        navigationTabStrip.setViewPager(viewPager);


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        imgLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataModel dataModel = new Common().getUserDataFromSignIn(new PreferenceHelper(getContext()).getString(Constants.USER_DATA_FIELD));
                String userName = String.valueOf(dataModel.getUserName());
                String name = String.valueOf(dataModel.getName());
                String age = String.valueOf(dataModel.getAge());

                CustomUserDetailsView customUserDetailsView = new CustomUserDetailsView(getContext());
                customUserDetailsView.setItem(userName,name,age);

                MaterialDialog.Builder dialogBuilder = new MaterialDialog.Builder(getContext());
                dialogBuilder.setButtonTextColor(getResources().getColor(R.color.colorPrimary));
                dialogBuilder.setTitle("Log out !!!");
                dialogBuilder.setView(customUserDetailsView);
                //dialogBuilder.setMessage("User Name : "+userName+"\nName : "+name+"\nAge : "+age+"\n\nAre you sure to Log out?");
                //dialogBuilder.setMessage("Are you sure to Log out?");

                dialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        new PreferenceHelper(getContext()).saveString(Constants.USER_DATA_FIELD,"");
                        new PreferenceHelper(getContext()).saveString(Constants.LOGIN_SATE_FIELD,"");
                        Intent intent = new Intent(getContext(), LoginActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }
                });
                dialogBuilder.setNegativeButton(android.R.string.cancel, null);
                MaterialDialog dialog = dialogBuilder.create();
                dialog.show();
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    getActivity().finish();
                    return true;
                } else
                    return false;
            }
        });

    }
}
