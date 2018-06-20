package com.vander.scaffold.form.validator

import android.support.design.widget.TextInputLayout
import android.view.View
import com.vander.scaffold.form.validator.rules.ValidateRule

data class TextValidation(
    override val input: TextInputLayout,
    override val rules: LinkedHashSet<ValidateRule<String>>
) : Validation<TextInputLayout, String>() {

  constructor(input: TextInputLayout, vararg rules: ValidateRule<String>) : this(input, linkedSetOf(*rules))

  override fun check() = input.editText != null

  override fun clearError() {
    input.editText?.addTextChangedListener(AfterTextChangedWatcher({ input.isErrorEnabled = false }))
  }
}

