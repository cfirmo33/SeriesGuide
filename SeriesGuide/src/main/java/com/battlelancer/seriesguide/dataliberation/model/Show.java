
package com.battlelancer.seriesguide.dataliberation.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Show {
    @SerializedName("tvdb_id")
    public int tvdbId;

    public String title;

    public List<Season> seasons;

    public boolean favorite;

    public boolean hidden;

    public String language;

    public int release_time;
    public int release_weekday;
    public String release_timezone;

    public String country;

    @SerializedName("last_watched_episode")
    public int lastWatchedEpisode;

    public String poster;

    @SerializedName("content_rating")
    public String contentRating;

    public String status;

    public int runtime;

    public String network;

    @SerializedName("imdb_id")
    public String imdbId;

    @SerializedName("trakt_id")
    public Integer traktId;

    @SerializedName("first_aired")
    public String firstAired;

    /*
     * Full dump only following.
     */

    public String overview;

    public double rating;
    public int rating_votes;
    public int rating_user;

    public String genres;

    @SerializedName("last_updated")
    public long lastUpdated;

    @SerializedName("last_edited")
    public long lastEdited;
}
