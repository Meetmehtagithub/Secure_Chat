package com.example.chat;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class pageradaptar extends FragmentPagerAdapter {

    int tabCount;
    public pageradaptar(@NonNull FragmentManager fm) {
        super(fm);
    }

    public pageradaptar(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        tabCount=behavior;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                return new chatfragment();
            case 1:
                return new callfragment();
            default:
                return  null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
