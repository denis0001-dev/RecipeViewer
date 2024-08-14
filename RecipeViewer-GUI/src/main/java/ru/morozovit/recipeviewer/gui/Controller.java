package ru.morozovit.recipeviewer.gui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import ru.morozovit.recipeviewer.RecipeParser;
import ru.morozovit.util.ExcParser;
import ru.morozovit.util.io.FileUtil;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import static java.lang.Double.MAX_VALUE;
import static java.lang.Integer.parseInt;

public class Controller implements Initializable {
    // Scene & stage
    protected Stage stage;
    @SuppressWarnings("unused")
    protected Scene scene;

    // Lists
    @FXML protected VBox ingredients;
    @FXML protected VBox steps;

    // Buttons & text fields
    @FXML protected TextField recipeName;
    @FXML protected Button loadFromExistingFile;
    @FXML protected Button create;
    @FXML protected Button clearRecipe;

    // Add buttons
    @FXML protected Button addIngredient;
    @FXML protected Button addStep;

    // GitHub project link
    @FXML protected Hyperlink githubProject;

    // Ingredient & step lists
    public final ArrayList<Ingredient> ingredientsList = new ArrayList<>();
    public final ArrayList<Step> stepsList = new ArrayList<>();

    @Deprecated
    public void openURL(String url) {
        try {
            Desktop.getDesktop().browse(URI.create(url));
        } catch (IOException _) {}
    }

    @FXML
    public void openGithubProject() {
        App.openURL("https://github.com/denis0001-dev/RecipeViewer");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        stage = App.getPrimaryStage();
        scene = stage.getScene();
    }

    public void addStep(String desc) {
        Step step_cls;

        GridPane step = new GridPane();
        step.setPrefHeight(30);
        step.setPrefWidth(322);

        // Columns & row
        ColumnConstraints column1 = new ColumnConstraints(21, 21, MAX_VALUE);
        column1.setHgrow(Priority.SOMETIMES);
        ColumnConstraints column2 = new ColumnConstraints(279, 279, MAX_VALUE);
        column2.setHgrow(Priority.SOMETIMES);
        ColumnConstraints column3 = new ColumnConstraints(28, 28, MAX_VALUE);
        column3.setHgrow(Priority.SOMETIMES);
        step.getColumnConstraints().addAll(column1, column2, column3);

        RowConstraints row1 = new RowConstraints(30, 30, MAX_VALUE);
        row1.setVgrow(Priority.SOMETIMES);
        step.getRowConstraints().addAll(row1);

        // Step number
        Label stepLabel = new Label();
        stepLabel.setAlignment(Pos.CENTER);
        stepLabel.setPrefHeight(30);
        stepLabel.setPrefWidth(30);
        stepLabel.setText("#"+(stepsList.size() + 1));
        step.add(stepLabel, 0, 0);

        // Text field
        TextField stepTextField = new TextField();
        stepTextField.setPrefHeight(26);
        stepTextField.setPrefWidth(152);
        stepTextField.setText(desc);
        step.add(stepTextField, 1, 0);

        // Remove button
        Button remove = new Button();
        step_cls = new Step(stepLabel,stepTextField,remove);
        remove.setPrefHeight(30);
        remove.setPrefWidth(28);
        remove.setText("X");
        remove.setOnAction(event -> {
            steps.getChildren().remove(step);
            stepsList.remove(step_cls);
            updateStepNumbers();
        });
        step.add(remove,2, 0);

        steps.getChildren().add(step);
        stepsList.add(step_cls);
    }

    @FXML
    public void addStep() {
        addStep("");
    }

    @FXML
    public void clear() {
        ingredients.getChildren().clear();
        ingredientsList.clear();
        steps.getChildren().clear();
        stepsList.clear();
        recipeName.setText("");
    }

    public void updateStepNumbers() {
        for (Step step : stepsList) {
            step.stepNumber().setText("#"+(stepsList.indexOf(step) + 1));
        }
    }

    @SuppressWarnings("CallToPrintStackTrace")
    @FXML
    public void loadFromFile() {
        File file = FileUtil.fileChooser("json");

        if (file == null) return;
        
        // Get the file contents
        String contents;
        try {
            contents = FileUtil.readFile(file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
            error("Coudln't read '"+file.getName()+"'", e);
            return;
        }

        // System.out.println("Parsing recipe: \n"+contents);

        RecipeParser parser;

        try {
            parser = new RecipeParser(contents);
        } catch (ParseException e) {
            e.printStackTrace();
            error("Coudln't parse the JSON. ",e);
            return;
        }

        // Clear the ingredients and steps
        ingredients.getChildren().clear();
        steps.getChildren().clear();
        ingredientsList.clear();
        stepsList.clear();

        // Parse the steps
        for (int i = 1; true; i++) {
            try {
                addStep(parser.getStep(i));
                // System.out.println(parser.getStep(i));
            } catch (InvalidParameterException e) {
                break;
            }
        }

        // Parse the ingredients
        for (int i = 1; true; i++) {
            try {
                ArrayList<String> ingredient = parser.getIngredient(i);
                addIngredient(ingredient.get(0), parseInt(ingredient.get(1)), ingredient.get(2));
            } catch (InvalidParameterException _) {
                break;
            }
        }

        // Add the recipe name to the text field
        recipeName.setText(file.getName().substring(0, file.getName().length() - 5));

    }

    public void addIngredient(String name, int count, String count2) {
        Ingredient ing;
        GridPane ingredient = new GridPane();
        ingredient.setPrefHeight(32);
        ingredient.setPrefWidth(280);

        // Columns
        ColumnConstraints c1 = new ColumnConstraints(10, 34, 183);
        c1.setHgrow(Priority.SOMETIMES);
        ColumnConstraints c2 = new ColumnConstraints(10, 155, 201);
        c2.setHgrow(Priority.SOMETIMES);
        ColumnConstraints c3 = new ColumnConstraints(10, 45, 94);
        c3.setHgrow(Priority.SOMETIMES);
        ColumnConstraints c4 = new ColumnConstraints(10,50, 92);
        c4.setHgrow(Priority.SOMETIMES);
        ColumnConstraints c5 = new ColumnConstraints(10,41, 92);
        c5.setHgrow(Priority.SOMETIMES);

        ingredient.getColumnConstraints().addAll(c1, c2, c3, c4, c5);

        // Row
        RowConstraints r1 = new RowConstraints(10, 30, MAX_VALUE);
        r1.setVgrow(Priority.SOMETIMES);
        ingredient.getRowConstraints().addAll(r1);

        // Number
        Label numberLabel = new Label();
        numberLabel.setAlignment(Pos.CENTER);
        numberLabel.setText("#"+(ingredientsList.size() + 1));
        ingredient.add(numberLabel, 0, 0);

        // Name
        TextField ingName = new TextField();
        ingName.setPrefHeight(26);
        ingName.setPrefWidth(152);
        ingName.setText(name);
        ingredient.add(ingName, 1, 0);

        // Count
        TextField ingCount = new TextField();
        ingCount.setPrefHeight(26);
        ingCount.setPrefWidth(152);
        ingCount.setText(String.valueOf(count));
        ingredient.add(ingCount, 2, 0);

        // Unit
        TextField ingUnit = new TextField();
        ingUnit.setPrefHeight(26);
        ingUnit.setPrefWidth(30);
        ingUnit.setText(count2);
        ingredient.add(ingUnit, 3, 0);

        // Remove button
        Button remove = new Button();
        remove.setText("X");
        remove.setMnemonicParsing(false);
        ing = new Ingredient(numberLabel, ingName, ingCount, ingUnit, remove);
        remove.setOnAction((e) -> {
            ingredients.getChildren().remove(ingredient);
            ingredientsList.remove(ing);
            updateIngredientNumbers();
        });
        ingredient.add(remove, 4, 0);
        ingredientsList.add(ing);
        ingredients.getChildren().add(ingredient);
    }

    @FXML
    public void addIngredient() {
        addIngredient("", 0, "");
    }

    public void updateIngredientNumbers() {
        for (Ingredient ing : ingredientsList) {
            ing.ingredientNumber().setText("#"+(ingredientsList.indexOf(ing) + 1));
        }
    }

    private void alert(String title, String header, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);

        // alert.getDialogPane().getButtonTypes().add(buttonType);

        alert.showAndWait();
    }

    private void info(@SuppressWarnings("SameParameterValue") String header, String message) {
        alert("Information", header, message, Alert.AlertType.INFORMATION);
    }

    private void error(String header, String message) {
        alert("Error", header, message, Alert.AlertType.ERROR);
    }

    private void error(String header, Throwable cause) {
        error(header, new ExcParser(cause).toString());
    }

    @SuppressWarnings("unchecked")
    @FXML
    public void create() {
        if (recipeName.getText().isEmpty()) {
            error("Recipe must have a name", "");
            return;
        } else if (ingredientsList.isEmpty()) {
            error("The recipe must contain at least one ingredient", "");
            return;
        } else if (stepsList.isEmpty()) {
            error("The recipe must contain at least one step", "");
            return;
        }
        try {
            JSONObject file = (JSONObject) new JSONParser().parse("{}");

            // Ingredients
            JSONArray ingredients = new JSONArray();
            for (Ingredient ing : ingredientsList) {
                JSONObject ingredient = new JSONObject();
                ingredient.put("name", ing.ingredientName().getText());
                ingredient.put("count", ing.ingredientCount().getText());
                ingredient.put("count2", ing.ingredientCount2().getText());
                ingredients.add(ingredient);
            }
            file.put("ingredients", ingredients);

            // Steps
            JSONArray steps = new JSONArray();
            for (Step step : stepsList) {
                JSONObject jstep = new JSONObject();
                jstep.put("step"+(stepsList.indexOf(step) + 1), step.step().getText());
                steps.add(jstep);
            }
            file.put("steps", steps);

            // Write to file
            FileUtil.writeToFile(FileUtil.token.WRITE, "recipes/"+recipeName.getText()+".json", file.toJSONString());
            info("Recipe created", "Recipe '"+recipeName.getText()+"' has been successfully created.");
        } catch (ParseException e) {
            error("Unexpected error.", e);
        } catch (IOException e) {
            error("Cannot write JSON file.", e);
        }
    }

    //
    // VIEW
    //

    // Select file
    @FXML protected TextField view_recipeFile;
    @FXML protected Button view_selectFile;

    @FXML protected TextField view_recipeMultiplier;

    @FXML protected VBox view_ingredients;

    @FXML protected Label view_stepNumber;
    @FXML protected Label view_stepDesc;

    @FXML protected Button view_previous;
    @FXML protected Button view_next;

    public int view_ingredientsCount = 0;
    public int view_currentStep = 1;
    public RecipeParser parser = null;

    @FXML
    public void viewSelectFile() {
        File file = FileUtil.fileChooser("json");
        if (file != null) {
            view_recipeFile.setText(file.getAbsolutePath());
        }
        viewRecipe();
    }
    
    @FXML
    public void viewRecipe() {
        File recipeFile = new File(view_recipeFile.getText());
        if (!recipeFile.exists()) return;

        String contents;
        try {
            contents = FileUtil.readFile(recipeFile.getAbsolutePath());
        } catch (FileNotFoundException _) {
            return;
        } catch (IOException e) {
            error("Cannot read file.", e);
            return;
        }

        try {
            parser = new RecipeParser(contents);

            view_ingredientsCount = 0;
            view_ingredients.getChildren().clear();
            for (int i = 1; true; i++) {
                try {
                    ArrayList<String> ing = parser.getIngredient(i);
                    view_addIngredient(ing.get(0), parseInt(ing.get(1)), ing.get(2));
                } catch (InvalidParameterException _) {
                    break;
                }
            }

            view_changeStep(1);

        } catch (ParseException | NumberFormatException e) {
            error("Error while parsing recipe.", e);
        }
    }

    public void view_addIngredient(String name, int count, String unit) {
        try {
            count = count * parseInt(view_recipeMultiplier.getText());
        } catch (NumberFormatException _) {}

        view_ingredientsCount += 1;
        GridPane ingredient = new GridPane();
        ingredient.setMaxHeight(MAX_VALUE);
        ingredient.setMaxWidth(MAX_VALUE);

        // Columns
        ColumnConstraints c1 = new ColumnConstraints(10,33,70);
        ColumnConstraints c2 = new ColumnConstraints(10, 133, 138);
        ColumnConstraints c3 = new ColumnConstraints(10, 31, 133);
        ColumnConstraints c4 = new ColumnConstraints(10, 37, MAX_VALUE);
        c1.setHgrow(Priority.SOMETIMES);
        c2.setHgrow(Priority.SOMETIMES);
        c3.setHgrow(Priority.SOMETIMES);
        c4.setHgrow(Priority.SOMETIMES);

        // Row
        RowConstraints r = new RowConstraints(10, 30, MAX_VALUE);

        ingredient.getColumnConstraints().addAll(c1, c2, c3, c4);
        ingredient.getRowConstraints().addAll(r);


        // Number
        Label number = new Label();
        number.setText("#" + view_ingredientsCount);
        number.setAlignment(Pos.CENTER);
        ingredient.add(number, 0, 0);

        // Name
        Label nameLabel = new Label();
        nameLabel.setText(name);
        ingredient.add(nameLabel, 1, 0);

        // Count
        Label countLabel = new Label();
        countLabel.setText(String.valueOf(count));
        ingredient.add(countLabel, 2, 0);

        // Unit
        Label unitLabel = new Label();
        unitLabel.setText(unit);
        ingredient.add(unitLabel, 3, 0);

        view_ingredients.getChildren().add(ingredient);
    }

    public void view_changeStep(int step) {
        try {
            String[] parsedText = parser.getStep(step).split(" ");
            for (int i2=0;i2< parsedText.length;i2++) {
                String current = parsedText[i2];
                if (Pattern.compile("\\*\\d+").matcher(current).find()) {
                    current = current.replace("*","");
                    current = String.valueOf(Integer.parseInt(current)*parseInt(view_recipeMultiplier.getText()));
                    parsedText[i2] = current;
                }
            }

            String parsedStep = String.join(" ", parsedText);

            view_stepDesc.setText(parsedStep);
            view_stepNumber.setText("Step #" + step);
            view_previous.setDisable(step == 1);
            view_next.setDisable(((JSONArray) parser.recipe.get("steps")).size() == step);

            view_currentStep = step;
        } catch (NullPointerException | InvalidParameterException _) {
        } catch (NumberFormatException e) {
            error("The multiplier must be a number!", e);
        } catch (Exception e) {
            error("Unexpected error. ", e);
        }
    }

    @FXML
    public void view_previousStep() {
        view_changeStep(view_currentStep - 1);
    }

    @FXML
    public void view_nextStep() {
        view_changeStep(view_currentStep + 1);
    }

    @FXML
    public void view_updateStep() {
        try {
            parseInt(view_recipeMultiplier.getText());
        } catch (NumberFormatException _) {
            return;
        }
        view_changeStep(view_currentStep);
    }

    public void view_updateIngredients() {
        view_ingredientsCount = 0;
        view_ingredients.getChildren().clear();
        for (int i = 1; true; i++) {
            try {
                ArrayList<String> ing = parser.getIngredient(i);
                view_addIngredient(ing.get(0), parseInt(ing.get(1)), ing.get(2));
            } catch (InvalidParameterException _) {
                break;
            }
        }
    }

    @FXML
    public void view_update() {
        try {
            view_updateIngredients();
        } catch (NumberFormatException _) {}
        view_updateStep();
    }
}