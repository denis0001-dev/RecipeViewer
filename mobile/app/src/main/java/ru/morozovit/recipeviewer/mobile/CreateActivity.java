package ru.morozovit.recipeviewer.mobile;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import ru.morozovit.recipeviewer.mobile.databinding.CreateActivityBinding;

public class CreateActivity extends AppCompatActivity {
    private CreateActivityBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = CreateActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.createMoreButton.setOnClickListener(v -> {
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
        });
        binding.createAddIngredientButton.setOnClickListener(v -> addIngredient());
        binding.createAddStepButton.setOnClickListener(v -> addStep());
        binding.createBackButton.setOnClickListener(v -> this.onBackPressed());
    }

    /** @noinspection deprecation*/
    @Override
    public void onBackPressed() {
        if (unsavedChanges()) {
            this.openUnsavedChangesDialog((a,b) -> {}, (a,b) -> super.onBackPressed(), (a,b) -> {
                // TODO save recipe
            });
            return;
        }
        super.onBackPressed();
    }

    private static final int PICK_JSON_FILE = 1;

    // System
    public final ArrayList<Ingredient> ingredients = new ArrayList<>();
    public final ArrayList<Step> steps = new ArrayList<>();

    public void openUnsavedChangesDialog(DialogInterface.OnClickListener cancelled,
                                         DialogInterface.OnClickListener dontsave,
                                         DialogInterface.OnClickListener save) {
        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.unsaved_changes)
                .setMessage(R.string.unsaved_changes_message)
                .setNeutralButton(R.string.cancel, cancelled)
                .setNegativeButton(R.string.dontsave, dontsave)
                .setPositiveButton(R.string.save, save)
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
        ArrayList<Ingredient> prevIngs = (ArrayList<Ingredient>) ingredients.clone();
        ArrayList<Step> prevSteps = (ArrayList<Step>) steps.clone();

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
        return !(
                ingredients.isEmpty() &&
                        steps.isEmpty() &&
                        binding.createRecipeName.getText().toString().isEmpty()
        );
    }

    public void silentClear() {
        binding.createRecipeName.setText("");
        ingredients.clear();
        steps.clear();
        binding.createIngredientsList.removeAllViews();
        binding.createStepsList.removeAllViews();
    }
    // File chooser
    /** @noinspection deprecation*/
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
                    },
                    (dialog, which) -> {
                        // TODO save recipe to the database
                    }
            );
            return;
        }

        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/json");
        startActivityForResult(intent, PICK_JSON_FILE);
    }

    /** @noinspection DataFlowIssue*/
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_JSON_FILE && resultCode == RESULT_OK) {
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
        }
    }

    public void processRecipe(@NonNull JSONObject recipe, String recipeName) throws JSONException {
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

        TextView number = (TextView) ll.getChildAt(0);
        TextInputLayout name = (TextInputLayout) ll.getChildAt(1);
        TextInputLayout count = (TextInputLayout) ll.getChildAt(2);
        TextInputLayout unit = (TextInputLayout) ll.getChildAt(3);
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
        TextView number = (TextView) ll.getChildAt(0);
        TextInputLayout desc = (TextInputLayout) ll.getChildAt(1);
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
