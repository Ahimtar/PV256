package cz.muni.fi.pv256.movio2.uco_422196;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
    private ArrayList<Film> mFilmList = new ArrayList<>();
    private Button mButton1;
    private Button mButton2;
    private Button mButton3;

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);

        try {
            mListener = (OnFilmSelectListener) activity;
        } catch (ClassCastException e) {
            Log.e(TAG, "Activity must implement OnFilmSelectListener", e);
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
        mFilmList.add(new Film( getCurrentTime().getTime(),"", "Popis1 Popis1 Popis1 Popis1", "Nazov1", 0.0f));
        mFilmList.add(new Film( getCurrentTime().getTime(),"", "Popis2 Popis2 Popis2 Popis2", "Nazov2", 0.0f));
        mFilmList.add(new Film( getCurrentTime().getTime(),"", "Popis3 Popis3 Popis3 Popis3", "Nazov3", 0.0f));

        mButton1 = (Button) view.findViewById(R.id.film1);
        mButton2 = (Button) view.findViewById(R.id.film2);
        mButton3 = (Button) view.findViewById(R.id.film3);

        mButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onFilmSelect(mFilmList.get(0));
            }
        });

        mButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onFilmSelect(mFilmList.get(1));
            }
        });

        mButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onFilmSelect(mFilmList.get(2));
            }
        });


        if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)) {
            mPosition = savedInstanceState.getInt(SELECTED_KEY);
        }

        return view;
    }

    private Date getCurrentTime() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+1:00"));
        return cal.getTime();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mPosition != ListView.INVALID_POSITION) {
            outState.putInt(SELECTED_KEY, mPosition);
        }
        super.onSaveInstanceState(outState);
    }

    private void setListener(Button button, final int position, final ArrayList<Film> filmList) {
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mPosition = position;
                mListener.onFilmSelect(filmList.get(position));
            }
        });
    }

    public interface OnFilmSelectListener {
        void onFilmSelect(Film film);
    }
}
