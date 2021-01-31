package com.school.rxhomework

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ActivityViewModel : ViewModel() {
    private val _state = MutableLiveData<State>(State.Loading)
    val state: LiveData<State>
        get() = _state

    private val getPostsSubject = PublishSubject.create<Unit>()
    val getPostsObserver: Observer<Unit> = getPostsSubject

    init {
        refreshData()
    }

    private fun refreshData() {
        getPostsSubject
            .doOnError {
                _state.value = State.Loaded(emptyList())
            }
            .switchMap {
                Repository.getPosts()
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                _state.postValue(State.Loaded(it))
            }


//        Repository.getPosts()
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe {
//                _state.postValue(State.Loaded(it))
//            }
    }
}
