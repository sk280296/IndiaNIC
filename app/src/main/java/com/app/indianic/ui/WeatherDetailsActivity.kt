package com.app.indianic.ui//package com.app.indianic
//
//import android.os.Bundle
//import androidx.activity.OnBackPressedCallback
//import androidx.activity.viewModels
//import androidx.fragment.app.Fragment
//import androidx.viewpager2.widget.ViewPager2
//import com.app.indianic.base.BaseActivity
//import com.app.indianic.base.BaseResponse
//import com.app.indianic.databinding.ActivitySetupBusinessProfileBinding
//import com.app.indianic.enums.TypefaceWeight
//import com.app.indianic.response.businessdetail.BusinessProfileUserResponse
//import com.app.indianic.utils.AppUtil
//import com.app.indianic.utils.Resource
//import com.app.indianic.utils.UserPreferences
//import com.app.indianic.utils.logError
//import com.app.indianic.utils.showShortToast
//import com.google.gson.Gson
//import dagger.hilt.android.AndroidEntryPoint
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.runBlocking
//import javax.inject.Inject
//
//@AndroidEntryPoint
//class WeatherDetailsActivity : BaseActivity<ActivitySetupBusinessProfileBinding>(
//    ActivitySetupBusinessProfileBinding::inflate
//), (Int, Boolean) -> Unit {
//
//
//    private var businessDetails: BusinessProfileUserResponse.Data.BusinessDetails? = null
//
//    @Inject
//    lateinit var prefs: UserPreferences
//
//    private var step: Int? = null
//
//    private val callback = object : OnBackPressedCallback(true) {
//        override fun handleOnBackPressed() {
//            val position = binding.viewPager2.currentItem
//            if (position > 0) {
//                binding.viewPager2.currentItem = position - 1
//            } else {
//                runBlocking(Dispatchers.IO) {
//                    prefs.saveAccessToken("")
//                    prefs.saveRefreshToken("")
//                    prefs.saveUserData(Gson().toJson(""))
//                    prefs.saveSelectedLocation(null)
//                    prefs.clear()
//                    if (step == -1) {
//                        prefs.saveRestartApplication("false")
//                        //openActivity(CreateBusinessProfileActivity::class.java)
//                    } else {
//                        prefs.saveRestartApplication("false")
//                        //openActivity(LoginActivity::class.java)
//                    }
//                }
//                finish()
//            }
//        }
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        runBlocking(Dispatchers.IO) {
//            prefs.saveRestartApplication("true")
//        }
//
//        step = intent.extras?.getInt("STEP", -1)
//
////        businessSignUpPagerAdapter = BusinessSignUpPagerAdapter(this@WeatherDetailsActivity)
////        binding.viewPager2.isUserInputEnabled = false
////        binding.viewPager2.adapter = businessSignUpPagerAdapter
//
//        setListener()
//
//        logError("step123: $step")
//
///*
//        if (step != -1) {
//            getBusinessProfileApiCall()
//        } else {
//            businessProfileFragment =
//                BusinessProfileFragment.newInstance("REGULAR", null)
//            businessInformationFragment =
//                BusinessInformationFragment.newInstance("REGULAR", null)
//            businessContactInformationFragment =
//                BusinessContactInformationFragment.newInstance("REGULAR", null)
//
//            fragmentList.addAll(arrayListOf<Fragment>().also {
//                it.add(businessProfileFragment!!)
//                it.add(businessInformationFragment!!)
//                it.add(businessContactInformationFragment!!)
//            })
//            businessSignUpPagerAdapter.setFragmentList(fragmentList)
//        }
//*/
//
//
//        onBackPressedDispatcher.addCallback(this, callback)
//
//        //binding.cvSignUp.isEnabled = false
//
//
//        /*businessProfileFragment =
//            BusinessProfileFragment.newInstance(if (step == null) "REGULAR" else "REEDIT")
//
//        fragmentList.addAll(arrayListOf<Fragment>().also {
//            it.add(businessProfileFragment!!)
//            it.add(businessInformationFragment)
//            it.add(businessContactInformationFragment)
//        })
//        businessSignUpPagerAdapter.setFragmentList(fragmentList)*/
//
//    }
//
//    private fun getBusinessProfileApiCall() {
//        collectLatestLifecycleFlow(
//            viewModel.getBusinessProfile()
//        ) {
//            if (it is Resource.Success) {
//                hideProgress()
//                logError("data123 ${it.data}")
//                val data = it.data as BusinessProfileUserResponse
//
//                businessDetails = data.data?.businessDetails
//
//
//                /*businessProfileFragment =
//                    BusinessProfileFragment.newInstance("EDIT", businessDetails)
//                businessInformationFragment =
//                    BusinessInformationFragment.newInstance("EDIT", businessDetails)*/
////                businessContactInformationFragment =
////                    BusinessContactInformationFragment.newInstance("EDIT", businessDetails)
//
////                fragmentList.addAll(arrayListOf<Fragment>().also {
////                    it.add(businessProfileFragment!!)
////                    it.add(businessInformationFragment!!)
////                    it.add(businessContactInformationFragment!!)
////                })
//              //  businessSignUpPagerAdapter.setFragmentList(fragmentList)
//
//                if (step != null) {
//                    binding.viewPager2.setCurrentItem(step!!, false)
//                }
//
//            } else if (it is Resource.Error) {
//                hideProgress()
//                if (it.data != null) {
//                    val error = it.data as BaseResponse
//                    logError("$error")
//                    if (!error.message.isNullOrEmpty()) {
//                        showShortToast(error.message)
//                    } else {
//                        showShortToast(getString(R.string.something_went_wrong))
//                    }
//                } else {
//                    showShortToast(getString(R.string.something_went_wrong))
//                }
//            } else if (it is Resource.Loading) {
//                showProgress()
//            }
//        }
//
//        //if (isValid())
//        /*  openActivityWithIntent(OtpVerificationActivity::class.java) {
//              putExtra("SCREEN_FLOW", ScreenFlow.SIGN_UP.value)
//              putExtra("EMAIL_ADDRESS", binding.etEmail.getText())
//          }*/
//
//    }
//
//
//    /*
//        private fun createDialog(): Dialog {
//            val dialog = Dialog(this@SetupBusinessProfileActivity)
//            val dialogBinding = DialogProfileCompletedBinding.inflate(layoutInflater)
//            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//            dialog.setContentView(dialogBinding.root)
//            dialogBinding.cvPreview.setOnClickListener {
//                openActivityWithIntent(PreviewBusinessDetailActivity::class.java) {
//                    putExtra("IS_PREVIEW", true)
//                }
//                dialog.dismiss()
//            }
//            dialogBinding.cvSubmit.setOnClickListener {
//                startNewActivity(HomeActivity::class.java)
//                dialog.dismiss()
//            }
//            return dialog
//        }
//    */
//
//    private fun setListener() {
//        binding.apply {
//            ivCheck1.setOnClickListener {
//                if (viewPager2.currentItem > 0) viewPager2.currentItem = 0
//            }
//            ivCheck2.setOnClickListener {
//                if (viewPager2.currentItem == 2) viewPager2.currentItem = 1
//            }/*ivCheck3.setOnClickListener {
//                viewPager2.currentItem = 2
//            }*/
////            cvContinue.setSafeOnClickListener {
////                val position = viewPager2.currentItem
////                if (position < fragmentList.lastIndex) {
////                    if (position == 1) {
////                        businessInformationFragment?.gotoNextStep()
////                        /*val isBusinessInformationValid =
////                            businessInformationFragment.isBusinessInformationValid()
////                        if (isBusinessInformationValid) {
////                            viewPager2.currentItem = position + 1
////                            businessInformationFragment.proceedSecondStepApiCall()
////                        }*/
////                    } else {
////                        businessProfileFragment?.gotoNextStep()
////
////                        /*val isBusinessProfileValid =
////                            businessProfileFragment.isBusinessProfileValid()
////                        if (isBusinessProfileValid) {
////                            viewPager2.currentItem = position + 1
////                            businessProfileFragment.proceedFirstStepApiCall()
////                        }*/
////                    }
////                } else {
////                    val isBusinessContactInformationValid =
////                        businessContactInformationFragment?.isBusinessContactInformationValid()
////                    if (isBusinessContactInformationValid == true) {
////                        businessContactInformationFragment?.submitThirdStepApiCall()/*val dialog = createDialog()
////                        dialog.show()*/
////                    }
////                }
////
////            }
//            ivBack.setOnClickListener {
//                val position = viewPager2.currentItem
//                if (position > 0) {
//                    viewPager2.currentItem = position - 1
//                } else {
//                    runBlocking(Dispatchers.IO) {
//                        prefs.saveAccessToken("")
//                        prefs.saveRefreshToken("")
//                        prefs.saveUserData(Gson().toJson(""))
//                        prefs.saveSelectedLocation(null)
//                        prefs.clear()
//                        if (step == -1) {
//                            prefs.saveRestartApplication("false")
//                            runOnUiThread {
//                                //openActivity(CreateBusinessProfileActivity::class.java)
//                            }
//                        } else {
//                            prefs.saveRestartApplication("false")
//                            runOnUiThread {
//                                //openActivity(LoginActivity::class.java)
//                            }
//                        }
//                    }
//                    finish()
//                }
//            }
//
//        }
//
//        binding.viewPager2.registerOnPageChangeCallback(mlistener)
//
//    }
//
//    private val mlistener = object : ViewPager2.OnPageChangeCallback() {
//        override fun onPageSelected(position: Int) {
//            logError("logerror: Isclicked")
//            //binding.progressBar.progress = position + 1
//            /* val progressAnimator: ObjectAnimator =
//                 ObjectAnimator.ofInt(binding.progressBar, "progress", (position + 1) * 20)
//             progressAnimator.start()*/
//            binding.apply {
//                if (position == 1) {
//                    tvContinue.text = getString(R.string.lbl_continue)
//                    changeTextAppearance(2)
//                    selectStep(2)
//                } else if (position == 2) {
//                    tvContinue.text = getString(R.string.finish)
//                    changeTextAppearance(3)
//                    selectStep(3)
//                } else {
//                    tvContinue.text = getString(R.string.lbl_continue)
//                    selectStep(1)
//                }
//            }
//
//        }
//    }
//
//    /*private fun changeCardColor(setEnable: Boolean) {
//        binding.cvSignUp.changeBackgroundColor(if (setEnable) R.color.color_E94826 else R.color.color_E94826_50)
//    }*/
//
//    private fun changeTextAppearance(step: Int) {
//        val str =
//            if (step == 2) binding.tvBusinessInformation.text.toString() else binding.tvContactInformation.text.toString()
//        val spannedString = AppUtil.createSpannable(
//            this,
//            str,
//            str,
//            true,
//            TypefaceWeight.MEDIUM,
//            true,
//            R.color.color_3C3B3E,
//        )
//        if (step == 2) binding.tvBusinessInformation.text = spannedString
//        else binding.tvContactInformation.text = spannedString
//    }
//
//    private fun selectStep(step: Int) {
//        binding.apply {
//            if (step == 1) {
//                ivCheck1.setImageResource(R.drawable.ic_radio_checked)
//                ivCheck2.setImageResource(R.drawable.ic_radio_unchecked)
//                ivCheck3.setImageResource(R.drawable.ic_radio_unchecked)
//            } else if (step == 2) {
//                ivCheck1.setImageResource(R.drawable.ic_radio_checked)
//                ivCheck2.setImageResource(R.drawable.ic_radio_checked)
//                ivCheck3.setImageResource(R.drawable.ic_radio_unchecked)
//            } else if (step == 3) {
//                ivCheck1.setImageResource(R.drawable.ic_radio_checked)
//                ivCheck2.setImageResource(R.drawable.ic_radio_checked)
//                ivCheck3.setImageResource(R.drawable.ic_radio_checked)
//            }
//        }
//    }
//
//    override fun invoke(step: Int, isNextStepEnabled: Boolean) {
//        if (isNextStepEnabled) {
//            binding.viewPager2.currentItem = step
//
//            binding.viewPager2.offscreenPageLimit = 3
//
//            /*binding.viewPager2.unregisterOnPageChangeCallback(mlistener)
//
//            Handler(Looper.getMainLooper()).postDelayed(Runnable {
//                binding.viewPager2.registerOnPageChangeCallback(
//                    mlistener
//                )
//            }, 1000)*/
//
//
//        }
//    }
//
//    /*
//        override fun onRequestPermissionsResult(
//            requestCode: Int, permissions: Array<String>, grantResults: IntArray
//        ) {
//            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//          */
//    /*  if (PermissionUtils.arePermissionsGranted(grantResults)) {
//                if (permissions.contains(Manifest.permission.CAMERA)) {
//                    openCamera()
//                } else {
//                    openGallery()
//                }
//            } else if (PermissionUtils.shouldShowRequestPermissionRationale(
//                    requireActivity(), permissions
//                )
//            ) {
//                // Some permissions denied, show a rationale or explanation to the user
//            } else {
//                // Some permissions denied with "Don't ask again" checked, ask the user to go to settings
//            }*//*
//
//    }
//*/
//
//    /*
//        override fun onStop() {
//            if (this.isFinishing) {
//                logError("ondestroy12345")
//                runBlocking(Dispatchers.IO) {
//                    prefs.saveAccessToken("")
//                    prefs.saveRefreshToken("")
//                    prefs.saveUserData(Gson().toJson(""))
//                    prefs.saveSelectedLocation(null)
//                    prefs.clear()
//
//                    logError("ondestroy12346")
//                }
//            } else {
//                // activity not dying just stopping
//                logError("ondestroy12347")
//            }
//            super.onStop()
//
//        }
//    */
//
//    /*
//        override fun onDestroy() {
//            logError("ondestroy123")
//            runBlocking(Dispatchers.IO) {
//                prefs.saveAccessToken("")
//                prefs.saveRefreshToken("")
//                prefs.saveUserData(Gson().toJson(""))
//                prefs.saveSelectedLocation(null)
//                prefs.clear()
//                if (step == null)
//                    openActivity(CreateBusinessProfileActivity::class.java)
//                else
//                    openActivity(LoginActivity::class.java)
//
//                super.onDestroy()
//            }
//        }
//    */
//
//}