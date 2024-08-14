package ru.morozovit.recipeviewer;

import org.json.simple.parser.ParseException;
import ru.morozovit.recipeviewer.localization.ValueProvider;
import ru.morozovit.util.io.FileUtil;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

public class RecipeGuider {
    private static final Scanner input = new Scanner(System.in);

    @SuppressWarnings("UnusedAssignment")
    public static void main(String[] args) throws IOException, ParseException {
        System.out.println(ValueProvider.neededRecipeNumber.get());
        String[] files = FileUtil.getFilesInDirectory(FileUtil.token.FILE,"recipes","");
        for (int i=0;i<files.length;i++) {
            System.out.println(i+". "+files[i]);
        }
        System.out.println();
        System.out.print(ValueProvider.yourNumber.get());
        final int fileNum = input.nextInt();
        final String file = FileUtil.readFile("recipes/"+files[fileNum]);
        System.out.println(ValueProvider.chosen.get()+files[fileNum]+".");
        RecipeParser parser = new RecipeParser(file);
        System.out.println();
        System.out.print(ValueProvider.recipeMultiplier.get());
        final int multiplier = input.nextInt();
        System.out.println();
        System.out.println(ValueProvider.ingredients.get());
        for (int i=1;true;i++) {
            try {
                ArrayList<String> ingredient = parser.getIngredient(i);
                System.out.printf("#%d. %s: %s %s\n",i,ingredient.getFirst(),Integer.parseInt(ingredient.get(1))*multiplier,ingredient.get(2));
            } catch (InvalidParameterException e) {
                break;
            }
        }
        System.out.println();
        System.out.print(ValueProvider.ingredientComplete.get());
        @SuppressWarnings("unused") String garbage = input.nextLine();
        garbage = input.nextLine();
        System.out.println();
        System.out.println(ValueProvider.showSteps.get());
        for (int i=1;true;i++) {
            try {
                String[] parsedText = parser.getStep(i).split(" ");
                for (int i2=0;i2< parsedText.length;i2++) {
                    String current = parsedText[i2];
                    if (Pattern.compile("\\*\\d+").matcher(current).find()) {
                        current = current.replace("*","");
                        current = String.valueOf(Integer.parseInt(current)*multiplier);
                        parsedText[i2] = current;
                    }
                }
                System.out.printf(ValueProvider.step.get()+"#%d. %s",i,String.join(" ",parsedText));
                garbage = input.nextLine();
            } catch (InvalidParameterException e) {
                break;
            }
        }
        System.out.println(ValueProvider.end.get());
    }
}
