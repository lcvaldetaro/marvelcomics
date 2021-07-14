package com.example.comics

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import com.squareup.picasso.Picasso

val TAG = "Utils"


fun toast (msg: String) {
    Toast.makeText(App.appContext!!, msg, Toast.LENGTH_LONG).show()
}

fun goToActivity(ctx: Context, obj: Class<*>) {
    val intent = Intent(ctx, obj)
    ctx.startActivity(intent)
}

fun loadImageIntoView (ctx: Context, imageFile: String, view: ImageView) {
    Log.d(TAG, "Loading image $imageFile")
    Picasso.with(ctx).load(imageFile).into(view)
}
