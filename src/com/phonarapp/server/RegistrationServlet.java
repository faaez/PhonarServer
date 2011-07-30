package com.phonarapp.server;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.phonarapp.common.CommonApi;

@SuppressWarnings("serial")
public class RegistrationServlet extends HttpServlet {
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) {
		String number = req.getParameter(CommonApi.MY_NUMBER_PARAM);
		String registrationId = req.getParameter(CommonApi.REGISTRATION_ID_PARAM);
		
		DataAccessLayer.registerUser(number, registrationId);
	}
}
