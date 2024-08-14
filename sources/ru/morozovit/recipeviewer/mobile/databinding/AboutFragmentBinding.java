package ru.morozovit.recipeviewer.mobile.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import ru.morozovit.recipeviewer.mobile.R;

public final class AboutFragmentBinding implements ViewBinding {
    private final ConstraintLayout rootView;
    public final TextView textviewFirst;

    private AboutFragmentBinding(ConstraintLayout rootView2, TextView textviewFirst2) {
        this.rootView = rootView2;
        this.textviewFirst = textviewFirst2;
    }

    public ConstraintLayout getRoot() {
        return this.rootView;
    }

    public static AboutFragmentBinding inflate(LayoutInflater inflater) {
        return inflate(inflater, (ViewGroup) null, false);
    }

    public static AboutFragmentBinding inflate(LayoutInflater inflater, ViewGroup parent, boolean attachToParent) {
        View root = inflater.inflate(R.layout.about_fragment, parent, false);
        if (attachToParent) {
            parent.addView(root);
        }
        return bind(root);
    }

    public static AboutFragmentBinding bind(View rootView2) {
        int id = R.id.textview_first;
        TextView textviewFirst2 = (TextView) ViewBindings.findChildViewById(rootView2, id);
        if (textviewFirst2 != null) {
            return new AboutFragmentBinding((ConstraintLayout) rootView2, textviewFirst2);
        }
        throw new NullPointerException("Missing required view with ID: ".concat(rootView2.getResources().getResourceName(id)));
    }
}
