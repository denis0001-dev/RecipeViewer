package ru.morozovit.recipeviewer.mobile;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import ru.morozovit.recipeviewer.mobile.databinding.CreateActivityBinding;
import ru.morozovit.util.android.BetterActivityResult;

public class CreateActivity extends AppCompatActivity {
    private CreateActivityBinding binding;
    protected BetterActivityResult<Intent, ActivityResult> activityLauncher = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = CreateActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        activityLauncher = BetterActivityResult.registerActivityForResult(this);

        binding.createMoreButton.setOnClickListener((v -> {
            PopupMenu popup = new PopupMenu(this, v);
            popup.getMenuInflater().inflate(R.menu.create_more, popup.getMenu());
            popup.getMenu().getItem(1).setOnMenuItemClickListener(v1 -> {
                clear();
                return true;
            });
            popup.getMenu().getItem(0).setOnMenuItemClickListener(v1 -> {
                openFileChooser();
                return true;
            });
            popup.show();
        }));
        binding.createAddIngredientButton.setOnClickListener(v -> addIngredient());
        binding.createAddStepButton.setOnClickListener(v -> addStep());
        binding.createBackButton.setOnClickListener(v -> this.onBackPressed());
        binding.createCreateRecipe.setOnClickListener(v -> {
            JSONObject recipe = generateJSON();
            if (recipe != null) {
                save(recipe);
            }
        });

        Bundle b = getIntent().getExtras();
        if (b != null) {
            try {
                JSONObject recipe = new JSONObject(b.getString("recipe"));
                JSONObject recipeBody = recipe.getJSONObject("body");
                processRecipe(recipeBody, recipe.getString("name"));
            } catch (Exception e) {
                Snackbar.make(
                        binding.createMainLayout,
                        R.string.error_loading_recipes,
                        Snackbar.LENGTH_SHORT
                ).setAnchorView(binding.createCreateRecipe).show();
            }
        }
    }

    @Nullable
    public JSONObject generateJSON() {
        try {
            // Generate recipe JSON
            JSONObject recipe = new JSONObject();
            recipe.put("name", binding.createRecipeName.getText().toString());
            JSONObject recipeBody = new JSONObject();

            // Ingredients
            JSONArray ingredientsArray = new JSONArray();
            for (Ingredient ingredient : ingredients) {
                JSONObject ingredientObject = new JSONObject();
                ingredientObject.put("name", ingredient.getName());
                ingredientObject.put("count", ingredient.getCount());
                ingredientObject.put("count2", ingredient.getUnit());
                ingredientsArray.put(ingredientObject);
            }
            recipeBody.put("ingredients", ingredientsArray);

            // Steps
            JSONArray stepsArray = new JSONArray();
            for (int i = 0; i < steps.size(); i++) {
                Step step = steps.get(i);
                JSONObject stepObject = new JSONObject();
                stepObject.put("step"+(i+1), step.getDescription());
                stepsArray.put(stepObject);
            }
            recipeBody.put("steps", stepsArray);
            recipe.put("body", recipeBody);
            return recipe;
        } catch (Exception e) {
            Snackbar.make(
                    binding.createMainLayout,
                    R.string.error_generating_recipe,
                    Snackbar.LENGTH_SHORT
            ).show();
            return null;
        }
    }

    public static class ModalBottomSheet extends BottomSheetDialogFragment {
        public static final String TAG = "ModalBottomSheet";
        private final JSONObject recipe;

        public ModalBottomSheet(JSONObject recipe) {
            this.recipe = recipe;
        }

        @Override
        public View onCreateView(
                @NonNull LayoutInflater inflater,
                ViewGroup container,
                Bundle savedInstanceState
        ) {
            return inflater.inflate(R.layout.recipe_created, container, false);
        }

        /** @noinspection DataFlowIssue*/
        @Override
        public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
            try {
                BottomSheetBehavior<? extends View> behavior = BottomSheetBehavior.from(((CoordinatorLayout) view).getChildAt(0));
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                behavior.setFitToContents(false);

                /* Function<TextInputLayout, View.OnFocusChangeListener> listener = (inp) -> (v, hasFocus) -> {
                    LinearLayout main = (LinearLayout) ((CoordinatorLayout) view).getChildAt(0);
                    if (hasFocus) {
                        /* for (int i = 0; i < main.getChildCount(); i++) {
                            View child = main.getChildAt(i);
                            if (!(child instanceof TextInputLayout)) {
                                child.setVisibility(View.GONE);
                            }
                            LayoutInflater inflater = getLayoutInflater();
                            Button save = (Button) inflater.inflate(R.layout.save_button, main, false);
                            main.addView(save);
                            save.setOnClickListener(v1 -> inp.clearFocus());
                            OnBackPressedCallback backCallback = new OnBackPressedCallback(true) {
                                @Override
                                public void handleOnBackPressed() {
                                    inp.clearFocus();
                                }
                            };
                            getActivity().getOnBackPressedDispatcher().addCallback(activity, backCallback);
                        } *\/
                    } else {
                        /* for (int i = 0; i < main.getChildCount(); i++) {
                            View child = main.getChildAt(i);
                            child.setVisibility(View.VISIBLE);
                        } *\/
                    }
                }; */

                Button saveToLib = view.findViewById(R.id.create_recipeCreated_save);
                Button saveToInternal = view.findViewById(R.id.create_recipeCreated_save_to_internal);
                // TextInputLayout name = findViewById(R.id.create_recipeCreated_name);
                TextView recipeCode = view.findViewById(R.id.create_recipeCreated_recipe);
                Button editInfo = view.findViewById(R.id.create_recipeCreated_editInfo);
                JSONObject recipeBody = recipe.getJSONObject("body");
                recipeCode.setText(recipeBody.toString(2));
                saveToLib.setOnClickListener(v -> {
                    RecipeDatabase db = RecipeDatabase.getInstance();
                    try {
                        db.saveRecipe(recipe);
                        this.dismiss();
                        Snackbar.make(
                                getActivity().findViewById(R.id.create_mainLayout),
                                R.string.recipe_saved,
                                Snackbar.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        new MaterialAlertDialogBuilder(getActivity())
                                .setTitle(R.string.error_saving_recipe)
                                .setMessage(R.string.error_saving_recipe_details)
                                .setPositiveButton(R.string.ok, null)
                                .show();
                    }
                });
                editInfo.setOnClickListener(v -> {
                    try {
                        Intent intent = new Intent(getActivity(), RecipeDataEditActivity.class);
                        Bundle args = new Bundle();
                        args.putString("name", recipe.getString("name"));
                        try {
                            args.putString("desc", recipe.getString("desc"));
                        } catch (JSONException e) {
                            args.putString("desc", "");
                        }
                        intent.putExtras(args);
                        // startActivity(intent);
                        ((CreateActivity) this.getActivity()).activityLauncher.launch(intent, result -> {
                            if (result.getResultCode() == Activity.RESULT_OK) {
                                try {
                                    recipe.put("name", result.getData().getStringExtra("name"));
                                    recipe.put("desc", result.getData().getStringExtra("desc"));
                                } catch (JSONException e) {}
                            }
                        });
                    } catch (Exception e) {

                    }
                });

            } catch (Exception e) {
                try {
                    //noinspection DataFlowIssue
                    Snackbar.make(
                            getActivity().findViewById(R.id.create_mainLayout),
                            R.string.error_creating_bottom_sheet,
                            Snackbar.LENGTH_SHORT
                    ).show();
                } catch (Exception ignored) {}
            }
        }
    }

    public void save(JSONObject recipe) {
        try {
            ModalBottomSheet modalBottomSheet = new ModalBottomSheet(recipe);
            modalBottomSheet.show(getSupportFragmentManager(), ModalBottomSheet.TAG);
        } catch (Exception e) {
            Snackbar.make(
                    binding.createMainLayout,
                    R.string.error_saving_recipe,
                    Snackbar.LENGTH_SHORT
            ).show();
        }
    }

    /** @noinspection deprecation*/
    @Override
    public void onBackPressed() {
        if (unsavedChanges()) {
            this.openUnsavedChangesDialog((a,b) -> {}, (a,b) -> super.onBackPressed());
            return;
        }
        super.onBackPressed();
    }

    // System
    public final ArrayList<Ingredient> ingredients = new ArrayList<>();
    public final ArrayList<Step> steps = new ArrayList<>();

    public final DialogInterface.OnClickListener DEFAULT_SAVE = (dialog, which) -> {
        RecipeDatabase db = RecipeDatabase.getInstance();
        try {
            //noinspection DataFlowIssue
            db.saveRecipe(generateJSON());
            dialog.dismiss();
        } catch (Exception e) {
            Snackbar.make(binding.createMainLayout, R.string.error_saving_recipe, Snackbar.LENGTH_SHORT).show();
        }
    };

    public void openUnsavedChangesDialog(DialogInterface.OnClickListener cancelled,
                                         DialogInterface.OnClickListener proceed) {
        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.unsaved_changes)
                .setMessage(R.string.unsaved_changes_message)
                .setNeutralButton(R.string.cancel, cancelled)
                .setNegativeButton(R.string.dontsave, proceed)
                .setPositiveButton(R.string.save, (d, w) -> {
                    DEFAULT_SAVE.onClick(d, w);
                    proceed.onClick(d, w);
                })
                .show();
    }

    // System
    /** @noinspection unchecked*/
    public void clear() {
        // Alias for easy access
        LinearLayout ingList = binding.createIngredientsList;
        LinearLayout stepsList = binding.createStepsList;

        // Back up data
        final String recipeName = binding.createRecipeName.getText().toString();
        final ArrayList<Ingredient> prevIngs = (ArrayList<Ingredient>) ingredients.clone();
        final ArrayList<Step> prevSteps = (ArrayList<Step>) steps.clone();

        silentClear();

        Snackbar.make(binding.createMainLayout, R.string.cleared, Snackbar.LENGTH_LONG).setAction(R.string.undo, v -> {
            for (Ingredient ing: prevIngs) {
                ingList.addView(ing.root);
                ingredients.add(ing);
            }
            for (Step step: prevSteps) {
                stepsList.addView(step.root);
                steps.add(step);
            }
            binding.createRecipeName.setText(recipeName);
        }).setAnchorView(binding.createCreateRecipe).show();
    }

    public boolean unsavedChanges() {
        JSONObject recipe = generateJSON();
        try {
            RecipeDatabase db = RecipeDatabase.getInstance();
            //noinspection DataFlowIssue
            return !db.getRecipe(recipe.getString("name")).toString().equals(recipe.toString());
        } catch (RecipeDatabase.StorageException e) {
            return !(binding.createRecipeName.getText().toString().isEmpty() && ingredients.isEmpty() && steps.isEmpty());
        } catch (Exception e) {
            return false;
        }
    }

    public void silentClear() {
        binding.createRecipeName.setText("");
        ingredients.clear();
        steps.clear();
        binding.createIngredientsList.removeAllViews();
        binding.createStepsList.removeAllViews();
    }
    /** @noinspection DataFlowIssue*/ // File chooser
    private void openFileChooser() {
        if (!(
                ingredients.isEmpty() &&
                        steps.isEmpty() &&
                        binding.createRecipeName.getText().toString().isEmpty()
        )) {
            openUnsavedChangesDialog(
                    (a,b) -> {},
                    (dialog, which) -> {
                        silentClear();
                        openFileChooser();
                    }
            );
            return;
        }

        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/json");
        activityLauncher.launch(intent, result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                try {
                    Intent data = result.getData();
                    Uri uri = data.getData();
                    String json;
                    try {
                        json = readJsonFile(uri);
                    } catch (Exception e) {
                        Snackbar.make(binding.createMainLayout, R.string.error_reading_file, Snackbar.LENGTH_SHORT).show();
                        return;
                    }
                    try {
                        JSONObject jsonObj = new JSONObject(json);
                        String fileName = null;
                        try (Cursor cursor = MainActivity.
                                getInstance().
                                getContentResolver().
                                query(uri, null, null, null, null)) {
                            if (cursor != null && cursor.moveToFirst()) {
                                int displayNameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                                fileName = cursor.getString(displayNameIndex);

                                // Remove JSON extension
                                int dotPosition = fileName.lastIndexOf('.');
                                if (dotPosition != -1) {
                                    fileName = fileName.substring(0, dotPosition);
                                }
                            }
                        }
                        processRecipe(jsonObj, fileName);
                    } catch (Exception e) {
                        Snackbar.make(binding.createMainLayout, R.string.error_parsing_json, Snackbar.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Snackbar.make(binding.createMainLayout, R.string.error_reading_file, Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void processRecipe(@NonNull JSONObject recipe, String recipeName) throws JSONException {
        // TODO new recipe format
        JSONArray ingredients = (JSONArray) recipe.get("ingredients");
        JSONArray steps = (JSONArray) recipe.get("steps");
        // Ingredients
        for (int i = 0; i < ingredients.length(); i++) {
            JSONObject ingredientObj = ingredients.getJSONObject(i);
            String name = ((String) ingredientObj.get("name")).replaceAll("_", " ");
            Object countObj = ingredientObj.get("count");
            double count = countObj instanceof Integer ? (int) countObj : (double) countObj;
            String unit = ((String) ingredientObj.get("count2")).replaceAll("_", " ");
            addIngredient(name, count, unit);
        }

        // Steps
        for (int i = 0; i < steps.length(); i++) {
            String stepText = steps.getJSONObject(i).getString("step"+(i+1));
            addStep(stepText);
        }

        // Name
        binding.createRecipeName.setText(recipeName);
    }

    @NonNull
    private String readJsonFile(Uri uri) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (InputStream inputStream = MainActivity.getInstance().getContentResolver().openInputStream(uri)) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }
        return sb.toString();
    }

    public void addIngredient(String _name, double _count, String _unit) {
        LinearLayout list = binding.createIngredientsList;
        LinearLayout ll = (LinearLayout) LayoutInflater.from(MainActivity.getInstance()).inflate(R.layout.ingredient, list, false);

        Button remove = (Button) ll.getChildAt(4);

        list.addView(ll);
        ((LinearLayout.LayoutParams) ll.getLayoutParams()).bottomMargin = MainActivity.getDpInPixels(5f);
        ll.requestLayout();

        Ingredient ingredient = new Ingredient(ll, ingredients.size() + 1, _name, _count, _unit);
        remove.setOnClickListener(v1 -> {
            list.removeView(ll);
            ingredients.remove(ingredient);
            updateIngredientNumbers();
        });
        ingredients.add(ingredient);
    }

    public void addIngredient() {
        addIngredient("", 0, "");
    }

    public void addStep(String _desc) {
        LinearLayout list = binding.createStepsList;
        LinearLayout ll = (LinearLayout) LayoutInflater.from(MainActivity.getInstance()).inflate(R.layout.step, list, false);
        Button remove = (Button) ll.getChildAt(2);

        list.addView(ll);
        ((LinearLayout.LayoutParams) ll.getLayoutParams()).bottomMargin = MainActivity.getDpInPixels(5f);
        ll.requestLayout();

        Step step = new Step(ll, steps.size() + 1, _desc);
        remove.setOnClickListener(v1 -> {
            list.removeView(ll);
            steps.remove(step);
            updateStepNumbers();
        });
        steps.add(step);
    }

    public void addStep() {
        addStep("");
    }

    public void updateIngredientNumbers() {
        for (Ingredient ingredient : ingredients) {
            ingredient.setNumber(binding.createIngredientsList.indexOfChild(ingredient.root) + 1);
        }
    }

    public void updateStepNumbers() {
        for (Step step : steps) {
            step.setNumber(binding.createStepsList.indexOfChild(step.root) + 1);
        }
    }
}
