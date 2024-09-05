package ru.morozovit.recipeviewer.mobile;

import android.os.Bundle;
import android.util.TypedValue;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import ru.morozovit.recipeviewer.mobile.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private static MainActivity instance = null;
    private ActivityMainBinding binding;
    public static final int CENTER_GRAVITY = 8388659;

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

        binding.bottomNavigation.setSelectedItemId(R.id.page_home);

        // Navigation items
        BottomNavigationView navigation = findViewById(R.id.bottom_navigation);
        navigation.setOnItemSelectedListener(item -> {
            int selectedItemIndex = item.getItemId();

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            Fragment newFragment;
            if (selectedItemIndex == R.id.page_home) {
                newFragment = new HomeFragment();
            } else if (selectedItemIndex == R.id.page_library) {
                newFragment = new LibraryFragment();
            } else if (selectedItemIndex == R.id.page_settings) {
                newFragment = new SettingsFragment();
            } else {
                newFragment = new Fragment();
            }
            fragmentTransaction.replace(R.id.main_view, newFragment);
            fragmentTransaction.setCustomAnimations(R.anim.exit_to_right, R.anim.enter_from_right);
            fragmentTransaction.commit();
            return true;
        });
    }

    public void switchTab(int id) {
        binding.bottomNavigation.setSelectedItemId(id);
    }

    public static int getDpInPixels(float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, dp, getInstance().getResources().getDisplayMetrics());
    }
}