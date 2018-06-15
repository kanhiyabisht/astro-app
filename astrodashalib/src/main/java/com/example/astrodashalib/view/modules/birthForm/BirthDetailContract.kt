package com.example.astrodashalib.view.modules.birthForm

import com.example.astrodashalib.data.models.UserModel
import com.example.astrodashalib.view.base.BasePresenter
import com.example.astrodashalib.view.base.BaseView

/**
 * Created by himanshu on 26/09/17.
 */
interface BirthDetailContract {
    interface View : BaseView {

        fun showLoader()

        fun onUserRegistrationSuccess(userModel: UserModel)

        fun onUserRegistrationError()

        fun onUserUpdateSuccess(userModel: UserModel)

        fun onUserUpdateError()

        fun onUserFound(userModel :UserModel)

        fun onNoUserFound()

        fun dismissLoader()
    }

    interface Presenter : BasePresenter<View> {
        fun getUser(phoneNumber: String)

        fun registerUser(userModel: UserModel)

        fun updateUser(userId: String, userModel: UserModel)
    }
}