package com.example.android.architecture.blueprints.todoapp

import android.content.Context
import androidx.room.Room
import com.example.android.architecture.blueprints.todoapp.data.source.DefaultTasksRepository
import com.example.android.architecture.blueprints.todoapp.data.source.TasksDataSource
import com.example.android.architecture.blueprints.todoapp.data.source.TasksRepository
import com.example.android.architecture.blueprints.todoapp.data.source.local.TasksLocalDataSource
import com.example.android.architecture.blueprints.todoapp.data.source.local.ToDoDatabase
import com.example.android.architecture.blueprints.todoapp.data.source.remote.TasksRemoteDataSource

//object keyword in Kotlin creates a singleton class. It is used to obtain a data type with a single implementation.
//basically, it ensures you that only one instance of that class is created even if 2 threads try to create it.
//a singleton class is a class that can have only one object (an instance of the class) at a time
//the purpose of the singleton class is to control object creation, limiting the number of objects to only one. The singleton allows only one entry point to create the new instance of the class.
object ServiceLocator { //ServiceLocator class purpose is to be able to construct(create) and store a repository

    private var database: ToDoDatabase? = null
    @Volatile //Annotate the repository with @Volatile because it could be used and requested by multiple threads
    var tasksRepository: TasksRepository? = null



    //Right now the only thing your ServiceLocator needs to do is know how to return a TasksRepository.
    //It'll return a pre-existing DefaultTasksRepository or make and return a new DefaultTasksRepository if needed.

//Either provides an already existing repository or creates a new one.
//This method should be synchronized on "this" to avoid -in situations with multiple threads running- ever accidentally creating two repository instances.
    fun provideTasksRepository(context: Context): TasksRepository {  //this method takes a Context to be able to set up the database
        synchronized(this) {//make sure  that the creation of the TasksRepository never actually happens twice via putting it in a synchronized block, this could actually happen if this class is getting accesed by multiple threads, and it will, so put the code in a synchronized block
            return tasksRepository ?: createTasksRepository(context)  //the syntax here reads: if tasksRepository is null, then call createTasksRepository() and return the object the method returns instead
        }
    }

//Method for creating a new repository. Will call createTaskLocalDataSource and create a new TasksRemoteDataSource.
    private fun createTasksRepository(context: Context): TasksRepository {
        val newRepo = DefaultTasksRepository(TasksRemoteDataSource, createTaskLocalDataSource(context))
        tasksRepository = newRepo
        return newRepo
    }

//Method for creating a new local data source. Will call createDataBase.
    private fun createTaskLocalDataSource(context: Context): TasksDataSource {
        val database = database ?: createDataBase(context) //the syntax here reads: if database is null, then call createDataBase() and return the object the method returns instead
        return TasksLocalDataSource(database.taskDao())
    }

//Method for creating a new database.
    private fun createDataBase(context: Context): ToDoDatabase {
        val result = Room.databaseBuilder(
            context.applicationContext,
            ToDoDatabase::class.java, "Tasks.db"
        ).build()
        database = result
        return result
    }

}