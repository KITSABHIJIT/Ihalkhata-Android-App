package com.example.ihalkhata.utils;

import java.util.List;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class WebServiceConnect {
	public static String connectWebservice(String SOAP_ACTION, String METHOD_NAME,
			List<Parameter> paramList) {
		String result = null;
		SoapObject request = new SoapObject("WebServiceConstants.NAMESPACE", METHOD_NAME);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		for (Parameter p : paramList)
			request.addProperty(p.getParamKey(), p.getParamValue());
		envelope.setOutputSoapObject(request);
		HttpTransportSE androidHttpTransport = new HttpTransportSE("WebServiceConstants.URL");

		try {
			androidHttpTransport.call(SOAP_ACTION, envelope);
			// SoapObject resultsRequestSOAP = (SoapObject) envelope.bodyIn;
			Object resultsRequestSOAP = (Object) envelope.getResponse();
			result = resultsRequestSOAP.toString();
			System.out.println("Received :" + resultsRequestSOAP.toString());
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return result;
	}
}
