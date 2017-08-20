package com.abhirishi.personal.collabarm;

import org.jetbrains.annotations.NotNull;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TimePicker;
import com.abhirishi.personal.collabarm.models.Alarm;
import com.abhirishi.personal.collabarm.models.Friend;
import com.abhirishi.personal.collabarm.util.FirebaseUtil;
import java.util.Calendar;

public class FriendActivity extends AppCompatActivity
	implements AlarmsFragment.OnAlarmListFragmentInteractionListener, DatePickerDialog.OnDateSetListener {

	private static final int CONTENT_VIEW_ID = 10101010;
	private Friend friend;
	private Fragment setBy;
	private Fragment setFor;


	private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
		= new BottomNavigationView.OnNavigationItemSelectedListener() {

		@Override
		public boolean onNavigationItemSelected(@NonNull MenuItem item) {
			switch (item.getItemId()) {
			case R.id.set_for:
				setFor.getView().setVisibility(View.VISIBLE);
				setBy.getView().setVisibility(View.GONE);
				return true;
			case R.id.set_by:
				setBy.getView().setVisibility(View.VISIBLE);
				setFor.getView().setVisibility(View.GONE);
				return true;
			}
			return false;
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friend);
		friend = (Friend) getIntent().getSerializableExtra("friend");

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		toolbar.setTitle(friend.getName());
		setSupportActionBar(toolbar);

		FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Calendar instance = Calendar.getInstance();
				int year = instance.get(Calendar.YEAR);
				int day = instance.get(Calendar.DATE);
				int month = instance.get(Calendar.MONTH);
				DatePickerDialog datePickerDialog = new DatePickerDialog(FriendActivity.this, FriendActivity.this, year,
					month, day);
				datePickerDialog.show();
			}
		});

		FrameLayout content = (FrameLayout) findViewById(R.id.content);
		//noinspection ResourceType
		content.setId(CONTENT_VIEW_ID);
		if (savedInstanceState == null) {
			setBy = new AlarmsFragment(ALARMS.SET_BY, friend.getName());
			setFor = new AlarmsFragment(ALARMS.SET_FOR, friend.getName());
			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			//noinspection ResourceType
			ft.add(CONTENT_VIEW_ID, setBy).add(CONTENT_VIEW_ID, setFor).commit();
		}


		BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
		navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
		Spinner spinner = (Spinner) findViewById(R.id.spinner_nav);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
			R.array.permissions, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);


	}

	private void showTimePicker(final int year, final int month, final int dayOfMonth) {
		TimePickerDialog timePickerDialog = new TimePickerDialog(FriendActivity.this,
			new TimePickerDialog.OnTimeSetListener() {
				@Override
				public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
					Calendar instance = Calendar.getInstance();
					instance.set(year, month, dayOfMonth, hourOfDay, minute);
					FirebaseUtil.setAlarmFor(FirebaseUtil.getUsername(), friend.getName(), instance.getTimeInMillis());
				}
			}, 10, 10, false);
		timePickerDialog.show();
	}

	@Override
	public void onListFragmentInteraction(@NotNull Alarm item) {

	}

	@Override
	public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
		showTimePicker(year, month, dayOfMonth);
	}
}
