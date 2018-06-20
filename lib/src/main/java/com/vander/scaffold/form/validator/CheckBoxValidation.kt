package com.vander.scaffold.form.validator

import android.widget.CheckBox
import com.vander.scaffold.form.validator.rules.ValidateRule

data class CheckBoxValidation(
    override val input: CheckBox,
    override val rules: LinkedHashSet<ValidateRule<Boolean>>
) : Validation<CheckBox, Boolean>() {
  constructor(input: CheckBox, vararg rules: ValidateRule<Boolean>) : this(input, linkedSetOf(*rules))

  override fun check() = true

  override fun clearError() {
    input.setOnCheckedChangeListener({ buttonView, _ -> buttonView.error = null})
  }
}