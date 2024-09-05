package ru.morozovit.recipeviewer.mobile;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ru.morozovit.recipeviewer.mobile.databinding.IngredientsActivityBinding;

public class IngredientsActivity extends AppCompatActivity {
    public IngredientsActivityBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = IngredientsActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.ingredientsToolbar.setNavigationOnClickListener(v -> onBackPressed());
        // Process arguments
        Bundle b = getIntent().getExtras();
        if (b != null) {
            try {
                JSONObject recipe = new JSONObject(b.getString("recipe"));
                processIngredients(recipe.getJSONObject("body").getJSONArray("ingredients"));
            } catch (JSONException e) {
                Snackbar.make(binding.getRoot(), R.string.error_loading_ingredients, Snackbar.LENGTH_SHORT).show();
            }
        }
        binding.ingredientsList.setTransitionName("ingredients");
        ActivityOptions.makeSceneTransitionAnimation(
                this,
                binding.ingredientsList,
                "ingredients"
        );
        getWindow().setExitTransition(null);
        getWindow().setEnterTransition(null);
        supportPostponeEnterTransition();

        ViewCompat.setTransitionName(binding.getRoot(), "ingredients");
        supportStartPostponedEnterTransition();
    }

    /** @noinspection DataFlowIssue*/
    @SuppressLint("SetTextI18n")
    private void processIngredients(@NonNull JSONArray ingredients) throws JSONException {
        binding.ingredientsList.removeAllViews();
        for (int i = 0; i < ingredients.length(); i++) {
            JSONObject ingredient = ingredients.getJSONObject(i);
            String ingName = ingredient.getString("name");
            String ingCount = ingredient.getString("count");
            String ingUnit = ingredient.getString("count2");

            LinearLayout ingView = (LinearLayout)
                    LayoutInflater.from(this)
                            .inflate(R.layout.ingredient, binding.ingredientsList, false);
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
            binding.ingredientsList.addView(ingView);

            if (i != ingredients.length() - 1) {
                ((LinearLayout.LayoutParams) ingView.getLayoutParams()).bottomMargin = getResources().getDimensionPixelSize(R.dimen.padding_medium);
            }
        }
    }

    /** @noinspection deprecation*/
    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        binding.ingredientsList.setTransitionName("ingredients");
        ActivityOptions.makeSceneTransitionAnimation(
                this,
                binding.ingredientsList,
                "ingredients"
        );

        setResult(RESULT_OK);
        finishAfterTransition();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}