package ru.morozovit.recipeviewer.mobile;

import android.view.View;

public class Item {
    private final String headline;
    private final String content;
    private final View.OnClickListener onClick;

    public Item(String headline, String content, View.OnClickListener onClick) {
        this.headline = headline;
        this.content = content;
        this.onClick = onClick;
    }

    public String getHeadline() {
        return headline;
    }

    public String getContent() {
        return content;
    }

    public View.OnClickListener getOnClick() {
        return onClick;
    }
}
