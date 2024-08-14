package ru.morozovit.recipeviewer.mobile.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentContainerView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import ru.morozovit.recipeviewer.mobile.R;

public final class ActivityMainBinding implements ViewBinding {
    public final BottomNavigationView bottomNavigation;
    public final FragmentContainerView mainView;
    private final ConstraintLayout rootView;

    private ActivityMainBinding(ConstraintLayout rootView2, BottomNavigationView bottomNavigation2, FragmentContainerView mainView2) {
        this.rootView = rootView2;
        this.bottomNavigation = bottomNavigation2;
        this.mainView = mainView2;
    }

    public ConstraintLayout getRoot() {
        return this.rootView;
    }

    public static ActivityMainBinding inflate(LayoutInflater inflater) {
        return inflate(inflater, (ViewGroup) null, false);
    }

    public static ActivityMainBinding inflate(LayoutInflater inflater, ViewGroup parent, boolean attachToParent) {
        View root = inflater.inflate(R.layout.activity_main, parent, false);
        if (attachToParent) {
            parent.addView(root);
        }
        return bind(root);
    }

    public static ActivityMainBinding bind(View rootView2) {
        int id = R.id.bottom_navigation;
        BottomNavigationView bottomNavigation2 = (BottomNavigationView) ViewBindings.findChildViewById(rootView2, id);
        if (bottomNavigation2 != null) {
            id = R.id.main_view;
            FragmentContainerView mainView2 = (FragmentContainerView) ViewBindings.findChildViewById(rootView2, id);
            if (mainView2 != null) {
                return new ActivityMainBinding((ConstraintLayout) rootView2, bottomNavigation2, mainView2);
            }
        }
        throw new NullPointerException("Missing required view with ID: ".concat(rootView2.getResources().getResourceName(id)));
    }
}
