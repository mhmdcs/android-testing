package com.example.android.architecture.blueprints.todoapp.data.source

import androidx.lifecycle.LiveData
import com.example.android.architecture.blueprints.todoapp.data.Result
import com.example.android.architecture.blueprints.todoapp.data.Task

//create a class FakeDataSouce, which will be a test double of a LocalDataSource and RemoteDataSource
//this is so that you will be able to test the DefaultTasksRepository since it's depended on these two DataSources

//add a parameter to the class of MutableList type and set its default value to an empty MutableList, and implement TasksDataSource API
class FakeDataSource(var  tasks: MutableList<Task>? = mutableListOf()): TasksDataSource {

    //write fake version of the methods with "real enough" and "working" implementation, but much less complicated than the real one.


    override suspend fun getTasks(): Result<List<Task>> {
        //If tasks isn't null, you should return a Success result. If tasks is null, then you should return an Error result.

        //this expression says : *if* the mutable list of tasks is not empty, then return a Success Result along with my list of tasks (ArrayList(it)
                                //and if it is null, return an error result with exception "Tasks not found"
        tasks?.let {
        return Result.Success(ArrayList(it))
        }
        return Result.Error(
            Exception("Tasks not found")
        )
    }

    override suspend fun deleteAllTasks() {
        //clear the mutable tasks list.
        tasks?.clear() //as long as the mutable list of tasks is not null, then clear out the list
    }

    override suspend fun saveTask(task: Task) {
        //add the task to the list.
        tasks?.add(task) // ? is a null null check. This line says: as long as the mutable list of tasks is not null, then add the task to the mutable list
    }

    override fun observeTasks(): LiveData<Result<List<Task>>> {
        TODO("Not yet implemented")
    }

    override suspend fun refreshTasks() {
        TODO("Not yet implemented")
    }

    override fun observeTask(taskId: String): LiveData<Result<Task>> {
        TODO("Not yet implemented")
    }

    override suspend fun getTask(taskId: String): Result<Task> {
        TODO("Not yet implemented")
    }

    override suspend fun refreshTask(taskId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun completeTask(task: Task) {
        TODO("Not yet implemented")
    }

    override suspend fun completeTask(taskId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun activateTask(task: Task) {
        TODO("Not yet implemented")
    }

    override suspend fun activateTask(taskId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun clearCompletedTasks() {
        TODO("Not yet implemented")
    }

    override suspend fun deleteTask(taskId: String) {
        TODO("Not yet implemented")
    }

}