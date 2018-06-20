package com.vander.scaffold.form.validator

import com.vander.scaffold.form.FormResult
import io.reactivex.Single

interface TextInputValidation {
  fun validate(): Single<FormResult>
}