package com.martialcoder.soundplay

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.play.core.appupdate.AppUpdateInfo


class InAppUpdate : AppCompatActivity() {
    private lateinit var appUpdateManager:AppUpdateManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_in_app_update)

        appUpdateManager = AppUpdateManagerFactory.create(this)
        checkUpdates()
    }
    private fun checkUpdates(){
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo
        appUpdateInfoTask.addOnSuccessListener { appApdateInfo ->
            //update availability condition below code //means if update available
            if(appApdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                &&  appApdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)){
                appUpdateManager.startUpdateFlowForResult(
                    appApdateInfo,
                    activityResultLauncher, //passing activity result launcher
                    AppUpdateOptions.newBuilder(AppUpdateType.FLEXIBLE)
//                        .setAllowAssetPackDeletion(true)   //if you want to delete old version from all aceess use true other vise you dont need to clear old version then remove this line
                        .build()
                )
            }
        }
        appUpdateManager.registerListener(listener)
    }

    private val listener= InstallStateUpdatedListener{state ->
        //check the condition
        if (state.installStatus() == InstallStatus.DOWNLOADED){
            popupSnakebarForCompleteUpdate()
        }
    }

    private  val activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()
    ){ result :androidx.activity.result.ActivityResult ->
        if (result.resultCode != RESULT_OK) {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
        }
    }
    fun popupSnakebarForCompleteUpdate(){
        Snackbar.make(
            findViewById(R.id.button),
            "An update has just been downloaded.",
            Snackbar.LENGTH_INDEFINITE
        ).apply {
            setAction("Install"){appUpdateManager.completeUpdate() }
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
        appUpdateManager.appUpdateInfo.addOnSuccessListener {appUpdateInfo ->
            if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED){
                popupSnakebarForCompleteUpdate()
            }
        }
    }
}


