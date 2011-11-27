package com.phonarapp.server;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.phonarapp.common.CommonApi;

@SuppressWarnings("serial")
public class PhonarServerServlet extends HttpServlet {
	
	public static final Logger LOG 
		= Logger.getLogger(PhonarServerServlet.class.getName());
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
		String number = req.getParameter(CommonApi.MY_NUMBER_PARAM);
		String target = req.getParameter(CommonApi.TARGET_NUMBER_PARAM);
		
		LOG.severe(number + " " + target);
		
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(C2DMPusher.DATA + CommonApi.RESPONSE_PARAM, 
				CommonApi.REQUEST_VALUE);
		map.put(C2DMPusher.DATA + CommonApi.MY_NUMBER_PARAM, number);
		
		String regId = DataAccessLayer.getUserRegId(target);
		
		if (regId == null) {
			int x = 1 / 0;
		} else {
			boolean result 
				= C2DMPusher.sendNoRetry(regId, "something", map, false );
			if (!result) {
				LOG.severe("HOLY SHIT BROKEN REQUEST");
			}
		}
	}
}
