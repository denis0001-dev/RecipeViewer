package ru.morozovit.recipeviewer.mobile.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.viewbinding.ViewBinding;
import ru.morozovit.recipeviewer.mobile.R;

public final class IngredientBinding implements ViewBinding {
    private final LinearLayout rootView;

    private IngredientBinding(LinearLayout rootView2) {
        this.rootView = rootView2;
    }

    public LinearLayout getRoot() {
        return this.rootView;
    }

    public static IngredientBinding inflate(LayoutInflater inflater) {
        return inflate(inflater, (ViewGroup) null, false);
    }

    public static IngredientBinding inflate(LayoutInflater inflater, ViewGroup parent, boolean attachToParent) {
        View root = inflater.inflate(R.layout.ingredient, parent, false);
        if (attachToParent) {
            parent.addView(root);
        }
        return bind(root);
    }

    public static IngredientBinding bind(View rootView2) {
        if (rootView2 != null) {
            return new IngredientBinding((LinearLayout) rootView2);
        }
        throw new NullPointerException("rootView");
    }
}
