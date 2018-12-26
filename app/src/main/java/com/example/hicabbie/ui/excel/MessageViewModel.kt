package com.example.hicabbie.ui.excel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.example.hicabbie.data.db.DaoMessage
import com.example.hicabbie.data.db.entity.EMessage
import com.example.hicabbie.utils.Logger
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MessageViewModel @Inject constructor(private val msgDao: DaoMessage) : ViewModel() {
    private val cd = CompositeDisposable()
    private val TAG: String = MessageViewModel::class.java.simpleName

    val messages = MutableLiveData<List<EMessage>>()
    fun printInfo() {
        Logger.e("INFO ", " ${System.identityHashCode(msgDao)}")
        cd.add(
            msgDao.getLatestmessage(false)
                .subscribe({
                    messages.postValue(it)
                },
                    { it.printStackTrace() })
        )
    }

    override fun onCleared() {
        cd.dispose()
        super.onCleared()
    }


    fun updateMessageStatus(from: Long, to: Long) {
        Logger.e(TAG, " UPDATE from $from - to$to")
        cd.add(
            Single.fromCallable {
                msgDao.updateMessageStatus(from, to)
            }.subscribeOn(Schedulers.io())
                .subscribe({
                    Logger.e(TAG, " UPDATE $it")
                }, { it.printStackTrace() })
        )
    }

}