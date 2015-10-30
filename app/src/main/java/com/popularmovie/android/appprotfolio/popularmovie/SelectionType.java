package com.popularmovie.android.appprotfolio.popularmovie;


public enum SelectionType {
    Popular("Most Popular"), HighestRated("Highest Rated"), Favourite("Favourite");
    private String sortSelection;

    SelectionType(String s) {
        this.sortSelection = s;
    }

    public String getSortType() {
        return this.sortSelection;
    }
}
