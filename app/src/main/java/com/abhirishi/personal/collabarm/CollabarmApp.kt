package com.abhirishi.personal.collabarm

import android.app.Application
import com.facebook.stetho.Stetho


class CollabarmApp : Application() {

    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)
    }

}
