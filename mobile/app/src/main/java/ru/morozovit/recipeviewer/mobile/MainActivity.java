package ru.morozovit.recipeviewer.mobile;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import ru.morozovit.recipeviewer.mobile.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private static MainActivity instance = null;

    @SuppressWarnings("FieldCanBeLocal") private ActivityMainBinding binding;

    public MainActivity() {
        super();
        instance = this;
    }

    public static MainActivity getInstance() {
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.bottomNavigation.setSelectedItemId(R.id.page_1);

        // Navigation items
        BottomNavigationView navigation = findViewById(R.id.bottom_navigation);
        navigation.setOnItemSelectedListener(item -> {
            int selectedItemIndex = item.getItemId();

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            //noinspection ExtractMethodRecommender
            Fragment newFragment;
            if (selectedItemIndex == R.id.page_1) {
                newFragment = new HomeFragment();
            } else if (selectedItemIndex == R.id.page_2) {
                newFragment = new CreateFragment();
            } else if (selectedItemIndex == R.id.page_3) {
                newFragment = new ViewFragment();
            } else if (selectedItemIndex == R.id.page_4) {
                newFragment = new SettingsFragment();
            } else if (selectedItemIndex == R.id.page_5) {
                newFragment = new AboutFragment();
            } else {
                newFragment = new Fragment();
            }
            fragmentTransaction.replace(R.id.main_view, newFragment);
            fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
            fragmentTransaction.commit();
            return true;
        });
    }

    public void switchTab(int id) {
        binding.bottomNavigation.setSelectedItemId(id);
    }
}