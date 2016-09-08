package com.example.yehu.practice1;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
//import android.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yehu.practice1.tabs.SlidingTabLayout;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;


import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;
import me.tatarka.support.job.JobInfo;
import me.tatarka.support.job.JobScheduler;


import android.app.Activity;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements MaterialTabListener {

    private Toolbar toolbar;
    private MaterialTabHost tabHost;
    private ViewPager viewPager;
    public static final int MOVIES_SEARCH_RESULTS = 0;
    public static final int MOVIES_HITS = 1;
    public static final int MOVIES_UPCOMING = 2;
    private static final String TAG_SORT_NAME = "sortName";
    private static final String TAG_SORT_DATE = "sortDate";
    private static final String TAG_SORT_RATINGS = "sortRatings";
    private JobScheduler mJobScheduler;
    ImageView iconSortName;
    private static final float BYTES_PER_PX = 4.0F;
    public static final int TAB_COUNT = 3;
    private static final int JOB_ID = 100;
    private ViewPagerAdapter adapter;
    private Fragment fragment;
    private static final long POLL_FREQUENCY = 12000000;
    private ViewGroup containerAppBar;
    private ViewPager mPager;
    private ViewPagerAdapter mAdapter;
    private FloatingActionsMenu menuMultipleActions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mJobScheduler = JobScheduler.getInstance(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                constructJob();
            }
        }, 30000);
        //containerAppBar= (ViewGroup) findViewById(R.id.container_app_bar);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);

        tabHost = (MaterialTabHost) findViewById(R.id.materialTabHost);
        viewPager = (ViewPager) findViewById(R.id.viewPager);


        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                tabHost.setSelectedNavigationItem(position);
            }
        });

        for (int i = 0; i < adapter.getCount(); i++) {
            tabHost.addTab(
                    tabHost.newTab()
                            .setText(adapter.getPageTitle(i))
                            .setTabListener(this)
            );
        }

        buildFAB();

    }

    public void onDrawerItemClicked(int index){
        viewPager.setCurrentItem(index);

    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    private void buildFAB() {

        //add action of visible or unvisible
        //https://github.com/futuresimple/android-floating-action-button

       /* final View actionB = findViewById(R.id.button_gone);
        FloatingActionButton actionC = new FloatingActionButton(getBaseContext());
        actionC.setTitle("Hide/Show Action above");
        actionC.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                actionB.setVisibility(actionB.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
            }
        });*/

        menuMultipleActions= (FloatingActionsMenu) findViewById(R.id.multiple_actions_up);
        //menuMultipleActions.addButton(actionC);

        final FloatingActionButton sortByDate = (FloatingActionButton) findViewById(R.id.sort_by_date);
        sortByDate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment = (Fragment) adapter.instantiateItem(viewPager, viewPager.getCurrentItem());
                //menuMultipleActions.setEnabled(!menuMultipleActions.isEnabled());
                if (fragment instanceof SortListener) {
                    ((SortListener) fragment).onSortByDate();

                }
            }
        });

        final FloatingActionButton sortByRating = (FloatingActionButton) findViewById(R.id.sort_by_rating);
        sortByRating.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //menuMultipleActions.setEnabled(!menuMultipleActions.isEnabled());
                fragment = (Fragment) adapter.instantiateItem(viewPager, viewPager.getCurrentItem());
                if (fragment instanceof SortListener) {
                    ((SortListener) fragment).onSortByRating();

                }
            }
        });

        final FloatingActionButton sortByName = (FloatingActionButton) findViewById(R.id.sort_by_name);
        sortByName.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment = (Fragment) adapter.instantiateItem(viewPager, viewPager.getCurrentItem());

                if (fragment instanceof SortListener) {

                    ((SortListener) fragment).onSortByName();

                }
            }
        });

    }

    private void constructJob() {
        JobInfo.Builder builder = new JobInfo.Builder(JOB_ID, new ComponentName(this, ServiceMoviesBoxOffice.class));
        builder.setPeriodic(POLL_FREQUENCY)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                .setPersisted(true);
        mJobScheduler.schedule(builder.build());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.system) {
            Toast.makeText(this, "Hey you just hit" + item.getTitle(), Toast.LENGTH_SHORT).show();
            return true;
        }
        if (id == R.id.navigate) {
            startActivity(new Intent(this, SubActivity.class));
        }
        if (id == R.id.tab_with_library) {
            startActivity(new Intent(this, ActivityUsingTabLibrary.class));
            return true;
        }
        if (id == R.id.vector_test_activity) {
            startActivity(new Intent(this, VectorTestActivity.class));
            return true;
        }
        if (R.id.action_recycler_item_animations == id) {
            startActivity(new Intent(this, ActivityRecyclerItemAnimations.class));
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onTabSelected(MaterialTab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabReselected(MaterialTab tab) {

    }

    @Override
    public void onTabUnselected(MaterialTab tab) {

    }
    private void toggleTranslateFAB (float slideOffset){
        if( menuMultipleActions!= null){
            menuMultipleActions.setTranslationX(slideOffset * 200);
        }
    }

    public void onDrawerSlide(float slideOffset){
        toggleTranslateFAB(slideOffset);
    }

    /*public void onClick(View v) {
        Fragment fragment = (Fragment) adapter.instantiateItem(viewPager, viewPager.getCurrentItem());
        if (fragment instanceof SortListener) {

            if (v.getTag().equals(TAG_SORT_NAME)) {
                ((SortListener) fragment).onSortByName();
            }
            if (v.getTag().equals(TAG_SORT_DATE)) {
                ((SortListener) fragment).onSortByDate();
            }
            if (v.getTag().equals(TAG_SORT_RATINGS)) {
                ((SortListener) fragment).onSortByRating();
            }
        }
    }*/


    public class ViewPagerAdapter extends FragmentStatePagerAdapter {

        int icons[] = {R.drawable.number1, R.drawable.number2, R.drawable.number3};

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch (position) {
                case MOVIES_SEARCH_RESULTS:
                    fragment = FragmentSearch.newInstance("", "");
                    break;
                case MOVIES_HITS:
                    fragment = FragmentBoxOffice.newInstance("", "");
                    break;
                case MOVIES_UPCOMING:
                    fragment = FragmentUpcoming.newInstance("", "");
                    break;

            }
            return fragment;
        }

        @Override
        public int getCount() {
            return TAB_COUNT;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return getResources().getStringArray(R.array.tabs)[position];
        }

        private Drawable getIcon(int position) {
            return getResources().getDrawable(icons[position]);
        }
    }
}










