package com.phonarapp.server;

import java.io.IOException;
import javax.servlet.http.*;

import com.location.phonar.common.CommonApi;

@SuppressWarnings("serial")
public class PhonarServerServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
		String number = req.getParameter(CommonApi.MY_NUMBER_PARAM);
		String target = req.getParameter(CommonApi.TARGET_NUMBER_PARAM);
		
		resp.setContentType("text/plain");
		resp.getWriter().println(number + " wants to know where " + target + " is.");
	}
}
