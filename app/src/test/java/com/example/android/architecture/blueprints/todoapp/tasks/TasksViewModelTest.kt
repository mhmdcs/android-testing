package com.example.android.architecture.blueprints.todoapp.tasks

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.android.architecture.blueprints.todoapp.Event
import com.example.android.architecture.blueprints.todoapp.data.Task
import com.example.android.architecture.blueprints.todoapp.data.source.FakeTestRepository
import com.example.android.architecture.blueprints.todoapp.getOrAwaitValue
import org.hamcrest.CoreMatchers.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


//Add the AndoirdJUnit4 test runner if you're using AndroidX Test library code
//@RunWith(AndroidJUnit4::class)
class TasksViewModelTest {


    //use the @Before rule annotation to create a setup method and remove repeated code. Since all of these tests are going to test the TasksViewModel and will need a view model, move the ViewModel initialization code to @Before block.

    // Subject under test
    private lateinit var tasksViewModel: TasksViewModel
    //Warning. Do NOT initialize the tasksViewModel above, and instead initialize it within the @Before rule block
    //Or it will cause the same instance of the ViewModel to be used for all tests. This is something you should avoid because each test should have a fresh instance of the subject under test (the ViewModel in this case).

    // Use a fake repository to be injected into the viewmodel
    private lateinit var tasksRepository: FakeTestRepository


    //This ViewModel construction for testing used the AndroidX Test library, since we're now instead passing in the repository in the ViewModel instead of the application context, we won't need the AndroidX Test library or the @RunWith(AndroidJUnit4::class) annotation, this will speed up the testing performance
//    @Before
//    fun setupViewModel() {
//        tasksViewModel = TasksViewModel(ApplicationProvider.getApplicationContext())
//    }

    //in setupViewModel method now it initializes the FakeTestRepository with three tasks and then constructs the tasksViewModel with this repository.
    @Before
    fun setupViewModel() {
        // We initialise the tasks to 3, with one active and two completed
        tasksRepository = FakeTestRepository()
        val task1 = Task("Title1", "Description1")
        val task2 = Task("Title2", "Description2", true)
        val task3 = Task("Title3", "Description3", true)
        tasksRepository.addTasks(task1, task2, task3)

        tasksViewModel = TasksViewModel(tasksRepository)

    }

    //create LiveDataTestUtil.kt file class so you can use the getOrAwaitValue Kotlin extension function

    //Two main things that you should do to test LiveData
    //First is to use architecture component InstantTaskExecutorRule
    //Second is to make sure that LiveData is observed

    //First: the InstantTaskExecutorRule is a JUnit rule, JUnit rules are classes that allow you to define some code before and after each test runs
    //This rule below runs all architecture component-related background jobs in the same thread
    //So it ensures that the test results happen 1. synchronously and 2. in a repeatable order. Two things that are pretty important for tests
    //basically when you're testing LiveData you should also include this rule
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()



    //Second: Testing LiveData is tricky because LiveData should be observed, the fact that LiveData is observed is important, when LiveData isn't observed it doesn't do much
    //You need active observers to trigger any onChange events, and to trigger any LiveData transformations
    //In short, to get the expected behavior for your LiveData, you need to observer the LiveData

    @Test
    fun addNewTask_setNewTaskEventBoilerplateCodeWay(){
        //we need to observe the newTask Event LiveData here, but this poses a problem, in TasksViewModelTest, we don't have an activity or a fragment or any lifecycle-owners with which to observe this LiveData
        //so to get around this, there's a observeForever method, what it does is that it ensures that LiveData is constantly observed without needing a lifecycle-owner
        //when you observeForever, you also need to remember to call removeObserver method, to make sure that you don't have an observer memory leak



        //GIVEN a fresh TasksViewModel
        // val tasksViewModel = TasksViewModel(ApplicationProvider.getApplicationContext()) this initialization is now in a @Before rule block

        //create observer dummy - no need for it to do anything!
        val observer  = Observer<Event<Unit>> {}
        try {

            //observe LiveData forever
            tasksViewModel.newTaskEvent.observeForever(observer)

            //WHEN adding a new task
            tasksViewModel.addNewTask()


            //THEN the new task event is triggered
            //now because newTaskEvent is being observed, I can now get the value from it, and write an assert statement
            val value = tasksViewModel.newTaskEvent.value
            assertThat(value?.getContentIfNotHandled(),not(nullValue())) //because we're testing an Event, we use getContentIfNotHandled in the assert statement

        } finally {
            //to make sure the removal of the observer absolutely happens, we put it in a finally block, after the try block
            tasksViewModel.newTaskEvent.removeObserver(observer)
        }

    }



    //this is the same as above but better since it doesn't have boilerplate code thanks to the LiveDataTestUtil.kt class and getOrAwaitValue Kotlin extension function
    @Test
    fun addNewTask_setNewTaskEvent(){

        //GIVEN a fresh TasksViewModel
        // val tasksViewModel = TasksViewModel(ApplicationProvider.getApplicationContext()) this initialization is now in a @Before rule block

        //WHEN adding a new task
        tasksViewModel.addNewTask()


        //THEN the new task event is triggered
        val value = tasksViewModel.newTaskEvent.getOrAwaitValue()//Observe and get the LiveData value for newTaskEvent using getOrAwaitValue extension function
        assertThat(value.getContentIfNotHandled(),not(nullValue())) //because we're testing an Event, we use getContentIfNotHandled in the assert statement

    }


    //write a test for the TasksAddViewVisible LiveData. This test should check that if you've set your filter type to show all tasks, then the Add task FAB button is visible.
    @Test
    fun setFilterAllTasks_tasksAddViewVisible(){
        //GIVEN a fresh ViewModel
       // val tasksViewModel = TasksViewModel(ApplicationProvider.getApplicationContext()) this initialization is now in a @Before rule block

        //WHEN the filter type is ALL_TASKS
        tasksViewModel.setFiltering(TasksFilterType.ALL_TASKS)

        //THEN the "Add task" action is visible
        val value = tasksViewModel.tasksAddViewVisible.getOrAwaitValue()
        assertThat(value,`is`(true))
    }


}