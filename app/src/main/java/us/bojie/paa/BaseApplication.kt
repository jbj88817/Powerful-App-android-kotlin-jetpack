package us.bojie.paa

import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import us.bojie.paa.di.DaggerAppComponent

class BaseApplication : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder().application(this).build()
    }
}