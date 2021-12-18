package com.example.android.architecture.blueprints.todoapp.statistics

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.android.architecture.blueprints.todoapp.MainCoroutineRule
import com.example.android.architecture.blueprints.todoapp.data.source.FakeTestRepository
import com.example.android.architecture.blueprints.todoapp.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class StatisticsViewModelTest {

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule() //Add the InstantTaskExecutorRule since you are testing Architecture Components.

    // Set the main coroutines dispatcher for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule() //Add the MainCoroutineRule since you are testing coroutines and view models.

            // Subject under test
    private lateinit var statisticsViewModel: StatisticsViewModel

    // Use a test-double fake repository to be injected into the view model.
    private lateinit var tasksRepository: FakeTestRepository

    @Before  //Create a @Before method that sets up the subject under test and dependencies.
    fun setupStatisticsViewModel() {
        // Initialise the repository with no tasks.
        tasksRepository = FakeTestRepository()

        statisticsViewModel = StatisticsViewModel(tasksRepository)
    }

    @Test
    fun loadTasks_loading(){

        //WHEN - refresh occurs
        mainCoroutineRule.pauseDispatcher() //TestCoroutineDispatcher pauses the coroutine right before the viewModelScope.launch in refresh() so that we can capture that the dataLoading was set to true

        statisticsViewModel.refresh()

        //THEN - verify(assert) that the refresh icon appears and then disappears
        assertThat(statisticsViewModel.dataLoading.getOrAwaitValue(), `is`(true))

        mainCoroutineRule.resumeDispatcher() //TestCoroutineDispatcher resumes and executes all of the coroutine inside the viewModelScope.launch in refresh() immediately so that we can capture that the dataLoading was set to false

        assertThat(statisticsViewModel.dataLoading.getOrAwaitValue(), `is`(false))

    }
}