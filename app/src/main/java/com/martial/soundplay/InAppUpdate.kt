package com.martial.soundplay

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.snackbar.Snackbar

import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.ActivityResult.RESULT_IN_APP_UPDATE_FAILED
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability


class InAppUpdate : AppCompatActivity() {
    private var appUpdateManager: AppUpdateManager? = null
    private lateinit var textViewUpdateStatus: TextView
    private lateinit var progressBarUpdate: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_in_app_update)

        textViewUpdateStatus = findViewById(R.id.textViewUpdateStatus)
        progressBarUpdate = findViewById(R.id.progressBarUpdate)

        textViewUpdateStatus.text = "Checking for updates..."


        appUpdateManager = AppUpdateManagerFactory.create(this)
        checkUpdate()


    }

    private fun checkUpdate() {
        val appUpdateInfoTask = appUpdateManager?.appUpdateInfo
        appUpdateInfoTask?.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
            ) {
                textViewUpdateStatus.text = "Update available. Starting update..."
                appUpdateManager?.startUpdateFlowForResult(
                    appUpdateInfo,
                    AppUpdateType.IMMEDIATE,
                    this,
                    Companion.REQUEST_CODE
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        inProgressUpdate()
    }

    private fun inProgressUpdate(){
        val appUpdateInfoTask = appUpdateManager?.appUpdateInfo
        appUpdateInfoTask?.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS
                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
            ) {
                showProgressDialogOrSnackbar()
                appUpdateManager?.startUpdateFlowForResult(
                    appUpdateInfo,
                    AppUpdateType.IMMEDIATE,
                    this,
                    Companion.REQUEST_CODE
                )
            }
        }
    }

    private fun showProgressDialogOrSnackbar() {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Update in progress...")
        progressDialog.setCancelable(false)
        progressDialog.show()

        textViewUpdateStatus.text = "Update in progress..."
        progressBarUpdate.visibility = ProgressBar.VISIBLE

        /*Snackbar.make(
            findViewById(android.R.id.content),
            "Update in progress...",
            Snackbar.LENGTH_INDEFINITE
        ).show()
        */          //If you want to show snack-bar instead of progress dialog


    }

    companion object {
        private const val REQUEST_CODE = 100
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode== Companion.REQUEST_CODE){
           when(resultCode){
               RESULT_OK -> {
                   Toast.makeText(this,"Update Success",Toast.LENGTH_SHORT).show()
                   progressBarUpdate.visibility = ProgressBar.INVISIBLE
                   textViewUpdateStatus.text = "Update Success"
                   startActivity()

               }
               RESULT_CANCELED -> {
                   Toast.makeText(this,"Update Cancelled",Toast.LENGTH_SHORT).show()
                   startActivity()

               }
               RESULT_IN_APP_UPDATE_FAILED -> {
                   Toast.makeText(this,"Update Failed",Toast.LENGTH_SHORT).show()
               }
           }
        }
    }
    private fun startActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

}



