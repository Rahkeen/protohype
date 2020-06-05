package com.example.todolist.view

import android.text.Editable
import android.text.TextWatcher
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class ObservableTextWatcher : TextWatcher {

    private val textPublisher: PublishSubject<String> =
        PublishSubject.create()

    override fun afterTextChanged(s: Editable) {
        textPublisher.onNext(s.toString())
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit

    fun observeTextChanges(): Observable<String> {
        return textPublisher
            .debounce(500, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
    }
}