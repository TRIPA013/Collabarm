package com.abhirishi.personal.collabarm;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;

import static android.Manifest.permission.READ_CONTACTS;

public class FirstActivity extends AppCompatActivity {

	private static final int SIGN_IN_REQUEST_CODE = 100;
	private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 99;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_first);
		checkPermissionsForReadingContacts();
	}

	private void checkPermissionsForReadingContacts() {
		if (ContextCompat.checkSelfPermission(this, READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {

			if (ActivityCompat.shouldShowRequestPermissionRationale(this, READ_CONTACTS)) {

			}
			else {
				ActivityCompat.requestPermissions(this,
					new String[] { READ_CONTACTS },
					MY_PERMISSIONS_REQUEST_READ_CONTACTS);
			}
		}
		else {
			authenticateUser();
		}

	}

	private void authenticateUser() {
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

	@Override
	public void onRequestPermissionsResult(int requestCode,
		String permissions[], int[] grantResults) {
		switch (requestCode) {
		case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
			if (grantResults.length > 0
				&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				authenticateUser();
			}
			else {

			}
			return;
		}

		}
	}

}
