package lab.justonebyte.moneysubuu.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import lab.justonebyte.moneysubuu.data.AppDatabase
import lab.justonebyte.moneysubuu.data.CategoryDao
import lab.justonebyte.moneysubuu.data.CategoryRepository
import lab.justonebyte.moneysubuu.data.CategoryRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(ActivityComponent::class)
abstract class CategoryModule {
    @Singleton
    @Provides
    fun provideCategoryDao(db: AppDatabase): CategoryDao = db.categoryDao()

    @Binds
    abstract fun bindCategoryRepository(
        categoryRepository: CategoryRepositoryImpl
    ): CategoryRepository


}