package zthreex.android.Client;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;

public class BootBroadcastReceiver extends BroadcastReceiver {

	static final String BootACTION = "android.intent.action.BOOT_COMPLETED";

	/*
	 * 功能：若为锁死状态，开机自动进入锁死进程  时间：2011.3.15 
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if (intent.getAction().equals(BootACTION) && (LockActivity.lockState == true)) {
				Intent i = new Intent(context, LockActivity.class);
				i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(i);
			}
		if (intent.getAction().equals(BootACTION) && (ClientService.ServiceState == 1)) {
			SmsManager sms = SmsManager.getDefault();
			sms.sendTextMessage(SendActivity.getDesAddr(), "1", "Reboot", null, null);
		}
	}
}
