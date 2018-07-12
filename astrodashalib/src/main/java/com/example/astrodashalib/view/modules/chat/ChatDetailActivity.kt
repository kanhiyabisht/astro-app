package com.example.astrodashalib.view.modules.chat

import android.content.*
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.ActionMode
import android.view.Gravity
import android.view.View
import com.example.astrodashalib.*
import com.example.astrodashalib.chat.messageController.MessageControllerFactory
import com.example.astrodashalib.chat.messageController.MessageControllerInterface
import com.example.astrodashalib.data.GooglePaymentDetails
import com.example.astrodashalib.data.models.*
import com.example.astrodashalib.generic.GenericCallback
import com.example.astrodashalib.generic.GenericQueryCallback
import com.example.astrodashalib.helper.*
import com.example.astrodashalib.localDB.DbConstants
import com.example.astrodashalib.model.CurrentAntardashaFalRequestBody
import com.example.astrodashalib.service.device.DeviceIdService
import com.example.astrodashalib.service.faye.FayeIntentService
import com.example.astrodashalib.util.IabBroadcastReceiver
import com.example.astrodashalib.util.IabHelper
import com.example.astrodashalib.utils.BaseConfiguration.*
import com.example.astrodashalib.view.adapter.ChatAdapter
import com.example.astrodashalib.view.adapter.SimpleDialogAdapter
import com.example.astrodashalib.view.widgets.dialog.MaterialDialog
import com.example.astrodashalib.view.widgets.dialog.ProgressDialogFragment
import com.google.gson.Gson
import com.orhanobut.dialogplus.DialogPlus
import com.orhanobut.dialogplus.OnItemClickListener
import com.paytm.pgsdk.PaytmOrder
import com.paytm.pgsdk.PaytmPGService
import com.paytm.pgsdk.PaytmPaymentTransactionCallback
import kotlinx.android.synthetic.main.activity_chat_detail.*
import kotlinx.android.synthetic.main.progress_layout.*
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class ChatDetailActivity : AppCompatActivity(), ChatDetailContract.View, ChatAdapter.OnItemClickListener, ChatReciever.ChatRecieverInterface {

    var chatModelList: ArrayList<ChatModel> = ArrayList()
    var loginUserId: String? = null
    var chatAdapter: ChatAdapter? = null
    var broadcastRecieverHashMap: HashMap<String, ChatBroadcastInterface> = HashMap()
    var messageController: MessageControllerInterface? = null
    var currentAntardashaFalRequestBody: CurrentAntardashaFalRequestBody? = null
    var mInsertDates: InsertDates? = null
    var recentDateTxt = ""
    var reciever: ChatReciever? = null
    var isProgressShowing = false
    var mPresenter: ChatDetailContract.Presenter? = null
    var chatModel: ChatModel? = null
    var copyChatModel: ChatModel? = null
    private var copySendDialog: MaterialDialog? = null
    private var emailErrorDialog: MaterialDialog? = null
    var paymentList: ArrayList<DialogListOption> = arrayListOf(
            DialogListOption("Paytm", R.drawable.paytm_icon)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        val style = intent.getIntExtra(STYLE, 0)
        setTheme(style)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_detail)
        chatModelList = ArrayList()
        mPresenter = ChatDetailPresenter()
        mPresenter?.attachView(this)
        currentAntardashaFalRequestBody = intent.getSerializableExtra(ANTAR_DASHA_REQUEST_BODY) as CurrentAntardashaFalRequestBody?
        initaliseChatUsersData()
        loginUserId = getUserId(applicationContext)
        chatUserId = getCrmUserId(applicationContext)

        when {
            loginUserId.equals("-1") -> {
                progress_view_ll.gone()
                chatRecyclerView.gone()
            }
            chatUserId.isNullOrEmpty() -> {
                mPresenter?.getCrmUserId(getUserId(applicationContext))
                progress_view_ll.gone()
                chatRecyclerView.gone()
            }
            else -> {
                progress_view_ll.visible()
                chatRecyclerView.gone()
            }
        }

        try {
            messageController = MessageControllerFactory.getInstance(applicationContext)
            send_btn.setOnClickListener {
                if (loginUserId.equals("-1") || chatUserId.isNullOrEmpty()) {
                    toast("Fill birth details")
                } else if (chat_edit_text.text.toString().trim().isNotEmpty()) {
                    if (getFreeQuestionCount(applicationContext) >= 1) {
                        showPaymentDialog()
                        chatModel = messageController?.saveChat(chat_edit_text.text.toString(), chatUserId, "", "")
                        coordinator_ll.hideKeyboard()
                    } else {
                        messageController?.sendChat(chat_edit_text.text.toString(), chatUserId, "", "")
                        setFreeQuestionCount(1, applicationContext)
                    }

                    chat_edit_text.setText("")
                }
            }

            if (getUserModel(getUserId(applicationContext), applicationContext) != null)
                onUserDataChange()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun initaliseChatUsersData() {
        val userName = intent.getStringExtra(NAME) ?: ""
        val email = intent.getStringExtra(EMAIL) ?: ""
        val phone = intent.getStringExtra(MOBILE_NO) ?: ""
        val deviceId = "4d8e6a8bc968f22cb850043d096ca250250932f8f93cfbfbb67433474da72f89"
        val userId = "5b27580d1d0700650c78c5fe";
        val crmUserId = "5acc4629840d00b20dee2ba0";
        val userModel = UserModel(userId, "", userName, "", "", "", "", "", "", "", "", "", getDeviceId(applicationContext), phone, email, 1);
        setDeviceId(deviceId, applicationContext)
        setEmail(email, applicationContext)
        setPhoneNumber(phone, applicationContext)
        setUserId(userId, applicationContext)
        setCrmUserId(crmUserId, applicationContext)
        setUserModel(userId, Gson().toJson(userModel), applicationContext);
    }

    fun onUserDataChange() {
        loginUserId = getUserId(applicationContext)

        if (getCrmUserId(applicationContext).isEmpty())
            mPresenter?.getCrmUserId(getUserId(applicationContext))
        else
            startFayeService()
    }

    fun showPaymentDialog() {
        val dialog = DialogPlus.newDialog(this@ChatDetailActivity)
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
        emailErrorDialog = MaterialDialog(this@ChatDetailActivity).apply {
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

    override fun startPaytmSDK(paytmHashRequestBody: PaytmHashRequestBody, paytmHashResponse: PaytmHashResponse) {
        //TODO change before creating library
//        PaytmPGService.getProductionService()
        val Service = PaytmPGService.getStagingService()
        val paramMap = HashMap<String, String>()
        paramMap.apply {
            put(MID, PAYTM_MERCHANT_ID)
            put(ORDER_ID, paytmHashRequestBody.orderId)
            put(CUST_ID, paytmHashRequestBody.customerId)
            put(INDUSTRY_TYPE_ID, PAYTM_INDUSTRY_TYPE_ID)
            put(CHANNEL_ID, PAYTM_CHANNEL_ID)
            put(TXN_AMOUNT, paytmHashRequestBody.txnAmt)
            put(WEBSITE, PAYTM_WEBSITE)
            put(CALLBACK_URL, PAYTM_CALLBACK_URL)
            put(EMAIL, paytmHashRequestBody.email)
            put(MOBILE_NO, paytmHashRequestBody.mobileNo)
            put(CHECKSUMHASH, paytmHashResponse.mChecksumhash)
        }

        val Order = PaytmOrder(paramMap)
        Service.initialize(Order, null)
        Service.startPaymentTransaction(this, true, true,
                object : PaytmPaymentTransactionCallback {

                    override fun someUIErrorOccurred(inErrorMessage: String) {
                        this@ChatDetailActivity.complain(inErrorMessage)
                    }

                    override fun onTransactionResponse(inResponse: Bundle) {
                        try {
                            Log.d("LOG", "Payment Transaction : " + inResponse)

                            val paytmPaymentDetails = PaytmPaymentDetails(inResponse)
                            when {
                                paytmPaymentDetails.respCode == "01" -> mPresenter?.getPaytmOrderStatus(PaytmOrderStatusBody(paytmPaymentDetails.orderId), paytmPaymentDetails)

                                paytmPaymentDetails.respMsg.isNotEmpty() -> {
                                    this@ChatDetailActivity.complain(paytmPaymentDetails.respMsg)
                                }

                                else -> this@ChatDetailActivity.complain("Transaction error")
                            }

                        } catch (e: Exception) {
                            e.printStackTrace()
                        }

                    }

                    override fun networkNotAvailable() {
                        this@ChatDetailActivity.complain("No internet connection")
                    }

                    override fun clientAuthenticationFailed(inErrorMessage: String) {
                        // 1. Server error or downtime. // 2. Server unable to
                        // generate checksum or checksum response is not in
                        // proper format. // 3. Server failed to authenticate
                        // that client. That is value of payt_STATUS is 2. //
                        this@ChatDetailActivity.complain(inErrorMessage)
                    }

                    override fun onErrorLoadingWebPage(iniErrorCode: Int, inErrorMessage: String, inFailingUrl: String) {
                        this@ChatDetailActivity.complain(inErrorMessage)

                    }

                    override fun onBackPressedCancelTransaction() {
                        this@ChatDetailActivity.complain("Back Pressed")
                    }

                    override fun onTransactionCancel(inErrorMessage: String, inResponse: Bundle) {
                        Log.d("LOG", "Payment Transaction Failed " + inErrorMessage)
                        this@ChatDetailActivity.complain(inErrorMessage)
                    }
                })
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
                    chat_edit_text.text.toString(), Gson().toJson(paytmPaymentDetails, PaytmPaymentDetails::class.java), date.time.toString())

            mPresenter?.postPaymentDetails(paymentDetail, date.time, userId)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onPaytmOrderStatusError(message: String) {
        try {
            complain(message)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getIndTotalRemedyCost(): Double {
        return (Constant.INDIA_REMEDY_PRICE).toDouble()
    }

    override fun onPaymentSuccess(paymentDetail: PaymentDetail, purchaseTimestamp: Long) {
        //send chat
        chatModel?.let { messageController?.sendChat(it) }
    }

    override fun onPaymentError(purchaseTimestamp: Long) {
        //save chat as not paid
        chatModel?.let { messageController?.sendChat(it) }
    }

    override fun onStart() {
        super.onStart()
        inBackground = false
        if (!loginUserId.equals("-1") && !chatUserId.isNullOrEmpty())
            updateRecievedChatStatus(true, true)
        else {
            chatModelList = ArrayList()
            val list: ArrayList<ChatModel> = ArrayList()
            executeInsertDatesTask(list, false, false)
        }
    }

    fun updateRecievedChatStatus(isScreenOpenFirstTime: Boolean, shouldFetchList: Boolean) {
        try {
            val timestamp = DateTimeUtil.getCurrentTimestampSeconds()
            val chatModel = ChatModel()
            chatModel.chatStatus = DbConstants.STATUS_READ
            chatModel.readTimestamp = java.lang.Double.valueOf(timestamp.toDouble())
            chatModel.isIncoming = true
            messageController?.updateRecievedChatStatus(chatModel, loginUserId, chatUserId, GenericCallback { error, model ->
                messageController?.sendReadTimestamp(loginUserId, chatUserId)
                if (shouldFetchList)
                    fetchChatFromDb(isScreenOpenFirstTime)
            })

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun fetchChatFromDb(isScreenOpenFirstTime: Boolean) {
        try {
            chatModelList = ArrayList()
            messageController?.fetchChatsOfTwoUser(loginUserId, chatUserId, object : GenericQueryCallback<ChatModel> {

                override fun callback(error: JSONObject, list: ArrayList<ChatModel>) {
                    Log.e("chatList", list.size.toString())
                    if (list.isNotEmpty()) {
                        setFreeQuestionCount(1, applicationContext)
                    }
                    executeInsertDatesTask(list, isScreenOpenFirstTime, true)
                }
            })

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


    fun executeInsertDatesTask(list: ArrayList<ChatModel>, isScreenOpenFirstTime: Boolean, isUserLogin: Boolean) {
        try {
            mInsertDates = InsertDates(list, isScreenOpenFirstTime, this, isUserLogin)
            mInsertDates?.execute()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    class InsertDates(val chatArrayList: ArrayList<ChatModel>, val isScreenOpenFirstTime: Boolean, var chatDetailActivity: ChatDetailActivity?, val isUserLogin: Boolean) : AsyncTask<Void, Void, Void>() {

        var initialChatArrayList: ArrayList<ChatModel> = ArrayList<ChatModel>()
        var finalChatArrayList: ArrayList<ChatModel> = ArrayList<ChatModel>()
        var chatListHashMap: LinkedHashMap<String, ArrayList<ChatModel>> = LinkedHashMap();
        var lastChatDate: String = ""


        override fun doInBackground(vararg params: Void?): Void? {
            var result: Void? = null
            try {
                this.initialChatArrayList.clear();
                this.initialChatArrayList.addAll(chatArrayList);
                val todayDate: Date = Date()
                val simpleDateFormat: SimpleDateFormat = SimpleDateFormat("MMMM dd");
                val todayDateText: String = simpleDateFormat.format(todayDate);

                if (isUserLogin) {
                    if (initialChatArrayList.isEmpty()) {
                        val welcomeChatModel = ChatModel.getWelcomeChatModel(DateTimeUtil.getCurrentTimestampSeconds().toDouble(), chatDetailActivity?.loginUserId)
                        initialChatArrayList.add(0, welcomeChatModel)

                        val loginChatModel = ChatModel.getLoginChatModel(DateTimeUtil.getCurrentTimestampSeconds().toDouble(), chatDetailActivity?.loginUserId)
                        initialChatArrayList.add(1, loginChatModel)

                    } else {
                        Collections.sort(initialChatArrayList) { lhs, rhs -> if (lhs.sentTimestamp < rhs.sentTimestamp) -1 else 1 }
                        val timestamp = initialChatArrayList[0].sentTimestamp
                        val welcomeChatModel = ChatModel.getWelcomeChatModel(timestamp, chatDetailActivity?.loginUserId)
                        initialChatArrayList.add(0, welcomeChatModel)

                        val loginChatModel = ChatModel.getLoginChatModel(timestamp, chatDetailActivity?.loginUserId)
                        initialChatArrayList.add(1, loginChatModel)
                    }
                } else {
                    val timestamp = DateTimeUtil.getCurrentTimestampSeconds().toDouble()
                    val welcomeChatModel = ChatModel.getWelcomeChatModel(timestamp, chatDetailActivity?.loginUserId)
                    initialChatArrayList.add(welcomeChatModel)
                }


                initialChatArrayList.forEach { chatModel ->

                    if (!isCancelled) {
                        val timestampSec: Long = chatModel.sentTimestamp.toLong() * 1000

                        var chatDateText: String = getDateText(simpleDateFormat, timestampSec);
                        chatDateText = if (chatDateText.equals(todayDateText)) DbConstants.TODAY else chatDateText;
                        lastChatDate = chatDateText;

                        var chatModelArrayList: ArrayList<ChatModel> = ArrayList<ChatModel>();
                        chatListHashMap.get(chatDateText)?.let { chatModelArrayList = it };
                        chatModelArrayList.add(chatModel);
                        chatListHashMap.put(chatDateText, chatModelArrayList)
                    } else
                        return result
                }

                for ((chatDate, chatList) in chatListHashMap) {
                    if (!isCancelled) {
                        val chat = ChatModel(chatDate);
                        finalChatArrayList.add(chat);
                        finalChatArrayList.addAll(chatList);
                    } else
                        return result
                }

            } catch (e: Exception) {
                e.printStackTrace();
            }
            return result
        }

        fun isChatUnread(chatModel: ChatModel): Boolean = chatModel.chatStatus != null && chatModel.chatStatus == DbConstants.STATUS_UNREAD


        fun getDateText(simpleDateFormat: SimpleDateFormat, timestampSec: Long): String {
            val chatDate = Date(timestampSec);
            return simpleDateFormat.format(chatDate);
        }

        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)

            try {
                chatDetailActivity?.let {
                    it.recentDateTxt = lastChatDate;
                    it.setChatDetailRecyclerView(finalChatArrayList, isScreenOpenFirstTime);
                    if (isScreenOpenFirstTime)
                        it.getReadTimestamp()
                }
            } catch (e: Exception) {
                e.printStackTrace();
            }
        }
    }


    fun getReadTimestamp() {
        messageController?.getReadTimestamp(chatUserId, loginUserId, GenericCallback { error, `object` -> fetchChatFromDb(false) })
    }

    override fun onItemClicked(view: View, position: Int) {
        copyChatModel = chatModelList[position]
        showCopySendDialog(copyChatModel)
    }

    fun showCopySendDialog(chatModel: ChatModel?) {
        copySendDialog = MaterialDialog(this@ChatDetailActivity).apply {
            title("Message Options")
            message1("Copy", object : MaterialDialog.ItemClickListener {
                override fun onClick() {
                    val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    clipboardManager.primaryClip = ClipData.newPlainText("text label", chatModel?.value
                            ?: "")
                }
            })
            if (chatModel?.chatStatus == DbConstants.STATUS_NOT_PAID)
                message2("Resend", object : MaterialDialog.ItemClickListener {
                    override fun onClick() {
                        try {
                            showPaymentDialog()
                            this@ChatDetailActivity.chatModel = copyChatModel
                            coordinator_ll.hideKeyboard()
                        } catch (e: Exception) {

                        }
                    }
                })
            else hideMessage2()
            hideBtn()
            show()
        }
    }

    fun setChatDetailRecyclerView(finalChatArrayList: ArrayList<ChatModel>, isFirstTime: Boolean) {
        chatModelList = ArrayList()
        chatModelList.addAll(finalChatArrayList);
        if (chatAdapter != null)
            notifyDataSetChangedToAdpater(chatAdapter!!)
        else
            initializeChatRecyclerView(this)


        chatRecyclerView.visible()
        progress_view_ll.gone()
    }

    fun addChatToScreen(chatModel: ChatModel?) {
        try {
            val bool = !recentDateTxt.equals(DbConstants.TODAY) && updateDateTextAndChatModelList()
            chatModel?.let {
                chatModelList.add(it)
                if (it.isIncoming)
                    updateRecievedChatStatus(false, false)
            }



            notifyDataSetChangedToAdpater(chatAdapter)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun updateDateTextAndChatModelList(): Boolean {
        recentDateTxt = DbConstants.TODAY
        chatModelList.add(ChatModel(DbConstants.TODAY))
        return true
    }

    fun initializeChatRecyclerView(onItemClickListener: ChatAdapter.OnItemClickListener) {
        chatRecyclerView.setHasFixedSize(true);
        val linearLayoutManager = LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManager.setStackFromEnd(true);
        chatRecyclerView.setLayoutManager(linearLayoutManager);
        chatAdapter = ChatAdapter(this, onItemClickListener);
        chatAdapter?.setData(chatModelList);
        chatRecyclerView.setAdapter(chatAdapter);
    }

    fun notifyDataSetChangedToAdpater(chatAdapter: ChatAdapter?) {
        chatAdapter?.let {
            it.setData(chatModelList);
            it.notifyDataSetChanged();
        }
        chatRecyclerView.setVisibility(View.VISIBLE);
        chatRecyclerView.smoothScrollToPosition(chatModelList.size - 1);
    }

    override fun onResume() {
        super.onResume()
        broadcastRecieverHashMap.put(ACTION_REFRESH_CHAT_STATUS, RefreshChatStatus())
        broadcastRecieverHashMap.put(ACTION_ADD_NEW_CHAT, AddNewChat())
        broadcastRecieverHashMap.put(ACTION_NETWORK_CHANGE, NetworkChange())
        broadcastRecieverHashMap.put(ACTION_REFRESH_CHAT_LIST, RefreshChatScreen())
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            broadcastRecieverHashMap.put(CONNECTIVITY_ACTION, ConnectivityChange())

        reciever = ChatReciever(this);
        val filter = IntentFilter()
        filter.apply {
            addAction(ACTION_REFRESH_CHAT_STATUS)
            addAction(ACTION_ADD_NEW_CHAT)
            addAction(ACTION_NETWORK_CHANGE)
            addAction(ACTION_REFRESH_CHAT_LIST)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                addAction(CONNECTIVITY_ACTION)
        }
        registerReceiver(reciever, filter);
    }

    override fun execute(context: Context, intent: Intent) {
        try {
            broadcastRecieverHashMap[intent.action]?.exec(this@ChatDetailActivity, this, intent.extras)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun onStop() {
        super.onStop()
        inBackground = true
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

    override fun startFayeService() {
        if (getDeviceId(applicationContext).isEmpty())
            DeviceIdService.startService(this)
        chatUserId = getCrmUserId(applicationContext)
        FayeIntentService.startFayeService(this)
        updateRecievedChatStatus(true, true)
    }


    override fun onCrmUserError() {
        if (getDeviceId(applicationContext).isEmpty())
            DeviceIdService.startService(this)
    }

    override fun setCrmId(id: String) {
        setCrmUserId(id, applicationContext)
    }



    override fun onUserUpdateSuccess(userModel: UserModel) {
        setEmail(userModel.email, applicationContext)
        setUserModel(getUserId(applicationContext), Gson().toJson(userModel), applicationContext)
    }

    override fun onUserUpdateError() {
        toast("Email Verification failed")
    }

    override fun onDestroy() {
        try {
            copySendDialog?.dismiss()
            mPresenter?.detachView()
            mInsertDates?.let {
                it.chatDetailActivity = null
                it.cancel(false)
            }

//            stopService(Intent(this, FayeService::class.java))
            unregisterReceiver(reciever)
            super.onDestroy();
        } catch (e: Exception) {
            e.printStackTrace();
        }
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
        var chatUserId: String? = null

        @JvmField
        val TAG = ChatDetailActivity::class.java.simpleName

        @JvmField
        val ACTION_REFRESH_CHAT_LIST = "RefreshChatList"

        @JvmField
        val ACTION_REFRESH_CHAT_STATUS = "RefreshChat"

        @JvmField
        val ACTION_ADD_NEW_CHAT = "AddChat"

        @JvmField
        val ACTION_NETWORK_CHANGE = "NetworkChange"

        @JvmField
        val CONNECTIVITY_ACTION = "android.net.conn.CONNECTIVITY_CHANGE"

        @JvmField
        var inBackground = true

        @JvmField
        val RC_REQUEST = 10001

        @JvmField
        val STYLE = "style"

        @JvmField
        val NAME = "name"

        @JvmField
        val ANTAR_DASHA_REQUEST_BODY = "antarDashaRequestBody"


        @JvmStatic
        fun createIntent(context: Context?, style: Int, email: String, phoneNumber: String, userName: String, currentAntardashaFalRequestBody: CurrentAntardashaFalRequestBody): Intent {
            return Intent(context, ChatDetailActivity::class.java).apply {
                this.putExtra(STYLE, style)
                this.putExtra(EMAIL, email)
                this.putExtra(MOBILE_NO, phoneNumber)
                this.putExtra(NAME, userName)
                this.putExtra(ANTAR_DASHA_REQUEST_BODY, currentAntardashaFalRequestBody)
            }
        }

        @JvmStatic
        fun createIntent(context: Context?): Intent {
            return Intent(context, ChatDetailActivity::class.java)
        }
    }

}
