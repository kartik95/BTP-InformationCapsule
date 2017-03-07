package btp.psychosocialeducationapp;

//import android.os.Bundle;
//import android.support.design.widget.FloatingActionButton;
//import android.support.design.widget.Snackbar;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;
//import android.view.View;
//import android.view.Menu;
//import android.view.MenuItem;
//
//public class MainActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
//}

//import android.os.Bundle;
//import android.support.design.widget.TabLayout;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentPagerAdapter;
//import android.support.v4.view.ViewPager;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class MainActivity extends AppCompatActivity {
//
//    private Toolbar toolbar;
//    private TabLayout tabLayout;
//    private ViewPager viewPager;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
//
//        viewPager = (ViewPager) findViewById(R.id.viewpager);
//        setupViewPager(viewPager);
//
//        tabLayout = (TabLayout) findViewById(R.id.tabs);
//        tabLayout.setupWithViewPager(viewPager);
//    }
//
//    private void setupViewPager(ViewPager viewPager) {
//        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
//        adapter.addFragment(new LatestFragment(), "Latest");
//        adapter.addFragment(new SavedFragment(), "Saved");
////        adapter.addFragment(new ThreeFragment(), "THREE");
//        viewPager.setAdapter(adapter);
//    }
//
//    class ViewPagerAdapter extends FragmentPagerAdapter {
//        private final List<Fragment> mFragmentList = new ArrayList<>();
//        private final List<String> mFragmentTitleList = new ArrayList<>();
//
//        public ViewPagerAdapter(FragmentManager manager) {
//            super(manager);
//        }
//
//        @Override
//        public Fragment getItem(int position) {
//            return mFragmentList.get(position);
//        }
//
//        @Override
//        public int getCount() {
//            return mFragmentList.size();
//        }
//
//        public void addFragment(Fragment fragment, String title) {
//            mFragmentList.add(fragment);
//            mFragmentTitleList.add(title);
//        }
//
//        @Override
//        public CharSequence getPageTitle(int position) {
//            return mFragmentTitleList.get(position);
//        }
//    }
//}

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;


public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{
    private static Toolbar toolbar;
    private static ViewPager viewPager;
    private static TabLayout tabLayout;
    private GoogleApiClient mGoogleApiClient;
    private HashMap<String, NewsFeed> savedNewsFeeds;
    private RecyclerViewAdapter adapter;
    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private Animation fab_open,fab_close,rotate_forward,rotate_backward;
    private Boolean isFabOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        fab = (FloatingActionButton)findViewById(R.id.fab);
//        fab1 = (FloatingActionButton)findViewById(R.id.fab1);
//        fab2 = (FloatingActionButton)findViewById(R.id.fab2);
//        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
//        fab_close = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_close);
//        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_forward);
//        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_backward);
//        fab.setOnClickListener(this);
//        fab1.setOnClickListener(this);
//        fab2.setOnClickListener(this);

//        viewPager = (ViewPager) findViewById(R.id.viewPager);
//        setupViewPager(viewPager);
//
//        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
//        tabLayout.setupWithViewPager(viewPager);//setting tab over viewpager
        SharedPreferences prefs = this.getSharedPreferences("My_Prefs2", Context.MODE_PRIVATE);
        Set<String> keysSet = prefs.getStringSet("mapKeys", null);
        if (keysSet != null) {
            savedNewsFeeds = new HashMap<>();
            List<String> mapKeys = new ArrayList<>(keysSet);
            Gson gson = new GsonBuilder().registerTypeAdapter(Uri.class, new MainActivity.UriDeserializer()).create();
            for (int i=0; i<mapKeys.size(); i++){
                String key = mapKeys.get(i);
                Log.d("Key : ", key);
                String json = prefs.getString(key, "");
                Log.d("Json : ", json);
                NewsFeed newsFeed = gson.fromJson(json, NewsFeed.class);
                savedNewsFeeds.put(key, newsFeed);
            }
        }

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new MainActivity.GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        ArrayList<NewsFeed> newsFeeds = getNewsFeeds(getTitles(), getDates(), getImageURIs(), getDescriptions());
        if (savedNewsFeeds == null) {savedNewsFeeds = new HashMap<>();}
        adapter = new RecyclerViewAdapter(this, newsFeeds, savedNewsFeeds);
        recyclerView.setAdapter(adapter);
//
//        try {
//            Glide.with(this).load(R.drawable.backdrop).into((ImageView) findViewById(R.id.backdrop));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


        fab = (FloatingActionButton) findViewById(R.id.fab1);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Favorites.class);
                startActivity(intent);
            }
        });
//        final FrameLayout frameLayout = (FrameLayout) findViewById(R.id.frame_layout);
//        frameLayout.getBackground().setAlpha(0);
//        final FloatingActionsMenu fabMenu = (FloatingActionsMenu) findViewById(R.id.fab_menu);
//        fabMenu.setOnFloatingActionsMenuUpdateListener(new FloatingActionsMenu.OnFloatingActionsMenuUpdateListener() {
//            @Override
//            public void onMenuExpanded() {
//                frameLayout.getBackground().setAlpha(240);
//                frameLayout.setOnTouchListener(new View.OnTouchListener() {
//                    @Override
//                    public boolean onTouch(View v, MotionEvent event) {
//                        fabMenu.collapse();
//                        return true;
//                    }
//                });
//            }
//
//            @Override
//            public void onMenuCollapsed() {
//                frameLayout.getBackground().setAlpha(0);
//                frameLayout.setOnTouchListener(null);
//            }
//        });
        //Implementing tab selected listener over tablayout
//        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                viewPager.setCurrentItem(tab.getPosition());//setting current selected item over viewpager
//                switch (tab.getPosition()) {
//                    case 0:
//                        Log.e("TAG","TAB1");
//                        break;
//                    case 1:
//                        Log.e("TAG","TAB2");
//                        break;
//                }
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//            }
//        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_settings) {
            return true;
        }
        if (item.getItemId() == R.id.logout) {

            SharedPreferences.Editor editor = getSharedPreferences("My_Prefs", Context.MODE_PRIVATE).edit();
            editor.clear();
            editor.commit();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            mGoogleApiClient.clearDefaultAccountAndReconnect();
            mGoogleApiClient.disconnect();
            startActivity(intent);
            finish();
            return true;
        }
        if (item.getItemId() == R.id.edit_profile) {

            Intent intent = new Intent(MainActivity.this, EditActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }


    //Setting View Pager
//    private void setupViewPager(ViewPager viewPager) {
//        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
//        adapter.addFrag(new LatestFragment(), "Latest");
//        adapter.addFrag(new SavedFragment(), "Saved");
//        viewPager.setAdapter(adapter);
//    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }


    //View Pager fragments setting adapter class
//    class ViewPagerAdapter extends FragmentPagerAdapter {
//        private final List<Fragment> mFragmentList = new ArrayList<>();//fragment arraylist
//        private final List<String> mFragmentTitleList = new ArrayList<>();//title arraylist
//
//        public ViewPagerAdapter(FragmentManager manager) {
//            super(manager);
//        }
//
//        @Override
//        public Fragment getItem(int position) {
//            return mFragmentList.get(position);
//        }
//
//        @Override
//        public int getCount() {
//            return mFragmentList.size();
//        }
//
//
//        //adding fragments and title method
//        public void addFrag(Fragment fragment, String title) {
//            mFragmentList.add(fragment);
//            mFragmentTitleList.add(title);
//        }
//
//        @Override
//        public CharSequence getPageTitle(int position) {
//            return mFragmentTitleList.get(position);
//        }
//    }

    private ArrayList getTitles() {
        ArrayList<String> titles = new ArrayList<>();
        titles.add("What is Schizophrenia?");
        titles.add("Symptoms of Schizophrenia");
        titles.add("Causes of Schizophrenia");
        titles.add("Therapy of Schizophrenia");
        titles.add("Suggestions regarding Schizophrenia");
        titles.add("Incidents of Schizophrenia");
        return titles;
    }


    private ArrayList getDescriptions() {
        ArrayList<String> descs = new ArrayList<>();
        descs.add("Schizophrenia is a mental disorder characterized by abnormal social behavior and failure to understand what is real.");
        descs.add("Common symptoms include false beliefs, unclear or confused thinking, hearing voices that others do not, reduced social engagement and emotional expression, and a lack of motivation.");
        descs.add("The causes of schizophrenia include environmental and genetic factors. Possible environmental factors include being raised in a city, cannabis use, certain infections, parental age, and poor nutrition during pregnancy.");
        descs.add("Schizophrenia is a mental disorder characterized by abnormal social behavior and failure to understand what is real.");
        descs.add("Common symptoms include false beliefs, unclear or confused thinking, hearing voices that others do not, reduced social engagement and emotional expression, and a lack of motivation.");
        descs.add("The causes of schizophrenia include environmental and genetic factors. Possible environmental factors include being raised in a city, cannabis use, certain infections, parental age, and poor nutrition during pregnancy.");
        return descs;
    }


    private ArrayList getDates() {
        ArrayList<String> dates = new ArrayList<>();
        dates.add("January 28th, 2017");
        dates.add("February 2nd, 2017");
        dates.add("February14th, 2017");
        dates.add("January 28th, 2017");
        dates.add("February 2nd, 2017");
        dates.add("February14th, 2017");
        return dates;
    }


    private ArrayList<Uri> getImageURIs() {
        ArrayList<Uri> imageURIs = new ArrayList<>();
        for (int i =1; i<=6; i++){
            Uri uri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/drawable/btp" + i);
            imageURIs.add(uri);
        }
        return imageURIs;
    }


    private ArrayList<NewsFeed> getNewsFeeds(ArrayList<String> titles, ArrayList<String> dates, ArrayList<Uri> imageUris,
                                             ArrayList<String> descs) {
        ArrayList<NewsFeed> newsFeeds = new ArrayList<>();
        for(int i=0; i<titles.size(); i++) {
            NewsFeed newsFeed = new NewsFeed(titles.get(i), dates.get(i), imageUris.get(i), descs.get(i));
            newsFeeds.add(newsFeed);
        }
        return newsFeeds;
    }

    public class UriDeserializer implements JsonDeserializer<Uri> {
        @Override
        public Uri deserialize(final JsonElement src, final Type srcType,
                               final JsonDeserializationContext context) throws JsonParseException {
            return Uri.parse(src.getAsString());
        }
    }

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}