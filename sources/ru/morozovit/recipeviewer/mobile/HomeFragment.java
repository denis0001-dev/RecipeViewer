package ru.morozovit.recipeviewer.mobile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import ru.morozovit.recipeviewer.mobile.databinding.HomeFragmentBinding;

public class HomeFragment extends Fragment {
    private HomeFragmentBinding binding;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.binding = HomeFragmentBinding.inflate(inflater, container, false);
        return this.binding.getRoot();
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            this.binding.createYourRecipeButton.setOnClickListener(new HomeFragment$$ExternalSyntheticLambda0());
            this.binding.viewARecipeButton.setOnClickListener(new HomeFragment$$ExternalSyntheticLambda1());
        } catch (Exception e) {
            this.binding.createYourRecipeButton.setText(R.string.error);
            this.binding.viewARecipeButton.setText(R.string.error);
        }
    }

    public void onDestroyView() {
        super.onDestroyView();
        this.binding = null;
    }
}
