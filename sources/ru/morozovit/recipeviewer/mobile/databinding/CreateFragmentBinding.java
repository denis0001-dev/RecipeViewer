package ru.morozovit.recipeviewer.mobile.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import ru.morozovit.recipeviewer.mobile.R;

public final class CreateFragmentBinding implements ViewBinding {
    public final Button addStepButton;
    public final Button createAddIngredientButton;
    public final LinearLayout createAddStepButton;
    public final Button createBackButton;
    public final LinearLayout createIngredientsList;
    public final Button createMoreButton;
    private final LinearLayout rootView;

    private CreateFragmentBinding(LinearLayout rootView2, Button addStepButton2, Button createAddIngredientButton2, LinearLayout createAddStepButton2, Button createBackButton2, LinearLayout createIngredientsList2, Button createMoreButton2) {
        this.rootView = rootView2;
        this.addStepButton = addStepButton2;
        this.createAddIngredientButton = createAddIngredientButton2;
        this.createAddStepButton = createAddStepButton2;
        this.createBackButton = createBackButton2;
        this.createIngredientsList = createIngredientsList2;
        this.createMoreButton = createMoreButton2;
    }

    public LinearLayout getRoot() {
        return this.rootView;
    }

    public static CreateFragmentBinding inflate(LayoutInflater inflater) {
        return inflate(inflater, (ViewGroup) null, false);
    }

    public static CreateFragmentBinding inflate(LayoutInflater inflater, ViewGroup parent, boolean attachToParent) {
        View root = inflater.inflate(R.layout.create_fragment, parent, false);
        if (attachToParent) {
            parent.addView(root);
        }
        return bind(root);
    }

    public static CreateFragmentBinding bind(View rootView2) {
        View view = rootView2;
        int id = R.id.addStepButton;
        Button addStepButton2 = (Button) ViewBindings.findChildViewById(view, id);
        if (addStepButton2 != null) {
            id = R.id.create_addIngredientButton;
            Button createAddIngredientButton2 = (Button) ViewBindings.findChildViewById(view, id);
            if (createAddIngredientButton2 != null) {
                id = R.id.create_addStepButton;
                LinearLayout createAddStepButton2 = (LinearLayout) ViewBindings.findChildViewById(view, id);
                if (createAddStepButton2 != null) {
                    id = R.id.create_backButton;
                    Button createBackButton2 = (Button) ViewBindings.findChildViewById(view, id);
                    if (createBackButton2 != null) {
                        id = R.id.create_ingredientsList;
                        LinearLayout createIngredientsList2 = (LinearLayout) ViewBindings.findChildViewById(view, id);
                        if (createIngredientsList2 != null) {
                            id = R.id.create_moreButton;
                            Button createMoreButton2 = (Button) ViewBindings.findChildViewById(view, id);
                            if (createMoreButton2 != null) {
                                return new CreateFragmentBinding((LinearLayout) view, addStepButton2, createAddIngredientButton2, createAddStepButton2, createBackButton2, createIngredientsList2, createMoreButton2);
                            }
                        }
                    }
                }
            }
        }
        throw new NullPointerException("Missing required view with ID: ".concat(rootView2.getResources().getResourceName(id)));
    }
}
