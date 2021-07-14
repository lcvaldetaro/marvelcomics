package com.example.comics

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.squareup.picasso.Picasso

val TAG = "Utils"

val View.ctx: Context
    get() = context

fun toast (msg: String) {
    Toast.makeText(App.appContext!!, msg, Toast.LENGTH_LONG).show()
}

fun goToActivity(ctx: Context, obj: Class<*>) {
    val intent = Intent(ctx, obj)
    ctx.startActivity(intent)
}

fun loadImageIntoView (ctx: Context, imageFile: String, view: ImageView) {
    Picasso.with(ctx).load(imageFile).into(view)
}
