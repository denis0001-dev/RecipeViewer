package ru.morozovit.recipeviewer.mobile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.google.android.material.textfield.TextInputLayout;
import ru.morozovit.recipeviewer.mobile.databinding.CreateFragmentBinding;

public class CreateFragment extends Fragment {
    private CreateFragmentBinding binding;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.binding = CreateFragmentBinding.inflate(inflater, container, false);
        return this.binding.getRoot();
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MainActivity activity = MainActivity.getInstance();
        activity.findViewById(R.id.create_moreButton).setOnClickListener(new CreateFragment$$ExternalSyntheticLambda0());
        activity.findViewById(R.id.create_addIngredientButton).setOnClickListener(new CreateFragment$$ExternalSyntheticLambda1(activity));
    }

    static /* synthetic */ void lambda$onViewCreated$0(View v) {
        PopupMenu popup = new PopupMenu(MainActivity.getInstance(), v);
        popup.getMenuInflater().inflate(R.menu.create_more, popup.getMenu());
        popup.show();
    }

    static /* synthetic */ void lambda$onViewCreated$1(MainActivity activity, View v) {
        LinearLayout list = (LinearLayout) activity.findViewById(R.id.create_ingredientsList);
        LinearLayout ll = (LinearLayout) LayoutInflater.from(activity).inflate(R.layout.ingredient, list, false);
        TextInputLayout textInputLayout = (TextInputLayout) ll.getChildAt(1);
        TextInputLayout textInputLayout2 = (TextInputLayout) ll.getChildAt(2);
        TextInputLayout textInputLayout3 = (TextInputLayout) ll.getChildAt(3);
        list.addView(ll);
        ((LinearLayout.LayoutParams) ll.getLayoutParams()).bottomMargin = MainActivity.getDpInPixels(5.0f);
        ll.requestLayout();
        ((TextView) ll.getChildAt(0)).setText("#?");
    }

    public void onDestroyView() {
        super.onDestroyView();
        this.binding = null;
    }
}
