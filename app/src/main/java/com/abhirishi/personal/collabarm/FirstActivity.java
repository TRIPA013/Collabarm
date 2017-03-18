package com.abhirishi.personal.collabarm;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class FirstActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_first);
		if (!User.isLoggedIn()) {
			Intent intent = new Intent(this, RegistrationActivity.class);
			startActivity(intent);
		}
		finish();
	}
}
