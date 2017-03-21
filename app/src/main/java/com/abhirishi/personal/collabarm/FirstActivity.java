package com.abhirishi.personal.collabarm;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;

public class FirstActivity extends AppCompatActivity {

	private static final int SIGN_IN_REQUEST_CODE = 100;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_first);

		if (FirebaseAuth.getInstance().getCurrentUser() == null) {
			tryLogin();
		}
		else {
			onAlreadyLoggedIn();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
		Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == SIGN_IN_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				onAlreadyLoggedIn();
			}
			else {
				tryLogin();
			}
		}
	}
	private void onAlreadyLoggedIn() {
		Intent intent = new Intent(this, CollabarmActivity.class);
		startActivity(intent);
		finish();
	}

	private void tryLogin() {
		startActivityForResult(
			AuthUI.getInstance()
				.createSignInIntentBuilder()
				.setTheme(R.style.AppTheme)
				.build(),
			SIGN_IN_REQUEST_CODE
		);
	}

}
