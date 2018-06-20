package com.vander.scaffold.form.validator.rules

import android.support.annotation.StringRes
import android.util.Patterns

open class EmailRule(@StringRes override val errorRes: Int) : ValidateRule<String>() {
  override fun validate(value: String?) = Patterns.EMAIL_ADDRESS.matcher(value).matches()
}