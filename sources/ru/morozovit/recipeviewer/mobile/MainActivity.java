package ru.morozovit.recipeviewer.mobile;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import ru.morozovit.recipeviewer.mobile.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    public static final int CENTER_GRAVITY = 8388659;
    private static MainActivity instance = null;
    private ActivityMainBinding binding;

    public MainActivity() {
        instance = this;
    }

    public static MainActivity getInstance() {
        return instance;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView((View) this.binding.getRoot());
        BottomNavigationView nav = this.binding.bottomNavigation;
        nav.setSelectedItemId(R.id.page_1);
        nav.setOnItemSelectedListener(new MainActivity$$ExternalSyntheticLambda0(this));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onCreate$0$ru-morozovit-recipeviewer-mobile-MainActivity  reason: not valid java name */
    public /* synthetic */ boolean m0lambda$onCreate$0$rumorozovitrecipeviewermobileMainActivity(MenuItem item) {
        Fragment newFragment;
        int selectedItemIndex = item.getItemId();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
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
    }

    public void switchTab(int id) {
        this.binding.bottomNavigation.setSelectedItemId(id);
    }

    public static int getDpInPixels(float dp) {
        return (int) TypedValue.applyDimension(1, dp, getInstance().getResources().getDisplayMetrics());
    }
}
