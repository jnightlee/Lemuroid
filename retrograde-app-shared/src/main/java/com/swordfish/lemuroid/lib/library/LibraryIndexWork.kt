package com.swordfish.lemuroid.lib.library

import android.content.Context
import androidx.work.ExistingWorkPolicy
import androidx.work.ListenableWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.RxWorker
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.swordfish.lemuroid.lib.injection.AndroidWorkerInjection
import com.swordfish.lemuroid.lib.injection.WorkerKey
import dagger.Binds
import dagger.android.AndroidInjector
import dagger.multibindings.IntoMap
import io.reactivex.Single
import timber.log.Timber
import javax.inject.Inject

class LibraryIndexWork(context: Context, workerParams: WorkerParameters) : RxWorker(context, workerParams) {
    @Inject lateinit var gameLibrary: GameLibrary

    override fun createWork(): Single<Result> {
        AndroidWorkerInjection.inject(this)
        return gameLibrary.indexGames()
                .toSingleDefault(Result.success())
                .doOnError { Timber.e("Library indexing failed with exception: $it") }
                .onErrorReturn { Result.success() } // We need to return success or the Work chain will die forever.
    }

    companion object {
        val UNIQUE_WORK_ID = LibraryIndexWork::class.java.simpleName
        fun newRequest() = OneTimeWorkRequestBuilder<LibraryIndexWork>().build()

        fun enqueueUniqueWork(applicationContext: Context) {
            WorkManager.getInstance(applicationContext).enqueueUniqueWork(
                UNIQUE_WORK_ID,
                ExistingWorkPolicy.APPEND,
                newRequest()
            )
        }
    }

    @dagger.Module(subcomponents = [Subcomponent::class])
    abstract class Module {
        @Binds
        @IntoMap
        @WorkerKey(LibraryIndexWork::class)
        abstract fun bindMyWorkerFactory(builder: Subcomponent.Builder): AndroidInjector.Factory<out ListenableWorker>
    }

    @dagger.Subcomponent
    interface Subcomponent : AndroidInjector<LibraryIndexWork> {
        @dagger.Subcomponent.Builder
        abstract class Builder : AndroidInjector.Builder<LibraryIndexWork>()
    }
}