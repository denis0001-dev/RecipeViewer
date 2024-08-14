package ru.morozovit.recipeviewer;

import org.json.simple.*;
import org.json.simple.parser.*;
import ru.morozovit.recipeviewer.localization.ValueProvider;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Iterator;

public class RecipeParser {
    public JSONObject recipe;
    public RecipeParser(String recipeJson) throws ParseException {
        this.recipe = (JSONObject) new JSONParser().parse(recipeJson);
    }
    public ArrayList<String> getIngredient(int number) {
        JSONArray ingredients = (JSONArray) this.recipe.get("ingredients");
        @SuppressWarnings("rawtypes") Iterator itr = ingredients.iterator();
        int counter = 0;
        ArrayList<String> returnarr = new ArrayList<>(3);
        while (itr.hasNext()) {
            counter++;
            JSONObject itrObj = (JSONObject) itr.next();
            if (counter == number) {
                returnarr.add(itrObj.get("name").toString());
                returnarr.add(itrObj.get("count").toString());
                returnarr.add(itrObj.get("count2").toString());
                return returnarr;
            }
        }
        throw new InvalidParameterException(String.valueOf(ValueProvider.noSuchIngredient.get()));
    }
    public String getStep(int number) {
        JSONArray steps = (JSONArray) this.recipe.get("steps");
        @SuppressWarnings("rawtypes") Iterator itr = steps.iterator();
        int counter = 0;
        while (itr.hasNext()) {
            counter++;
            JSONObject itrObj = (JSONObject) itr.next();
            if (counter==number) {
                return (String) itrObj.get("step"+counter);
            }
        }
        throw new InvalidParameterException(String.valueOf(ValueProvider.noSuchStep.get()));
    }
}
