package com.phonarapp.server;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.phonarapp.common.CommonApi;

@SuppressWarnings("serial")
public class PhonarServerServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
		//String number = req.getParameter(CommonApi.MY_NUMBER_PARAM);
		String target = req.getParameter(CommonApi.TARGET_NUMBER_PARAM);
		
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("data.lol", "yoyojorge");
		
		C2DMPusher.sendNoRetry(DataAccessLayer.getUserRegId(target),
				"something", map, false );
		
	}
}
