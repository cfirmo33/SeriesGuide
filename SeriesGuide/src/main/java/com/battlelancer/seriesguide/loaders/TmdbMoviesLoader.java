package com.battlelancer.seriesguide.loaders;

import android.text.TextUtils;
import com.battlelancer.seriesguide.R;
import com.battlelancer.seriesguide.SgApp;
import com.battlelancer.seriesguide.settings.DisplaySettings;
import com.battlelancer.seriesguide.tmdbapi.SgTmdb;
import com.uwetrottmann.androidutils.AndroidUtils;
import com.uwetrottmann.androidutils.GenericSimpleLoader;
import com.uwetrottmann.tmdb2.entities.Movie;
import com.uwetrottmann.tmdb2.entities.MovieResultsPage;
import com.uwetrottmann.tmdb2.services.MoviesService;
import com.uwetrottmann.tmdb2.services.SearchService;
import dagger.Lazy;
import java.io.IOException;
import java.util.List;
import javax.inject.Inject;
import retrofit2.Response;

/**
 * Loads a list of movies from TMDb.
 */
public class TmdbMoviesLoader extends GenericSimpleLoader<TmdbMoviesLoader.Result> {

    public static class Result {
        public List<Movie> results;
        public int emptyTextResId;

        public Result(List<Movie> results, int emptyTextResId) {
            this.results = results;
            this.emptyTextResId = emptyTextResId;
        }
    }

    @Inject Lazy<MoviesService> moviesService;
    @Inject Lazy<SearchService> searchService;
    private String mQuery;

    public TmdbMoviesLoader(SgApp app, String query) {
        super(app);
        app.getServicesComponent().inject(this);
        mQuery = query;
    }

    @Override
    public Result loadInBackground() {
        String languageCode = DisplaySettings.getContentLanguage(getContext());

        List<Movie> results = null;
        String action = null;
        try {
            Response<MovieResultsPage> response;

            if (TextUtils.isEmpty(mQuery)) {
                action = "get now playing movies";
                response = moviesService.get()
                        .nowPlaying(null, languageCode)
                        .execute();
            } else {
                action = "search for movies";
                response = searchService.get()
                        .movie(mQuery, null, languageCode, false, null, null, null)
                        .execute();
            }

            if (response.isSuccessful()) {
                MovieResultsPage page = response.body();
                if (page != null) {
                    results = page.results;
                }
            } else {
                SgTmdb.trackFailedRequest(getContext(), action, response);
            }
        } catch (IOException e) {
            SgTmdb.trackFailedRequest(getContext(), action, e);
            // only check for connection here to allow hitting the response cache
            return new Result(null,
                    AndroidUtils.isNetworkConnected(getContext()) ? R.string.search_error
                            : R.string.offline);
        }

        return new Result(results, R.string.no_results);
    }
}
