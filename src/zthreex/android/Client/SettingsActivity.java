package zthreex.android.Client;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SettingsActivity extends Activity {
	Button btnPasswordSettings,btnControlPasswordSettings,btnSendSettings,btnReturn;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		
		btnPasswordSettings = (Button)findViewById(R.id.btnPasswordSettings);
		btnPasswordSettings.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), PasswordActivity.class);
				startActivity(intent);
			}
		});
		
		btnControlPasswordSettings = (Button)findViewById(R.id.btnControlPasswordSettings);
		btnControlPasswordSettings.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), ControlPasswordActivity.class);
				startActivity(intent);
			}
		});
		
		btnSendSettings = (Button)findViewById(R.id.btnSendSettings);
		btnSendSettings.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), SendActivity.class);
				startActivity(intent);
			}
		});
		
		
		btnReturn = (Button)findViewById(R.id.btnReturn);
		btnReturn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), ClientActivity.class);
				startActivity(intent);
			}
		});
		
	}

}
