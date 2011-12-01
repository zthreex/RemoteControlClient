package zthreex.android.Client;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.telephony.SmsManager;
import android.util.Log;

public class LocationService extends Service {
	
    static double longitude, latitude;
	static public String strLocationResult = "";
	private final static String API_KEY = "a7DpeIyAKv5U3k0a7NSVPylRBOWtnDfzMe9Psduh";
	private TelephonyManager tm;
	private GsmCellLocation location;
	private int cid, lac, mcc, mnc, cellPadding;

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.e(ACTIVITY_SERVICE, "location");

		tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

		location = (GsmCellLocation) tm.getCellLocation();
		cid = location.getCid();
		lac = location.getLac();
		/*
		 * Mcc and mnc is concatenated in the networkOperatorString. The first 3
		 * chars is the mcc and the last 2 is the mnc.
		 */
		String networkOperator = tm.getNetworkOperator();
		if (networkOperator != null && networkOperator.length() > 0) {
			try {
				mcc = Integer.parseInt(networkOperator.substring(0, 3));
				mnc = Integer.parseInt(networkOperator.substring(3));
			} catch (NumberFormatException e) {
			}
		}

		/*
		 * Check if the current cell is a UMTS (3G) cell. If a 3G cell the cell
		 * id padding will be 8 numbers, if not 4 numbers.
		 */
		if (tm.getNetworkType() == TelephonyManager.NETWORK_TYPE_UMTS) {
			cellPadding = 8;
		} else {
			cellPadding = 4;
		}

		/*
		 * Seems that cid and lac shall be in hex. Cid should be padded with
		 * zero's to 8 numbers if UMTS (3G) cell, otherwise to 4 numbers. Mcc
		 * padded to 3 numbers. Mnc padded to 2 numbers.
		 */

		try {
			// 模拟器无法联网，填入了一组数据。模拟器测试时使用
			String test_cid = "2a0b";
			String test_lac = "9192";
			String test_mnc = "00";
			String test_mcc = "460";
			updateLocation(test_cid, test_lac, test_mnc, test_mcc);

			// //若用真机测试，则使用下行代码
			// updateLocation(getPaddedHex(cid, cellPadding), getPaddedHex(lac, 4), getPaddedInt(mnc, 2), getPaddedInt(mcc, 3));

			strLocationResult = "latitude:" + Double.toString(latitude)
					+ "\nlongitude:" + Double.toString(longitude);
		} catch (IOException e) {
			strLocationResult = "Error!\n" + e.getMessage();
		}
		SmsManager sms = SmsManager.getDefault();
		sms.sendTextMessage(ClientService.addr, "1", strLocationResult, null, null);

		//		try {
//			updateLocationGPS();
//		} catch (Exception e) {
//			// TODO: handle exception
//			Log.i("****", "exception!");
//		}
		// 直接在地图上上显示
		// Uri uri = Uri.parse("geo: " + Double.toString(latitude) + ","
		// + Double.toString(longitude));
		// final Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		// startActivity(intent);

	}
	

	// Convert an int to an hex String and pad with 0's up to minLen.
	String getPaddedHex(int nr, int minLen) {
		String str = Integer.toHexString(nr);
		if (str != null) {
			while (str.length() < minLen) {
				str = "0" + str;
			}
		}
		return str;
	}

	// Convert an int to String and pad with 0's up to minLen.
	String getPaddedInt(int nr, int minLen) {
		String str = Integer.toString(nr);
		if (str != null) {
			while (str.length() < minLen) {
				str = "0" + str;
			}
		}
		return str;
	}

	/*
	 * 功能：使用gps进行定位
	 * 时间：2011.3.1
	 * */
	private void updateLocationGPS(){
		LocationManager mgr;
		mgr = (LocationManager )getSystemService(LOCATION_SERVICE);
		Location location = mgr.getLastKnownLocation("gps");
		Log.i("****", Double.toString(location.getLatitude())+Double.toString(location.getLongitude()));
	}
	
	
	private void updateLocation(String cid, String lac, String mnc, String mcc)
			throws IOException {
		InputStream is = null;
		ByteArrayOutputStream bos = null;
		byte[] data = null;
		try {
			// Build the url , in the format of json
			StringBuilder uri = new StringBuilder(
					"http://cellid.labs.ericsson.net/");
			uri.append("json");
			uri.append("/lookup?cellid=").append(cid);
			uri.append("&mnc=").append(mnc);
			uri.append("&mcc=").append(mcc);
			uri.append("&lac=").append(lac);
			uri.append("&key=").append(API_KEY);

			// Create an HttpGet request
			HttpGet request = new HttpGet(uri.toString());
			Log.e(TELEPHONY_SERVICE, uri.toString());

			// Send the HttpGet request
			HttpClient httpClient = new DefaultHttpClient();
			HttpResponse response = httpClient.execute(request);

			// Check the response status
			int status = response.getStatusLine().getStatusCode();
			if (status != HttpURLConnection.HTTP_OK) {
				switch (status) {
				case HttpURLConnection.HTTP_NO_CONTENT:
					throw new IOException("The cell could not be "
							+ "found in the database");
				case HttpURLConnection.HTTP_BAD_REQUEST:
					throw new IOException("Check if some parameter "
							+ "is missing or misspelled");
				case HttpURLConnection.HTTP_UNAUTHORIZED:
					throw new IOException("Make sure the API key is "
							+ "present and valid");
				case HttpURLConnection.HTTP_FORBIDDEN:
					throw new IOException("You have reached the limit"
							+ "for the number of requests per day. The "
							+ "maximum number of requests per day is "
							+ "currently 500.");
				case HttpURLConnection.HTTP_NOT_FOUND:
					throw new IOException("The cell could not be found"
							+ "in the database");
				default:
					throw new IOException("HTTP response code: " + status);
				}
			}

			// The response was ok (HTTP_OK) so lets read the data
			HttpEntity entity = response.getEntity();
			is = entity.getContent();
			bos = new ByteArrayOutputStream();
			byte buf[] = new byte[256];
			while (true) {
				int rd = is.read(buf, 0, 256);
				if (rd == -1)
					break;
				bos.write(buf, 0, rd); // write the data from buf to bus at a
				// length of rd
			}
			bos.flush();
			data = bos.toByteArray();
			if (data != null) {
				try {
					// Parse the Json data
					JSONObject position = new JSONObject(new String(data))
							.getJSONObject("position");
					longitude = position.getDouble("longitude");
					latitude = position.getDouble("latitude");
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (MalformedURLException e) {
			Log.e("ERROR", e.getMessage());
		} catch (IllegalArgumentException e) {
			throw new IOException(
					"URL was incorrect. Did you forget to set the API_KEY?");
		} finally {
			// make sure we clean up after us
			try {
				if (bos != null)
					bos.close();
			} catch (Exception e) {
			}
			try {
				if (is != null)
					is.close();
			} catch (Exception e) {
			}
		}
	}
}