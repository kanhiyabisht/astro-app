package com.example.astrodashalib.view.modules.pricingPlan

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Gravity
import com.example.astrodashalib.Constant
import com.example.astrodashalib.R
import com.example.astrodashalib.complain
import com.example.astrodashalib.data.models.*
import com.example.astrodashalib.helper.getEmail
import com.example.astrodashalib.helper.getPhoneNumber
import com.example.astrodashalib.helper.getUserId
import com.example.astrodashalib.model.CurrentAntardashaFalRequestBody
import com.example.astrodashalib.toast
import com.example.astrodashalib.utils.BaseConfiguration
import com.example.astrodashalib.view.adapter.SimpleDialogAdapter
import com.example.astrodashalib.view.modules.chat.ChatDetailActivity
import com.example.astrodashalib.view.modules.chat.ChatDetailActivity.Companion.STYLE
import com.example.astrodashalib.view.widgets.dialog.MaterialDialog
import com.example.astrodashalib.view.widgets.dialog.ProgressDialogFragment
import com.google.gson.Gson
import com.orhanobut.dialogplus.DialogPlus
import com.orhanobut.dialogplus.OnItemClickListener
import com.paytm.pgsdk.PaytmOrder
import com.paytm.pgsdk.PaytmPGService
import com.paytm.pgsdk.PaytmPaymentTransactionCallback
import kotlinx.android.synthetic.main.activity_pricing_plan.*
import java.text.SimpleDateFormat
import java.util.*

class PricingPlanActivity : AppCompatActivity(),PricingPlanContract.View {

    private var emailErrorDialog: MaterialDialog? = null
    var isProgressShowing = false
    var paymentList: ArrayList<DialogListOption> = arrayListOf(
            DialogListOption("Paytm", R.drawable.paytm_icon)
    )

    var mPresenter: PricingPlanContract.Presenter? = null
    var pricingPlan=0;

    var chatTxt:String="";

    override fun onCreate(savedInstanceState: Bundle?) {
        val style = intent.getIntExtra(ChatDetailActivity.STYLE, 0)
        setTheme(style)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pricing_plan)
        //setSupportActionBar(toolbar)
        mPresenter = PricingPlanPresenter()
        mPresenter?.attachView(this)
        chatTxt = intent.getStringExtra(CHAT_TXT) ?: ""
        cv1.setOnClickListener {
            pricingPlan=500
            showPaymentDialog()
        }

        cv2.setOnClickListener {
            pricingPlan=1200
            showPaymentDialog()
        }

        cv3.setOnClickListener {
            pricingPlan=2200
            showPaymentDialog()
        }
    }

    fun showPaymentDialog() {
        val dialog = DialogPlus.newDialog(this@PricingPlanActivity)
                .setHeader(R.layout.dialog_header)
                .setAdapter(SimpleDialogAdapter(this, paymentList))
                .setOnItemClickListener(paymentOnItemClickListener)
                .setGravity(Gravity.CENTER)
                .setExpanded(false)
                .create()
        dialog.show()

    }

    var paymentOnItemClickListener: OnItemClickListener = OnItemClickListener { dialog, item, view, position ->
        when (position) {
            0 -> launchPaytmFlow()
        }
        dialog.dismiss()
    }

    fun launchPaytmFlow() {
        //TODO : Change the way email is getting accessed
        if (getEmail(this).isEmpty()) {
            showEmailErrorDialog()
            return
        }
        getPaytmChecksum()
    }

    fun showEmailErrorDialog() {
        emailErrorDialog = MaterialDialog(this@PricingPlanActivity).apply {
            title("Error")
            message1("Email is required for payment", object : MaterialDialog.ItemClickListener {
                override fun onClick() {
                    dismiss()
                }
            })
            hideMessage2()
            addPositiveButton("OK") {
                dismiss()
            }
            show()
        }
    }

    fun getPaytmChecksum() {
        mPresenter?.getPaytmHash(PaytmHashRequestBody("0nf7" + System.currentTimeMillis(), getUserId(this), getPhoneNumber(this).replace("+91", ""), getIndTotalRemedyCost().toString(), getEmail(this)))
    }

    override fun onHashError() {
        toast("Paytm can't be opened right now")
    }


    override fun showLoader() {
        try {
            if (!isProgressShowing) {
                var fragment = supportFragmentManager.findFragmentByTag(ProgressDialogFragment.TAG) as ProgressDialogFragment?
                if (fragment == null) {
                    isProgressShowing = true
                    fragment = ProgressDialogFragment.newInstance()
                    fragment.show(supportFragmentManager, ProgressDialogFragment.TAG)
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun dismissLoader() {
        try {
            val fragment = supportFragmentManager.findFragmentByTag(ProgressDialogFragment.TAG) as ProgressDialogFragment?
            fragment?.let {
                isProgressShowing = false
                it.dismiss()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun postPaytmPaymentDetails(paytmPaymentDetails: PaytmPaymentDetails) {
        try {
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val date = sdf.parse(paytmPaymentDetails.txnDate)
            val userId = getUserId(this)
//            val mUserModel = Gson().fromJson(getUserModel(getLatestUserShown()), UserModel::class.java)
            //TODO get name from somewhere
//            val userName = mUserModel.userName
            val userName = ""
            val paymentDetail = PaymentDetail(getIndTotalRemedyCost().toString(), "", "", userName, Constant.PAYTM, "", userId,
                    chatTxt, Gson().toJson(paytmPaymentDetails, PaytmPaymentDetails::class.java), date.time.toString())

            mPresenter?.postPaymentDetails(paymentDetail, date.time, userId)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getIndTotalRemedyCost(): Double {
//        return (Constant.INDIA_REMEDY_PRICE).toDouble()
        return (pricingPlan).toDouble()
    }

    override fun onPaytmOrderStatusError(message: String) {
        try {
            complain(message)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun startPaytmSDK(paytmHashRequestBody: PaytmHashRequestBody, paytmHashResponse: PaytmHashResponse) {
        //TODO change before creating library
//        PaytmPGService.getProductionService()
        val Service = PaytmPGService.getStagingService()
        val paramMap = HashMap<String, String>()
        paramMap.apply {
            put(ChatDetailActivity.MID, BaseConfiguration.PAYTM_MERCHANT_ID)
            put(ChatDetailActivity.ORDER_ID, paytmHashRequestBody.orderId)
            put(ChatDetailActivity.CUST_ID, paytmHashRequestBody.customerId)
            put(ChatDetailActivity.INDUSTRY_TYPE_ID, BaseConfiguration.PAYTM_INDUSTRY_TYPE_ID)
            put(ChatDetailActivity.CHANNEL_ID, BaseConfiguration.PAYTM_CHANNEL_ID)
            put(ChatDetailActivity.TXN_AMOUNT, paytmHashRequestBody.txnAmt)
            put(ChatDetailActivity.WEBSITE, BaseConfiguration.PAYTM_WEBSITE)
            put(ChatDetailActivity.CALLBACK_URL, BaseConfiguration.PAYTM_CALLBACK_URL)
            put(ChatDetailActivity.EMAIL, paytmHashRequestBody.email)
            put(ChatDetailActivity.MOBILE_NO, paytmHashRequestBody.mobileNo)
            put(ChatDetailActivity.CHECKSUMHASH, paytmHashResponse.mChecksumhash)
        }

        val Order = PaytmOrder(paramMap)
        Service.initialize(Order, null)
        Service.startPaymentTransaction(this, true, true,
                object : PaytmPaymentTransactionCallback {

                    override fun someUIErrorOccurred(inErrorMessage: String) {
                        this@PricingPlanActivity.complain(inErrorMessage)
                    }

                    override fun onTransactionResponse(inResponse: Bundle) {
                        try {
                            Log.d("LOG", "Payment Transaction : " + inResponse)

                            val paytmPaymentDetails = PaytmPaymentDetails(inResponse)
                            when {
                                paytmPaymentDetails.respCode == "01" -> mPresenter?.getPaytmOrderStatus(PaytmOrderStatusBody(paytmPaymentDetails.orderId), paytmPaymentDetails)

                                paytmPaymentDetails.respMsg.isNotEmpty() -> {
                                    this@PricingPlanActivity.complain(paytmPaymentDetails.respMsg)
                                }

                                else -> this@PricingPlanActivity.complain("Transaction error")
                            }

                        } catch (e: Exception) {
                            e.printStackTrace()
                        }

                    }

                    override fun networkNotAvailable() {
                        this@PricingPlanActivity.complain("No internet connection")
                    }

                    override fun clientAuthenticationFailed(inErrorMessage: String) {
                        // 1. Server error or downtime. // 2. Server unable to
                        // generate checksum or checksum response is not in
                        // proper format. // 3. Server failed to authenticate
                        // that client. That is value of payt_STATUS is 2. //
                        this@PricingPlanActivity.complain(inErrorMessage)
                    }

                    override fun onErrorLoadingWebPage(iniErrorCode: Int, inErrorMessage: String, inFailingUrl: String) {
                        this@PricingPlanActivity.complain(inErrorMessage)

                    }

                    override fun onBackPressedCancelTransaction() {
                        this@PricingPlanActivity.complain("Back Pressed")
                    }

                    override fun onTransactionCancel(inErrorMessage: String, inResponse: Bundle) {
                        Log.d("LOG", "Payment Transaction Failed " + inErrorMessage)
                        this@PricingPlanActivity.complain(inErrorMessage)
                    }
                })
    }

    override fun onPaymentSuccess(paymentDetail: PaymentDetail, purchaseTimestamp: Long) {
        //use onActivityResult for sending success signal for sending msg
    }

    override fun onPaymentError(purchaseTimestamp: Long) {
        //use onActivityResult for sending success signal for saving msg as not paid
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter?.detachView()
    }

    companion object {

        @JvmField
        val MID = "MID"

        @JvmField
        val ORDER_ID = "ORDER_ID"

        @JvmField
        val CUST_ID = "CUST_ID"

        @JvmField
        val INDUSTRY_TYPE_ID = "INDUSTRY_TYPE_ID"

        @JvmField
        val CHANNEL_ID = "CHANNEL_ID"

        @JvmField
        val TXN_AMOUNT = "TXN_AMOUNT"

        @JvmField
        val WEBSITE = "WEBSITE"

        @JvmField
        val CALLBACK_URL = "CALLBACK_URL"

        @JvmField
        val CHECKSUMHASH = "CHECKSUMHASH"

        @JvmField
        val EMAIL = "EMAIL"

        @JvmField
        val MOBILE_NO = "MOBILE_NO"

        @JvmField
        val CHAT_TXT = "CHAT_TXT"

        @JvmField
        val STYLE = "style"

        @JvmStatic
        fun createIntent(context: Context?, style: Int, chatTxt: String): Intent {
            return Intent(context, ChatDetailActivity::class.java).apply {
                this.putExtra(STYLE, style)
                this.putExtra(CHAT_TXT,chatTxt)
            }
        }
    }
}
