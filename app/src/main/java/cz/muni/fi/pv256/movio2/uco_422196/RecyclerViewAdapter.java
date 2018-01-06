package cz.muni.fi.pv256.movio2.uco_422196;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ja on 29.12.2017.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context mAppContext;
    private List<Object> mFilmList;
    private FilmListFragment mFilmListFragment;

    public RecyclerViewAdapter(List<Object> filmList, Context context, FilmListFragment filmListFragment) {
        mFilmList = filmList;
        mFilmListFragment = filmListFragment;
        mAppContext = context.getApplicationContext();
    }

    @Override
    public int getItemCount() {
        return mFilmList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return mFilmList.get(position) instanceof Film ? 1 : 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewIsFilm) {
        LayoutInflater inflater = (LayoutInflater) mAppContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (BuildConfig.logging) {
            Log.d("Inflating", "Inflating " + parent.getId());
        }
        View view;
        if (viewIsFilm == 0){
            view = inflater.inflate(R.layout.category, parent, false);
            //TextView text = (TextView) view.findViewById(R.id.list_item_text);
            return new CategoryViewHolder(view);
        } else {
            view = inflater.inflate(R.layout.list_item_film, parent, false);
            return new FilmViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if(mFilmList.get(position) instanceof Film) {

            FilmViewHolder filmHolder = (FilmViewHolder) holder;
            Film film = (Film) mFilmList.get(position);
            if (BuildConfig.logging) {
                Log.d("Binding", "Binding " + film.getTitle());
            }
            filmHolder.text.setText(film.getTitle());
            filmHolder.popularity.setText(String.valueOf(film.getPopularity()));
            int coverId = mAppContext.getResources().getIdentifier(film.getSmallPath(), "drawable", mAppContext.getPackageName());
            Drawable cover = mAppContext.getResources().getDrawable(coverId);
            filmHolder.coverImageView.setImageDrawable(cover);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mFilmListFragment.clickedFilm(position);
                }
            });

            Palette palette = Palette.generate(BitmapFactory.decodeResource(mAppContext.getResources(), coverId));
            int backgroundColorOpaque = palette.getDarkVibrantColor(0x000000);
            int backgroundColorTransparent = Color.argb(128, Color.red(backgroundColorOpaque), Color.green(backgroundColorOpaque), Color.blue(backgroundColorOpaque));
            GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{Color.TRANSPARENT, backgroundColorOpaque});
            filmHolder.text.setBackgroundColor(backgroundColorTransparent);
            filmHolder.popularity.setBackgroundColor(backgroundColorOpaque);
            filmHolder.itemView.setTag(film);
        }
        else
        {
            CategoryViewHolder categoryHolder = (CategoryViewHolder) holder;
            categoryHolder.text.setText((String) mFilmList.get(position));
        }

    }

    static class FilmViewHolder extends RecyclerView.ViewHolder {
        private ImageView coverImageView;
        public TextView text;
        public TextView popularity;

        public FilmViewHolder(View view) {
            super(view);
            text = (TextView) itemView.findViewById(R.id.list_item_text);
            coverImageView = (ImageView) view.findViewById(R.id.list_item_icon);
            popularity = (TextView) itemView.findViewById(R.id.list_item_popularity);
        }
    }

    static class CategoryViewHolder extends RecyclerView.ViewHolder {
        public TextView text;

        public CategoryViewHolder(View view) {
            super(view);
            text = (TextView) itemView.findViewById(R.id.category_type);
        }
    }
}
