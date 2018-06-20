package com.vander.scaffold.form.validator.rules

import android.support.annotation.StringRes

open class NotEmptyRule(@StringRes override val errorRes: Int) : ValidateRule<String>() {
  override fun validate(value: String?) = value?.isNotBlank() ?: false
}