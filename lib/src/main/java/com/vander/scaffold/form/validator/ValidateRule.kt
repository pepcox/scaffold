package com.vander.scaffold.form.validator

import androidx.annotation.StringRes

abstract class ValidateRule {
  @StringRes open val errorRes: Int = -1
  open val errorMessageParams: Array<out Any>? = null
  abstract fun validate(text: String): Boolean
}