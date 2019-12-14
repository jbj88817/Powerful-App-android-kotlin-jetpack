package us.bojie.paa.di.main

import dagger.Module
import dagger.android.ContributesAndroidInjector
import us.bojie.paa.ui.main.account.AccountFragment
import us.bojie.paa.ui.main.account.ChangePasswordFragment
import us.bojie.paa.ui.main.account.UpdateAccountFragment
import us.bojie.paa.ui.main.blog.BlogFragment
import us.bojie.paa.ui.main.blog.UpdateBlogFragment
import us.bojie.paa.ui.main.blog.ViewBlogFragment
import us.bojie.paa.ui.main.create_blog.CreateBlogFragment

@Module
abstract class MainFragmentBuildersModule {

    @ContributesAndroidInjector()
    abstract fun contributeBlogFragment(): BlogFragment

    @ContributesAndroidInjector()
    abstract fun contributeAccountFragment(): AccountFragment

    @ContributesAndroidInjector()
    abstract fun contributeChangePasswordFragment(): ChangePasswordFragment

    @ContributesAndroidInjector()
    abstract fun contributeCreateBlogFragment(): CreateBlogFragment

    @ContributesAndroidInjector()
    abstract fun contributeUpdateBlogFragment(): UpdateBlogFragment

    @ContributesAndroidInjector()
    abstract fun contributeViewBlogFragment(): ViewBlogFragment

    @ContributesAndroidInjector()
    abstract fun contributeUpdateAccountFragment(): UpdateAccountFragment
}