package com.abhirishi.personal.collabarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Toast.makeText(context, "Alarm received!", Toast.LENGTH_LONG).show();

		Intent newIntent = new Intent(context, AlarmActivity.class);

		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		context.startActivity(newIntent);
	}
}
