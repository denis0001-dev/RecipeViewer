package ru.morozovit.recipeviewer.mobile

import android.annotation.SuppressLint
import android.app.ActivityOptions.makeSceneTransitionAnimation
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat.setTransitionName
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import ru.morozovit.recipeviewer.mobile.databinding.IngredientsActivityBinding

@Suppress("OVERRIDE_DEPRECATION")
class IngredientsActivity: AppCompatActivity() {
    private var binding: IngredientsActivityBinding? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = IngredientsActivityBinding.inflate(layoutInflater)
        with(binding!!) {
            setContentView(root)
            ingredientsToolbar.setNavigationOnClickListener {onBackPressed()}
            val b = intent.extras
            try {
                val recipe = JSONObject(b?.getString("recipe")!!)
                processIngredients(recipe.getJSONObject("body").getJSONArray("ingredients"))
            } catch (e: JSONException) {
                Snackbar.make(
                    root,
                    R.string.error_loading_ingredients,
                    Snackbar.LENGTH_SHORT).show()
            }
            ingredientsList.transitionName = "ingredients"
            makeSceneTransitionAnimation(
                this@IngredientsActivity,
                ingredientsList,
                "ingredients"
            )
            window.exitTransition = null
            window.enterTransition = null
            supportPostponeEnterTransition()
            setTransitionName(root, "ingredients")
            supportStartPostponedEnterTransition()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun processIngredients(ingredients: JSONArray) {
        binding!!.ingredientsList.removeAllViews()
        for (i in 0..ingredients.length()) {
            val ingredient = ingredients.getJSONObject(i)
            val ingName = ingredient.getString("name")
            val ingCount = ingredient.getString("count")
            val ingUnit = ingredient.getString("unit")

            val ingView = LayoutInflater.from(this)
                .inflate(
                    R.layout.ingredient,
                    binding!!.ingredientsList,
                    false) as LinearLayout
            ingView.removeView(ingView.getChildAt(4))
            val ingNumber = ingView.getChildAt(0) as TextView
            ingNumber.text = "#${i+1}"
            val ingNameView = ingView.getChildAt(1) as TextInputLayout
            ingNameView.editText?.apply {
                setText(ingName)
                isEnabled = false
            }
            val ingCountView = ingView.getChildAt(2) as TextInputLayout
            ingCountView.editText?.apply {
                setText(ingCount)
                isEnabled = false
            }

            val ingUnitView = ingView.getChildAt(3) as TextInputLayout
            ingUnitView.editText?.apply {
                setText(ingUnit)
                isEnabled = false
            }
            binding!!.ingredientsList.addView(ingView)

            if (i != ingredients.length() - 1) {
                (ingView.layoutParams as LinearLayout.LayoutParams)
                    .bottomMargin =
                    resources.getDimensionPixelSize(R.dimen.padding_medium)
            }
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        binding!!.ingredientsList.transitionName = "ingredients"
        makeSceneTransitionAnimation(
            this,
            binding!!.ingredientsList,
            "ingredients"
        )
        setResult(RESULT_OK)
        finishAfterTransition()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}