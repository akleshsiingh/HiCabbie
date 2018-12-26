package com.example.hicabbie.ui.excel

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.example.hicabbie.R
import com.example.hicabbie.data.db.entity.EMessage
import com.example.hicabbie.ui.base.BaseActivity
import com.example.hicabbie.utils.Logger
import com.example.hicabbie.utils.click
import com.example.hicabbie.utils.vis
import com.example.hicabbie.utils.visible
import jxl.WorkbookSettings
import kotlinx.android.synthetic.main.activity_excel.*
import java.util.*
import javax.inject.Inject


class ExcelActivity : BaseActivity() {


    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory


    override fun gtContentView() = R.layout.activity_excel

    private val TAGG: String = ExcelActivity::class.java.simpleName
    var scrollToBottom = false
    var scrolledUpto = 0
    val adapterMsg = AdapterMessage()
    override fun onViewReady(savedInstanceState: Bundle?, intent: Intent?) {

        val wbSettings = WorkbookSettings()
        wbSettings.locale = Locale("en", "EN")

        val vm = ViewModelProviders.of(this, viewModelFactory)[MessageViewModel::class.java]
        vm.printInfo()

        tvUnReadStatus.vis = false
        vm.messages.observe(this, android.arch.lifecycle.Observer {
            it ?: return@Observer
            //Logger.e(TAGG, " count ${it.size}")
            if (it.isNotEmpty()) {
                adapterMsg.addToList(it as MutableList<EMessage>)
                vm.updateMessageStatus(it[0].id, it[it.size - 1].id)
                for (i in it)
                    Logger.e(TAGG, " ${i.toString()}")
                if (scrollToBottom && adapterMsg.itemCount > 0) {
                    listMessage.scrollToPosition(adapterMsg.itemCount - 1)
                    tvUnReadStatus.vis = false
                } else {
                    updateUnreadCount()
                }
            }
        })

        tvUnReadStatus.click {
            listMessage.scrollToPosition(scrolledUpto)
            tvUnReadStatus.vis = false
        }

        val lManager = LinearLayoutManager(this)
        listMessage.apply {
            layoutManager = lManager
            adapter = adapterMsg
        }

        listMessage.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)


                if (adapterMsg.itemCount == (lManager.findLastVisibleItemPosition() + 1)) {
                    scrollToBottom = true
                    scrolledUpto = adapterMsg.itemCount
                    Logger.e(TAGG, " NOW AUTO SCROLL TO BOTTOM")
                    tvUnReadStatus.vis = false
                } else if (dy > 0 && !scrollToBottom) {
                    val pos = lManager.findLastVisibleItemPosition()
                    if (scrolledUpto < pos) {
                        scrolledUpto = pos
                        updateUnreadCount()
                    }
                } else {
                    scrollToBottom = false
                }
            }
        })

    }

    private fun updateUnreadCount() {
        val unread = adapterMsg.itemCount - scrolledUpto
        Logger.e(TAGG, " NEW UNREAD MESSAGE COUNT ${unread}")
        tvUnReadStatus.vis = true
        tvUnReadStatus.text = "$unread new messages"
    }


}

