package ru.morozovit.recipeviewer.mobile

import java.lang.Double.parseDouble

fun CharSequence?.toDouble(): Number {
    return if (isNullOrBlank()) 0.0 else parseDouble(this.toString())
};