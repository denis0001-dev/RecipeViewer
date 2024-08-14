package ru.morozovit.recipeviewer.gui;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public record Step(
        Label stepNumber,
        TextField step,
        Button remove
) {}
