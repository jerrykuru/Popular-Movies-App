package com.popularmovie.android.appprotfolio.popularmovie;


public class   MovieSelection {
    private String page = null;
    private SelectionType selectionType = null;


    MovieSelection(SelectionType selectionType, String page){
        this.page = page;
        this.selectionType = selectionType;
    }

    public String getPage(){
        return page;
    }

    public SelectionType getSelectionType(){
        return selectionType;
    }
}
