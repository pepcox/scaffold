package com.vander.scaffold.screen

import android.os.Bundle
import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

/**
 * @author marian on 24.9.2017.
 */
abstract class ScreenModel<T : Screen.State, in U : Screen.Intents>(default: T? = null) : ViewModel() {
  val state: BehaviorSubject<T> = if (default != null) BehaviorSubject.createDefault(default) else BehaviorSubject.create()
  val event: PublishSubject<Event> = PublishSubject.create()
  lateinit var args: Bundle

  val stateValue
    get() = checkNotNull(state.value)

  abstract fun collectIntents(intents: U, result: Observable<Result>): Disposable

  internal fun collect(intents: U, result: Observable<Result>): Disposable = CompositeDisposable().apply {
    intents.events().forEach { add(it.subscribe()) }
    add(collectIntents(intents, result))
  }

  protected fun BehaviorSubject<T>.next(state: T.() -> T) =
      this.onNext(state.invoke(stateValue))

  protected fun BehaviorSubject<T>.init(state: T) {
    if (!hasValue()) onNext(state)
  }
}