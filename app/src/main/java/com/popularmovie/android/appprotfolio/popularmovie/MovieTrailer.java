package com.popularmovie.android.appprotfolio.popularmovie;


public class MovieTrailer {


    /**
     *  "id": "54282ba2c3a3680b1b002905",
     "iso_639_1": "en",
     "key": "NuUnJ5WL1g",
     "name": "Trailer",
     "site": "YouTube",
     "size": 360,
     "type": "Trailer"
     */

    private String movieTrailerID;
    private String iso_639_1;
    private String key;
    private String name;
    private String site;
    private String size;
    private String type;
    private String movieId;

    public String getMovieTrailerID() {
        return movieTrailerID;
    }

    public void setMovieTrailerID(String movieTrailerID) {
        this.movieTrailerID = movieTrailerID;
    }

    public String getIso_639_1() {
        return iso_639_1;
    }

    public void setIso_639_1(String iso_639_1) {
        this.iso_639_1 = iso_639_1;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }
}
