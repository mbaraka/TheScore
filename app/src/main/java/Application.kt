package com.thescore

import android.app.Application
import com.thescore.dagger.AppComponent
import com.thescore.dagger.AppContextModule
import com.thescore.dagger.DaggerAppComponent
import com.thescore.database.DBHelper

class Application : Application() {

    override fun onCreate() {
        super.onCreate()
        init()
    }

    private fun init(){
        appComponent = DaggerAppComponent.builder().appContextModule(AppContextModule(applicationContext)).build()
    }

    fun getAppComponent(): AppComponent {
        return appComponent
    }

    companion object {
        lateinit var appComponent: AppComponent
    }
}