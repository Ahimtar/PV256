package cz.muni.fi.pv256.movio2.uco_422196;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static cz.muni.fi.pv256.movio2.uco_422196.DownloadService.ACTION;
import static cz.muni.fi.pv256.movio2.uco_422196.DownloadService.ERROR;
import static cz.muni.fi.pv256.movio2.uco_422196.DownloadService.NEW;
import static cz.muni.fi.pv256.movio2.uco_422196.DownloadService.NO_ERROR;
import static cz.muni.fi.pv256.movio2.uco_422196.DownloadService.POPULAR;

/**
 * Created by Ja on 4.12.2017.
 */

public class FilmListFragment extends Fragment {

    private static final String TAG = FilmListFragment.class.getSimpleName();
    private static final String SELECTED_KEY = "selected_position";

    private int mPosition = ListView.INVALID_POSITION;
    private OnFilmSelectListener mListener;
    private Context mContext;
    protected static List<Object> mFilmList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mRecyclerViewAdapter;
    private ViewStub mEmptyView;
    private DownloadReceiver mReceiver;

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

        mEmptyView = (ViewStub) view.findViewById(R.id.empty);
        if (!online()) {
            ((TextView) (view.findViewById(R.id.emptyText))).setText("Offline");
        }
        else {
            if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)) {
                mPosition = savedInstanceState.getInt(SELECTED_KEY);

                if (mPosition != ListView.INVALID_POSITION) {
                    mRecyclerView.smoothScrollToPosition(mPosition);
                }
            }
            mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_films);
            mRecyclerViewAdapter = new RecyclerViewAdapter(new ArrayList<>(), getContext(), this);
            mRecyclerView.setAdapter(mRecyclerViewAdapter);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

            Intent intent = new Intent(getActivity(), DownloadService.class);
            getActivity().startService(intent);
            IntentFilter intentFilter = new IntentFilter(ACTION);
            mReceiver = new DownloadReceiver();
            LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mReceiver, intentFilter);
        }
        return view;
    }

    public boolean online() {
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo network = cm.getActiveNetworkInfo();
        return (network != null && network.isConnected());
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

    public interface OnFilmSelectListener {
        void onFilmSelect(Film film);
    }

    public class DownloadReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String error = intent.getStringExtra(ERROR);
            if(error.equals(NO_ERROR)) {

                ArrayList<FilmDTO> filmNewList = (ArrayList<FilmDTO>)intent.getSerializableExtra(NEW);
                ArrayList<FilmDTO> filmPopularList = (ArrayList<FilmDTO>)intent.getSerializableExtra(POPULAR);
                mFilmList.add("New");
                addListToMFilmList(filmNewList);
                mFilmList.add("Popular");
                addListToMFilmList(filmPopularList);

                if (mRecyclerViewAdapter != null) {
                    mRecyclerViewAdapter.dataUpdate(mFilmList);
                }
            }
            else {
                mRecyclerView.setVisibility(View.GONE);
                mEmptyView.setVisibility(View.VISIBLE);
            }
        }
    }

    private void addListToMFilmList(ArrayList<FilmDTO> filmList){
        for (FilmDTO m : filmList) {
            Film film = new Film(m.getReleaseDateAsLong(), m.getCoverPath(), m.getTitle(), m.getSmallPath(), m.getPopularityAsFloat(), m.getDescription());
            mFilmList.add(film);
        }
    }
}
