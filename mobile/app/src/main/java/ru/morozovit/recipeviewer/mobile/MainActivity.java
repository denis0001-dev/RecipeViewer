package ru.morozovit.recipeviewer.mobile;

import static androidx.viewpager.widget.ViewPager.SCROLL_STATE_IDLE;

import android.os.Bundle;
import android.util.TypedValue;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.navigation.NavigationBarView;

import ru.morozovit.recipeviewer.mobile.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private static MainActivity instance = null;
    private ActivityMainBinding binding;

    public MainActivity() {
        super();
        instance = this;
    }

    /** @noinspection deprecation*/
    public static class ViewPagerAdapter extends FragmentPagerAdapter {
        public final Page[] fragments = new Page[] {new HomeFragment(), new LibraryFragment(), new SettingsFragment()};

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return (Fragment) fragments[position];
        }

        @Override
        public int getCount() {
            return fragments.length;
        }
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
        /* navigation.setOnItemSelectedListener(item -> {
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
            fragmentTransaction.commit();
            return true;
        }); */
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        binding.mainView.setAdapter(adapter);
        final Wrapper pageListener = new Wrapper();
        NavigationBarView.OnItemSelectedListener listener = item -> {
            binding.mainView.setOnPageChangeListener(null);
            int selectedItemIndex = item.getItemId();
            if (selectedItemIndex == R.id.page_home) {
                binding.mainView.setCurrentItem(0);
            } else if (selectedItemIndex == R.id.page_library) {
                binding.mainView.setCurrentItem(1);
            } else if (selectedItemIndex == R.id.page_settings) {
                binding.mainView.setCurrentItem(2);
            }
            binding.mainView.setOnPageChangeListener((ViewPager.OnPageChangeListener) pageListener.value);
            return true;
        };
        pageListener.value = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
            @Override
            public void onPageSelected(int position) {
                binding.bottomNavigation.setOnItemSelectedListener(null);
                switch (position) {
                    case 0: binding.bottomNavigation.setSelectedItemId(R.id.page_home);
                    break;
                    case 1: binding.bottomNavigation.setSelectedItemId(R.id.page_library);
                    break;
                    case 2: binding.bottomNavigation.setSelectedItemId(R.id.page_settings);
                    break;
                    default: break;
                }
                binding.bottomNavigation.setOnItemSelectedListener(listener);
            }
            @Override
            public void onPageScrollStateChanged(int state) {
                int position = binding.mainView.getCurrentItem();
                if (state == SCROLL_STATE_IDLE) {
                    adapter.fragments[position].onPageSelected(position);
                }
            }
        };
        binding.mainView.setOnPageChangeListener((ViewPager.OnPageChangeListener) pageListener.value);
        binding.bottomNavigation.setOnItemSelectedListener(listener);
    }

    public static int getDpInPixels(float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, dp, getInstance().getResources().getDisplayMetrics());
    }
}