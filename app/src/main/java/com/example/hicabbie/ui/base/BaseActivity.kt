package com.example.hicabbie.ui.base

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import dagger.android.support.DaggerAppCompatActivity

abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(gtContentView())
        onViewReady(savedInstanceState, intent)
    }
    abstract fun gtContentView(): Int

    abstract fun onViewReady(savedInstanceState: Bundle?, intent: Intent?)
}