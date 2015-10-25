package com.popularmovie.android.appprotfolio.popularmovie;


public class   MovieSelection {
    private String page = "1";
    private SelectionType selectionType = null;

    public void setPage(String page) {
        this.page = page;
    }

    public void setSelectionType(SelectionType selectionType) {
        this.selectionType = selectionType;
    }

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
