package com.vander.scaffold.form.validator.rules

import android.support.annotation.StringRes

open class CheckBoxRule(@StringRes override val errorRes: Int) : ValidateRule<Boolean>() {
  override fun validate(value: Boolean?) = value?.let { it } ?: false
}