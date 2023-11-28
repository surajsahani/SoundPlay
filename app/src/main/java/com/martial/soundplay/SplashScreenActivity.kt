package com.martial.soundplay

import android.app.Activity
import android.app.ProgressDialog.show
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.IntentSender
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.view.accessibility.AccessibilityEventCompat.setAction
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.InstallState
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.ActivityResult
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability

const val UPDATE_REQUEST_CODE = 524

class SplashScreenActivity : AppCompatActivity(), InstallStateUpdatedListener {

    lateinit var handler: Handler


    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { resultLauncher ->
            if (resultLauncher.resultCode == RESULT_OK) {
            }
        }
    private val appUpdateManager: AppUpdateManager by lazy {
        AppUpdateManagerFactory.create(applicationContext)
    }

//    private val appUpdateListener: InstallStateUpdatedListener by lazy {
//        InstallStateUpdatedListener { state ->
//            if (state.installStatus() == InstallStatus.DOWNLOADED) {
//                // show progress bar
//                showSnackbarForCompleteUpdate()
//                startNextActivity(MainActivity::class.java)
//            } else if (state.installStatus() == InstallStatus.INSTALLED) {
//                appUpdateManager.unregisterListener(this)
//            } else {
//                Log.d(TAG, "InstallStateUpdatedListener: state: " + state.installStatus());
//            }
//        }
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
    }

    override fun onResume() {
        super.onResume()
        checkUpdate()
//        appUpdateManager.appUpdateInfo.addOnSuccessListener { appUpdateInfo ->
//            if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
//                popupSnackbarForCompleteUpdate()
//            }
//
//            try {
//                if (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
//                    // If an in-app update is already running, resume the update.
//                    appUpdateManager.startUpdateFlowForResult(
//                        appUpdateInfo,
//                        AppUpdateType.IMMEDIATE,
//                        this,
//                        UPDATE_REQUEST_CODE
//                    )
//                }
//            } catch (e: IntentSender.SendIntentException) {
//                e.printStackTrace()
//            }
//        }
    }

    private fun checkUpdate() {

        val appUpdateInfoTask = appUpdateManager.appUpdateInfo

        appUpdateInfoTask.addOnSuccessListener {

//            val appUpdateInfoTask = appUpdateManager.appUpdateInfo
//            appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
//                if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
//                    if (appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
//                        appUpdateManager.startUpdateFlowForResult(
//                            appUpdateInfo,
//                            AppUpdateType.FLEXIBLE,
//                            this,
//                            UPDATE_REQUEST_CODE)
//                        appUpdateManager.registerListener(this)
//                    } else if (appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
//                        appUpdateManager.startUpdateFlowForResult(
//                            appUpdateInfo,
//                            AppUpdateType.IMMEDIATE,
//                            this,
//                            UPDATE_REQUEST_CODE)
//                        appUpdateManager.registerListener(this)
//                    }
//                }
//
//                if (appUpdateInfo.installStatus() == InstallStatus.INSTALLED) {
//                    popupSnackbarForState("An update has just been downloaded.", Snackbar.LENGTH_LONG)
//                }
//            }

            if (it.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                && it.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
            ) {
                appUpdateManager.startUpdateFlowForResult(
                    it,
                    this,
                    AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE)
                        .setAllowAssetPackDeletion(true).build(),
                    UPDATE_REQUEST_CODE
                )
                resultLauncher.launch(intent)

            } else {
                startNextActivity(MainActivity::class.java)
                Toast.makeText(this, "No Update Available", Toast.LENGTH_SHORT).show()
            }
        }
            .addOnFailureListener {
                startNextActivity(MainActivity::class.java)
                Toast.makeText(this, "Update Failed", Toast.LENGTH_SHORT).show()
            }
    }


    private fun startNextActivity(activity: Class<*>) {
        val intent = Intent(this, activity)
        startActivity(intent)
    }


//    private fun showSnackbarForCompleteUpdate() {
//        try {
//            Snackbar.make(
//                window.decorView.rootView,
//                "An update has just been downloaded.",
//                Snackbar.LENGTH_INDEFINITE
//            ).apply {
//                setAction("RESTART") {
//                    appUpdateManager.unregisterListener(appUpdateListener)
//                    appUpdateManager.completeUpdate()
//                }
//                show()
//            }
//
//        } catch (e: Exception) {
//        }
//    }

    private fun requestUserToInstallUpdate() {
        val alertDialog = AlertDialog.Builder(this).create()
        alertDialog.setTitle("Update available")
        alertDialog.setMessage("An update is available. Install now?")
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES") { _, _ ->
            appUpdateManager.completeUpdate()
        }
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No") { dialog, _ ->
            dialog.dismiss()
        }
        alertDialog.show()
    }

    override fun onStateUpdate(state: InstallState) {
        if (state.installStatus() == InstallStatus.DOWNLOADED) {
            // After the update is downloaded, show Snackbar
            // and request user confirmation to restart the app.
            popupSnackbarForCompleteUpdate()
        } else if (state.installStatus() == InstallStatus.INSTALLED) {
            popupSnackbarForState("An update has just been downloaded.", Snackbar.LENGTH_LONG)
            appUpdateManager.unregisterListener(this@SplashScreenActivity)
        }
    }

    private fun popupSnackbarForState(text: String, length: Int) {
        Snackbar.make(
            findViewById(R.id.textView),
            text,
            length
        ).show()
    }

    private fun popupSnackbarForCompleteUpdate() {
        Snackbar.make(
            findViewById(R.id.textView),
            "An update has just been downloaded from Play Store.",
            Snackbar.LENGTH_INDEFINITE
        ).apply {
            setAction("RESTART") {
                appUpdateManager.completeUpdate()
                appUpdateManager.unregisterListener(this@SplashScreenActivity)
            }
            show()
        }
    }

    override fun onDestroy() {
        appUpdateManager.unregisterListener(this)
        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == UPDATE_REQUEST_CODE) {
            when (resultCode) {
                Activity.RESULT_CANCELED -> {
                    popupSnackbarForState("You cancel for update new version.", Snackbar.LENGTH_SHORT)
                }
                ActivityResult.RESULT_IN_APP_UPDATE_FAILED -> {
                    popupSnackbarForState("App download failed.", Snackbar.LENGTH_SHORT)
                }
            }
        }
    }
}