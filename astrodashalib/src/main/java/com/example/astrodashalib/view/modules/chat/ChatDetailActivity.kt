package com.example.astrodashalib.view.modules.chat

import android.app.FragmentTransaction
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.ActionMode
import android.view.Gravity
import android.view.View

import com.google.gson.Gson
import com.example.astrodashalib.*
import com.example.astrodashalib.R
import com.example.astrodashalib.chat.messageController.MessageControllerFactory
import com.example.astrodashalib.chat.messageController.MessageControllerInterface
import com.example.astrodashalib.data.GooglePaymentDetails
import com.example.astrodashalib.data.models.*
import com.example.astrodashalib.generic.GenericCallback
import com.example.astrodashalib.generic.GenericQueryCallback
import com.example.astrodashalib.helper.*
import com.example.astrodashalib.localDB.DbConstants
import com.example.astrodashalib.service.device.DeviceIdService
import com.example.astrodashalib.service.faye.FayeIntentService
import com.example.astrodashalib.service.faye.FayeService
import com.example.astrodashalib.util.IabBroadcastReceiver
import com.example.astrodashalib.util.IabHelper
import com.example.astrodashalib.view.SampleQuestionListFragment
import com.example.astrodashalib.view.adapter.ChatAdapter
import com.example.astrodashalib.view.adapter.SimpleDialogAdapter
import com.example.astrodashalib.view.widgets.dialog.MaterialDialog
import com.example.astrodashalib.view.widgets.dialog.ProgressDialogFragment
import com.orhanobut.dialogplus.DialogPlus
import com.orhanobut.dialogplus.OnItemClickListener
import kotlinx.android.synthetic.main.activity_chat_detail.*
import kotlinx.android.synthetic.main.app_bar_chat_detail.*
import kotlinx.android.synthetic.main.progress_layout.*
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class ChatDetailActivity : AppCompatActivity(), ChatDetailContract.View, SampleQuestionListFragment.OnFragmentInteractionListener, ChatAdapter.OnItemClickListener, ChatReciever.ChatRecieverInterface {

    var chatModelList: ArrayList<ChatModel> = ArrayList()
    var loginUserId: String? = null
    var mHelper: IabHelper? = null
    var mBroadcastReceiver: IabBroadcastReceiver? = null
    var isSetupFinished = false
    var chatAdapter: ChatAdapter? = null
    var broadcastRecieverHashMap: HashMap<String, ChatBroadcastInterface> = HashMap()
    var messageController: MessageControllerInterface? = null
    var currentListItemIndex: Int = 0
    private val currentActionMode: ActionMode? = null
    var mInsertDates: InsertDates? = null
    var recentDateTxt = ""
    var reciever: ChatReciever? = null
    var isProgressShowing = false
    var mPresenter: ChatDetailContract.Presenter? = null
    var chatModel: ChatModel? = null
    var copyChatModel: ChatModel? = null
    private var dialog: MaterialDialog? = null
    var paymentList: ArrayList<DialogListOption> = arrayListOf(
            DialogListOption("Google Wallet", R.drawable.google_wallet_icon),
            DialogListOption("Paytm", R.drawable.paytm_icon)
    )

    var messageActionList: ArrayList<DialogListOption> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_detail)
        setSupportActionBar(toolbar)
        chatModelList = ArrayList()
        mPresenter = ChatDetailPresenter()
        mPresenter?.attachView(this)
        setupIabHelper()

        profileIv.setOnClickListener {
            drawerLayout.toggleDrawer(GravityCompat.END)
        }


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
                    if (getFreeQuestionCount(applicationContext) != 1) {
                        showPaymentDialog()
                        chatModel = messageController?.saveChat(chat_edit_text.text.toString(), chatUserId, "", "")
                        coordinator_ll.hideKeyboard()
                    } else {
                        messageController?.sendChat(chat_edit_text.text.toString(), chatUserId, "", "")
                        setFreeQuestionCount(0,applicationContext)
                    }

                    chat_edit_text.setText("")
                }
            }

            if (getUserModel(getLatestUserShown(applicationContext),applicationContext) != null)
                onUserDataChange()

        } catch (e: Exception) {
            e.printStackTrace()
        }
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
            0 -> {
                launchGoogleWalletFlow()
            }
        }
        dialog.dismiss()
    }

    fun setupIabHelper() {
        if (AppHelper.isGooglePlayServicesAvailable(this)) {
            val base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEArCve41bRnl/ElsJZBTlsNO2PP7DGY7u+BBrcKkL+VLNaNV7MH1mOnQDX5bvKI7hwPUt26OZP3/v5PZUXKA2NY8FP9nvydaIWO0XhVaxCxX5DWcw3nTD+b7/F3U14y7E6LJ1JkXAkoaYlYQ2ictlG7XQkVed6epMhuqXgQBR5ebFs3KAQ4KRarCRMz5rxMv+LKWXYlbAQdYKOGBkeKfEPwtTVFnQoMo5cpSPuHLK8NNpUfOAwv9Z1Fsyr9mvP1CTJ7bq9JHXpzBSyFMbvEXUNR8SeHDYFImLtLLfozjNw6PpN15QaBlL7OIHjmcOKyRLuaCMcAfhR5R6ZkZjspdZMIwIDAQAB"
            mHelper = IabHelper(this, base64EncodedPublicKey)
            mHelper?.let {
                it.enableDebugLogging(false)
                it.startSetup(mOnIabSetupFinishedListener)
            }
        } else
            complain("Google play service not available")
    }

    var mOnIabSetupFinishedListener: IabHelper.OnIabSetupFinishedListener = IabHelper.OnIabSetupFinishedListener { result ->
        Log.d(TAG, "Setup finished.")

        if (!result.isSuccess) {
            isSetupFinished = false
            complain("Problem setting up in-app billing: " + result)
            return@OnIabSetupFinishedListener
        }
        isSetupFinished = true

        if (mHelper == null) return@OnIabSetupFinishedListener

        mBroadcastReceiver = IabBroadcastReceiver(mIabBroadcastListener)
        registerReceiver(mBroadcastReceiver, IntentFilter(IabBroadcastReceiver.ACTION))
        Log.d(TAG, "Setup successful. Querying inventory.")
        try {
            mHelper?.queryInventoryAsync(mGotInventoryListener)
        } catch (e: IabHelper.IabAsyncInProgressException) {
            complain("Error querying inventory. Another async operation in progress.")
        }
    }

    var mIabBroadcastListener: IabBroadcastReceiver.IabBroadcastListener = IabBroadcastReceiver.IabBroadcastListener { }

    var mGotInventoryListener: IabHelper.QueryInventoryFinishedListener = IabHelper.QueryInventoryFinishedListener { result, inventory ->
        try {
            Log.d(TAG, "Query inventory finished.")

            if (mHelper == null) return@QueryInventoryFinishedListener

            if (result.isFailure) {
                complain("Failed to query inventory: " + result)
                return@QueryInventoryFinishedListener
            }

            Log.d(TAG, "Query inventory was successful.")
            Constant.upayIdHashMap.values.forEach { value ->
                val gasPurchase = inventory.getPurchase(value)
                gasPurchase?.let {
                    try {
                        mHelper?.consumeAsync(it, mConsumeFinishedListener)
                    } catch (e: IabHelper.IabAsyncInProgressException) {
                        complain("Error consuming gas. Another async operation in progress.")
                    }

                    return@QueryInventoryFinishedListener
                }
            }

            Log.d(TAG, "Initial inventory query finished; enabling main UI.")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    var mConsumeFinishedListener: IabHelper.OnConsumeFinishedListener = IabHelper.OnConsumeFinishedListener { purchase, result ->
        try {
            Log.d(TAG, "Consumption finished. Purchase: $purchase, result: $result")

            if (mHelper == null) return@OnConsumeFinishedListener

            if (result.isSuccess) {
                Log.d(TAG, "Consumption successful. Provisioning.")
                val googlePaymentDetails = GooglePaymentDetails(purchase)
                val userId = getUserId(applicationContext)
                val mUserModel = Gson().fromJson(getUserModel(getLatestUserShown(applicationContext),applicationContext), UserModel::class.java)
                val userName = mUserModel.userName
                val paymentDetail = PaymentDetail(getIndTotalRemedyCost().toString(), "", "", userName, Constant.GOOGLE_WALLET, "", userId,
                        chat_edit_text.text.toString(), Gson().toJson(googlePaymentDetails, GooglePaymentDetails::class.java), googlePaymentDetails.mPurchaseTime.toString())
                mPresenter?.postPaymentDetails(paymentDetail, googlePaymentDetails.mPurchaseTime,getUserId(applicationContext))
            } else {
                complain("Error while consuming: " + result)
            }

            Log.d(TAG, "End consumption flow.")

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun launchGoogleWalletFlow() {
        val payload = ""
        try {
            if (isSetupFinished)
                mHelper?.launchPurchaseFlow(this, Constant.upayIdHashMap[1], RC_REQUEST, mPurchaseFinishedListener, payload)
            else
                complain("In app billing not setup")
        } catch (e: IabHelper.IabAsyncInProgressException) {
            complain("Error launching purchase flow. Another async operation in progress.")
        } catch (e: Exception) {
            complain("Error in getting product from play store")
        }
    }

    var mPurchaseFinishedListener: IabHelper.OnIabPurchaseFinishedListener = IabHelper.OnIabPurchaseFinishedListener { result, purchase ->
        try {
            Log.d(TAG, "Purchase finished: $result, purchase: $purchase")

            if (mHelper == null) return@OnIabPurchaseFinishedListener

            if (result.isFailure) {
                complain("Error purchasing: " + result)
                return@OnIabPurchaseFinishedListener
            }


            if (purchase.sku == Constant.upayIdHashMap[1]) {
                try {
                    mHelper?.consumeAsync(purchase, mConsumeFinishedListener)
                } catch (e: IabHelper.IabAsyncInProgressException) {
                    complain("Error consuming gas. Another async operation in progress.")
                    return@OnIabPurchaseFinishedListener
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getIndTotalRemedyCost(): Double {
        return (Constant.INDIA_REMEDY_PRICE).toDouble()
    }

    override fun onHashError() {
        toast("Paytm can't be opened right now")
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
                    Log.e("chatList",list.size.toString())
                    if (list.isNotEmpty()) {
                        setFreeQuestionCount(0,applicationContext)
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
        messageActionList = arrayListOf(DialogListOption("Copy", null))
        if (copyChatModel?.chatStatus == DbConstants.STATUS_NOT_PAID)
            messageActionList.add(DialogListOption("Resend", null))
        showCopySendDialog2(copyChatModel)
    }

    fun showCopySendDialog2(chatModel: ChatModel?) {
        dialog = MaterialDialog(this@ChatDetailActivity).apply {
            title("Message Options")
            message1("Copy", object : MaterialDialog.ItemClickListener {
                override fun onClick() {
                    val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val clipData = android.content.ClipData.newPlainText("text label", chatModel?.value ?: "")
                    clipboardManager.primaryClip = clipData
                }
            })
            if (chatModel?.chatStatus == DbConstants.STATUS_NOT_PAID)
                message2("Resend", object : MaterialDialog.ItemClickListener {
                    override fun onClick() {
                        try {
                            showPaymentDialog()
                            this@ChatDetailActivity.chatModel = copyChatModel
                            coordinator_ll.hideKeyboard()
                        }catch (e :Exception){

                        }
                    }
                })
            else hideMessage2()
            hideBtn()
            show()
        }
    }


    fun showCopySendDialog() {
        val dialog = DialogPlus.newDialog(this)
                .setHeader(R.layout.dialog_header_1)
                .setAdapter(SimpleDialogAdapter(this, messageActionList))
                .setOnItemClickListener(OnItemClickListener)
                .setGravity(Gravity.CENTER)
                .setExpanded(false)
                .create()
        dialog.show()

    }

    var OnItemClickListener: OnItemClickListener = OnItemClickListener { dialog, item, view, position ->
        when (position) {
            0 -> {
                val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clipData = android.content.ClipData.newPlainText("text label", copyChatModel?.value ?: "")
                clipboardManager.primaryClip = clipData
            }
            1 -> {
                showPaymentDialog()
                chatModel = copyChatModel
                coordinator_ll.hideKeyboard()
            }
        }
        dialog.dismiss()
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

    override fun setCrmId(id:String){
        setCrmUserId(id,applicationContext)
    }


    override fun closeDrawer() {
        onBackPressed()
    }

    override fun onBackPressed() {
        when {
            drawerLayout.isDrawerOpen(GravityCompat.START) -> drawerLayout.closeDrawer(GravityCompat.START)
            drawerLayout.isDrawerOpen(GravityCompat.END) -> {

                if (supportFragmentManager.backStackEntryCount > 0)
                    supportFragmentManager.popBackStack()
                else
                    drawerLayout.closeDrawer(GravityCompat.END)
            }
            else -> super.onBackPressed()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        mHelper?.handleActivityResult(requestCode, resultCode, data)
    }


    override fun onUserUpdateSuccess(userModel: UserModel) {
        setEmail(userModel.email,applicationContext)
        setUserModel(getLatestUserShown(applicationContext), Gson().toJson(userModel),applicationContext)
    }

    override fun onUserUpdateError() {
        toast("Email Verification failed")
    }

    override fun onDestroy() {
        try {
            dialog?.dismiss()
            mPresenter?.detachView()
            mInsertDates?.let {
                it.chatDetailActivity = null
                it.cancel(false)
            }

            stopService(Intent(this, FayeService::class.java))
            unregisterReceiver(reciever)
            super.onDestroy();
        } catch (e: Exception) {
            e.printStackTrace();
        }
    }


    companion object {


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


        @JvmStatic
        fun createIntent(context: Context?): Intent {
            return Intent(context, ChatDetailActivity::class.java)
        }
    }

}
