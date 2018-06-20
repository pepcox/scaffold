package com.vander.scaffold.form

import android.support.design.widget.TextInputLayout
import android.text.Editable
import android.view.View
import android.widget.CheckBox
import com.jakewharton.rxbinding2.widget.afterTextChangeEvents
import com.vander.scaffold.form.validator.TextInputValidation
import com.vander.scaffold.form.validator.Validation
import com.vander.scaffold.form.validator.rules.ValidateRule
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

typealias FormData = Map<Int, Any>
typealias FormResult = Pair<Boolean, Map<Int, Any>>

class Form : TextInputValidation {
  private val state: MutableMap<Int, Any> = mutableMapOf()
  private lateinit var items: Map<View, Set<ValidateRule<*>>>


  @Suppress("UNCHECKED_CAST")
  private fun View.validate(vararg rules: ValidateRule<*>): Boolean =
      if (this is TextInputLayout) {
        if (visibility != View.VISIBLE) true else
          (rules as Array<ValidateRule<String>>).find { !it.validate(editText?.text.toString()) }
              .let {
                error = it?.let {
                  if (it.errorRes != -1) context.getString(it.errorRes)
                  else it.errorMessage.invoke(editText?.text.toString())
                }
                it == null
              }
      } else if (this is CheckBox) {
        if (visibility != View.VISIBLE) true else
          (rules as Array<ValidateRule<Boolean>>).find { !it.validate(isChecked) }
              .let {
                error = it?.let {
                  if (it.errorRes != -1) context.getString(it.errorRes)
                  else it.errorMessage.invoke("Nieco")
                }
                it == null
              }
        isChecked
      } else {
        false
      }

  private fun Map<View, Set<ValidateRule<*>>>.validate(): Single<FormResult> =
      map { (item, rules) -> item.validate(*rules.toTypedArray()) }
          .find { !it }
          .let { Single.just(FormResult(it == null, state.toMap())) }

  private fun init(vararg validations: Validation<*, *>) {
    check(validations.all { it.check() })
    validations.forEach { it.clearError() }
    items = validations.associate { it.input to it.rules }
  }

  override fun validate(): Single<FormResult> = items.validate()

  fun with(vararg validations: Validation<*, *>) = object : TextInputValidation {
    override fun validate(): Single<FormResult> {
      val map = items.toMutableMap()
      validations.forEach {
        if (map.containsKey(it.input)) {
          map[it.input] = map[it.input]!!.plus(it.rules)
        } else {
          map[it.input] = it.rules
        }
      }
      return map.validate()
    }
  }

  @Suppress("UNCHECKED_CAST")
  fun state(): Observable<FormData> =
      items.filter { it.key is TextInputLayout }.map { (it as Map.Entry<TextInputLayout, Set<ValidateRule<String>>>) }
          .map { (input, _) -> input.editText!!.afterTextChangeEvents().skipInitialValue().map { input.id to it.editable()!! } }
          .let { Observable.merge(it) }
          .debounce(300, TimeUnit.MILLISECONDS)
          .observeOn(AndroidSchedulers.mainThread())
          .filter { (id, editable) -> state[id].toString() != editable.toString() }
          .doOnNext { (id, editable) -> if (editable.isBlank()) state.remove(id) else state[id] = editable }
          .map { state.toMap() }// TODO add state for checkbox input

  fun restore(formData: FormData) {
    if (state.isEmpty()) {
      state.putAll(formData)
      items.filter { it.key is TextInputLayout }.map { (it.key as TextInputLayout).apply { error = null } }.filter { state.containsKey(it.id) }.forEach {
        it.editText!!.text = (state[it.id] as Editable)
        it.editText!!.apply { setSelection(length()) }
      }
      items.filter { it.key is CheckBox }.map { (it.key as CheckBox).apply { error = null } }.filter { state.containsKey(it.id) }.forEach {
        it.isChecked = (state[it.id] as Boolean)
      }
    }
  }

  companion object {
    fun init(vararg validations: Validation<*, *>) = Form().apply { init(*validations) }
  }

}