//package com.app.indianic.ui.common
//
//import android.os.Bundle
//import androidx.activity.OnBackPressedCallback
//import com.app.indianic.base.BaseActivity
//import com.app.indianic.databinding.ActivityNoInternetBinding
//import com.app.indianic.ui.home.activity.HomeActivity
//import com.app.indianic.ui.signup.activity.OnboardingActivity
//import com.app.indianic.utils.AppUtil
//import com.app.indianic.utils.UserPreferences
//import com.app.indianic.utils.startNewActivity
//import dagger.hilt.android.AndroidEntryPoint
//import kotlinx.coroutines.flow.first
//import kotlinx.coroutines.runBlocking
//import javax.inject.Inject
//
//@AndroidEntryPoint
//class NoInternetActivity :
//    BaseActivity<ActivityNoInternetBinding>(ActivityNoInternetBinding::inflate) {
//
//    @Inject
//    lateinit var prefs: UserPreferences
//
//    private val callback = object : OnBackPressedCallback(true) {
//        override fun handleOnBackPressed() {
//
//        }
//    }
//
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        binding.cvGoToHome.setOnClickListener {
//            if (AppUtil.isInternetAvailable(this@NoInternetActivity)) {
//                runBlocking {
//                    if (prefs.userId.first().isNullOrEmpty()) {
//                        runOnUiThread {
//                            startNewActivity(OnboardingActivity::class.java)
//                        }
//                    } else {
//                        runOnUiThread {
//                            startNewActivity(HomeActivity::class.java)
//                        }
//                    }
//                }
//
//            } else {
//                //showToast("")
//            }
//        }
//
//        onBackPressedDispatcher.addCallback(this@NoInternetActivity, callback)
//
//
//    }
//
//}
