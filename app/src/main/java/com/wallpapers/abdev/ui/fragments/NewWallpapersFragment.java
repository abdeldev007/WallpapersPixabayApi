package com.wallpapers.abdev.ui.fragments;

import android.app.SearchManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.wallpapers.abdev.R;
import com.wallpapers.abdev.adapter.WallpapersAdapter;
import com.wallpapers.abdev.model.Image;
import com.wallpapers.abdev.network.ApiManager;
import com.wallpapers.abdev.response.ImageSearchResponse;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewWallpapersFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewWallpapersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewWallpapersFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private GridLayoutManager mLayoutManager;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String cat;
    private OnFragmentInteractionListener mListener;

    public NewWallpapersFragment(String cat) {
        this.cat = cat;
    }

    public NewWallpapersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewWallpapersFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewWallpapersFragment newInstance(String param1, String param2) {
        NewWallpapersFragment fragment = new NewWallpapersFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    ArrayList<Image> list;
    SwipeRefreshLayout swipe_refrech;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    WallpapersAdapter adapter;
    int pos = 0;
    String[] caegories = {"backgrounds", "fashion", "nature"
            , "science", "education", "feelings",
            "health", "people", "religion", "places"
            , "animals", "industry", "computer", "food"
            , "sports", "transportation", "travel"
            , "buildings", "business", "music"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Context context = getContext();

        View view = inflater.inflate(R.layout.fragment_new_wallpapers, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        list = new ArrayList<>();
        adapter = new WallpapersAdapter(getContext(), list);
        mLayoutManager = new GridLayoutManager(getContext(), 3);

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setNestedScrollingEnabled(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        getContent(cat, 1);

        swipe_refrech = view.findViewById(R.id.swipe_container);
        swipe_refrech.setOnRefreshListener(this);
//        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
        swipe_refrech.setColorSchemeResources(R.color.browser_actions_text_color,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        if (swipe_refrech.isRefreshing()) {
            swipe_refrech.setRefreshing(false);
        }
        setHasOptionsMenu(true);

        return view;
    }

    private void getContent(String search, int page) {

        ApiManager.getInstance().searchCategory(
                cat, page, new Callback<ImageSearchResponse>() {
                    @Override
                    public void success(ImageSearchResponse imageSearchResponse, Response response) {
                        list.clear();
                        list.addAll(imageSearchResponse.images);
                        adapter.notifyDataSetChanged();
                        if (swipe_refrech.isRefreshing()) {
                            swipe_refrech.setRefreshing(false);
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });


    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    int page = 1;

    @Override
    public void onRefresh() {
        page++;
        if (page > 1000)
            page = 0;
        getContent(cat, page);


    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getContext().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getActivity().getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                ApiManager.getInstance().searchCategoryAndKey(
                        cat, query, new Callback<ImageSearchResponse>() {
                            @Override
                            public void success(ImageSearchResponse imageSearchResponse, Response response) {
                                list.clear();
                                list.addAll(imageSearchResponse.images);
                                adapter.notifyDataSetChanged();
                                if (swipe_refrech.isRefreshing()) {
                                    swipe_refrech.setRefreshing(false);
                                }
                            }

                            @Override
                            public void failure(RetrofitError error) {

                            }
                        });
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {

                /* if (!TextUtils.isEmpty(query)) {

                }

                 */
                return false;
            }

        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
