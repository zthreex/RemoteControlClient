package zthreex.android.Client;

import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.util.Log;

public class ClientService extends Service {

	public SMSObserver sObserver;
	public SMSHandler shandler;
	public Cursor cur;
	public Uri uri;
	public static int ServiceState;
	public static String addr = "";
	public static String peopleName = "";
	public static String strLog = "";
	public static String ControlPassword = "abcd";
	private final String LockCommand = "001"; // 锁死手机控制字
	private final String LocationRequestCommand = "002"; //获取地理位置控制字
//	private final String ContactRequestCommand = "003"; // 003后跟联系人姓名，可取回电话号码
//	private final String HelpCommand = "help";

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.i("****", "service oncreate");
		uri = Uri.parse("content://sms");
		cur = getContentResolver().query(uri, null, null, null, null);
		shandler = new SMSHandler();
		sObserver = new SMSObserver(shandler);
		getContentResolver().registerContentObserver(uri, true, sObserver);
		ServiceState = 1;
	}

	@Override
	public boolean onUnbind(Intent arg0) {
		super.onUnbind(arg0);
		return false;
	}

	@Override
	public void onStart(Intent intent, int startid) {
		super.onStart(intent, startid);
		Log.i("****", "service onstart");
		ServiceState = 1;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.i("****", "service ondestroy");
		getContentResolver().unregisterContentObserver(sObserver);
		ServiceState = 0;
	}

	/*
	 * 功能：记录指定类型的操作时间和类型，增加到全局变量strLog中，便于查看历史记录 时间：2011.2.16 作者：zx
	 */
	public void logToPhone(String type) {
		SimpleDateFormat sDateFormat = new SimpleDateFormat(
				"yyyy-MM-dd   hh:mm:ss");
		String date = sDateFormat.format(new java.util.Date());
		strLog += date + "   " + type + "\n";
	}

	/*
	 * 功能：遍历数据库信息并写入控制台 时间：2011.3.2 作者：zx
	 */
	public void logToConsole(Cursor cur) {
		if (cur.moveToFirst()) {
			do {
				for (int j = 0; j < cur.getColumnCount(); j++) {
					String info = "name:" + cur.getColumnName(j) + "="
							+ cur.getString(j) + "\n";
					Log.i("====>", info);
				}
			} while (cur.moveToNext());
		}
		cur.moveToFirst();
	}

	/*
	 * 功能：将标签为****的自定义信息写入控制台 时间：2011.3.2 作者：zx
	 */
	public void logToConsole(String msg) {
		Log.i("****", msg);
	}

	public class SMSObserver extends ContentObserver {
		// inner message
		public static final int SMS_Location = 0;
		public static final int SMS_ContactRequest = 1;
		public static final int SMS_Default = 2;
		public static final int SMS_Help = 3;
		public static final int SMS_ContactDelete = 4;
		public static final int SMS_Lock = 5;
		Handler handler;

		public SMSObserver(Handler h) {
			super(h);
			// TODO Auto-generated constructor stub
			handler = h; // pass the arg h to the memeber handler
		}

		public void onChange(boolean selfChange) {
			// TODO Auto-generated method stub
			super.onChange(selfChange);

			// update cur each time
			cur = getContentResolver().query(uri, null, null, null, null);
			cur.moveToFirst();

			addr = cur.getString(2); // the number of incoming phone

			// 读取短信内容，取前四位作为密码进行判定，取后三位作为控制字
			String body = cur.getString(11);
			if (body.length() >=7) {
				String getControlPassword = body.substring(0, 4);
				String getControlWord = body.substring(4, 7);
//				String contactCommand = body.substring(0, 3);

				if (getControlPassword.equals(ControlPassword)) {
					Log.i("****", "got control!");
					if (getControlWord.equalsIgnoreCase(LocationRequestCommand)) {
						handler.sendEmptyMessage(SMS_Location);
						Log.i("****", "SMS_Location!!!");
					}else if (getControlWord.equals(LockCommand)) {
						handler.sendEmptyMessage(SMS_Lock);
						Log.i("****", "SMS_Lock!!");
					}else {
						handler.sendEmptyMessage(SMS_Default);
						Log.i("****", "SMS_Default!!!");
					} 
					
					//暂不使用的功能
//					else if (contactCommand.equals(ContactRequestCommand)) {
//						handler.sendEmptyMessage(SMS_ContactRequest);
//						Log.i("****", "SMS_ContactRequest!!!");
//						peopleName = body.substring(3);
//						Log.i("****", peopleName);
//					}
//					 else if (body.equals(HelpCommand)) {
//							handler.sendEmptyMessage(SMS_Help);
//							Log.i("****", "SMS_Help!!!");
//						} 
				}
			}
		}
	}

	public class SMSHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SMSObserver.SMS_Location:
				startLocationService();
//				try {
//					Thread.sleep(10000);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				SmsManager sms = SmsManager.getDefault();
//				sms.sendTextMessage(addr, "1", LocationService.strLocationResult, null, null);
				stopLocationService();
				logToPhone("SMS_Location");
				logToPhone(LocationService.strLocationResult);
				break;
			case SMSObserver.SMS_Lock:
				startLock();
				alramTask();
				logToPhone("SMS_Lock");
				break;
//			case SMSObserver.SMS_ContactRequest:
//				String number = requestContactNum(peopleName);
//				Log.i("****", number);
//				logToPhone("SMS_ContactRequest");
//				logToPhone(number);
//				break;
//			case SMSObserver.SMS_Help:
//				Log.i("****", "help!!!");
//				offerHelp();
//				logToPhone("SMS_Help");
//				break;
			default:
				break;
			}

			// update the database
			getContentResolver().unregisterContentObserver(sObserver);
			getContentResolver().registerContentObserver(uri, true, sObserver);
		}
	}

	private void startLocationService() {
		Intent intent = new Intent(ClientService.this, LocationService.class);
		ClientService.this.startService(intent);
	}

	private void stopLocationService() {
		Intent intent = new Intent(ClientService.this, LocationService.class);
		ClientService.this.stopService(intent);
	}

	private void offerHelp() {
		String helpMsg = new String("location:002.help:help");
		SmsManager sms = SmsManager.getDefault();
		sms.sendTextMessage("5556", "1", helpMsg, null, null);
	}
	
	/*
	 * 功能：锁死后定时发送位置  时间：2011.3.15 作者：zx
	 */
	public void alramTask(){
		Timer mTimer = new Timer(true);
		TimerTask mTimerTask = new TimerTask() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				startLocationService();
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				SmsManager sms = SmsManager.getDefault();
				sms.sendTextMessage(SendActivity.getDesAddr(), "1", LocationService.strLocationResult, null, null);
				stopLocationService();
			}
		};
		mTimer.schedule(mTimerTask, 10000,1000*60*SendActivity.getSendPeriod());
	}

	/*
	 * 功能：输入姓名得到联系人电话号码 时间：2011.3.2 作者：zx
	 */
	private String requestContactNum(String peopleName) {
		// 根据指定的联系人姓名查到相应的表
		Cursor curPeople = getContentResolver().query(
				ContactsContract.Contacts.CONTENT_URI,
				null,
				ContactsContract.Contacts.DISPLAY_NAME + " =  '" + peopleName
						+ "'", null, null);
		// logToConsole(curPeople);
		if (curPeople.moveToFirst()) {
			String hasNumber = curPeople.getString(3);
			if (hasNumber.equalsIgnoreCase("1")) {
				// 取得相应的id
				String contactId = curPeople.getString(10);
				// 通过id查询号码
				Cursor curNumber = getContentResolver().query(
						ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
						null,
						ContactsContract.CommonDataKinds.Phone.CONTACT_ID
								+ "= '" + contactId + "'", null, null);
				curNumber.moveToFirst();
				String strPhoneNumber = curNumber
						.getString(curNumber
								.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
				return strPhoneNumber;
			} else
				return "This guy doesn't has a phone number.";
		} else
			return "Name not found.";
	}

	/*
	 * 功能：启动锁死进程 时间：2011.3.14 作者：zx
	 */
	private void startLock() {
		Intent i = new Intent(ClientService.this, LockActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(i);
	}

	/*
	 * 功能：结束锁死进程 时间：2011.3.14 作者：zx
	 */
	// private void stopLock(){
	// }

}