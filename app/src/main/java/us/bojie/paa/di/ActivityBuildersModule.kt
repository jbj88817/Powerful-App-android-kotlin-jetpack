package us.bojie.paa.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import us.bojie.paa.di.auth.AuthFragmentBuildersModule
import us.bojie.paa.di.auth.AuthModule
import us.bojie.paa.di.auth.AuthScope
import us.bojie.paa.di.auth.AuthViewModelModule
import us.bojie.paa.di.main.MainFragmentBuildersModule
import us.bojie.paa.di.main.MainModule
import us.bojie.paa.di.main.MainScope
import us.bojie.paa.di.main.MainViewModelModule
import us.bojie.paa.ui.auth.AuthActivity
import us.bojie.paa.ui.main.MainActivity

@Module
abstract class ActivityBuildersModule {

    @AuthScope
    @ContributesAndroidInjector(
        modules = [AuthModule::class, AuthFragmentBuildersModule::class, AuthViewModelModule::class]
    )
    abstract fun contributeAuthActivity(): AuthActivity

    @MainScope
    @ContributesAndroidInjector(
        modules = [MainModule::class, MainFragmentBuildersModule::class, MainViewModelModule::class]
    )
    abstract fun contributeMainActivity(): MainActivity
}