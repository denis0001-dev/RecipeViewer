package ru.morozovit.recipeviewer.mobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import ru.morozovit.recipeviewer.mobile.databinding.LibraryFragmentBinding;

public class LibraryFragment extends Fragment {
    private LibraryFragmentBinding binding;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = LibraryFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MainActivity activity = MainActivity.getInstance();

        binding.libCreate.setOnClickListener(v -> {
            Intent intent = new Intent(activity, CreateActivity.class);
            startActivity(intent);
        });

        // Handle back button press
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                activity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                return true;
            }
            return false;
        });
    }
}