package com.example.astrodashalib.view.modules.birthForm

import android.accounts.Account
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker
import com.google.gson.Gson
import com.example.astrodashalib.*
import com.example.astrodashalib.R
import com.example.astrodashalib.data.models.Country
import com.example.astrodashalib.data.models.UserModel
import com.example.astrodashalib.helper.*
import com.example.astrodashalib.model.Place
import com.example.astrodashalib.view.ChatDetailActivity.Companion.ACCOUNT_KIT_PHONE_REQUEST_CODE
import com.example.astrodashalib.view.widgets.dialog.ProgressDialogFragment
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import kotlinx.android.synthetic.main.fragment_birth_detail.*
import java.text.SimpleDateFormat
import java.util.*


class BirthDetailFragment : Fragment(), android.app.DatePickerDialog.OnDateSetListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, BirthDetailContract.View {

    val mCountrySelected: Country by lazy {
        Constant.getIndiaCountryData()
    }
    var mPresenter: BirthDetailContract.Presenter? = null

    var isProgressShowing = false
    var localUserModel: UserModel? = null

    var day: String? = null
    var month: String? = null
    var year: String? = null
    var hour: String? = null
    var minute: String? = null

    var mPlaceName: String? = null
    var mLat: String? = null
    var mLng: String? = null

    var genderValue: Int? = null

    private var mListener: OnBirthDetailFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = container?.inflate(R.layout.fragment_birth_detail)
        mPresenter = BirthDetailPresenter()
        mPresenter?.attachView(this)
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        countryEt.apply {
            inputType = InputType.TYPE_NULL
            setText(mCountrySelected.country)
        }

        cityEt.apply {
            inputType = InputType.TYPE_NULL
            setOnClickListener {
                mListener?.onCityViewClick(mCountrySelected.countryCode)
            }
        }

        phoneNumberEt.apply {
            inputType = InputType.TYPE_NULL
            setOnClickListener {
                startAccountsKitPhoneVerification()
            }
        }
        localUserModel = Gson().fromJson(getUserModel(getLatestUserShown()), UserModel::class.java)
        localUserModel?.let {
            nameEt.setText(it.userName)
            when (it.gender) {
                1 -> radioGroup.check(R.id.maleBtn)
                2 -> radioGroup.check(R.id.femaleBtn)
            }

            this.year = it.year
            this.month = it.month
            this.day = it.day
            this.hour = it.hour
            this.minute = it.minute
            this.mPlaceName = it.city
            this.mLat = it.latitude
            this.mLng = it.longitude
            val calender: Calendar = GregorianCalendar()
            calender.set(it.year.toInt(),it.month.toInt() -1 , it.day.toInt())
            dob.setText(calender.getDateTxt())
            tob.setText("${it.hour} : ${it.minute}")
            cityEt.setText(it.city)
            phoneNumberEt.setText(it.phone)
            phoneNumberEt.setOnClickListener {
                context.toast("Phone number cannot be changed")
            }
        }




        dob.setOnClickListener {
            try {
                val calendar = Calendar.getInstance()
                val year = calendar.get(Calendar.YEAR)
                val month = calendar.get(Calendar.MONTH)
                val day = calendar.get(Calendar.DAY_OF_MONTH)
                when (Build.VERSION.SDK_INT == Build.VERSION_CODES.N) {
                    true -> {
                        val datePickerDialog = DatePickerDialog.newInstance(this, year, month, day)
                        datePickerDialog.show(activity.getFragmentManager(), DATE_PICKER_DIALOG)
                    }
                    else -> {
                        val datePickerDialog = android.app.DatePickerDialog(activity, android.R.style.Theme_Holo_Light_Dialog_MinWidth, this, year, month, day)
                        datePickerDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                        datePickerDialog.show()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        tob.setOnClickListener {
            try {
                val calendar = Calendar.getInstance()
                val timePickerDialog = TimePickerDialog(context, if (Build.VERSION.SDK_INT == Build.VERSION_CODES.N) android.R.style.Theme_Material_Light_Dialog_Alert else android.R.style.Theme_Holo_Light_Dialog_MinWidth
                        , this, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false)

                if (Build.VERSION.SDK_INT != Build.VERSION_CODES.N)
                    timePickerDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                timePickerDialog.show()
            } catch (e: Exception) {
                e.printStackTrace()
            }


        }

        submitIv.setOnClickListener {
            when {
                nameEt.text.toString().trim().isEmpty() -> context.toast("Enter your name")
                radioGroup.checkedRadioButtonId == -1 -> context.toast("Select gender")
                dob.text.toString().trim().isEmpty() -> context.toast("Enter your date of birth")
                tob.text.toString().trim().isEmpty() -> context.toast("Enter your time of birth")
                cityEt.text.toString().trim().isEmpty() -> context.toast("Enter place of birth")
                phoneNumberEt.text.toString().trim().isEmpty() -> context.toast("Enter your phone number")
                else -> {

                    genderValue = when (radioGroup.checkedRadioButtonId) {
                        R.id.maleBtn -> 1
                        R.id.femaleBtn -> 2
                        else -> null
                    }
                    localUserModel = Gson().fromJson(getUserModel(getLatestUserShown()), UserModel::class.java)

                    if (localUserModel != null) {
                        if (localUserModel?.userName == nameEt.text.toString().trim() && localUserModel?.gender == genderValue && localUserModel?.year == year && localUserModel?.month == month && localUserModel?.day == day && localUserModel?.hour == hour && localUserModel?.minute == minute && localUserModel?.city == mPlaceName)
                            context.toast("You have not change the data")
                        else
                            mPresenter?.updateUser(getUserId(), getUserModel())
                    } else
                        mPresenter?.getUser(phoneNumberEt.text.toString().trim())

                    mListener?.closeDrawer()
                }
            }
        }

        backIv.setOnClickListener {
            mListener?.closeDrawer()
        }
    }

    override fun onUserFound(userModel: UserModel) {
        mPresenter?.updateUser(userModel.id, getUserModel())
    }

    override fun onNoUserFound() {
        mPresenter?.registerUser(getUserModel())
    }

    fun getUserModel(): UserModel {
        return UserModel(null, "", nameEt.text.toString().trim(), day, month, year, hour, minute, mPlaceName,
                mCountrySelected.country, mLat, mLng, getDeviceId(), phoneNumberEt.text.toString().trim(), "", genderValue ?: 0)
    }

    override fun showLoader() {
        try {
            if (!isProgressShowing) {
                var fragment = activity.supportFragmentManager.findFragmentByTag(ProgressDialogFragment.TAG) as ProgressDialogFragment?
                if (fragment == null) {
                    isProgressShowing = true
                    fragment = ProgressDialogFragment.newInstance()
                    fragment.show(activity.supportFragmentManager, ProgressDialogFragment.TAG)
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onUserRegistrationSuccess(userModel: UserModel) {
        val userKey = System.currentTimeMillis().toString() + "-" + UUID.randomUUID().toString()
        setUserId(userModel.id)
        setPhoneNumber(userModel.phone)
        setLatestUserShown(userKey)
        setUserModel(userKey, Gson().toJson(userModel))

    }

    override fun onUserRegistrationError() {
        context.toast("User registration error")
    }

    override fun onUserUpdateSuccess(userModel: UserModel) {
        var userKey = getLatestUserShown()
        if (userKey.isEmpty()) {
            userKey = System.currentTimeMillis().toString() + "-" + UUID.randomUUID().toString()
            setLatestUserShown(userKey)
        }
        setUserId(userModel.id)
        setPhoneNumber(userModel.phone)
        setUserModel(userKey, Gson().toJson(userModel))
    }

    override fun onUserUpdateError() {
        context.toast("User update error")
    }

    override fun dismissLoader() {
        try {
            val fragment = activity.supportFragmentManager.findFragmentByTag(ProgressDialogFragment.TAG) as ProgressDialogFragment?
            fragment?.let {
                isProgressShowing = false
                it.dismiss()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        try {
            this.year = StringBuilder().append("").append(year).toString()
            this.month = StringBuilder().append("").append(month + 1).toString()
            this.day = StringBuilder().append("").append(dayOfMonth).toString()
            val calender: Calendar = GregorianCalendar()
            calender.set(year, month, dayOfMonth)
            dob.setText(calender.getDateTxt())
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun onDateSet(view: DatePickerDialog?, year: Int, month: Int, dayOfMonth: Int) {
        try {
            this.year = StringBuilder().append("").append(year).toString()
            this.month = StringBuilder().append("").append(month + 1).toString()
            this.day = StringBuilder().append("").append(dayOfMonth).toString()
            val calender = Calendar.getInstance()
            calender.set(year, month, dayOfMonth)
            val sdf = SimpleDateFormat("MMM dd, YYYY");
            sdf.timeZone = calender.timeZone
            dob.setText(sdf.format(calender.time))
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        this.hour = hourOfDay.toString()
        this.minute = minute.toString()
        tob.setText("$hour : $minute")
    }

    fun startAccountsKitPhoneVerification() {
        /*val intent = Intent(context, AccountKitActivity::class.java)
        intent.setAccountKitPhoneConfiguration()
        activity.startActivityForResult(intent, ACCOUNT_KIT_PHONE_REQUEST_CODE)*/
    }

    fun setCity(place: Place) {
        place.let {
            mPlaceName = it.place
//            mLat = it.lat
//            mLng = it.lon
            cityEt.setText(it.place)
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnBirthDetailFragmentInteractionListener) {
            mListener = context
            mListener?.onBirthViewAttach(this)
        } else
            throw RuntimeException(context?.toString() + " must implement OnFragmentInteractionListener")
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {

        when (requestCode) {
            ACCOUNT_KIT_PHONE_REQUEST_CODE -> handlePhoneVerificationResult(data)
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun handlePhoneVerificationResult(data: Intent) {
        /*val loginResult = data.getParcelableExtra<AccountKitLoginResult>(AccountKitLoginResult.RESULT_KEY)
        if (!loginResult.wasCancelled() && loginResult.error == null) {
            AccountKit.getCurrentAccount(object : AccountKitCallback<Account> {
                override fun onSuccess(account: Account) {
                    val phoneNumber = account.phoneNumber
                    phoneNumberEt.setText(phoneNumber.toString())
                }

                override fun onError(error: AccountKitError) {
                    context.toast("Verification Failed")
                }
            })
        } else
            context.toast("Verification Failed")*/
    }


    interface OnBirthDetailFragmentInteractionListener {
        fun onCityViewClick(countryCode: String)
        fun onBirthViewAttach(fragment: Fragment)
        fun closeDrawer()
    }

    companion object {

        @JvmField
        val DATE_PICKER_DIALOG = "DatePickerDialog"

        @JvmField
        val TAG = "Birth form"

        @JvmStatic
        fun newInstance(): BirthDetailFragment {
            val fragment = BirthDetailFragment()
            return fragment
        }
    }
}
