package com.chutyrooms.mvvmnews.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.chutyrooms.mvvmnews.models.Article

@Database(
    entities = [Article::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class ArticleDatabase: RoomDatabase(){
    abstract fun getArticleDao():ArticleDao

    companion object{
        @Volatile
        // other thread can immediately see when a threat changes this instance In Kotlin
        // in order to force changes in a variable to be immediately visible to other threads,
        // we can use the annotation @Volatile .
        // If a variable is not shared between multiple threads,
        // you don't need to use volatile keyword with that variable
        private var instance: ArticleDatabase?=null

        private val LOCK= Any() // synchronize setting that instance so
                            // that we really make sure that there is only a singlr instance of our db at onece

        operator fun invoke(context: Context)= instance ?: synchronized(LOCK)
        {
            instance?:createDatabase(context).also{ instance = it }
        }

        private fun createDatabase(context: Context)=
            Room.databaseBuilder(
                context.applicationContext,
                ArticleDatabase::class.java,
                "article_db.db"
            ).build()



    }
}