package com.example.astrodashalib.view.modules.phoneVerification

import android.accounts.Account
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
//import com.facebook.accountkit.*
//import com.facebook.accountkit.ui.AccountKitActivity
import com.google.gson.Gson
import com.example.astrodashalib.*
import com.example.astrodashalib.R
import com.example.astrodashalib.data.models.UserModel
import com.example.astrodashalib.helper.*
import com.example.astrodashalib.view.ChatDetailActivity
import kotlinx.android.synthetic.main.activity_phone_verification.*
import kotlinx.android.synthetic.main.progress_layout.*
import java.util.*

class PhoneVerificationActivity : AppCompatActivity(), PhoneVerificationContract.View {

    var mPresenter: PhoneVerificationContract.Presenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone_verification)
        setPhoneVerificationShown(true)
        mPresenter = PhoneVerificationPresenter()
        mPresenter?.attachView(this)
        addNumberBtn.setOnClickListener {
            startAccountsKitPhoneVerification()
        }
        skipTv.setOnClickListener {
            proceedToChatView()
        }
    }

    fun startAccountsKitPhoneVerification() {
        /*val intent = Intent(this, AccountKitActivity::class.java)
        intent.setAccountKitPhoneConfiguration()
        startActivityForResult(intent, ACCOUNT_KIT_PHONE_REQUEST_CODE)*/
    }

    override fun onUserError() {
        toast("Verification Failed")
        proceedToChatView()
    }

    override fun onUserSuccess(userModel: UserModel) {
        val userKey = System.currentTimeMillis().toString() + "-" + UUID.randomUUID().toString()
        setUserId(userModel.id)
        setPhoneNumber(userModel.phone)
        setLatestUserShown(userKey)
        setUserModel(userKey, Gson().toJson(userModel))
        toast("Verified")
        proceedToChatView()
    }

    override fun showLoader() {
        progress_view_ll.visible()
        verifLl.gone()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            ACCOUNT_KIT_PHONE_REQUEST_CODE -> data?.let { handlePhoneVerificationResult(it) }
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun handlePhoneVerificationResult(data: Intent) {
        /*val loginResult = data.getParcelableExtra<AccountKitLoginResult>(AccountKitLoginResult.RESULT_KEY)
        if (!loginResult.wasCancelled() && loginResult.error == null) {
            AccountKit.getCurrentAccount(object : AccountKitCallback<Account> {
                override fun onSuccess(account: Account) {
                    val phoneNumber = account.phoneNumber
                    mPresenter?.getUser(phoneNumber.toString())
                }

                override fun onError(error: AccountKitError) {
                    toast("Verification Failed")
                    proceedToChatView()
                }
            })
        } else {
            toast("Verification Failed")
            proceedToChatView()
        }*/
    }

    private fun proceedToChatView() {
        ChatDetailActivity.createIntent(this).let {
            it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(it)
        }
    }

    companion object {
        @JvmField
        val ACCOUNT_KIT_PHONE_REQUEST_CODE = 5123

        @JvmStatic
        fun createIntent(context: Context?) : Intent{
            return Intent(context,PhoneVerificationActivity::class.java)
        }
    }
}
