package zthreex.android.Client;

import android.app.Activity;
import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LockActivity extends Activity {
	
	public static String leftMessage = "Çë¾¡¿ìÁªÏµÊ§Ö÷ 13909184165";
	public static String systemPassword = "abcd";
	public static Boolean lockState;
	
	TextView myOwnMessage;
	EditText  getUnlockPassword;
	Button btnStopLock;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,
		              WindowManager.LayoutParams. FLAG_FULLSCREEN);

		setContentView(R.layout.lock);
		
		lockState=true;
		
		disableGuardkey();			//½ûÓÃËøÆÁ¼ü

		myOwnMessage = (TextView) findViewById(R.id.myOwnMessage);
		myOwnMessage.setText(leftMessage);
		
		getUnlockPassword = (EditText) findViewById(R.id.getUnlockPassword);
		
		btnStopLock = (Button)findViewById(R.id.btnStopLock);
		btnStopLock.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String inputString = getUnlockPassword.getText().toString();
				if(inputString.equals(systemPassword)){
					LockActivity.this.onDestroy();
					Intent intent = new Intent();
					intent.setClass(getApplicationContext(), ClientActivity.class);
					startActivity(intent);
				}
				else {
					Toast.makeText(getApplicationContext(), "ÃÜÂë´íÎó£¡", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	@Override
	public void onStart() {
		super.onStart();
		disableGuardkey();
		Log.e("****", "Lock onstart");
	}

	@Override
	public void onStop() {
		super.onStop();
		lockState=false;
		Log.e("****", "Lock onstop");
		// this.onStart();
	}

	@Override
	public void onRestart() {
		super.onRestart();
		Log.e("****", "Lock onrestart");
	}

	@Override
	public void onResume() {
		super.onResume();
		disableGuardkey();
		lockState=true;
		Log.e("****", "Lock resume");
//		this.onStart();
	}

	@Override
	public void onPause() {
		super.onPause();
		lockState=false;
		Log.e("****", "Lock pause");
	}
	
	/* ÆÁ±ÎËøÆÁ¼ü */
	public void disableGuardkey() {
		KeyguardManager mKeyguardManager = null;
		KeyguardLock mKeyguardLock = null;
		mKeyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);  
		mKeyguardLock = mKeyguardManager.newKeyguardLock("");  
		mKeyguardLock.disableKeyguard();  
	}

	/* ÆÁ±ÎÁËhome¼ü */
	public void onAttachedToWindow() {
		this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD);
		super.onAttachedToWindow();
	}

	/* ÆÁ±ÎbackspaceÓësearch¼ü */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
		}
		if (keyCode == KeyEvent.KEYCODE_SEARCH) {
		}
		return true;
	}
	
}
