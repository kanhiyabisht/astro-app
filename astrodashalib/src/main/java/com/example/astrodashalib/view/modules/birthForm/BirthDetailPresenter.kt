package com.example.astrodashalib.view.modules.birthForm

import android.util.Log
import com.example.astrodashalib.Constant
import com.example.astrodashalib.data.models.UserModel
import com.example.astrodashalib.helper.getUserId
import com.example.astrodashalib.provider.rest.RestProvider
import com.example.astrodashalib.view.modules.birthForm.BirthDetailContract
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription

/**
 * Created by himanshu on 26/09/17.
 */
class BirthDetailPresenter : BirthDetailContract.Presenter {

    var mView: BirthDetailContract.View? = null
    val mCompositeSubscription = CompositeSubscription()

    override fun attachView(view: BirthDetailContract.View) {
        mView = view
    }

    override fun detachView() {
        mView = null
        mCompositeSubscription.clear()
    }

    override fun getUser(phoneNumber: String) {
        mView?.showLoader()
        mCompositeSubscription.add(RestProvider.getUserService().getUser(Constant.KEY_VALUE, phoneNumber, getUserId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ model ->
                    try {
                        if (model?.id != null)
                            mView?.let {
                                it.dismissLoader()
                                it.onUserFound(model)
                            }
                        else
                            mView?.let {
                                it.dismissLoader()
                                it.onNoUserFound()
                            }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }, { e ->
                    try {
                        mView?.let {
                            it.dismissLoader()
                            it.onUserRegistrationError()
                        }
                        Log.e(TAG, "onError: " + e.message)
                    } catch (e1: Exception) {
                        e1.printStackTrace()
                    }

                }))
    }

    override fun registerUser(userModel: UserModel) {

        mView?.showLoader()
        mCompositeSubscription.add(RestProvider.getUserService().registerUser(Constant.KEY_VALUE, userModel, getUserId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ model ->
                    try {
                        userModel.id = model.id
                        mView?.let {
                            it.dismissLoader()
                            it.onUserRegistrationSuccess(userModel)
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }, { e ->
                    try {
                        mView?.let {
                            it.dismissLoader()
                            it.onUserRegistrationError()
                        }
                        Log.e(TAG, "onError: " + e.message)
                    } catch (e1: Exception) {
                        e1.printStackTrace()
                    }

                }))
    }

    override fun updateUser(userId: String, userModel: UserModel) {

        mView?.showLoader()
        mCompositeSubscription.add(RestProvider.getUserService().updateUser(Constant.KEY_VALUE, userId, userModel, userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ statusModel ->
                    try {
                        mView?.let {
                            it.dismissLoader()
                            userModel.id = userId
                            it.onUserUpdateSuccess(userModel)
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }


                }, { e ->
                    mView?.let {
                        it.dismissLoader()
                        it.onUserUpdateError()
                    }
                }))
    }

    companion object {
        val TAG = "BirthPresenter"
    }
}