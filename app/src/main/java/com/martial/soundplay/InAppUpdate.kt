package com.martial.soundplay

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.snackbar.Snackbar

import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability


class InAppUpdate : AppCompatActivity() {
    private lateinit var appUpdateManager: AppUpdateManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_in_app_update)

        appUpdateManager = AppUpdateManagerFactory.create(this)
        checkUpdates()
    }

    private fun checkUpdates() {

        // Returns an intent object that you use to check for an update.
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo

        // Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            //update availability condition below code //means if update available
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                // This example applies an immediate update. To apply a flexible update
                // instead, pass in AppUpdateType.FLEXIBLE
                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
            ) {
                // Request the update.
//                appUpdateManager.startUpdateFlowForResult(
//                    appUpdateInfo,
//                    activityResultLauncher, //passing activity result launcher
//                    AppUpdateOptions.newBuilder(AppUpdateType.FLEXIBLE)
////                        .setAllowAssetPackDeletion(true)   //if you want to delete old version from all aceess use true other vise you dont need to clear old version then remove this line
//                        .build()
//                )
            }
        }
        appUpdateManager.registerListener(listener)
    }

    private val listener = InstallStateUpdatedListener { state ->
        //check the condition
        if (state.installStatus() == InstallStatus.DOWNLOADED) {
            popupSnakebarForCompleteUpdate()
        }
    }

    private val activityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { result: androidx.activity.result.ActivityResult ->
        if (result.resultCode != RESULT_OK) {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
        }
    }

    fun popupSnakebarForCompleteUpdate() {
        Snackbar.make(
            findViewById(R.id.button),
            "An update has just been downloaded.",
            Snackbar.LENGTH_INDEFINITE
        ).apply {
            setAction("Install") { appUpdateManager.completeUpdate() }
                .setActionTextColor(getColor(android.R.color.holo_blue_dark))
            show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        appUpdateManager.unregisterListener(listener)
    }

    override fun onResume() {
        super.onResume()
        appUpdateManager.appUpdateInfo.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                popupSnakebarForCompleteUpdate()
            }
        }
    }
}


