@file:Suppress("UsePropertyAccessSyntax")

package ru.morozovit.recipeviewer.mobile

import android.annotation.SuppressLint
import android.widget.LinearLayout
import android.widget.TextView
import com.google.android.material.textfield.TextInputLayout

@Suppress("unused", "MemberVisibilityCanBePrivate")
@SuppressLint("SetTextI18n")
class Ingredient(
    val root: LinearLayout,
    number: Int = 0,
    name: String = "",
    count: Number = 0,
    unit: String = ""
) {
    constructor(root: LinearLayout): this(root, 0, "", 0, "")
    constructor(root: LinearLayout, number: Int): this(root, number, "", 0, "")

    private val number = root.getChildAt(0) as TextView
    private val name = root.getChildAt(1) as TextInputLayout
    private val count = root.getChildAt(2) as TextInputLayout
    private val unit = root.getChildAt(3) as TextInputLayout
    init {
        setNumber(number)
        setName(name)
        setCount(count)
        setUnit(unit)
    }

    fun setNumber(value: Int) = number.setText("#$value")
    fun setName(value: String) = name.editText?.setText(value)
    fun setCount(value: Number) = count.editText?.setText("$value")
    fun setUnit(value: String) = unit.editText?.setText(value)

    fun getNumber(): String = number.text.toString()
    fun getName(): String = name.editText?.text.toString()
    fun getCount(): Number = count.editText?.text?.toDouble()!!
    fun getUnit(): String = unit.editText?.text.toString()
}