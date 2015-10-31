package com.popularmovie.android.appprotfolio.popularmovie;


public class MovieReviews {
    private String movieReviewId;

    public String getMovieReviewId() {
        return movieReviewId;
    }

    public void setMovieReviewId(String movieReviewId) {
        this.movieReviewId = movieReviewId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMovieReviewUri() {
        return movieReviewUri;
    }

    public void setMovieReviewUri(String movieReviewUri) {
        this.movieReviewUri = movieReviewUri;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    private String author;
    private String content;
    private String movieReviewUri;
    private String movieId;


}
