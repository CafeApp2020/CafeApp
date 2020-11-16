package com.cafeapp.mycafe.frameworks.picasso

import android.widget.ImageView
import com.squareup.picasso.Picasso

public fun setImage(url: String, imageView: ImageView){
    Picasso
        .get()
        .load(url)
        .into(imageView)
}