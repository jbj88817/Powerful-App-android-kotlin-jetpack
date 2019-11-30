package us.bojie.paa.di.auth


import dagger.Module
import dagger.android.ContributesAndroidInjector
import us.bojie.paa.ui.auth.ForgotPasswordFragment
import us.bojie.paa.ui.auth.LauncherFragment
import us.bojie.paa.ui.auth.LoginFragment
import us.bojie.paa.ui.auth.RegisterFragment

@Module
abstract class AuthFragmentBuildersModule {

    @ContributesAndroidInjector
    abstract fun contributeLauncherFragment(): LauncherFragment

    @ContributesAndroidInjector
    abstract fun contributeLoginFragment(): LoginFragment

    @ContributesAndroidInjector
    abstract fun contributeRegisterFragment(): RegisterFragment

    @ContributesAndroidInjector
    abstract fun contributeForgotPasswordFragment(): ForgotPasswordFragment

}