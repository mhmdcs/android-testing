package com.example.android.architecture.blueprints.todoapp.taskdetail

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.android.architecture.blueprints.todoapp.R
import com.example.android.architecture.blueprints.todoapp.ServiceLocator
import com.example.android.architecture.blueprints.todoapp.data.Task
import com.example.android.architecture.blueprints.todoapp.data.source.FakeAndroidTestRepository
import com.example.android.architecture.blueprints.todoapp.data.source.TasksRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@MediumTest //Generally unit tests are annotated with @SmallTest, integration tests are annotated with @MediumTest, and end-to-end tests are annotated with @LargeTest, these annotations help you group and choose which size of test to run
@RunWith(AndroidJUnit4::class) ////Add AndroidJUnit4 test runner when you're using AndroidX Test library code
@ExperimentalCoroutinesApi //add ExperimentalCoroutinesApi because runBlockingTest is an experimental coroutine method
class TaskDetailFragmentTest{

    private lateinit var repository: TasksRepository

    @Before //annotate the method with @Before means that it'll always run before each test
    fun initRepository(){
        repository = FakeAndroidTestRepository()
        ServiceLocator.tasksRepository = repository //this is how you use the setter you defined in the Service Locator annotated with @VisibleForTesting, here you're swapping the real repository with the fake repository
    }

    //now you need to ensure that the repository is completely reset between tests and properly cleaned up
    //as a rule, your test should be completely independent from one another, that means that when this class has a bunch of different tests, the repository must be reset to the original state each time
    //note that there is no defined order in which tests are run, if you have 10 tests here they could run in different orders, because of this you should never assume that a group of tests or a single test is going to run with another test
    //so they should have absolutely no relation or dependencies on one another
    @After //annotate the method with @After means that it'll always run after each test
    fun cleanupDb() = runBlockingTest {
        ServiceLocator.resetRepository()
    }


    @Test
    fun activeTaskDetails_DisplayedInUi() = runBlockingTest {
        //basically we're going to be given one task, an active task, and then we're gonna make sure that it loads up properly in our fragment

        //GIVEN - Add active (aka incomplete) task to the DB
        val activeTask = Task("Active Task","AndroidX Rocks", false)
        repository.saveTask(activeTask) //Save activeTask in the repository before launching the fragment.

        //WHEN - Details fragment launched to display task
        val bundle = TaskDetailFragmentArgs(activeTask.id).toBundle()
        launchFragmentInContainer<TaskDetailFragment>(bundle, R.style.AppTheme) //use FragmentScenario API's launchFragmentInContainer method. The reason we've explicitly given the app theme is because when you're using launchFragmentInContainer you launch the fragment in an empty activity, and because fragments inherit their theme from the activity, you want to make sure you've given the correct theme here
        Thread.sleep(3000) //let the execution of the current running thread sleep for 3 seconds to read the view data or read error more appropriately
    }


}