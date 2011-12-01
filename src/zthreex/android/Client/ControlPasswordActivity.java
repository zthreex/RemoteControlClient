package zthreex.android.Client;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ControlPasswordActivity extends Activity {
	Button btnConfirm, btnCancel;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.controlpasswordsettings);
		
		btnConfirm = (Button) findViewById(R.id.btnConfirm);
		btnConfirm.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				EditText oldPassword = (EditText) findViewById(R.id.oldPassword);
				EditText newPassword = (EditText) findViewById(R.id.newPassword);
				EditText confimedNewPassword = (EditText) findViewById(R.id.confimedNewPassword);

				if(   (oldPassword.getText().toString().length()==0) 
						&&(newPassword.getText().toString().length()==0)
						&&(confimedNewPassword.getText().toString().length()==0)  ){
					Toast.makeText(getApplicationContext(), "��ȷ��������ɣ�", Toast.LENGTH_SHORT).show();
				}
				
				if(oldPassword.getText().toString().equals(LockActivity.systemPassword)){
					if(newPassword.getText().toString().equals(confimedNewPassword.getText().toString())){
						ClientService.ControlPassword= newPassword.getText().toString();
						Toast.makeText(getApplicationContext(), "�޸ĳɹ����뷵��", Toast.LENGTH_SHORT).show();
					}
					else{
						Toast.makeText(getApplicationContext(), "��ȷ���������������һ�£�", Toast.LENGTH_SHORT).show();
					}
				}
				else{
					Toast.makeText(getApplicationContext(), "�������", Toast.LENGTH_SHORT).show();
				}
				
			}
		});
		
		btnCancel = (Button) findViewById(R.id.btnCancel);
		btnCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), SettingsActivity.class);
				startActivity(intent);
			}
		});

	}

}
