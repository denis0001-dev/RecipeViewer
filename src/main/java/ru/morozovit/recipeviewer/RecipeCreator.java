package ru.morozovit.recipeviewer;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import ru.morozovit.recipeviewer.localization.ValueProvider;
import ru.morozovit.util.io.FileUtil;

import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class RecipeCreator {
    private static final Scanner inputStream = new Scanner(System.in);

    @SuppressWarnings({"unchecked", "RedundantSuppression"})
    public static void main(String[] args) throws ParseException, IOException {
        JSONObject file = (JSONObject) new JSONParser().parse("{}");
        JSONArray ingredients = new JSONArray();

        System.out.println(ValueProvider.ingredientEnter.get());
        System.out.println(ValueProvider.format.get());
        System.out.println(ValueProvider.multiplyTip.get());
        System.out.println(ValueProvider.doneMsg.get());

        int counter = 0;

        while (true) {
            counter++;
            System.out.print(ValueProvider.ingredient.get()+" #"+counter+": ");
            String[] input = inputStream.nextLine().split(" ");
            if (Arrays.equals(input, "".split(" "))) {
                break;
            }
            if (!(input.length ==3)) {
                throw new RuntimeException(ValueProvider.formatException.get());
            }
            ingredients.add(new JSONParser().parse("{\"name\": \""+input[0]+"\",\"count\": "+input[1]+",\"count2\": \""+input[2]+"\"}"));
            System.out.println(ingredients.toJSONString());
            System.out.println();
            file.put("ingredients",ingredients);
            System.out.println(file.toJSONString());
        }

        System.out.println(ValueProvider.enterSteps.get());
        System.out.println(ValueProvider.doneMsg.get());
        System.out.println();

        JSONArray steps = new JSONArray();


        counter = 0;
        while (true) {
            counter++;
            System.out.print(ValueProvider.step.get()+"#"+counter+": ");
            String input = inputStream.nextLine();
            if (input.isEmpty()) {
                break;
            }
            steps.add(new JSONParser().parse("{\"step"+counter+"\": \""+input+"\"}"));

            file.put("steps",steps);
        }
        System.out.print(ValueProvider.nameRecipe.get());
        String name = inputStream.nextLine();
        FileUtil.writeToFile(FileUtil.token.WRITE,"recipes/"+name+".json",file.toJSONString());
        System.out.println(ValueProvider.recipeSuccess.get());
    }
}
