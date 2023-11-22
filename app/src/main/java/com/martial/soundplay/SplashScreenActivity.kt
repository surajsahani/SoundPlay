package com.martial.soundplay

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import kotlin.coroutines.jvm.internal.CompletedContinuation.context

const val UPDATE_REQUEST_CODE = 524

class SplashScreenActivity : AppCompatActivity() {

    lateinit var handler: Handler

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { resultLauncher ->
            if (resultLauncher.resultCode == RESULT_OK) {
            }
        }
    private val appUpdateManager: AppUpdateManager by lazy {
        AppUpdateManagerFactory.create(applicationContext)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        checkUpdate()
    }

    private fun checkUpdate() {
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo
        appUpdateInfoTask.addOnSuccessListener {
            if (it.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE && it.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {

                appUpdateManager.startUpdateFlowForResult(
                    it,
                    this,
                    AppUpdateOptions.newBuilder(AppUpdateType.FLEXIBLE)
                        .setAllowAssetPackDeletion(true)
                        .build(),
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
}