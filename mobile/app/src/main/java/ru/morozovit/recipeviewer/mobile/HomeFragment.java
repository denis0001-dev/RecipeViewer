package ru.morozovit.recipeviewer.mobile;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;

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
            binding.createYourRecipeButton.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), CreateActivity.class);
                startActivity(intent);
            });
            binding.viewARecipeButton.setOnClickListener((v) ->
                    Snackbar.make(
                            binding.homeMainLayout,
                            R.string.not_implemented,
                            Snackbar.LENGTH_SHORT
                    ).show()
            );
        } catch (Exception e) {
            Snackbar.make(
                    binding.homeMainLayout,
                    R.string.error,
                    Snackbar.LENGTH_SHORT
            ).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}