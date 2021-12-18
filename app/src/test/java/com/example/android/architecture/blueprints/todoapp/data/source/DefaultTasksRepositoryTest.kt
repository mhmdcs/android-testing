package com.example.android.architecture.blueprints.todoapp.data.source

import com.example.android.architecture.blueprints.todoapp.MainCoroutineRule
import com.example.android.architecture.blueprints.todoapp.data.Result
import com.example.android.architecture.blueprints.todoapp.data.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.core.IsEqual
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class DefaultTasksRepositoryTest {
    private val task1 = Task("Title1","Description1")
    private val task2 = Task("Title2","Description2")
    private val task3 = Task("Title3","Description3")

    //two lists, one that's representing the list in the remoteTasksDatSource and one for the list in the localTasksDatSource
    private val remoteTasks = listOf(task1,task2).sortedBy { it.id }
    private val localTasks = listOf(task3).sortedBy { it.id }
    private val newTasks = listOf(task3).sortedBy { it.id }

    private lateinit var tasksRemoteDataSource: FakeDataSource
    private lateinit var tasksLocalDataSource: FakeDataSource

    //class under test
    private lateinit var tasksRepository: DefaultTasksRepository

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    //this method will be called at the start of every test, so create it with a @Before rule annotation
    @Before
    fun createRepository(){
        tasksRemoteDataSource = FakeDataSource(remoteTasks.toMutableList())
        tasksLocalDataSource = FakeDataSource(localTasks.toMutableList())

        //Get a reference to the class under test
        tasksRepository = DefaultTasksRepository(
            // TODO Dispatchers.Unconfined should be replaced with Dispatchers.Main
            //  this requires understanding more about coroutines + testing
            //  so we will keep this as Unconfined for now. THIS IS NOT BEST PRACTICE.
            //  Update: You have now created a custom JUnit rule MainCoroutineRule() that swaps Dispatchers.Main with TestCoroutineDispatcher
            //  now you can use Dispatchers.Main instead of Dispatchers.Unconfined.
            //  Do note that similar to the TestCoroutineDispatcher, Dispatchers.Unconfined executes tasks immediately, but it doesn't include all of the other testing benefits of the TestCoroutineDispatcher, such as being able to pause execution, thus for tests a TestCoroutineDispatcher is preferable and we switched to it using MainCoroutineRule
            tasksRemoteDataSource,tasksLocalDataSource, Dispatchers.Main) //
    }


    //Write a test for the repository's getTasks method. Check that when you call getTasks with true (meaning that
    //it should reload from the remote data source) that it returns data from the remote data source (as opposed to the local data source)
    //use runBlockingTest in your test classes when you're calling a suspend function, and don't forget to add @ExperimentalCoroutinesApi above the class since runBlockingTest's kotlinx.coroutines.test library is in an experimental state as of now
    //runBlockingTest ensures that the test is run synchronously and immediately, it ensures that the code is going to run in a deterministic order which is pretty important for tests
    //it also essentially makes your coroutines run like non-coroutines, so it's really only meant for testing
    @Test
    fun getTasks_requestAllTasksFromRemoteDataSource() = mainCoroutineRule.runBlockingTest {
        //When tasks are requested from the tasks repository
        val tasks = tasksRepository.getTasks(true) as Result.Success

        //Then tasks are loaded from the remote data source
        assertThat(tasks.data, IsEqual(remoteTasks))
    }

}