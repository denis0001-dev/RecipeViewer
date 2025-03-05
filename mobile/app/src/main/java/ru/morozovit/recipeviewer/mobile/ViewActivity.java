package ru.morozovit.recipeviewer.mobile;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

import ru.morozovit.recipeviewer.mobile.databinding.ViewActivityBinding;
import ru.morozovit.recipeviewer.mobile.databinding.ViewIngredientFragmentBinding;
import ru.morozovit.recipeviewer.mobile.databinding.ViewIngredientsBinding;

public class ViewActivity extends AppCompatActivity {
    public ViewActivityBinding binding;
    public ArrayList<IngredientData> ingredients;
    public ArrayList<String> steps;
    public String name;

    public ViewPagerAdapter pagerAdapter;

    /** @noinspection unchecked*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ViewActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Process arguments
        Bundle b = getIntent().getExtras();
        if (b != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ingredients = b.getSerializable("ingredients", ArrayList.class);
            } else {
                ingredients = (ArrayList<IngredientData>) b.getSerializable("ingredients");
            }
            steps = b.getStringArrayList("steps");
            name = b.getString("name");
        }

        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        binding.viewPager.setAdapter(pagerAdapter);
        binding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                pagerAdapter.fragments[position].onPageSelected(position);
            }
            @Override
            public void onPageScrollStateChanged(int state) {}
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
        });
        binding.viewToolbar.setNavigationOnClickListener(v -> onBackPressed());
        binding.viewToolbar.setSubtitle(name);
    }

    /** @noinspection deprecation*/
    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        finish();
    }

    /** @noinspection deprecation*/
    public class ViewPagerAdapter extends FragmentPagerAdapter {
        public final Page[] fragments;

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
            fragments = new Page[steps.size() + 1];
            fragments[0] = new IngredientsPrepare(ingredients, binding.viewPager);
            for (int i = 1; i < getCount(); i++) {
                fragments[i] = new StepFragment(steps.get(i - 1), i, binding.viewPager);
            }
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

    public static class IngredientsPrepare extends Fragment implements Page {
        public ViewIngredientsBinding binding;
        public final ArrayList<IngredientData> ingredients;
        public final ViewPager pager;

        public IngredientsPrepare(ArrayList<IngredientData> ingredients, ViewPager pager) {
            this.ingredients = ingredients;
            this.pager = pager;
        }

        @Override
        public View onCreateView(
                @NonNull LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            return inflater.inflate(R.layout.view_ingredients, container, false);
        }

        /** @noinspection DataFlowIssue*/
        @SuppressLint("SetTextI18n")
        @Override
        public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            ViewActivity activity = (ViewActivity) getActivity();
            binding = ViewIngredientsBinding.bind(view);

            binding.viewIngredients.removeAllViews();
            for (int i = 0; i < ingredients.size(); i++) {
                IngredientData ingredient = ingredients.get(i);
                String ingName = ingredient.getName();
                String ingCount = String.valueOf(ingredient.getCount());
                String ingUnit = ingredient.getUnit();

                LinearLayout ingView = (LinearLayout)
                        LayoutInflater.from(activity).inflate(
                                R.layout.ingredient,
                                binding.viewIngredients,
                                false
                        );
                ingView.removeView(ingView.getChildAt(4));
                TextView ingNumber = (TextView) ingView.getChildAt(0);
                ingNumber.setText("#"+(i+1));
                TextInputLayout ingNameView = (TextInputLayout) ingView.getChildAt(1);
                ingNameView.getEditText().setText(ingName);
                ingNameView.getEditText().setEnabled(false);

                TextInputLayout ingCountView = (TextInputLayout) ingView.getChildAt(2);
                ingCountView.getEditText().setText(ingCount);
                ingCountView.getEditText().setEnabled(false);

                TextInputLayout ingUnitView = (TextInputLayout) ingView.getChildAt(3);
                ingUnitView.getEditText().setText(ingUnit);
                ingUnitView.getEditText().setEnabled(false);
                binding.viewIngredients.addView(ingView);

                if (i != ingredients.size() - 1) {
                    ((LinearLayout.LayoutParams) ingView.getLayoutParams()).bottomMargin = getResources().getDimensionPixelSize(R.dimen.padding_medium);
                }
            }

            binding.viewIngsNext.setOnClickListener(v -> pager.setCurrentItem(1));
        }
    }

    public static class StepFragment extends Fragment implements Page {
        public ViewIngredientFragmentBinding binding;
        public final String step;
        public final int stepNumber;
        public final ViewPager pager;

        public StepFragment(String step, int stepNumber, ViewPager pager) {
            this.step = step;
            this.stepNumber = stepNumber;
            this.pager = pager;
        }

        @Override
        public View onCreateView(
                @NonNull LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            return inflater.inflate(R.layout.view_ingredient_fragment, container, false);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            binding = ViewIngredientFragmentBinding.bind(view);

            binding.viewStepLabel.setText("Step #"+stepNumber);
            binding.viewStepText.setText(step);
            binding.viewStepPrev.setOnClickListener(v -> pager.setCurrentItem(pager.getCurrentItem() - 1));
        }

        @Override
        public void onPageSelected(int position) {
            //noinspection DataFlowIssue
            if (pager.getCurrentItem() == pager.getAdapter().getCount() - 1) {
                binding.viewStepFinish.setVisibility(View.VISIBLE);
                binding.viewStepFinish.setOnClickListener(v -> {
                    //noinspection DataFlowIssue
                    getActivity().setResult(RESULT_OK);
                    getActivity().finish();
                });
                binding.viewStepNext.setVisibility(View.GONE);
            } else {
                binding.viewStepFinish.setVisibility(View.GONE);
                binding.viewStepNext.setVisibility(View.VISIBLE);
                binding.viewStepNext.setOnClickListener(v -> pager.setCurrentItem(pager.getCurrentItem() + 1));
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
        ingredients = null;
        steps = null;
    }
}
