package com.androhungers.rssnewsreader.common;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.androhungers.rssnewsreader.services.NavigationManager;

import java.util.HashMap;

/**
 * This is used for fragment navigation
 * Using this easily push, pop fragments from stack
 * Total fragment manager is here
 */
public class FragmentNavigationManager implements NavigationManager {
    private AppCompatActivity  activity;
    private  int containerId;

    private FragmentManager fragmentManager;
    private HashMap<String, Integer> tagIntDic = new HashMap<String, Integer>();
    private int initialId = 0;

    public FragmentNavigationManager(AppCompatActivity activity, int containerId) {
        this.activity = activity;
        this.containerId = containerId;

        fragmentManager = activity.getSupportFragmentManager();
    }

    @Override
    public void push(Fragment fragment, String tag) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(containerId, fragment, tag).addToBackStack(tag);
        int id = transaction.commit();
        if (tagIntDic.size() == 0) {
            initialId = id;
        }
        tagIntDic.put(tag, id);
    }

    @Override
    public void pop(String tag) {
        if (tag == null) {
            fragmentManager.popBackStack();
            return;
        }
        int id = tagIntDic.get(tag);
        fragmentManager.popBackStack(id, 0);
    }


    @Override
    public boolean onBackPressed() {
        if (fragmentManager.getBackStackEntryCount() == 1) {
            activity.finish();
            return false;
        }
        pop(null);
        return true;
    }

    @Override
    public boolean isTop(String tag) {

        FragmentManager.BackStackEntry entry = fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1);
        if (entry.getName() == tag) {
            return true;
        }

        return false;
    }
}
