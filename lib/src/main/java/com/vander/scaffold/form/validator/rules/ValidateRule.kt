package com.vander.scaffold.form.validator.rules

import android.support.annotation.StringRes

abstract class ValidateRule<in T> {
  @StringRes open val errorRes: Int = -1
  open val errorMessage: ((String) -> String) = { "" }
  abstract fun validate(value: T?): Boolean
}