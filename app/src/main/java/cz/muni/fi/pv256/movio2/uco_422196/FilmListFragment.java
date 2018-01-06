package cz.muni.fi.pv256.movio2.uco_422196;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by Ja on 4.12.2017.
 */

public class FilmListFragment extends Fragment {

    private static final String TAG = FilmListFragment.class.getSimpleName();
    private static final String SELECTED_KEY = "selected_position";

    private int mPosition = ListView.INVALID_POSITION;
    private OnFilmSelectListener mListener;
    private Context mContext;
    private List<Object> mFilmList;
    private RecyclerView mRecyclerView;

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);

        try {
            mListener = (OnFilmSelectListener) activity;
        } catch (ClassCastException e) {
            if (BuildConfig.logging){
                Log.e(TAG, "Activity must implement OnFilmSelectListener", e);
            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mListener = null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getActivity().getApplicationContext();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        if (!fillRecyclerView(view)) {
            view = inflater.inflate(R.layout.empty_layout, container, false);
            if (!online()) {
                ((TextView) (view.findViewById(R.id.emptyText))).setText("Offline");
            }
        }
        else {
            if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)) {
                mPosition = savedInstanceState.getInt(SELECTED_KEY);

                if (mPosition != ListView.INVALID_POSITION) {
                    mRecyclerView.smoothScrollToPosition(mPosition);
                }
            }
        }
        return view;
    }

    private Date getCurrentTime() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+1:00"));
        return cal.getTime();
    }

    public boolean online() {
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo network = cm.getActiveNetworkInfo();
        return (network != null && network.isConnected());
    }

    private boolean fillRecyclerView(View rootView) {
        mFilmList = new ArrayList<Object>();
        mFilmList.add("New");
        mFilmList.addAll(FilmData.getInstance().getFilmNewList());
        mFilmList.add("Popular");
        mFilmList.addAll(FilmData.getInstance().getFilmPopularList());

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview_films);

        if (mFilmList != null && !mFilmList.isEmpty()) {
            setAdapter(mRecyclerView, mFilmList);
            return true;
        }
        return false;
    }

    private void setAdapter(RecyclerView filmRV, final List<Object> filmList) {
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(filmList, mContext, this);
        filmRV.setAdapter(adapter);
        filmRV.setLayoutManager(new LinearLayoutManager(mContext));
        filmRV.setItemAnimator(new DefaultItemAnimator());
    }

    public void clickedFilm(int position)
    {
        mPosition = position;
        mListener.onFilmSelect((Film)mFilmList.get(position));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mPosition != ListView.INVALID_POSITION) {
            outState.putInt(SELECTED_KEY, mPosition);
        }
        super.onSaveInstanceState(outState);
    }

    //private void setListener(Button button, final int position, final ArrayList<Film> filmList) {
    //    button.setOnClickListener(new View.OnClickListener() {
    //        public void onClick(View v) {
    //            mPosition = position;
    //            mListener.onFilmSelect(filmList.get(position));
    //        }
    //    });
    //}

    public interface OnFilmSelectListener {
        void onFilmSelect(Film film);
    }
}
