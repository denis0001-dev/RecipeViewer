package ru.morozovit.recipeviewer.mobile;

import android.annotation.SuppressLint;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.material.textfield.TextInputLayout;

/** @noinspection DataFlowIssue*/
public class Step {
    public final LinearLayout root;

    public final TextView number;
    public final TextInputLayout description;

    @SuppressLint("SetTextI18n")
    public Step(@NonNull LinearLayout root, int number, String description) {
        this.root = root;
        this.number = (TextView) root.getChildAt(0);
        setNumber(number);
        this.description = (TextInputLayout) root.getChildAt(1);
        setDescription(description);
    }

    public Step(LinearLayout root) {
        this(root, 0, "");
    }

    public Step(LinearLayout root, int number) {
        this(root, number, "");
    }

    @SuppressLint("SetTextI18n")
    public void setNumber(int number) {
        this.number.setText("#" + number);
    }

    public int getNumber() {
        return Integer.parseInt(number.getText().toString().substring(1));
    }

    public String getDescription() {
        return description.getEditText().getText().toString();
    }

    public void setDescription(String description) {
        this.description.getEditText().setText(description);
    }
}
