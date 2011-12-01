package zthreex.android.Client;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SendActivity extends Activity {
	Button btnSendConfirm,btnSendReturn;
	EditText desAddr,sendPeriod;
	static private String strDesAddr = "5554";
	static private int intSendPeriod = 5;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.send);
		
		desAddr = (EditText) findViewById(R.id.desAddr);
		sendPeriod = (EditText) findViewById(R.id.sendPeriod);
		
		btnSendConfirm = (Button)findViewById(R.id.btnSendConfirm);
		btnSendConfirm.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				strDesAddr = desAddr.getText().toString();
				intSendPeriod = Integer.valueOf(sendPeriod.getText().toString());
				Toast.makeText(getApplicationContext(), "设置成功，请返回", Toast.LENGTH_SHORT).show();
			}
		});
		
		btnSendReturn = (Button)findViewById(R.id.btnSendReturn);
		btnSendReturn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), SettingsActivity.class);
				startActivity(intent);
			}
		});
	}
	
	public static String getDesAddr(){
		return strDesAddr;
	}
	
	public static int getSendPeriod(){
		return intSendPeriod;
	}

}
