package ru.morozovit.recipeviewer.mobile

import android.annotation.SuppressLint
import android.widget.LinearLayout
import android.widget.TextView
import com.google.android.material.textfield.TextInputLayout

@Suppress("UsePropertyAccessSyntax", "unused", "MemberVisibilityCanBePrivate")
@SuppressLint("SetTextI18n")
class Step(
    val root: LinearLayout,
    number: Int,
    description: String
) {
    private val number = root.getChildAt(0) as TextView
    private val description = root.getChildAt(1) as TextInputLayout

    constructor(root: LinearLayout): this(root, 0, "")
    constructor(root: LinearLayout, number: Int): this(root, number, "")

    init {
        setNumber(number)
        setDescription(description)
    }

    fun getNumber() = number.text!!
    fun getDescription() = description.editText!!.text!!

    fun setNumber(value: Int) = number.setText("#$value")
    fun setDescription(value: String) = description.editText!!.setText(value)
}