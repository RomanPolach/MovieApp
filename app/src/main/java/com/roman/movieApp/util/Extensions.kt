package com.roman.movieApp.util

import android.content.res.Resources
import android.view.View
import com.airbnb.epoxy.EpoxyController
import com.airbnb.epoxy.EpoxyRecyclerView

/** Easily add models to an EpoxyRecyclerView, the same way you would in a buildModels method of EpoxyController. */
fun EpoxyRecyclerView.withModels(buildModelsCallback: EpoxyController.() -> Unit) {
    setControllerAndBuildModels(object : EpoxyController() {
        override fun buildModels() {
            buildModelsCallback()
        }
    })
}

val Int.dp: Int
    get() = (this / Resources.getSystem().displayMetrics.density).toInt()

val Int.px: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()

inline var View.isVisible: Boolean
    get() = visibility == View.VISIBLE
    set(value) {
        visibility = if (value) View.VISIBLE else View.GONE
    }