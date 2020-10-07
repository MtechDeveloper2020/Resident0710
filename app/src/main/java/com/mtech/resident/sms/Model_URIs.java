package com.mtech.resident.sms;

public class Model_URIs {

	String TransactionLog_URI;
	String Perform_URI;
	
	public Model_URIs(){

		//this.TransactionLog_URI = "http://ekyc.m-techinnovations.com/aadhaarAuthenticationAPI/AadhaarAuthenticationService.svc/GenerateOTPForAadhaarAuthenticationService";
//		this.TransactionLog_URI = "http://ekyc.m-techinnovations.com/aadhaarAuthenticationAPI/AadhaarAuthenticationService.svc/GenerateOTPAuthenticationService";
//
//		this.Perform_URI = "http://ekyc.m-techinnovations.com/aadhaarAuthenticationAPI/AadhaarAuthenticationService.svc/OTPAuthenticationService";

//		this.TransactionLog_URI = "http://ekyc.m-techinnovations.com/aadhaarAuthenticationAPI/AadhaarAuthenticationService.svc/GenerateOTPForAadhaarAuthenticationService";
//		this.Perform_URI = "http://ekyc.m-techinnovations.com/aadhaarAuthenticationAPI/AadhaarAuthenticationService.svc/PerformOTPAadhaarAuthenticationService";

		this.TransactionLog_URI = "http://ekyc.m-techinnovations.com/aadhaarAuthenticationAPI/AadhaarAuthenticationService.svc/GenerateOTPAndroidService";
		this.Perform_URI = "http://services.m-techinnovations.com/SMSSendService/SMSSendService.svc/SendSMSService";
	}

	public String getTransactionLog_URI() { return this.TransactionLog_URI; }
	public String getPerform_URI() { return this.Perform_URI; }

}
