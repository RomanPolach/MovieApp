package com.roman.movieApp.ui.main.epoxy


import android.widget.TextView
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.roman.movieApp.R


@EpoxyModelClass(layout = R.layout.empty_layout)
abstract class EmptyModel : EpoxyModelWithHolder<EmptyModel.Holder>() {

    override fun bind(holder: Holder) {
        // No need to do anything
    }

    class Holder : KotlinEpoxyHolder() {
        val emptyLayoutText by bind<TextView>(R.id.empty_layout)
    }
}