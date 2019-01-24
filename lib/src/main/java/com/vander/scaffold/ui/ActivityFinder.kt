package com.vander.scaffold.ui

import android.content.Context
import android.content.ContextWrapper
import android.view.View
import androidx.appcompat.app.AppCompatActivity

/**
 * @author marian on 20.9.2017.
 */
object ActivityFinder {

  fun get(view: View) = get(view.context)

  fun get(context: Context): AppCompatActivity {
    return when (context) {
      is AppCompatActivity -> context
      is ContextWrapper -> get(context.baseContext)
      else -> throw IllegalStateException("No Activity found for $context")
    }
  }
}