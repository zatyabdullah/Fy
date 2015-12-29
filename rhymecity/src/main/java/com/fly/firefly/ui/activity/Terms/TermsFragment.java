package com.fly.firefly.ui.activity.Terms;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.fly.firefly.AnalyticsApplication;
import com.fly.firefly.FireFlyApplication;
import com.fly.firefly.R;
import com.fly.firefly.api.obj.TermsReceive;
import com.fly.firefly.base.BaseFragment;
import com.fly.firefly.ui.activity.FragmentContainerActivity;
import com.fly.firefly.ui.activity.Homepage.HomeActivity;
import com.fly.firefly.ui.module.TermsModule;
import com.fly.firefly.ui.presenter.TermsPresenter;
import com.fly.firefly.utils.SharedPrefManager;
import com.google.android.gms.analytics.Tracker;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;

import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class TermsFragment extends BaseFragment implements TermsPresenter.TermsView,Validator.ValidationListener  {

    // Validator Attributes
    private Validator mValidator;
    private Tracker mTracker;
    private static final String SCREEN_LABEL = "Terms & Condition";
    private SharedPrefManager pref;
    private int fragmentContainerId;

    @Inject TermsPresenter presenter;

    @InjectView(R.id.terms1) TextView terms1;
    @InjectView(R.id.terms2) TextView terms2;
    @InjectView(R.id.terms3) TextView terms3;
    @InjectView(R.id.terms4) TextView terms4;
    @InjectView(R.id.terms5) TextView terms5;
    @InjectView(R.id.terms6) TextView terms6;
    @InjectView(R.id.terms7) TextView terms7;
    //@InjectView(R.id.terms8) TextView terms8;


    @InjectView(R.id.terms_1) TextView terms_1;
    @InjectView(R.id.terms_2) TextView terms_2;
    @InjectView(R.id.terms_3) TextView terms_3;
    @InjectView(R.id.terms_4) TextView terms_4;
    @InjectView(R.id.terms_5) TextView terms_5;
    @InjectView(R.id.terms_6) TextView terms_6;
    @InjectView(R.id.terms_7) TextView terms_7;
  //  @InjectView(R.id.terms_8) TextView terms_8;

    public static TermsFragment newInstance() {

        TermsFragment fragment = new TermsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FireFlyApplication.get(getActivity()).createScopedGraph(new TermsModule(this)).inject(this);
        // Validator
        mValidator = new Validator(this);
        mValidator.setValidationListener(this);
        mValidator.setValidationMode(Validator.Mode.BURST);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.terms, container, false);
        ButterKnife.inject(this, view);

        presenter.onUpdateTerms();
       // pref = new SharedPrefManager(getActivity());





        /*Display Data*/
    /* JSONArray jsonTerm = getTermInfo(getActivity());

        //JSONArray jTerm_content = jsonTerm.getJSONArray();

       // for (int i = 0; i < jsonTerm.length(); i++)
       // {
            JSONObject term = (JSONObject) jsonTerm.opt(0);

            String id = term.optString("id");
            String term_title = term.optString("title");
            String content = term.optString("body");

            terms1.setText(term_title);
            terms_1.setText(Html.fromHtml(content));*/
        //}

        return view;
    }




    public void onSuccessUpdate(TermsReceive obj) {

        Log.e("Update", "success");
        if (obj.getStatus().equals("success")) {


            terms1.setText(obj.getTerm().get(0).getTitle());
            terms2.setText(obj.getTerm().get(1).getTitle());
            terms3.setText(obj.getTerm().get(2).getTitle());
            terms4.setText(obj.getTerm().get(3).getTitle());
            terms5.setText(obj.getTerm().get(4).getTitle());
            terms6.setText(obj.getTerm().get(5).getTitle());
            terms7.setText(obj.getTerm().get(6).getTitle());


            terms_1.setText(Html.fromHtml(obj.getTerm().get(0).getBody()));
            terms_2.setText(Html.fromHtml(obj.getTerm().get(1).getBody()));
            terms_3.setText(Html.fromHtml(obj.getTerm().get(2).getBody()));

            terms_4.setText(Html.fromHtml(obj.getTerm().get(3).getBody()));
            terms_4.setMovementMethod(LinkMovementMethod.getInstance());

            terms_5.setText(Html.fromHtml(obj.getTerm().get(4).getBody()));
            terms_5.setMovementMethod(LinkMovementMethod.getInstance());

            terms_6.setText(Html.fromHtml(obj.getTerm().get(5).getBody()));
            terms_6.setMovementMethod(LinkMovementMethod.getInstance());
            terms_7.setText(Html.fromHtml(obj.getTerm().get(6).getBody()));

        }
        else if (obj.getStatus().equals("error_validation")) {
            croutonAlert(getActivity(), obj.getMessage());
            Log.e("error validation", obj.getMessage());
        }
    }


    public void goHomePage()
    {
        Intent loginPage = new Intent(getActivity(), HomeActivity.class);
        getActivity().startActivity(loginPage);
        getActivity().finish();

    }

    //Validator Result//
    @Override
    public void onValidationSucceeded() {
        Log.e("Validation", "success");

    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        Log.e("Validation","fail");
        for (ValidationError error : errors) {
            View view = error.getView();

            String message = error.getCollatedErrorMessage(getActivity());
            String splitErrorMsg[] = message.split("\\r?\\n");

            // Display error messages
            if (view instanceof EditText) {
                ((EditText) view).setError(splitErrorMsg[0]);
            }
            else if (view instanceof CheckBox){
                ((CheckBox) view).setError(splitErrorMsg[0]);
                croutonAlert(getActivity(), splitErrorMsg[0]);
            }
        }

    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fragmentContainerId = ((FragmentContainerActivity) getActivity()).getFragmentContainerId();
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onResume();
        AnalyticsApplication.sendScreenView(SCREEN_LABEL);
        Log.e("Tracker", SCREEN_LABEL);
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onPause();
    }




}
