package ru.morozovit.recipeviewer.localization;

import ru.morozovit.util.io.FileUtil;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.Scanner;
import java.util.function.Supplier;

public class ValueProvider {
    public static String lang = "en";
    public static void selectLang() throws IOException {
        System.out.println("Select a language: ");
        System.out.println("1. English");
        System.out.println("2. Русский");
        System.out.println("Number: ");
        switch (new Scanner(System.in).nextInt()) {
            case (1):
                lang = "en";
                break;
            case (2):
                lang = "ru";
                break;
            default:
                throw new InvalidParameterException("Invalid Parameter");
        }
        FileUtil.writeToFile(FileUtil.token.WRITE,"lang.txt",lang);
    }
    public static boolean langfromfile() {
        String file;
        try {
            file = FileUtil.readFile("lang.txt");
            file = file.strip();
        } catch (IOException e) {
            lang = "en";
            return false;
        }
        if (file.equals("ru") || file.equals("en")) {
            lang = file;
            return true;
        } else if (file.isEmpty()){
            lang = "en";
            return false;
        } else {
            throw new InvalidParameterException(Locale_EN.invalidParameter);
        }
    }

    public static final Supplier<String> selectAnAction = () -> (ValueProvider.lang.equals("en")) ? Locale_EN.selectAnAction : Locale_RU.selectAnAction;
    public static final Supplier<String> createARecipe = () -> (ValueProvider.lang.equals("en")) ? Locale_EN.createARecipe : Locale_RU.createARecipe;
    public static final Supplier<String> viewARecipe = () -> (ValueProvider.lang.equals("en")) ? Locale_EN.viewARecipe : Locale_RU.viewARecipe;
    public static final Supplier<String> actionNumber = () -> (ValueProvider.lang.equals("en")) ? Locale_EN.actionNumber : Locale_RU.actionNumber;
    public static final Supplier<String> invalidParameter = () -> (ValueProvider.lang.equals("en")) ? Locale_EN.invalidParameter : Locale_RU.invalidParameter;
    public static final Supplier<String> neededRecipeNumber = () -> (ValueProvider.lang.equals("en")) ? Locale_EN.neededRecipeNumber : Locale_RU.neededRecipeNumber;
    public static final Supplier<String> yourNumber = () -> (ValueProvider.lang.equals("en")) ? Locale_EN.yourNumber : Locale_RU.yourNumber;
    public static final Supplier<String> chosen = () -> (ValueProvider.lang.equals("en")) ? Locale_EN.chosen : Locale_RU.chosen;
    public static final Supplier<String> recipeMultiplier = () -> (ValueProvider.lang.equals("en")) ? Locale_EN.recipeMultiplier : Locale_RU.recipeMultiplier;
    public static final Supplier<String> ingredients = () -> (ValueProvider.lang.equals("en")) ? Locale_EN.ingredients : Locale_RU.ingredients;
    public static final Supplier<String> ingredientComplete = () -> (ValueProvider.lang.equals("en")) ? Locale_EN.ingredientComplete : Locale_RU.ingredientComplete;
    public static final Supplier<String> showSteps = () -> (ValueProvider.lang.equals("en")) ? Locale_EN.showSteps : Locale_RU.showSteps;
    public static final Supplier<String> step = () -> (ValueProvider.lang.equals("en")) ? Locale_EN.step : Locale_RU.step;
    public static final Supplier<String> end = () -> (ValueProvider.lang.equals("en")) ? Locale_EN.end : Locale_RU.end;
    public static final Supplier<String> doneMsg = () -> (ValueProvider.lang.equals("en")) ? Locale_EN.doneMsg : Locale_RU.doneMsg;
    public static final Supplier<String> ingredientEnter = () -> (ValueProvider.lang.equals("en")) ? Locale_EN.ingredientEnter : Locale_RU.ingredientEnter;
    public static final Supplier<String> format = () -> (ValueProvider.lang.equals("en")) ? Locale_EN.format : Locale_RU.format;
    public static final Supplier<String> multiplyTip = () -> (ValueProvider.lang.equals("en")) ? Locale_EN.multiplyTip : Locale_RU.multiplyTip;
    public static final Supplier<String> ingredient = () -> (ValueProvider.lang.equals("en")) ? Locale_EN.ingredient : Locale_RU.ingredient;
    public static final Supplier<String> formatException = () -> (ValueProvider.lang.equals("en")) ? Locale_EN.formatException : Locale_RU.formatException;
    public static final Supplier<String> enterSteps = () -> (ValueProvider.lang.equals("en")) ? Locale_EN.enterSteps : Locale_RU.enterSteps;
    public static final Supplier<String> nameRecipe = () -> (ValueProvider.lang.equals("en")) ? Locale_EN.nameRecipe : Locale_RU.nameRecipe;
    public static final Supplier<String> recipeSuccess = () -> (ValueProvider.lang.equals("en")) ? Locale_EN.recipeSuccess : Locale_RU.recipeSuccess;
    public static final Supplier<String> noSuchIngredient = () -> (ValueProvider.lang.equals("en")) ? Locale_EN.noSuchIngredient : Locale_RU.noSuchIngredient;
    public static final Supplier<String> noSuchStep = () -> (ValueProvider.lang.equals("en")) ? Locale_EN.noSuchStep : Locale_RU.noSuchStep;
    public static final Supplier<String> changeLang = () -> (ValueProvider.lang.equals("en")) ? Locale_EN.changeLang : Locale_RU.changeLang;
}
