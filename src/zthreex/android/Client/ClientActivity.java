package zthreex.android.Client;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ToggleButton;
import zthreex.android.Client.R;

public class ClientActivity extends Activity {
	Button  btnLog,btnSettings;
	ToggleButton tbtnService;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		tbtnService = (ToggleButton) this.findViewById(R.id.tbtnService);
		tbtnService.setBackgroundResource(R.drawable.lock);
		
		tbtnService.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
//				Intent intent = new Intent();
//				intent.setClass(ClientActivity.this, LockActivity.class);
//				startActivity(intent);
				
				Intent intent = new Intent(ClientActivity.this, ClientService.class);
				if (tbtnService.isChecked()) {
					tbtnService.setBackgroundResource(R.drawable.light_bulb);
					ClientActivity.this.startService(intent);
				} else {
					tbtnService.setBackgroundResource(R.drawable.lock);
					ClientActivity.this.stopService(intent);
				}
			}
		});
		
		btnLog = (Button) findViewById(R.id.btnLog);
		btnLog.setBackgroundResource(R.drawable.news);
		btnLog.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.i("****",ClientService.strLog);
				Toast t = Toast.makeText(getApplicationContext(),
						ClientService.strLog, Toast.LENGTH_LONG);
				t.show();
			}
		});
		btnSettings = (Button) this.findViewById(R.id.btnSettings);
		btnSettings.setBackgroundResource(R.drawable.umbrella);
		btnSettings.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), SettingsActivity.class);
				startActivity(intent);
			}
		});
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	@Override
	public void onRestart() {
		super.onRestart();
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.e("****", "resume");
		if (ClientService.ServiceState == 1) {
			tbtnService.setChecked(true);
			tbtnService.setBackgroundResource(R.drawable.light_bulb);
		} else{
			tbtnService.setChecked(false);
			tbtnService.setBackgroundResource(R.drawable.lock);
			}
	}

	@Override
	public void onPause() {
		super.onPause();
		Log.e("****", "pause");
	}

}
