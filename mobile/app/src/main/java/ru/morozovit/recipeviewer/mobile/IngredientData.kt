package ru.morozovit.recipeviewer.mobile

import java.io.Serializable

data class IngredientData(val name: String, val count: Double, val unit: String): Serializable
