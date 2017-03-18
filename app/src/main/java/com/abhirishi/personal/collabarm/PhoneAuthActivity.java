package com.abhirishi.personal.collabarm;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import com.auth0.android.Auth0;
import com.auth0.android.authentication.AuthenticationAPIClient;
import com.auth0.android.authentication.AuthenticationException;
import com.auth0.android.authentication.PasswordlessType;
import com.auth0.android.callback.BaseCallback;
import com.auth0.android.result.Credentials;


public class PhoneAuthActivity extends AppCompatActivity {

	private AuthenticationAPIClient authentication;
	private Button verify;
	private AutoCompleteTextView phoneNumberView;
	private AutoCompleteTextView verificationCodeView;
	private TextInputLayout phoneNumberContainer;
	private TextInputLayout verificationContainer;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Auth0 account = new Auth0(getString(R.string.auth0_client_id), getString(R.string.auth0_domain));
		authentication = new AuthenticationAPIClient(account);
		setContentView(R.layout.activity_phone_auth);
		verify = (Button) findViewById(R.id.verify);
		phoneNumberView = (AutoCompleteTextView) findViewById(R.id.enter_phone_numer);
		verificationCodeView = (AutoCompleteTextView) findViewById(R.id.enter_verification_code);
		phoneNumberContainer = (TextInputLayout) findViewById(R.id.phone_text_input_layout);
		verificationContainer = (TextInputLayout) findViewById(R.id.verification_code_text_input_layout);
		verify.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(verificationContainer.getVisibility()== View.VISIBLE){
					authenticateVerificationCode(phoneNumberView.getText().toString(), verificationCodeView.getText().toString());
				} else {
					authenticatePhoneNumber(phoneNumberView.getText().toString());
				}
			}
		});
	}

	private void authenticateVerificationCode(String phoneNumber, String verificationCode) {
		authentication.loginWithPhoneNumber(phoneNumber, verificationCode).start(
			new BaseCallback<Credentials, AuthenticationException>() {
				@Override
				public void onSuccess(Credentials payload) {
					startActivity(new Intent(getApplicationContext(), RegistrationActivity.class));
					finish();
				}

				@Override
				public void onFailure(AuthenticationException error) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							verificationContainer.setError(getString(R.string.invalid_verification_code));
						}
					});
				}
			});
	}

	private void authenticatePhoneNumber(final String phoneNumber) {
		authentication
			.passwordlessWithSMS(phoneNumber, PasswordlessType.CODE).start(
			new BaseCallback<Void, AuthenticationException>() {
				@Override
				public void onSuccess(Void payload) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							phoneNumberContainer.setVisibility(View.GONE);
							verificationContainer.setVisibility(View.VISIBLE);
						}
					});
				}

				@Override
				public void onFailure(AuthenticationException error) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							phoneNumberContainer.setError(getString(R.string.invalid_phone_number));
						}
					});
				}
			});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// Your own Activity code
	}


}


