package ru.morozovit.recipeviewer.mobile.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import ru.morozovit.recipeviewer.mobile.R;

public final class SettingsFragmentBinding implements ViewBinding {
    private final ConstraintLayout rootView;
    public final TextView textviewSecond;

    private SettingsFragmentBinding(ConstraintLayout rootView2, TextView textviewSecond2) {
        this.rootView = rootView2;
        this.textviewSecond = textviewSecond2;
    }

    public ConstraintLayout getRoot() {
        return this.rootView;
    }

    public static SettingsFragmentBinding inflate(LayoutInflater inflater) {
        return inflate(inflater, (ViewGroup) null, false);
    }

    public static SettingsFragmentBinding inflate(LayoutInflater inflater, ViewGroup parent, boolean attachToParent) {
        View root = inflater.inflate(R.layout.settings_fragment, parent, false);
        if (attachToParent) {
            parent.addView(root);
        }
        return bind(root);
    }

    public static SettingsFragmentBinding bind(View rootView2) {
        int id = R.id.textview_second;
        TextView textviewSecond2 = (TextView) ViewBindings.findChildViewById(rootView2, id);
        if (textviewSecond2 != null) {
            return new SettingsFragmentBinding((ConstraintLayout) rootView2, textviewSecond2);
        }
        throw new NullPointerException("Missing required view with ID: ".concat(rootView2.getResources().getResourceName(id)));
    }
}
