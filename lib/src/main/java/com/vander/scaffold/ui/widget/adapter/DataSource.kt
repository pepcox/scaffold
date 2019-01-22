package com.vander.scaffold.ui.widget.adapter

import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.ListUpdateCallback

abstract class DataSource<out T : AdapterModel> {
  var listUpdateCallback: ListUpdateCallback? = null

  abstract val itemCount: Int

  abstract operator fun get(position: Int): T

  @LayoutRes fun getLayoutRes(position: Int): Int = this[position].layoutRes

  fun getItemId(position: Int): Long = this[position].id

}