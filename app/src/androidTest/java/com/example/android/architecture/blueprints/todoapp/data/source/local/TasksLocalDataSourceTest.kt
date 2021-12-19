package com.example.android.architecture.blueprints.todoapp.data.source.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.android.architecture.blueprints.todoapp.data.Result
import com.example.android.architecture.blueprints.todoapp.data.Task
import com.example.android.architecture.blueprints.todoapp.data.succeeded
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/*
* Notice that the only real difference between this and the DAO testing code is that the TasksLocalDataSource can
* be considered a medium "integration" test (as seen by the @MediumTest annotation), because the
* TasksLocalDataSourceTest will test both the code in TasksLocalDataSource and how it integrates with the DAO code.
* */

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@MediumTest
class TasksLocalDataSourceTest {

    //We want each task to execute synchronously since we're testing Architecture Components here
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    //create a lateinit field for the two components you're testing–- TasksLocalDataSource and your database:
    private lateinit var localDataSource: TasksLocalDataSource
    private lateinit var database: ToDoDatabase

    @Before //@Before method for initializing your database and datasource.
    fun setup(){
        database = Room.inMemoryDatabaseBuilder(
                ApplicationProvider.getApplicationContext(),
                ToDoDatabase::class.java
        ).allowMainThreadQueries().build()

        //Instantiate the TasksLocalDataSource, using your database and Dispatchers.Main.
        //This will run your queries on the main thread (this is allowed because of allowMainThreadQueries).
        localDataSource = TasksLocalDataSource(database.taskDao(), Dispatchers.Main)
    }

    @After //@After method for cleaning up your database using database.close()
    fun cleanUp() {
        database.close()
    }


    /*
    * The only real difference between TasksDaoTest and TasksLocalDataSourceTest is that the local data source returns an instance of the sealed Result class,
    * which is the format the repository expects. For example, in the test below we cast the result as a Success with result as Result.Success
    * */

    //runBlocking has been deprecated. Use its replacement runTest in the future, for now use runBlocking here to avoid libraries compatibility issues. https://github.com/Kotlin/kotlinx.coroutines/issues/1204
    @Test
    fun saveTask_retrievesTask() = runBlocking {
        // GIVEN - A new task saved in the database - Creates a task and inserts it into the database.
        val newTask = Task("title", "description", false)
        localDataSource.saveTask(newTask)

        // WHEN  - Task retrieved by ID - Retrieves the task using its id.
        val result = localDataSource.getTask(newTask.id)

        // THEN - Same task is returned - Asserts that that task was retrieved, and that all its properties match the inserted task.
        assertThat(result.succeeded, `is`(true))
        result as Result.Success
        assertThat(result.data.title, `is`("title"))
        assertThat(result.data.description, `is`("description"))
        assertThat(result.data.isCompleted, `is`(false))
    }

    @Test
    fun completeTask_retrievedTaskIsComplete() = runBlocking {
        // Given a new task in the persistent repository
        val newTask = Task("title")
        localDataSource.saveTask(newTask)

        // When completed in the persistent repository
        localDataSource.completeTask(newTask)
        val result = localDataSource.getTask(newTask.id)

        // Then the task can be retrieved from the persistent repository and is complete
        assertThat(result.succeeded, `is`(true))
        result as Result.Success
        assertThat(result.data.title, `is`(newTask.title))
        assertThat(result.data.isCompleted, `is`(true))
    }

}