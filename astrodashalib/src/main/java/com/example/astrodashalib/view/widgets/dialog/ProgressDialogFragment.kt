package com.example.astrodashalib.view.widgets.dialog


import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import com.example.astrodashalib.R


class ProgressDialogFragment : DialogFragment() {


    @SuppressLint("RestrictedApi")
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val contextThemeWrapper = ContextThemeWrapper(context, context.theme)
        val themeAwareInflater = inflater?.cloneInContext(contextThemeWrapper)
        val view = themeAwareInflater?.inflate(R.layout.fragment_progress_dialog, container, false)
        return view
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setCancelable(false)
        isCancelable = false
        dialog.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            requestFeature(Window.FEATURE_NO_TITLE)
        }
        return dialog
    }

    companion object {

        @JvmField
        val TAG = "ProgressLoader"

        @JvmStatic
        fun newInstance(): ProgressDialogFragment = ProgressDialogFragment()
    }
}
