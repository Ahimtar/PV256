package cz.muni.fi.pv256.movio2.uco_422196;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
    private DownloadingTask mDownloadingTask;

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
            mDownloadingTask = new DownloadingTask();
            mDownloadingTask.execute();
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

    private class DownloadingTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            if(BuildConfig.logging) {
                Log.d(TAG, "doInBackground - thread: " + Thread.currentThread().getName());
            }
            try {
                mFilmList.add("New");
                mFilmList.addAll(Networking.getFilmNewList());
                mFilmList.add("Popular");
                mFilmList.addAll(Networking.getFilmPopularList());
                FilmListFragment.this.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mRecyclerViewAdapter.dataUpdate(mFilmList);
                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(BuildConfig.logging) {
                Log.d(TAG, "onPostExecute - thread: " + Thread.currentThread().getName());
            }
            if (result)
                Toast.makeText(getActivity().getApplicationContext(), "Download successful", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getActivity().getApplicationContext(), "Download failed", Toast.LENGTH_SHORT).show();

            if (mFilmList.isEmpty()) {
                mRecyclerView.setVisibility(View.GONE);
                mEmptyView.setVisibility(View.VISIBLE);
            } else {
                mRecyclerView.setVisibility(View.VISIBLE);
                mEmptyView.setVisibility(View.GONE);
            }
            mDownloadingTask = null;
        }

        @Override
        protected void onCancelled() {
            if(BuildConfig.logging) {
                Log.d(TAG, "onCancelled - thread: " + Thread.currentThread().getName());
            }
        }
    }
}
