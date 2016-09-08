package com.example.yehu.practice1;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;



/**
 * A simple {@link Fragment} subclass.
 */
public class NavigationDrawerFragment extends Fragment{

    private RecyclerView recyclerView;
    public static final String PREF_FILE_NAME ="testpref";
    public static final String KEY_USER_LEARNED_DRAWER = "user_learned_drawer";
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private VivzAdapter adapter;

    private boolean mUserLearnedDrawer;
    private boolean mFromSavedInstanceState;

    private View containerView;

    public NavigationDrawerFragment() {
        // Required empty public constructor
    }

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mUserLearnedDrawer = Boolean.valueOf(readFromPreferences(getActivity(), KEY_USER_LEARNED_DRAWER, "false"));
        if (savedInstanceState != null){
            mFromSavedInstanceState = true;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View layout = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);

        recyclerView = (RecyclerView) layout.findViewById(R.id.drawerList);
        adapter = new VivzAdapter(getActivity(), getData());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                mDrawerLayout.closeDrawer(GravityCompat.START);
                ((MainActivity)getActivity()).onDrawerItemClicked(position - 1);
            }

            @Override
            public void onLongClick(View view, int position) {
            //    Toast.makeText(getActivity(), "onLongClick" + position, Toast.LENGTH_SHORT).show();
            }
        }));
        return layout;
    }

    public List<Information> getData(){
        List<Information> data = new ArrayList<>();
        int[] icons = {R.drawable.number1, R.drawable.number2, R.drawable.number3, R.drawable.number4};
        String[] titles = getResources().getStringArray(R.array.drawer_tabs);
        for (int i= 0; i<3; i++){
            Information current = new Information();
            current.iconId = icons[i%icons.length];
            current.title = titles[i%titles.length];
            data.add(current);
        }

        return data;
    }

    public void setUp(int fragmentId, DrawerLayout drawerLayout, final Toolbar toolbar) {
            containerView = getActivity().findViewById(fragmentId);
            mDrawerLayout = drawerLayout;
            mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){
                @Override
                public void onDrawerOpened(View drawerView) {
                    super.onDrawerOpened(drawerView);
                    if (!mUserLearnedDrawer){
                        mUserLearnedDrawer = true;
                        saveToPreferences(getActivity(),KEY_USER_LEARNED_DRAWER, mUserLearnedDrawer + "");
                    }
                    getActivity().invalidateOptionsMenu();
            }

                @Override
                public void onDrawerClosed(View drawerView) {
                    super. onDrawerClosed(drawerView);
                    getActivity().invalidateOptionsMenu();

                }

                @Override
                public void onDrawerSlide(View drawerView, float slideOffset) {
                    super.onDrawerSlide(drawerView, slideOffset);
                    ((MainActivity) getActivity()).onDrawerSlide(slideOffset);
                    toolbar.setAlpha(1 - slideOffset / 2);
                }
            };

        if (!mUserLearnedDrawer && !mFromSavedInstanceState){
            mDrawerLayout.openDrawer(containerView);
        }


            mDrawerLayout.setDrawerListener(mDrawerToggle);
            mDrawerLayout.post(new Runnable() {
                @Override
                public void run() {
                    mDrawerToggle.syncState();

                }
            });

    }

    public static  void saveToPreferences(Context context, String preferenceName, String preferenceVale){
        SharedPreferences sharedPreferences =context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(preferenceName, preferenceVale);
        editor.apply();
    }
    public static String readFromPreferences(Context context, String preferenceName, String defaultValue){
        SharedPreferences sharedPreferences =context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(preferenceName, defaultValue);
    }




    class RecyclerTouchListener implements RecyclerView.OnItemTouchListener{

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener (Context context, final RecyclerView recyclerView, final ClickListener clickListener){

            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener(){
                public boolean onSingleTapUp (MotionEvent e){

                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null){
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));

                    }
               //     Log.d("VIVZ", "onLongPress " + e);

                }
            });
        }


        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)){
                clickListener.onClick(child, rv.getChildPosition(child));
            }
          //  Log.d("VIVZ", "onInterceptTouchEvent " + gestureDetector.onTouchEvent(e) + "  " + e);
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
          //  Log.d("VIVZ", "onTouchEvent " + e);
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
          //  Log.d("VIVZ", "onRequestDisallowInterceptTouchEvent invoked");
        }


    }


    public static interface ClickListener{
        public void onClick(View view, int position);
        public void onLongClick(View view, int position);
    }
}






















