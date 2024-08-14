package ru.morozovit.recipeviewer.mobile;

import android.annotation.SuppressLint;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.material.textfield.TextInputLayout;

/** @noinspection DataFlowIssue*/
public class Ingredient {
    public final LinearLayout root;

    public final TextView number;
    public final TextInputLayout name;
    public final TextInputLayout count;
    public final TextInputLayout unit;

    public Ingredient(@NonNull LinearLayout root, int number, @NonNull String name, double count, @NonNull String unit) {
        this.root = root;
        this.number = (TextView) root.getChildAt(0);
        setNumber(number);
        this.name = (TextInputLayout) root.getChildAt(1);
        setName(name);
        this.count = (TextInputLayout) root.getChildAt(2);
        setCount(count);
        this.unit = (TextInputLayout) root.getChildAt(3);
        setUnit(unit);
    }

    public Ingredient(LinearLayout root) {
        this(root, 0, "", 0, "");
    }

    public Ingredient(LinearLayout root, int number) {
        this(root, number, "", 0, "");
    }

    public int getNumber() {
        return Integer.parseInt(number.getText().toString().substring(1));
    }

    @SuppressLint("SetTextI18n")
    public void setNumber(int number) {
        this.number.setText("#" + number);
    }

    public String getName() {
        return name.getEditText().getText().toString();
    }

    public void setName(String name) {
        this.name.getEditText().setText(name);
    }

    public double getCount() {
        return Double.parseDouble(count.getEditText().getText().toString());
    }

    public void setCount(double count) {
        this.count.getEditText().setText(String.valueOf(count));
    }

    public String getUnit() {
        return unit.getEditText().getText().toString();
    }

    public void setUnit(String unit) {
        this.unit.getEditText().setText(unit);
    }
}
