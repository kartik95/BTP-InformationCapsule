package btp.psychosocialeducationapp;


//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//
///**
// * A simple {@link Fragment} subclass.
// */
//public class LatestFragment extends Fragment {
//
//
//    public LatestFragment() {
//        // Required empty public constructor
//    }
//
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_latest2, container, false);
//    }
//
//}

//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//
//public class LatestFragment extends Fragment{
//
//    public LatestFragment() {
//        // Required empty public constructor
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_latest, container, false);
//    }
//
//}

import android.content.res.Resources;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.net.URI;
import java.util.ArrayList;

import static btp.psychosocialeducationapp.R.id.recyclerView;

public class LatestFragment extends Fragment {
    private View view;

    private String title;//String for tab title

    private static RecyclerView recyclerView;

    public LatestFragment() {
    }

    public LatestFragment(String title) {
        this.title = title;//Setting tab title
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_latest, container, false);

        setRecyclerView();
        return view;

    }

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

//    private ArrayList getDescriptions() {
//        ArrayList<String> descs = new ArrayList<>();
//        descs.add("Schizophrenia is a mental disorder characterized by abnormal social behavior and failure to understand what is real.");
//        descs.add("Common symptoms include false beliefs, unclear or confused thinking, hearing voices that others do not, reduced social engagement and emotional expression, and a lack of motivation.");
//        descs.add("The causes of schizophrenia include environmental and genetic factors. Possible environmental factors include being raised in a city, cannabis use, certain infections, parental age, and poor nutrition during pregnancy.");
//        return descs;
//    }

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
            Uri uri = Uri.parse("android.resource://" + getContext().getPackageName() + "/drawable/btp" + i);
            imageURIs.add(uri);
        }
        return imageURIs;
    }

    //Setting recycler view
    private void setRecyclerView() {

        recyclerView = (RecyclerView) view
                .findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
//        recyclerView
//                .setLayoutManager(new LinearLayoutManager(getActivity()));//Linear Items


//        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.setAdapter(adapter);
//        ArrayList<String> arrayList = new ArrayList<>();
//        for (int i = 0; i < 20; i++) {
//
//            arrayList.add(title+" Items " + i);//Adding items to recycler view
//        }

        ArrayList<String> titles = getTitles();
        ArrayList<String> dates = getDates();
        ArrayList<Uri> imageUris = getImageURIs();
//        RecyclerViewAdapter adapter = new RecyclerViewAdapter(getActivity(), arrayList);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(getActivity(), titles, dates, imageUris);
        recyclerView.setAdapter(adapter);// set adapter on recyclerview

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