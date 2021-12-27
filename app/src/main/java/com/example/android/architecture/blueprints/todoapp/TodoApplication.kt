/*
 * Copyright (C) 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.architecture.blueprints.todoapp

import android.app.Application
import com.example.android.architecture.blueprints.todoapp.data.source.TasksRepository
import timber.log.Timber
import timber.log.Timber.DebugTree

/**
 * An application that lazily provides a repository. Note that this Service Locator pattern is
 * used to simplify the sample. Consider a Dependency Injection framework like Koin or Dagger (or Dagger's wrapper Hilt).
 * Note that Koin is a service-locator framework, while Dagger/or Hilt are actual Dependency Injection frameworks.
 * But both do the same job more or less. Koin for small projects, Dagger/Hilt for big ones.
 * Also, sets up Timber in the DEBUG BuildConfig. Read Timber's documentation for production setups.
 */
class TodoApplication : Application() {

    //make sure that you make one instance of your repository class for your application
    //to absolutely ensure this, use the ServiceLocator in the application class here
    val taskRepository: TasksRepository
    get() = ServiceLocator.provideTasksRepository(this)

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) Timber.plant(DebugTree())
    }
}
