package com.example.android.architecture.blueprints.todoapp.taskdetail

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.android.architecture.blueprints.todoapp.R
import com.example.android.architecture.blueprints.todoapp.ServiceLocator
import com.example.android.architecture.blueprints.todoapp.data.Task
import com.example.android.architecture.blueprints.todoapp.data.source.FakeAndroidTestRepository
import com.example.android.architecture.blueprints.todoapp.data.source.TasksRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.not
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




    /*Espresso is a library used to write Android UI tests which works with FragmentScenario, it is how you write assertion statements for fragment tests
    Using Espresso, you can interact with your views in your fragments and then check their states

    The majority of espresso statements are made up of four basic parts:
    1- Static Espresso method - for example: "onView" is a static Espresso method that starts an Espresso statement, "OnView" is definitely one of the most common Espresso static methods, but there are other options such as "onData" static method, "onView" basically says that you're going to do something with a certain view
    2- ViewMatchers - for example: "withId" method is a ViewMatcher, its purpose is to find views in your UI by their ids, there's a plethora of other ViewMatchers like "withText" which gets a view with certain text in it. Note that you want to uniquely identify views in Espresso testing else you'll get the dread AmbiguousViewMatcherException which happens when the ViewMatcher identifies more than one view, so make sure that whatever ViewMatcher you use, you're getting a single view from it.
    3- ViewActions - "perform" method takes ViewActions, an example of ViewActions is click(), a ViewAction is something that can be done to a view, for example, with click() ViewAction, we're clicking the view
    4- ViewAssertions - "check" method takes in ViewAssertions, an example of ViewAssertions is matches() which is one of the most common ViewAssertions, the ViewAssertion is where you'll actually assert something about the view you're testing, to finish the ViewAssertion, you use a ViewMatcher within it, like isChecked(), so a full assertion is check(matches(isChecked)) which checks/makes sure that CheckBox view is checked

    One thing that can causes your espresso instrumented tests "flaky" is if you leave your device's animations turned on
    if there's some sort of animation in your app that causes a lag, like the loading animation for network delay in this app, and you're trying to test whether a view is on screen or whether it's still animating, espresso can sometimes accidentally fail the test, so for espresso UI testing it's best practice to turn off UI animations
    to turn of UI animations go to Developer Options in your phone and turn Window's animation scale/Transition animation scale/Animator animation scale from 1x to off
    turn them on back on your real phone when you're done testing!
    */
    @Test
    fun activeTaskDetails_DisplayedInUi() = runBlockingTest {
        //basically we're going to be given one task, an active task, and then we're gonna make sure that it loads up properly in our fragment

        //GIVEN - Add active (aka incomplete) task to the DB
        val activeTask = Task("Active Task","AndroidX Rocks", false)
        repository.saveTask(activeTask) //Save activeTask in the repository before launching the fragment.

        //WHEN - Details fragment launched to display task
        val bundle = TaskDetailFragmentArgs(activeTask.id).toBundle()
        launchFragmentInContainer<TaskDetailFragment>(bundle, R.style.AppTheme) //use FragmentScenario API's launchFragmentInContainer method. The reason we've explicitly given the app theme is because when you're using launchFragmentInContainer you launch the fragment in an empty activity, and because fragments inherit their theme from the activity, you want to make sure you've given the correct theme here

        //THEN - Task details are displayed on screen (Everything after the THEN uses the Espresso library)
        //make sure (assert) that the title and description are displayed and have the correct texts
        onView(withId(R.id.task_detail_title_text)).check(matches(isDisplayed()))
        onView(withId(R.id.task_detail_title_text)).check(matches(withText("Active Task")))
        onView(withId(R.id.task_detail_description_text)).check(matches(isDisplayed()))
        onView(withId(R.id.task_detail_description_text)).check(matches(withText("AndroidX Rocks")))
        //make sure (assert) that the state of the checkbox is also displayed and it's unchecked
        onView(withId(R.id.task_detail_complete_checkbox)).check(matches(isDisplayed()))
        onView(withId(R.id.task_detail_complete_checkbox)).check(matches(not(isChecked())))

        Thread.sleep(3000) //let the execution of the current running thread sleep for 3 seconds to read the view data or read error more appropriately. When running finishing real working tests, don't use Thread.sleep, tests are supposed to run as fast as possible
    }



}