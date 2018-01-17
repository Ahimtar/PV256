package cz.muni.fi.pv256.movio2.uco_422196;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Ja on 7.1.2018.
 */

public interface FilmAPI {
    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })

    @GET("3/movie/{id}?api_key=" + App.API_KEY)
    Call<FilmDTO> getFilmById(@Path("id") Long id);

    @GET("3/discover/movie?api_key=" + App.API_KEY + "&sort_by=release_date.desc")
    Call<FilmList> getFilmNewList(@Query("primary_release_date.gte") String dateWeekAgo, @Query("primary_release_date.lte") String dateNow);

    @GET("3/discover/movie?api_key=" + App.API_KEY + "&sort_by=popularity.desc")
    Call<FilmList> getFilmPopularList();
}
