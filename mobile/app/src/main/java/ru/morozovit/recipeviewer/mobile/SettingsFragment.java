package ru.morozovit.recipeviewer.mobile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import ru.morozovit.recipeviewer.mobile.databinding.SettingsFragmentBinding;

public class SettingsFragment extends Fragment {
    private SettingsFragmentBinding binding;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = SettingsFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /* binding.buttonFirst.setOnClickListener(v ->
                 NavHostFragment.findNavController(HomeFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment)
        ); */
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
