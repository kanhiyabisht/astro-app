@file:JvmName("ExtensionsUtils")

package com.example.astrodashalib

import android.app.Activity
import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.content.DialogInterface
import android.content.Intent
import android.net.ConnectivityManager
import android.support.design.widget.Snackbar
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
//import com.facebook.accountkit.ui.AccountKitActivity
//import com.facebook.accountkit.ui.AccountKitConfiguration
//import com.facebook.accountkit.ui.LoginType
//import com.facebook.accountkit.ui.ThemeUIManager
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by himanshu on 23/09/17.
 */
fun View.visible() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun ViewGroup.inflate(layoutRes: Int, attachToRoot: Boolean = false) = LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)

fun Context?.toast(text: CharSequence, duration: Int = Toast.LENGTH_SHORT) = this?.let { Toast.makeText(it, text, duration).show() }

fun DrawerLayout.toggleDrawer(drawerGravity: Int) {
    drawerGravity.let {
        if (isDrawerOpen(it)) closeDrawer(it) else openDrawer(it)
    }
}

fun Activity.finishWithMessage(message : String){
    toast(message)
    finish()
}

fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

fun View.showKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    requestFocus()
    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)

}

fun Context.isNetworkAvailable(): Boolean {
    val connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as? ConnectivityManager
    val activeNetworkInfo = connectivityManager?.getActiveNetworkInfo()
    return activeNetworkInfo != null && activeNetworkInfo.isConnected
}

fun Context.complain(message: String) {
    val builder = AlertDialog.Builder(this, R.style.AlertDialogCustom)
    builder.setTitle("Error")
    builder.setMessage(message)
    builder.setPositiveButton("OK") { dialog, which -> dialog.dismiss() }
    builder.show()
}

fun View.showSnackbar(message: String, actionTxt: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_SHORT).setAction(actionTxt) {}.show()
}

fun Calendar.getDateTxt(pattern: String = "MMM dd, yyyy"): String {
    val sdf = SimpleDateFormat(pattern);
    sdf.timeZone = timeZone
    return sdf.format(time)
}

/*fun Intent.setAccountKitPhoneConfiguration() {
    val configurationBuilder = AccountKitConfiguration.AccountKitConfigurationBuilder(LoginType.PHONE, AccountKitActivity.ResponseType.TOKEN)
    configurationBuilder.apply {
        setDefaultCountryCode(Constant.DEFAULT_COUNTRY_CODE)
        setUIManager(ThemeUIManager(R.style.VerificationTheme))
    }
    putExtra(AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION, configurationBuilder.build())
}

fun Intent.setAccountKitEmailConfiguration() {
    val configurationBuilder = AccountKitConfiguration.AccountKitConfigurationBuilder(LoginType.EMAIL, AccountKitActivity.ResponseType.TOKEN)
    configurationBuilder.setUIManager(ThemeUIManager(R.style.VerificationTheme))
    putExtra(AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION, configurationBuilder.build())
}*/

fun <T> unSafeLazy(initializer: () -> T): Lazy<T> {
    return lazy(LazyThreadSafetyMode.NONE) {
        initializer()
    }
}

fun View.onClick(function: () -> Unit) {
    setOnClickListener {
        function()
    }
}

var View.scale: Float
    get() = Math.min(scaleX, scaleY)
    set(value) {
        scaleY = value
        scaleX = value
    }
