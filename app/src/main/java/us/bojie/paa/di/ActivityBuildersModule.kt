package us.bojie.paa.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import us.bojie.paa.di.auth.AuthFragmentBuildersModule
import us.bojie.paa.di.auth.AuthModule
import us.bojie.paa.di.auth.AuthScope
import us.bojie.paa.di.auth.AuthViewModelModule
import us.bojie.paa.ui.auth.AuthActivity

@Module
abstract class ActivityBuildersModule {

    @AuthScope
    @ContributesAndroidInjector(
        modules = [AuthModule::class, AuthFragmentBuildersModule::class, AuthViewModelModule::class]
    )
    abstract fun contributeAuthActivity(): AuthActivity

}