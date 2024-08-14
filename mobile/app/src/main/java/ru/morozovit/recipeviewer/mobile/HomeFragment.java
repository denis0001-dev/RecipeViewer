package ru.morozovit.recipeviewer.mobile;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import ru.morozovit.recipeviewer.mobile.databinding.HomeFragmentBinding;

public class HomeFragment extends Fragment {
    private HomeFragmentBinding binding;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = HomeFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @SuppressLint("SetTextI18n")
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        try {
            // binding.createYourRecipeButton.setOnClickListener((v) -> MainActivity.getInstance().switchTab(R.id.page_create));
            binding.viewARecipeButton.setOnClickListener((v) -> MainActivity.getInstance().switchTab(R.id.page_view));
        } catch (Exception e) {
            binding.createYourRecipeButton.setText("Error");
            binding.viewARecipeButton.setText("Error");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}