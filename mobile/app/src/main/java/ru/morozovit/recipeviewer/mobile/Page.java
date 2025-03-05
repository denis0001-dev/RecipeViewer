package ru.morozovit.recipeviewer.mobile;

public interface Page {
    default void onPageSelected(int position) {
        // Default implementation does nothing
    }
}
