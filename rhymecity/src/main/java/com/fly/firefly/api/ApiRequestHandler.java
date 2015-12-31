package com.fly.firefly.api;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.fly.firefly.MainFragmentActivity;
import com.fly.firefly.api.obj.ChangePasswordReceive;
import com.fly.firefly.api.obj.ContactInfoReceive;
import com.fly.firefly.api.obj.DeviceInfoSuccess;
import com.fly.firefly.api.obj.FailedConnectToServer;
import com.fly.firefly.api.obj.ForgotPasswordReceive;
import com.fly.firefly.api.obj.LoginReceive;
import com.fly.firefly.api.obj.MobileCheckinReceive;
import com.fly.firefly.api.obj.PassengerInfoReveice;
import com.fly.firefly.api.obj.RegisterReceive;
import com.fly.firefly.api.obj.SearchFlightReceive;
import com.fly.firefly.api.obj.SeatSelectionReveice;
import com.fly.firefly.api.obj.SelectFlightReceive;
import com.fly.firefly.api.obj.ItineraryInfoReceive;
import com.fly.firefly.api.obj.TermsReceive;
import com.fly.firefly.api.obj.UpdateProfileReceive;
import com.fly.firefly.ui.object.ChangePasswordRequest;
import com.fly.firefly.ui.object.ContactInfo;
import com.fly.firefly.ui.object.DeviceInformation;
import com.fly.firefly.ui.object.ItineraryObj;
import com.fly.firefly.ui.object.LoginRequest;
import com.fly.firefly.ui.object.MobileCheckinObj;
import com.fly.firefly.ui.object.Passenger;
import com.fly.firefly.ui.object.PasswordRequest;
import com.fly.firefly.ui.object.RegisterObj;
import com.fly.firefly.ui.object.SearchFlightObj;
import com.fly.firefly.ui.object.SeatSelection;
import com.fly.firefly.ui.object.SelectFlight;
import com.fly.firefly.ui.object.TermsRequest;
import com.fly.firefly.ui.object.UpdateProfileRequest;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ApiRequestHandler {

    private final Bus bus;
    private final ApiService apiService;
    Context context;
    ProgressDialog mProgressDialog;
    public ApiRequestHandler(Bus bus, ApiService apiService) {
        this.bus = bus;
        this.apiService = apiService;
    }


    @Subscribe
    public void onLoginRequest(final LoginRequest event) {

        Log.e("Username", event.getUsername());
        Log.e("Password", event.getPassword());

       // initiateLoading();
       // loading(true);


        apiService.onRequestToLogin(event, new Callback<LoginReceive>() {

            @Override
            public void success(LoginReceive rhymesResponse, Response response) {


     Log.e("Success","OK");
               bus.post(new LoginReceive(rhymesResponse));
            }

            @Override
            public void failure(RetrofitError error) {

                bus.post(new FailedConnectToServer("Unable to connect to server"));
              //  loading(false);
            }

        });
    }



    @Subscribe
    public void onPasswordRequest(final PasswordRequest event) {

        Log.e("Email", event.getEmail());


       // initiateLoading();
        //loading(true);


        apiService.onRequestPassword(event, new Callback<ForgotPasswordReceive>() {

            @Override
            public void success(ForgotPasswordReceive rhymesResponse, Response response) {

                Log.e("Success", "OK");
                bus.post(new ForgotPasswordReceive(rhymesResponse));
                //loading(false);
            }

            @Override
            public void failure(RetrofitError error) {

                bus.post(new FailedConnectToServer("Unable to connect to server"));
               // loading(false);
            }

        });
    }

    @Subscribe
    public void onChangePasswordRequest(final ChangePasswordRequest event) {

        Log.e("Email", event.getEmail());
        Log.e("password", event.getNewPassword());
        Log.e("new_password", event.getCurrentPassword());


       // initiateLoading();
       //loading(true);


        apiService.onRequestChangePassword(event, new Callback<ChangePasswordReceive>() {

            @Override
            public void success(ChangePasswordReceive rhymesResponse, Response response) {

                Log.e("Success", "OK");
                bus.post(new ChangePasswordReceive(rhymesResponse));
               // loading(false);
            }

            @Override
            public void failure(RetrofitError error) {

                bus.post(new FailedConnectToServer("Unable to connect to server"));
               // loading(false);
            }

        });
    }

   /* @Subscribe
    public void onUpdateProfileRequest(final UpdateProfileRequest event) {

        Log.e("Email", event.getUsername());
        Log.e("password", event.getNewPassword());
        Log.e("new_password", event.getPassword());


        //initiateLoading();
        //loading(true);


        apiService.onRequestUpdateProfile(event, new Callback<UpdateProfileReceive>() {

            @Override
            public void success(UpdateProfileReceive rhymesResponse, Response response) {

                Log.e("Success", "OK");
                bus.post(new UpdateProfileReceive(rhymesResponse));
               // loading(false);
            }

            @Override
            public void failure(RetrofitError error) {

                bus.post(new FailedConnectToServer("Unable to connect to server"));
              //  loading(false);
            }

        });
    }*/




    @Subscribe
    public void onUpdateProfileRequest(final UpdateProfileRequest data) {

        // initiateLoading();
        // loading(true);


        apiService.onUpdateProfileRequest(data, new Callback<UpdateProfileReceive>() {

            @Override
            public void success(UpdateProfileReceive rhymesResponse, Response response) {

                Log.e("success", "True");
                bus.post(new UpdateProfileReceive(rhymesResponse));
                // loading(false);
            }

            @Override
            public void failure(RetrofitError error) {

                Log.e("Failed", "True");
                //loading(false);
                //bus.post(new RhymesFailureEvent(rhymesResponse));
            }

        });
    }

    /* Subscribe From HomePresenter - Send Device Information to server - ImalPasha */
    @Subscribe
    public void onDeviceInfo(final DeviceInformation event) {

        apiService.onSendDeviceInfo(event, new Callback<DeviceInfoSuccess>() {

            @Override
            public void success(DeviceInfoSuccess deviceResponse, Response response) {

                bus.post(new DeviceInfoSuccess(deviceResponse));

            }

            @Override
            public void failure(RetrofitError error) {

                bus.post(new FailedConnectToServer("Unable to connect to server"));

            }

        });
    }


    @Subscribe
    public void onRegisterRequest(final RegisterObj event) {

        apiService.onRegisterRequest(event, new Callback<RegisterReceive>() {

            @Override
            public void success(RegisterReceive rhymesResponse, Response response) {

                Log.e("Success", "True");
                bus.post(new RegisterReceive(rhymesResponse));
                // loading(false);
            }

            @Override
            public void failure(RetrofitError error) {

                Log.e("Failed", "True");

            }

        });
    }


    @Subscribe
    public void onSearchFlight(final SearchFlightObj event) {

        apiService.onSearchFlightRequest(event, new Callback<SearchFlightReceive>() {

            @Override
            public void success(SearchFlightReceive rhymesResponse, Response response) {
                Log.e(rhymesResponse.getStatus(), "x");
                bus.post(new SearchFlightReceive(rhymesResponse));
                // loading(false);

            }

            @Override
            public void failure(RetrofitError error) {

                Crouton.makeText(MainFragmentActivity.getContext(), "Unable to connect to server", Style.ALERT).show();
                Log.e("Failed", "True");
                //bus.post(new FailedConnectToServer("Unable to connect to server"));
                //loading(false);

            }

        });




    }

    @Subscribe
    public void onMobileCheckin(final MobileCheckinObj event) {

        //initiateLoading();
        //loading(true);

        apiService.onMobileCheckinRequest(event, new Callback<MobileCheckinReceive>() {

            @Override
            public void success(MobileCheckinReceive rhymesResponse, Response response) {
                Log.e(rhymesResponse.getStatus(), "x");
                bus.post(new MobileCheckinReceive(rhymesResponse));
                // loading(false);

            }

            @Override
            public void failure(RetrofitError error) {

                Crouton.makeText(MainFragmentActivity.getContext(), "Unable to connect to server", Style.ALERT).show();
                Log.e("Failed", "True");

            }

        });




    }

    @Subscribe
    public void onSearchFlight(final SelectFlight event) {

        apiService.onSelectFlight(event, new Callback<SelectFlightReceive>() {

            @Override
            public void success(SelectFlightReceive xx, Response response) {
                bus.post(new SelectFlightReceive(xx));
            }

            @Override
            public void failure(RetrofitError error) {
                Crouton.makeText(MainFragmentActivity.getContext(), "Unable to connect to server", Style.ALERT).show();
            }

        });
    }


    @Subscribe
    public void onPassengerInfo(final Passenger event) {

        apiService.onPassengerInfo(event, new Callback<PassengerInfoReveice>() {

            @Override
            public void success(PassengerInfoReveice responseData, Response response) {
                bus.post(new PassengerInfoReveice(responseData));

            }

            @Override
            public void failure(RetrofitError error) {

                //Crouton.makeText(MainFragmentActivity.getContext(), "Unable to connect to server", Style.ALERT).show();
                Log.e("Failed", error.getMessage());

            }

        });
    }

    @Subscribe
    public void onPassengerInfo(final ContactInfo event) {

        apiService.onContactInfo(event, new Callback<ContactInfoReceive>() {

            @Override
            public void success(ContactInfoReceive responseData, Response response) {

                bus.post(new ContactInfoReceive(responseData));

            }

            @Override
            public void failure(RetrofitError error) {

                Crouton.makeText(MainFragmentActivity.getContext(), "Unable to connect to server", Style.ALERT).show();
                Log.e("Failed", error.getMessage());

            }

        });
    }


    @Subscribe
    public void onTermsRequest(final TermsRequest data) {

        // initiateLoading();
        // loading(true);


        apiService.onTermsRequest(new Callback<TermsReceive>() {

            @Override
            public void success(TermsReceive rhymesResponse, Response response) {

                Log.e("success", "True");
                bus.post(new TermsReceive(rhymesResponse));
                // loading(false);
            }

            @Override
            public void failure(RetrofitError error) {

                Log.e("Failed", "True");
                //loading(false);
                //bus.post(new RhymesFailureEvent(rhymesResponse));
            }

        });
    }


    @Subscribe
    public void onSeatSelection(final SeatSelection event) {

        apiService.onSeatSelection(event, new Callback<SeatSelectionReveice>() {

            @Override
            public void success(SeatSelectionReveice responseData, Response response) {

                bus.post(new SeatSelectionReveice(responseData));

            }

            @Override
            public void failure(RetrofitError error) {

                Crouton.makeText(MainFragmentActivity.getContext(), "Unable to connect to server", Style.ALERT).show();
                Log.e("Failed", error.getMessage());

            }

        });
    }



    @Subscribe
    public void onItineraryRequest(final ItineraryObj event) {

        apiService.onItineraryRequest( new Callback<ItineraryInfoReceive>() {

            @Override
            public void success(ItineraryInfoReceive responseData, Response response) {

                bus.post(new ItineraryInfoReceive(responseData));

            }

            @Override
            public void failure(RetrofitError error) {

                Crouton.makeText(MainFragmentActivity.getContext(), "Unable to connect to server", Style.ALERT).show();
                Log.e("Failed", error.getMessage());

            }

        });
    }




























    /* ------------------------- Loading ------------------------- */

    /*public void initiateLoading(){

        context = MainFragmentActivity.getContext();
        mProgressDialog = new ProgressDialog(context);

    }

    public void loading(Boolean load){

        if(load){

            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setCancelable(true);
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.show();

        }else
        {
            if(mProgressDialog.isShowing()){
                mProgressDialog.dismiss();
            }
        }

    }*/

}

//apiService.onRequestToLogin(event.getUsername(),event.getPassword(), new Callback<LoginRequest>() {
//apiService.onRequestToLogin(123,"zhariffadam@me-tech.com.my","P@$$w0rd", new Callback<LoginRequest>() {
//apiService.getFeed2("adam", new Callback<LoginRequest>() {