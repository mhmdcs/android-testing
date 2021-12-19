package com.example.android.architecture.blueprints.todoapp.data.source.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.android.architecture.blueprints.todoapp.data.Task
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/*
* Since TasksDao is an interface that Room turns into a class using the magic of @Dao annotation, we had to create this testing  class manually rather than generate it
* Dao(Database Access Objects) tests are unit tests, they will be instrumented tests in the androidTest source set, this is because if you're running these tests locally,
* they'll use whatever version of SQLite that you have on your local machine (your laptop for example), and that could be very different than the version of SQLite that ships
* with an Android device, do note that different Android devices (tablets, phones, watches) ship with different versions of SQLite, so it's helpful to run these tests as instrumented tests on multiple devices
* */

@ExperimentalCoroutinesApi //since we're gonna be using runBlockingTest here
@RunWith(AndroidJUnit4::class) //because we're gonna be using code from AndroidX Test libraries, mainly the one to get the application's context when building the Room database
@SmallTest //unit tests are generally synonymous with small tests so we mark it  as @SmallTest
class TasksDaoTest {

    //We want each task to execute synchronously since we're testing Architecture Components here
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()


    private lateinit var database: ToDoDatabase

    @Before //@Before method for initializing your database
    fun initDb(){
        // Use the in-memory database builder so that the information stored here disappears when the process is killed.
        database = Room.inMemoryDatabaseBuilder( //normal database are meant to persist on-disk, in comparison, an in-memory database will be completely deleted once the process is killed and that's because it's never stored on-disk, note that this is something you would never want in production code, but it's perfect for our unit tests
                ApplicationProvider.getApplicationContext(), //get the application context from the AndroidX Test library
                ToDoDatabase::class.java //tell the builder what database class to use
                ).allowMainThreadQueries().build() //Normally Room doesn't allow database queries to be run on the main thread. Calling allowMainThreadQueries turns off this check. Don't do this in production code! This is just for testing purposes.
    }

    @After //@After method for cleaning up your database using database.close()
    fun closeDb() = database.close()

    @Test //we use runBlockingTest because insert Dao query is a suspend function
    fun insertTaskGetById() = runBlockingTest {
        //GIVEN - that a task is inserted into the database
        val task = Task("title","desc")
        database.taskDao().insertTask(task)

        //WHEN - we get the task from its task id from the database and store it
        val loaded = database.taskDao().getTaskById(task.id)

        //THEN - verify(assert) that the loaded task is equal to the inserted task
        assertThat<Task>(loaded as Task, notNullValue()) //assert that the loaded task exists
        assertThat(loaded.id, `is`(task.id))  //verify loaded task has same id as inserted task
        assertThat(loaded.title, `is`(task.title)) //verify loaded task has same title as inserted task
        assertThat(loaded.description, `is`(task.description)) //verify loaded task has same description as inserted task
        assertThat(loaded.isCompleted, `is`(task.isCompleted))//verify the loaded task isCompleted is false, since all tasks are active by default
    }

    @Test //Write a test that inserts a task, updates it, and then checks that it has the updated values
    fun updateTaskAndGetById() = runBlockingTest {
        //GIVEN - a task was inserted into the database
        val originalTask = Task("ORIGINAL TITLE","ORIGINAL DESC")
        database.taskDao().insertTask(originalTask)

        //WHEN - the task was updated (by creating a new task with the same ID but different attributes)
        val updatedTask = Task("UPDATED TITLE", "UPDATED DESC", true, originalTask.id)
        database.taskDao().updateTask(updatedTask)

        //THEN -verify(assert) that when you get the task by its ID, it has the updated values
        val loaded = database.taskDao().getTaskById(originalTask.id)
        assertThat(loaded?.id, `is`(updatedTask.id))
        assertThat(loaded?.title, `is`(updatedTask.title))
        assertThat(loaded?.description, `is`(updatedTask.description))
        assertThat(loaded?.isCompleted, `is`(updatedTask.isCompleted))
    }

}