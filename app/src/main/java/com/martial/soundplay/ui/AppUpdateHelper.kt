package com.martial.soundplay.ui

import android.app.Activity
import android.app.AlertDialog
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability

class AppUpdateHelper(private val activity: Activity) {
    private val appUpdateManager = AppUpdateManagerFactory.create(activity)
    private val updateType = AppUpdateType.FLEXIBLE
    private val updateRequestCode = 1001

    private val installStateUpdatedListener = InstallStateUpdatedListener { state ->
        if (state.installStatus() == InstallStatus.DOWNLOADED) {
            showUpdateDownloadedDialog()
        }
    }

    fun checkForUpdates() {
        appUpdateManager.registerListener(installStateUpdatedListener)
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo

        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE &&
                appUpdateInfo.isUpdateTypeAllowed(updateType)
            ) {
                appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    updateType,
                    activity,
                    updateRequestCode
                )
            }
        }
    }

    fun checkStatusOnResume() {
        appUpdateManager.appUpdateInfo.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                showUpdateDownloadedDialog()
            }
        }
    }

    fun onDestroy() {
        appUpdateManager.unregisterListener(installStateUpdatedListener)
    }

    private fun showUpdateDownloadedDialog() {
        AlertDialog.Builder(activity)
            .setTitle("Update Downloaded")
            .setMessage("A new update for SoundPlay is ready to install. Restart now to apply updates.")
            .setCancelable(false)
            .setPositiveButton("Restart") { _, _ ->
                appUpdateManager.completeUpdate()
            }
            .setNegativeButton("Later", null)
            .show()
    }
}
