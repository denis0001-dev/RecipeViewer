package ru.morozovit.recipeviewer.mobile

import android.view.View

data class Item(val headline: String, val content: String, val onClick: View.OnClickListener, var selected: Boolean? = false) {
    constructor(headline: String, content: String, onClick: View.OnClickListener): this(headline, content, onClick, false)
}
