package com.example.rsupportapprenticeship

import android.util.Log
import com.sendbird.android.SendbirdChat
import com.sendbird.android.exception.SendbirdException
import com.sendbird.android.handler.InitResultHandler
import com.sendbird.android.params.InitParams

class Application : android.app.Application() {
    override fun onCreate() {
        super.onCreate()

        SendbirdChat.init(InitParams(Key.APP_ID,applicationContext,true),object :
            InitResultHandler {
            override fun onMigrationStarted() {
                Log.e("Application", "Called when there's an update in Sendbird server.")
            }

            override fun onInitFailed(e: SendbirdException) {
                Log.e("Application", "Called when initialize failed. SDK will still operate properly as if useLocalCaching is set to false.")
            }

            override fun onInitSucceed() {
                Log.e("Application", "Called when initialization is completed.")
            }

        })
    }
}