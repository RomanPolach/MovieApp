package cz.ackee.cookbook.di

import org.koin.dsl.module

/**
 * Module for providing persistence related dependencies.
 */
val persistenceModule = module {


//    single {
//        Room.databaseBuilder(androidContext(), RoomStore::class.java, "mydb")
//            .fallbackToDestructiveMigration()
//            .build()
//    }
//
//    single {
//        get<RoomStore>().recipeDao()
//    }
}