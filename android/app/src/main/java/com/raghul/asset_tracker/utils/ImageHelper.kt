package com.raghul.asset_tracker.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.VectorDrawable

object ImageHelper {
     fun getBitmap(context:Context,drwable: Int): Bitmap {
        val drawable = context?.getDrawable(drwable)
        return getVectorBitmap(drawable as VectorDrawable)
    }
    private fun getVectorBitmap(vectorDrawable: VectorDrawable): Bitmap {
        val bitmap = Bitmap.createBitmap(
                vectorDrawable.intrinsicWidth,
                vectorDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        vectorDrawable.setBounds(0, 0, canvas.width, canvas.height);
        vectorDrawable.draw(canvas);

        return bitmap
    }
}