package ru.morozovit.recipeviewer.mobile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import ru.morozovit.recipeviewer.mobile.databinding.ViewFragmentBinding;

public class ViewFragment extends Fragment {
    private ViewFragmentBinding binding;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.binding = ViewFragmentBinding.inflate(inflater, container, false);
        return this.binding.getRoot();
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void onDestroyView() {
        super.onDestroyView();
        this.binding = null;
    }
}
