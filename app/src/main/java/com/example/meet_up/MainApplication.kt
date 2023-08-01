package com.example.meet_up

import android.app.Application
import com.example.meet_up.di.DaggerMainComponent
import com.example.meet_up.di.MainComponent

class MainApplication : Application() {

    val mainComponent
        get() = _mainComponent!!

    private var _mainComponent: MainComponent? = null

    override fun onCreate() {
        super.onCreate()

        initComponent()
        _instance = this
    }

    private fun initComponent() {
        _mainComponent = DaggerMainComponent.builder()
            .addContext(this)
            .build()
    }

    companion object {
        private var _instance: MainApplication? = null

        val INSTANCE: MainApplication
            get() = _instance!!
    }
}