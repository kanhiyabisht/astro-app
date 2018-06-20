package com.example.astrodashalib.util

import android.app.Activity
import android.content.DialogInterface
import android.support.v7.app.AlertDialog
import com.example.astrodashalib.R

/**
 * Created by himanshu on 05/10/17.
 */
object DialogUtils {

    fun showSmsRationaleDialog(activity: Activity, smsDialogInterface: SmsDialogInterface) {
        AlertDialog.Builder(activity, R.style.AlertDialogCustom)
                .setMessage(activity.getString(R.string.sms_permission_rationale))
                .setPositiveButton(android.R.string.ok) { dialogInterface, i -> smsDialogInterface.onOkClick() }
                .setNegativeButton(android.R.string.cancel) { dialogInterface, i -> smsDialogInterface.onCancelClick() }
                .create()
                .show()
    }
}