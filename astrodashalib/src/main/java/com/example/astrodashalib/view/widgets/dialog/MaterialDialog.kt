package com.example.astrodashalib.view.widgets.dialog

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.Dialog
import android.content.Context
import android.support.v4.view.animation.FastOutSlowInInterpolator
import android.view.View
import android.view.ViewPropertyAnimator
import android.widget.Button
import android.widget.TextView
import com.example.astrodashalib.*
import kotlinx.android.synthetic.main.dialog_item.*

/**
 * Created by himanshu on 27/10/17.
 */
class MaterialDialog(context: Context) : Dialog(context, R.style.MaterialDialogSheet) {

    private val titleText by unSafeLazy {
        findViewById(R.id.title) as TextView
    }
    private val messageText1 by unSafeLazy {
        findViewById(R.id.message1) as TextView
    }

    private val messageText2 by unSafeLazy {
        findViewById(R.id.message2) as TextView
    }
    private val positiveButton by unSafeLazy {
        findViewById(R.id.positiveButton) as Button
    }

    init {
        setContentView(R.layout.dialog_item)
        setCancelable(true)
        setCanceledOnTouchOutside(true)
        dialogContainer.onClick { hide() }
    }

    override fun onDetachedFromWindow() {
        super.dismiss()
        super.onDetachedFromWindow()
    }

    infix fun title(title: String): MaterialDialog {
        titleText.text = title
        return this
    }

    fun message1(message: String,onItemClickListener: ItemClickListener): MaterialDialog {
        messageText1.apply {
            text = message
            onClick {
                onItemClickListener.onClick()
                hide()
            }
        }
        return this
    }

    fun message2(message: String,onItemClickListener: ItemClickListener): MaterialDialog {
        messageText2.apply {
            text = message
            onClick {
                onItemClickListener.onClick()
                hide()
            }
        }
        return this
    }

    fun hideMessage2(): MaterialDialog {
        messageText2.gone()
        return this
    }

    fun hideBtn(): MaterialDialog {
        positiveButton.gone()
        return this
    }

    fun addPositiveButton(text: String, action: MaterialDialog.() -> Unit): MaterialDialog {
        positiveButton.text = text.toUpperCase()
        positiveButton.onClick {
            action()
        }
        return this
    }

    override fun show() {
        super.show()
        val view = findViewById(R.id.container) as View
        with(view) {
            alpha = 0F
            scale = .85F
            animate()
                    .scale(1F)
                    .alpha(1F)
                    .setDuration(120)
                    .setInterpolator(FastOutSlowInInterpolator())
                    .withLayer()
                    .start()
        }
    }

    override fun hide() {
        val view = findViewById(R.id.container) as View
        with(view) {
            animate()
                    .scale(.85F)
                    .alpha(0F)
                    .setDuration(110)
                    .setInterpolator(FastOutSlowInInterpolator())
                    .withLayer()
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator?) {
                            this@MaterialDialog.dismiss()
                        }
                    })
                    .start()
        }
    }

    private fun ViewPropertyAnimator.scale(scale: Float): ViewPropertyAnimator {
        scaleX(scale)
        scaleY(scale)
        return this
    }

    interface ItemClickListener{
        fun onClick()
    }
}