package us.bojie.paa.di

import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import us.bojie.paa.viewmodel.ViewModelProviderFactory

@Module
abstract class ViewModelFactoryModule {
    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelProviderFactory): ViewModelProvider.Factory
}