package com.phonarapp.server;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.phonarapp.common.CommonApi;

@SuppressWarnings("serial")
public class LocationReportServlet extends HttpServlet {

	public static final String DATA = "data.";
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String number = req.getParameter(CommonApi.MY_NUMBER_PARAM);
		String target = req.getParameter(CommonApi.TARGET_NUMBER_PARAM);
		String latitude = req.getParameter(CommonApi.LATITUDE_PARAM);
		String longitude = req.getParameter(CommonApi.LONGITUDE_PARAM);
		String altitude = req.getParameter(CommonApi.ALTITUDE_PARAM);
		
		String regId = DataAccessLayer.getUserRegId(number);
		
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(DATA + CommonApi.RESPONSE_PARAM, CommonApi.RESULT_VALUE);
		map.put(DATA + CommonApi.TARGET_NUMBER_PARAM, target);
		map.put(DATA + CommonApi.LATITUDE_PARAM, latitude);
		map.put(DATA + CommonApi.LONGITUDE_PARAM, longitude);
		map.put(DATA + CommonApi.ALTITUDE_PARAM, altitude);
		
		boolean result = C2DMPusher.sendNoRetry(regId, "somthing", map, false);
		if (!result) {
			PhonarServerServlet.LOG.severe("HOLY SHIT RESULT FAILED");
		}
	}
	
}
