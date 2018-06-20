package com.vander.scaffold.form.validator

import android.view.View
import com.vander.scaffold.form.validator.rules.ValidateRule

abstract class Validation<out I : View, R> {
  abstract val input: I
  abstract val rules: LinkedHashSet<ValidateRule<R>>
  abstract fun check(): Boolean
  abstract fun clearError()
}