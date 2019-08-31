package com.androhungers.rssnewsreader.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androhungers.rssnewsreader.R;
import com.androhungers.rssnewsreader.common.Constants;
import com.androhungers.rssnewsreader.common.PreferenceHelper;
import com.androhungers.rssnewsreader.model.signin.DataModel;
import com.androhungers.rssnewsreader.model.signin.SigninRequestModel;
import com.androhungers.rssnewsreader.model.signin.SigninResponseModel;
import com.androhungers.rssnewsreader.model.signup.SignupResponseModel;
import com.androhungers.rssnewsreader.viewModel.LoginViewModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.victor.loading.rotate.RotateLoading;

import net.cachapa.expandablelayout.ExpandableLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.mrapp.android.dialog.MaterialDialog;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.et_user_name) EditText etUserName;
    @BindView(R.id.et_password) EditText etPassword;
    @BindView(R.id.et_name) EditText etName;
    @BindView(R.id.et_age) EditText etAge;
    @BindView(R.id.ex) ExpandableLayout expandableLayout;
    @BindView(R.id.btn_sign_in) TextView textViewSignIn;
    @BindView(R.id.tv_sign_up) TextView textViewSignUp;
    @BindView(R.id.root_layout) RelativeLayout rootLayout;
    @BindView(R.id.progress) RotateLoading rotateLoading;

    private LoginViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        viewModel = ViewModelProviders.of(this).get(LoginViewModel.class);

        ButterKnife.bind(this);

        viewModel.STATE.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if(s.equalsIgnoreCase("signin")){
                    expandableLayout.collapse();
                    textViewSignIn.setText("Log In");
                    textViewSignUp.setText("Don't have an Account? Register");
                }else {
                    expandableLayout.expand();
                    textViewSignIn.setText("Register");
                    textViewSignUp.setText("Already have an Account? Log In");
                }
            }
        });

        viewModel.errorMsg.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                rotateLoading.stop();
                if(viewModel.STATE.getValue().equalsIgnoreCase("signin")){
                    textViewSignIn.setText("Log In");
                }else {
                    textViewSignIn.setText("Register");
                }

                MaterialDialog.Builder dialogBuilder = new MaterialDialog.Builder(LoginActivity.this);
                dialogBuilder.setButtonTextColor(getResources().getColor(R.color.colorPrimary));
                dialogBuilder.setTitle("Alert !!!");
                dialogBuilder.setMessage(s);

                dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                dialogBuilder.setNegativeButton(android.R.string.cancel, null);
                MaterialDialog dialog = dialogBuilder.create();
                dialog.show();
            }
        });

        viewModel.signinRequestModelMutableLiveData.observe(this, new Observer<SigninRequestModel>() {
            @Override
            public void onChanged(SigninRequestModel signinRequestModel) {

            }
        });



        viewModel.STATE.setValue("signin");

        setOnClickEvent();
    }

    private void setOnClickEvent(){
        textViewSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rotateLoading.start();
                textViewSignIn.setText("");
                viewModel.name.setValue(etName.getText().toString());
                viewModel.age.setValue(etAge.getText().toString());
                viewModel.userName.setValue(etUserName.getText().toString());
                viewModel.password.setValue(etPassword.getText().toString());

                if(viewModel.isValidInput()){
                    if(viewModel.STATE.getValue().equalsIgnoreCase("signin")){
                        viewModel.signinRequestModelMutableLiveData.setValue(viewModel.makeLoginRequest());
                        viewModel.requestForSignIn(viewModel.signinRequestModelMutableLiveData.getValue()).observe(LoginActivity.this, new Observer<SigninResponseModel>() {
                            @Override
                            public void onChanged(SigninResponseModel signinResponseModel) {
                                rotateLoading.stop();
                                textViewSignIn.setText("Log In");
                                if(signinResponseModel.isSuccess()){
                                    String data = new Gson().toJson(signinResponseModel.getData());
                                    new PreferenceHelper(getApplicationContext()).saveString(Constants.USER_DATA_FIELD,data);
                                    new PreferenceHelper(getApplicationContext()).saveString(Constants.LOGIN_SATE_FIELD,"true");

                                    Intent intent = new Intent(LoginActivity.this, RssFeedActivity.class);
                                    startActivity(intent);
                                    finish();
                                }else {
                                    MaterialDialog.Builder dialogBuilder = new MaterialDialog.Builder(LoginActivity.this);
                                    dialogBuilder.setButtonTextColor(getResources().getColor(R.color.colorPrimary));
                                    dialogBuilder.setTitle("Error !!!");
                                    dialogBuilder.setMessage("Please try with valid Credentials.");

                                    dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                        }
                                    });
                                    dialogBuilder.setNegativeButton(android.R.string.cancel, null);
                                    MaterialDialog dialog = dialogBuilder.create();
                                    dialog.show();
                                }

                            }
                        });
                    }else if(viewModel.STATE.getValue().equalsIgnoreCase("signup")) {

                        viewModel.signupRequestModelMutableLiveData.setValue(viewModel.makeSignUpRequest());
                        viewModel.requestForSignUp(viewModel.signupRequestModelMutableLiveData.getValue()).observe(LoginActivity.this, new Observer<SignupResponseModel>() {
                            @Override
                            public void onChanged(SignupResponseModel signupResponseModel) {
                                rotateLoading.stop();
                                textViewSignIn.setText("Register");
                                if(signupResponseModel.isSuccess()){
                                    String data = new Gson().toJson(signupResponseModel.getData());
                                    new PreferenceHelper(getApplicationContext()).saveString(Constants.USER_DATA_FIELD,data);
                                    new PreferenceHelper(getApplicationContext()).saveString(Constants.LOGIN_SATE_FIELD,"true");
                                    Intent intent = new Intent(LoginActivity.this, RssFeedActivity.class);
                                    startActivity(intent);
                                    finish();
                                }else {
                                    MaterialDialog.Builder dialogBuilder = new MaterialDialog.Builder(LoginActivity.this);
                                    dialogBuilder.setButtonTextColor(getResources().getColor(R.color.colorPrimary));
                                    dialogBuilder.setTitle("Error !!!");
                                    dialogBuilder.setMessage(signupResponseModel.getMessage());

                                    dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                        }
                                    });
                                    dialogBuilder.setNegativeButton(android.R.string.cancel, null);
                                    MaterialDialog dialog = dialogBuilder.create();
                                    dialog.show();
                                }

                            }
                        });

                    }
                }
            }
        });

        textViewSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(viewModel.STATE.getValue().equalsIgnoreCase("signin")){
                    viewModel.setState("signup");
                }else if(viewModel.STATE.getValue().equalsIgnoreCase("signup")) {
                    viewModel.setState("signin");
                }
            }
        });
    }
}
