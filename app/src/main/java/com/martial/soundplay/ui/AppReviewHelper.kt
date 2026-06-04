package com.martial.soundplay.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.google.android.play.core.review.ReviewManagerFactory

object AppReviewHelper {
    private const val PREFS_NAME = "review_prefs"
    private const val KEY_LAUNCH_COUNT = "launch_count"
    private const val KEY_REVIEW_PROMPTED = "review_prompted"

    fun incrementLaunchCount(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val count = prefs.getInt(KEY_LAUNCH_COUNT, 0)
        prefs.edit().putInt(KEY_LAUNCH_COUNT, count + 1).apply()
    }

    fun checkAndShowReviewPrompt(activity: Activity) {
        val prefs = activity.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val count = prefs.getInt(KEY_LAUNCH_COUNT, 0)
        val alreadyPrompted = prefs.getBoolean(KEY_REVIEW_PROMPTED, false)

        // Show automatically on or after the 3rd launch if not prompted before
        if (count >= 3 && !alreadyPrompted) {
            launchInAppReview(activity) {
                prefs.edit().putBoolean(KEY_REVIEW_PROMPTED, true).apply()
            }
        }
    }

    fun launchInAppReview(activity: Activity, onComplete: (() -> Unit)? = null) {
        val manager = ReviewManagerFactory.create(activity)
        val request = manager.requestReviewFlow()
        request.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val reviewInfo = task.result
                val flow = manager.launchReviewFlow(activity, reviewInfo)
                flow.addOnCompleteListener {
                    onComplete?.invoke()
                }
            } else {
                // Fallback to Play Store detail page if review flow fails or is blocked by quota
                openPlayStore(activity)
                onComplete?.invoke()
            }
        }
    }

    fun openPlayStore(activity: Activity) {
        val appId = activity.packageName
        try {
            activity.startActivity(
                Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$appId")).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
            )
        } catch (e: Exception) {
            activity.startActivity(
                Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$appId")).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
            )
        }
    }
}
