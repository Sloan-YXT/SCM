package com.viewpagerindicator;

public interface IconPagerAdapter {

    int getIconResId(int index);

    // From PagerAdapter
    int getCount();
}
