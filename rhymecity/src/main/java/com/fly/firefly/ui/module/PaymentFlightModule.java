package com.fly.firefly.ui.module;

import com.fly.firefly.AppModule;
import com.fly.firefly.ui.fragment.BookingFlight.PaymentFlightFragment;
import com.fly.firefly.ui.presenter.BF_PaymentFlightPresenter;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        injects = PaymentFlightFragment.class,
        addsTo = AppModule.class,
        complete = false
)
public class PaymentFlightModule {

    private final BF_PaymentFlightPresenter.PaymentFlightView paymentFlightView;

    public PaymentFlightModule(BF_PaymentFlightPresenter.PaymentFlightView paymentFlightView) {
        this.paymentFlightView = paymentFlightView;
    }

    @Provides
    @Singleton
    BF_PaymentFlightPresenter provideSearchFlightPresenter(Bus bus) {
        return new BF_PaymentFlightPresenter(paymentFlightView, bus);
    }
}
