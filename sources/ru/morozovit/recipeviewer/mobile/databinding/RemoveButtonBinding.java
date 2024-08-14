package ru.morozovit.recipeviewer.mobile.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.viewbinding.ViewBinding;
import ru.morozovit.recipeviewer.mobile.R;

public final class RemoveButtonBinding implements ViewBinding {
    private final Button rootView;

    private RemoveButtonBinding(Button rootView2) {
        this.rootView = rootView2;
    }

    public Button getRoot() {
        return this.rootView;
    }

    public static RemoveButtonBinding inflate(LayoutInflater inflater) {
        return inflate(inflater, (ViewGroup) null, false);
    }

    public static RemoveButtonBinding inflate(LayoutInflater inflater, ViewGroup parent, boolean attachToParent) {
        View root = inflater.inflate(R.layout.remove_button, parent, false);
        if (attachToParent) {
            parent.addView(root);
        }
        return bind(root);
    }

    public static RemoveButtonBinding bind(View rootView2) {
        if (rootView2 != null) {
            return new RemoveButtonBinding((Button) rootView2);
        }
        throw new NullPointerException("rootView");
    }
}
