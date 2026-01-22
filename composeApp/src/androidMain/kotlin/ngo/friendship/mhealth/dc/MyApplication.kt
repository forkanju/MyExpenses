package ngo.friendship.mhealth.dc

import android.app.Application
import ngo.friendship.mhealth.dc.di.initializeKoin
import org.koin.android.ext.koin.androidContext

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initializeKoin(
            config = {
                androidContext(this@MyApplication)
            }
        )
    }
}