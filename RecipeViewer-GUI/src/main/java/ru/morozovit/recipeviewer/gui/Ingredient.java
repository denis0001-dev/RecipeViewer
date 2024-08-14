package ru.morozovit.recipeviewer.gui;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * @param ingredientNumber Elements
 */
public record Ingredient(
        Label ingredientNumber,
        TextField ingredientName,
        TextField ingredientCount,
        TextField ingredientCount2,
        Button remove
) {}
