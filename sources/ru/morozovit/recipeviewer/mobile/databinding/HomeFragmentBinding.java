package ru.morozovit.recipeviewer.mobile.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import ru.morozovit.recipeviewer.mobile.R;

public final class HomeFragmentBinding implements ViewBinding {
    public final Button createYourRecipeButton;
    private final ScrollView rootView;
    public final Button viewARecipeButton;

    private HomeFragmentBinding(ScrollView rootView2, Button createYourRecipeButton2, Button viewARecipeButton2) {
        this.rootView = rootView2;
        this.createYourRecipeButton = createYourRecipeButton2;
        this.viewARecipeButton = viewARecipeButton2;
    }

    public ScrollView getRoot() {
        return this.rootView;
    }

    public static HomeFragmentBinding inflate(LayoutInflater inflater) {
        return inflate(inflater, (ViewGroup) null, false);
    }

    public static HomeFragmentBinding inflate(LayoutInflater inflater, ViewGroup parent, boolean attachToParent) {
        View root = inflater.inflate(R.layout.home_fragment, parent, false);
        if (attachToParent) {
            parent.addView(root);
        }
        return bind(root);
    }

    public static HomeFragmentBinding bind(View rootView2) {
        int id = R.id.createYourRecipeButton;
        Button createYourRecipeButton2 = (Button) ViewBindings.findChildViewById(rootView2, id);
        if (createYourRecipeButton2 != null) {
            id = R.id.viewARecipeButton;
            Button viewARecipeButton2 = (Button) ViewBindings.findChildViewById(rootView2, id);
            if (viewARecipeButton2 != null) {
                return new HomeFragmentBinding((ScrollView) rootView2, createYourRecipeButton2, viewARecipeButton2);
            }
        }
        throw new NullPointerException("Missing required view with ID: ".concat(rootView2.getResources().getResourceName(id)));
    }
}
