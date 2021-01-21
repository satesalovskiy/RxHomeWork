package com.school.rxhomework

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ActivityViewModel : ViewModel() {
    private val _state = MutableLiveData<State>(State.Loading)
    val state: LiveData<State>
        get() = _state

    private val callback = object : Callback<List<MainActivity.Adapter.Item>> {
        override fun onResponse(call: Call<List<MainActivity.Adapter.Item>>, response: Response<List<MainActivity.Adapter.Item>>) {
            if (response.isSuccessful) {
                response.body()?.let { _state.value = State.Loaded(it) }
            }
        }

        override fun onFailure(call: Call<List<MainActivity.Adapter.Item>>, t: Throwable) {
            _state.value = State.Loaded(emptyList())
        }
    }

    init {
        refreshData()
    }

    private fun refreshData() {
        Repository.getPosts(callback)
    }

    fun processAction(action: Action) {
        when (action) {
            Action.RefreshData -> refreshData()
        }
    }
}
