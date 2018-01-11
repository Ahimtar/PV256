package cz.muni.fi.pv256.movio2.uco_422196;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ListView;
import android.widget.Toast;

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

public class FilmListFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Film>>{

    private static final String TAG = FilmListFragment.class.getSimpleName();
    private static final String SELECTED_KEY = "selected_position";

    private int mPosition = ListView.INVALID_POSITION;
    private OnFilmSelectListener mListener;
    protected static List<Object> mFilmList = new ArrayList<>();
    protected static List<Object> mFavList = new ArrayList<>();
    private Context mContext;
    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mRecyclerViewAdapter;
    private ViewStub mEmptyView;
    private DownloadReceiver mReceiver;
    
    private SQLiteDatabase mDatabase;
    private FilmManager mFilmManager;
    private FilmDbHelper mDbHelper;
    private boolean mIsFavourite;

    public boolean getIsFavourite() {
        return mIsFavourite;
    }

    public void setIsFavourite(boolean isFavourite) {
        mIsFavourite = isFavourite;
    }

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
        if (getArguments() != null) {
            mIsFavourite = getArguments().getBoolean("isFavourite");
        }
        else {
            mIsFavourite = false;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        mEmptyView = (ViewStub) view.findViewById(R.id.empty);
        if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)) {
            mPosition = savedInstanceState.getInt(SELECTED_KEY);
        }
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_films);
        mRecyclerViewAdapter = new RecyclerViewAdapter(new ArrayList<>(), mContext, this);
        mRecyclerView.setAdapter(mRecyclerViewAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateData();
        return view;
    }

    public void updateData(){
        if (mIsFavourite) {
            mDbHelper = new FilmDbHelper(getActivity());
            mDatabase = mDbHelper.getWritableDatabase();
            mFilmManager = new FilmManager(mDatabase);
            getLoaderManager().initLoader(1, null, this);
        } else {
            if(mFilmList == null  || mFilmList.isEmpty()) {
                Intent intent = new Intent(getActivity(), DownloadService.class);
                getActivity().startService(intent);

                IntentFilter intentFilter = new IntentFilter(ACTION);
                mReceiver = new DownloadReceiver();
                LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mReceiver, intentFilter);
            }
            else {
                updateViewAdapter(mFilmList);
            }
        }
    }

    private void updateViewAdapter(final List<Object> filmList) {
        if (getActivity() == null) {
            return;
        }
        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {

                mRecyclerViewAdapter.dataUpdate(filmList);

                if (filmList != null && !(filmList.isEmpty())) {
                    mRecyclerView.setVisibility(View.VISIBLE);

                    if (mPosition == ListView.INVALID_POSITION) {
                        mPosition = 0;
                    }
                    else {
                        mRecyclerView.smoothScrollToPosition(mPosition);
                    }
                    mEmptyView.setVisibility(View.GONE);

                } else {
                    mRecyclerView.setVisibility(View.GONE);
                    mEmptyView.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public Loader<List<Film>> onCreateLoader(int id, Bundle args) {
        SqlLiteDataLoader loader = new SqlLiteDataLoader(this.getActivity(), mFilmManager, null, null, null, null, null);
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<List<Film>> loader, List<Film> data) {
        mFavList.clear();
        for (Film film : data) {
            mFavList.add(film);
        }
        mRecyclerViewAdapter.dataUpdate(mFavList);
        mDatabase.close();
    }

    @Override
    public void onLoaderReset(Loader<List<Film>> loader) {
    }

    public void clickedFilm(int position)
    {
        if (position == ListView.INVALID_POSITION) {
            return;
        }
        mPosition = position;
        if (mIsFavourite) {
            mListener.onFilmSelect((Film) mFavList.get(position));
        }
        else {
            mListener.onFilmSelect((Film) mFilmList.get(position));
        }
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
                mFilmList = new ArrayList<>();
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
                mRecyclerViewAdapter.dataUpdate(mFilmList);
            }
        }
    }

    private void addListToMFilmList(ArrayList<FilmDTO> filmList){
        for (FilmDTO m : filmList) {
            Film film = new Film(Long.parseLong(m.getId(),10), m.getReleaseDateAsLong(), m.getCoverPath(), m.getTitle(), m.getSmallPath(), m.getPopularityAsFloat(), m.getDescription());
            mFilmList.add(film);
        }
    }
}
