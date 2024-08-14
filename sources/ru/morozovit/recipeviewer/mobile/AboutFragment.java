package ru.morozovit.recipeviewer.mobile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import ru.morozovit.recipeviewer.mobile.databinding.AboutFragmentBinding;

public class AboutFragment extends Fragment {
    private AboutFragmentBinding binding;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.binding = AboutFragmentBinding.inflate(inflater, container, false);
        return this.binding.getRoot();
    }

    public void onDestroyView() {
        super.onDestroyView();
        this.binding = null;
    }
}
