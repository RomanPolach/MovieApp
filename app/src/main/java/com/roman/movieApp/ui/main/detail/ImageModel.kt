package com.roman.movieApp.ui.main.detail

import android.widget.ImageView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.roman.movieApp.R
import com.roman.movieApp.ui.main.epoxy.KotlinEpoxyHolder
import com.squareup.picasso.Picasso


@EpoxyModelClass(layout = R.layout.movie_image_layout)
abstract class ImageModel : EpoxyModelWithHolder<ImageModel.Holder>() {
    @EpoxyAttribute
    var imgUrl: String = ""

    override fun bind(holder: Holder) {
        Picasso.get().load(imgUrl).into(holder.imageView);
    }

    class Holder : KotlinEpoxyHolder() {
        val imageView by bind<ImageView>(R.id.image)
    }
}