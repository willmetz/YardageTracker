package rules

import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.rules.TestWatcher
import org.junit.runner.Description




class ImmediateSchedulersRule : TestWatcher() {

    override fun starting(description: Description?) {
        super.starting(description)

        RxJavaPlugins.setIoSchedulerHandler { scheduler -> Schedulers.trampoline() }
        RxJavaPlugins.setComputationSchedulerHandler { scheduler -> Schedulers.trampoline() }
        RxJavaPlugins.setNewThreadSchedulerHandler { scheduler -> Schedulers.trampoline() }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { scheduler -> Schedulers.trampoline() }
    }

    override fun finished(description: Description?) {
        super.finished(description)

        RxJavaPlugins.reset();
        RxAndroidPlugins.reset();
    }
}