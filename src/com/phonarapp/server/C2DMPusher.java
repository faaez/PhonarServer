package com.phonarapp.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Scanner;

import javax.servlet.http.HttpServletResponse;

public class C2DMPusher {
	public static final String AUTH_TOKEN_STRING = "DQAAAMIAAACvMiYgOS9cADfoe4fvrQyCdXPhyeuzgSn5rLBUtTiEo9zwvZdJ0IMMSXgfP-4_6gqPhtG5mq9tjiUmSxknHyPSHV5Bpf1X-9RNFfDEEpxyRqpsUsAwdxHsHamYuJKhL5uOPBA4bGzXe_Zn5umGBbZxDT7Y_tTq6jXsMXP9n_25iB9ctVzOOQThKzeZbtUnGvNY-ZqTukduQD6fbPBBON25RErbblrdMNJ0FZF6TF73vZ0Vqi8fDhDMiArnIWoeGeA2Hikd_bsaCeSuGFGWjmLF";

	public static final String DATA = "data.";

	private static final String UPDATE_CLIENT_AUTH = "Update-Client-Auth";

	public static final String C2DM_SEND_ENDPOINT = 
		"https://android.clients.google.com/c2dm/send";

	public static final String PARAM_REGISTRATION_ID = "registration_id";

	public static final String PARAM_DELAY_WHILE_IDLE = "delay_while_idle";

	public static final String PARAM_COLLAPSE_KEY = "collapse_key";

	private static final String UTF8 = "UTF-8";

	public static final int C2DM_MAX_JITTER_MSEC = 3000;

	public static boolean sendNoRetry(String registrationId, 
			String collapse, 
			Map<String, String> params,
			boolean delayWhileIdle)
	throws IOException {

		DataAccessLayer.storeAuthToken(AUTH_TOKEN_STRING);

		// Send a sync message to this Android device.
		StringBuilder postDataBuilder = new StringBuilder();
		postDataBuilder.append(PARAM_REGISTRATION_ID).
		append("=").append(registrationId);

		if (delayWhileIdle) {
			postDataBuilder.append("&")
			.append(PARAM_DELAY_WHILE_IDLE).append("=1");
		}
		postDataBuilder.append("&").append(PARAM_COLLAPSE_KEY).append("="). 
		append(collapse);

		for (Object keyObj: params.keySet()) {
			String key = (String) keyObj;
			if (key.startsWith("data.")) {
				String value = (String) params.get(key);
				postDataBuilder.append("&").append(key).append("=").
				append(URLEncoder.encode(value, UTF8));
			}
		}

		byte[] postData = postDataBuilder.toString().getBytes(UTF8);

		// Hit the dm URL.
		URL url = new URL(C2DM_SEND_ENDPOINT);

		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setDoOutput(true);
		conn.setUseCaches(false);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		conn.setRequestProperty("Content-Length", Integer.toString(postData.length));
		String authToken = DataAccessLayer.getAuthToken();
		conn.setRequestProperty("Authorization", "GoogleLogin auth=" + authToken);

		OutputStream out = conn.getOutputStream();
		out.write(postData);
		out.close();

		int responseCode = conn.getResponseCode();

		if (responseCode == HttpServletResponse.SC_UNAUTHORIZED ||
				responseCode == HttpServletResponse.SC_FORBIDDEN) {
			// The token is too old - return false to retry later, will fetch the token 
			// from DB. This happens if the password is changed or token expires. Either admin
			// is updating the token, or Update-Client-Auth was received by another server, 
			// and next retry will get the good one from database.
			return false;
		}

		// Check for updated token header
		String updatedAuthToken = conn.getHeaderField(UPDATE_CLIENT_AUTH);
		if (updatedAuthToken != null && !authToken.equals(updatedAuthToken)) {
			DataAccessLayer.storeAuthToken(updatedAuthToken);
		}

		String responseLine = new BufferedReader(new InputStreamReader(conn.getInputStream()))
		.readLine();

		if (responseLine == null || responseLine.equals("")) {
			throw new IOException("Got empty response from Google C2DM endpoint.");
		}

		String[] responseParts = responseLine.split("=", 2);
		if (responseParts.length != 2) {

			throw new IOException("Invalid response from Google " + 
					responseCode + " " + responseLine);
		}

		if (responseParts[0].equals("id")) {

			return true;
		}

		if (responseParts[0].equals("Error")) {
			String err = responseParts[1];
			// No retry. 
			// TODO(costin): show a nicer error to the user.
			throw new IOException("Server error: " + err);
		} else {
			// 500 or unparseable response - server error, needs to retry
			return false;
		}
	}

	public static String getNewAuthToken() throws IOException {

		StringBuilder builder = new StringBuilder();

		builder.append("Email=jeffreyhodesphonarapp@gmail.com").append("&");
		builder.append("Passwd=bronarBRONAR").append("&");
		builder.append("accountType=HOSTED_OR_GOOGLE").append("&");
		builder.append("Google-cURL-Example").append("&");
		builder.append("service=ac2dm");


		byte[] postData = builder.toString().getBytes(UTF8);

		// Hit the dm URL.
		URL url = new URL("https://www.google.com/accounts/ClientLogin");

		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setDoOutput(true);
		conn.setUseCaches(false);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		conn.setRequestProperty("Content-Length", Integer.toString(postData.length));

		OutputStream out = conn.getOutputStream();
		out.write(postData);
		out.close();
		
		String resultString = "";
		Scanner scanner = new Scanner(conn.getInputStream());
		while (scanner.hasNextLine()) {
			String s = scanner.nextLine();
			resultString = resultString + s;
			if (s.startsWith("Auth=")) {
				return s;
			}
		}
		
		return resultString;
	}

}