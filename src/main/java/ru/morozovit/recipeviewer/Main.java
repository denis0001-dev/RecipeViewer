package ru.morozovit.recipeviewer;

import org.json.simple.parser.ParseException;
import ru.morozovit.recipeviewer.localization.ValueProvider;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.Scanner;

public class Main {
    public static final String[] empty = new String[]{};

    public static void main(String[] args) throws IOException, ParseException {
        if (!ValueProvider.langfromfile()) {
            ValueProvider.selectLang();
        }
        System.out.println(ValueProvider.selectAnAction.get());
        System.out.println("1. "+ValueProvider.createARecipe.get());
        System.out.println("2. "+ValueProvider.viewARecipe.get());
        System.out.println("3. "+ValueProvider.changeLang.get());
        System.out.println("--------");
        System.out.println(ValueProvider.actionNumber.get());
        switch (new Scanner(System.in).nextInt()) {
            case (0):
                System.exit(0);
                break;
            case (1):
                RecipeCreator.main(empty);
                break;
            case (2):
                RecipeGuider.main(empty);
                break;
            case (3):
                ValueProvider.selectLang();
                break;
            default:
                throw new InvalidParameterException(ValueProvider.invalidParameter.get());
        }
    }
}