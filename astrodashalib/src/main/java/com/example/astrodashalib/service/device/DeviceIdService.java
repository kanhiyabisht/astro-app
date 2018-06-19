package com.example.astrodashalib.service.device;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.example.astrodashalib.Constant;
import com.example.astrodashalib.R;
import com.example.astrodashalib.data.models.StatusModel;
import com.example.astrodashalib.data.models.UserModel;
import com.example.astrodashalib.faye.FayeChannelController;
import com.example.astrodashalib.helper.PrefGetter;
import com.example.astrodashalib.provider.rest.RestProvider;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;


public class DeviceIdService extends IntentService {
    public static final String TAG = DeviceIdService.class.getSimpleName();
    Context ctx;
    String regid;
    CompositeSubscription mCompositeSubscription = new CompositeSubscription();

    public static void startService(Context context) {
        Intent intent = new Intent(context, DeviceIdService.class);
        context.startService(intent);
    }

    public DeviceIdService() {
        super("DeviceIdService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            try {
                ctx = DeviceIdService.this;
//                regid = FirebaseInstanceId.getInstance().getToken();
                regid="4d8e6a8bc968f22cb850043d096ca250250932f8f93cfbfbb67433474da72f89";
                if (regid != null && !regid.equals(PrefGetter.getDeviceId(ctx.getApplicationContext()))) {
                    PrefGetter.setDeviceId(regid,ctx.getApplicationContext());
                    if (!PrefGetter.getUserId(ctx.getApplicationContext()).equals("-1"))
                        FayeChannelController.newInstance(ctx).refreshChannels();
                    /*String latestUserShown = PrefGetter.getUserId(ctx.getApplicationContext());
                    if (!latestUserShown.isEmpty()) {
                        String phoneNumber = PrefGetter.getPhoneNumber(ctx.getApplicationContext());
                        String userId = PrefGetter.getUserId(ctx.getApplicationContext());
                        if (!phoneNumber.isEmpty() && !userId.equals("-1"))
                            updateUserModel(latestUserShown, userId, getUpdatedUserModel(latestUserShown, phoneNumber));
                    }*/
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @NonNull
    private UserModel getUpdatedUserModel(String latestUserShown, String phoneNumber) {
        final UserModel localUserModel = new Gson().fromJson(PrefGetter.getUserModel(latestUserShown,ctx.getApplicationContext()), UserModel.class);
        localUserModel.deviceId = regid;
        localUserModel.phone = phoneNumber;
        localUserModel.id = PrefGetter.getUserId(ctx.getApplicationContext());
        return localUserModel;
    }

    public void updateUserModel(final String latestUserShown, String userId, final UserModel localUserModel) {
        mCompositeSubscription.add(RestProvider.getUserService().updateUser(Constant.KEY_VALUE, userId, new UserModel(localUserModel.deviceId, localUserModel.phone), userId)
                .subscribe(new Subscriber<StatusModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(StatusModel statusModel) {
                        updateUserModelLocally(latestUserShown, localUserModel.phone);
                        if (!PrefGetter.getUserId(ctx.getApplicationContext()).equals("-1"))
                            FayeChannelController.newInstance(ctx).refreshChannels();
                    }
                }));
    }


    public void updateUserModelLocally(String latestUserShown, String phoneNumber) {
        PrefGetter.setDeviceId(regid,ctx.getApplicationContext());
        final UserModel localUserModel = getUpdatedUserModel(latestUserShown, phoneNumber);
        PrefGetter.setUserModel(latestUserShown, new Gson().toJson(localUserModel),ctx.getApplicationContext());
    }

    @Override
    public void onDestroy() {
        try {
            super.onDestroy();
            mCompositeSubscription.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}