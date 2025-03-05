@file:Suppress("DEPRECATION")

package ru.morozovit.recipeviewer.mobile

import android.content.SharedPreferences
import android.preference.PreferenceManager
import org.json.JSONException
import org.json.JSONObject

object RecipeDatabase {
    private lateinit var sharedPreferences: SharedPreferences

    class StorageException(message: String) : Exception(message)

    private fun init() {
        if (!::sharedPreferences.isInitialized) {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.getInstance().applicationContext)
        }
    }

    fun saveRecipe(recipe: JSONObject) {
        init()
        val key = recipe.getString("name")
        sharedPreferences.edit().putString(key, "$recipe").apply()
    }

    @Throws(JSONException::class, StorageException::class)
    fun getRecipe(name: String): JSONObject {
        init()
        val obj = JSONObject(sharedPreferences.getString(name, "{}")!!)
        if (!(obj.has("name") && obj.has("body"))) {
            throw StorageException(String.format("Recipe with name '%s' does not exist", name))
        }
        return obj
    }

    fun deleteRecipe(name: String) {
        init()
        sharedPreferences.edit().remove(name).apply()
    }

    fun getAllRecipes(): ArrayList<JSONObject> {
        init()
        val recipes = ArrayList<JSONObject>()
        for (key in sharedPreferences.all.keys) {
            try {
                recipes.add(getRecipe(key))
            } catch (_: Exception) {}
        }
        return recipes
    }
}