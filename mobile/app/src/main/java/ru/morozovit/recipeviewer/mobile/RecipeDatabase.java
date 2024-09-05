package ru.morozovit.recipeviewer.mobile;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public final class RecipeDatabase {
    private static RecipeDatabase INSTANCE = null;
    private final SharedPreferences sharedPreferences;

    public static class StorageException extends Exception {
        public StorageException(String message) {
            super(message);
        }
    }

    private RecipeDatabase(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    /** @noinspection deprecation*/
    public static RecipeDatabase getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RecipeDatabase(PreferenceManager.getDefaultSharedPreferences(MainActivity.getInstance().getApplicationContext()));
        }
        return INSTANCE;
    }

    public void saveRecipe(@NonNull JSONObject recipe) throws JSONException {
        String key = recipe.getString("name");
        sharedPreferences.edit().putString(key, recipe.toString()).apply();
    }

    @NonNull
    public JSONObject getRecipe(@NonNull String name) throws StorageException, JSONException {
        JSONObject obj = new JSONObject(sharedPreferences.getString(name, "{}"));
        if (!(obj.has("name") && obj.has("body"))) {
            throw new StorageException(String.format("Recipe with name '%s' does not exist", name));
        }
        return obj;
    }

    public void deleteRecipe(@NonNull String name) {
        sharedPreferences.edit().remove(name).apply();
    }

    @NonNull
    public ArrayList<JSONObject> getAllRecipes() {
        ArrayList<JSONObject> recipes = new ArrayList<>();
        for (String key : sharedPreferences.getAll().keySet()) {
            try {
                recipes.add(getRecipe(key));
            } catch (Exception e) {}
        }
        return recipes;
    }
}
