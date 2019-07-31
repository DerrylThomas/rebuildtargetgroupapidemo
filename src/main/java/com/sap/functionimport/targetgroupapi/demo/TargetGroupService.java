package com.sap.functionimport.targetgroupapi.demo;

import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TargetGroupService {
	private final String csrfTokenUrlTemplate = "%s/sap/opu/odata/SAP/API_MKT_TARGET_GROUP_SRV/$metadata";
	private final String rebuildTargetGroupURLTemplate = "%s/sap/opu/odata/SAP/API_MKT_TARGET_GROUP_SRV/RebuildTargetGroup?TargetGroupUUID=guid'%s'";
	private String csrfToken;
	private List<String> cookie;

	public ResponseEntity<String> rebuildTargetGroup(TargetGroupRequestEntity targetGroupRequestEntity,
			String authorization) {

		// Calls to the marketing system are authenticated by an x-csrf-token and a
		// corresponding cookie passed as a header.
		// Hence, first we need to obtaining the csrf token and cookie before calling
		// the actual marketing endpoint.
		getCsrfTokenAndCookie(authorization, targetGroupRequestEntity.getSystemURL());

		// Preparing the rebuildTargetGroupURL according to the template.
		// See <a
		// href="https://help.sap.com/viewer/0f9408e4921e4ba3bb4a7a1f75f837a7/1905.500/en-US/97ed2d7497c94d8589082a56b10ac9d1.html#loio97ed2d7497c94d8589082a56b10ac9d1__entity_targetgroups">https://help.sap.com/viewer/0f9408e4921e4ba3bb4a7a1f75f837a7/1905.500/en-US/97ed2d7497c94d8589082a56b10ac9d1.html#loio97ed2d7497c94d8589082a56b10ac9d1__entity_targetgroups</a>
		// for details about the target group API
		String rebuildTargetGroupURL = String.format(rebuildTargetGroupURLTemplate,
				targetGroupRequestEntity.getSystemURL(), targetGroupRequestEntity.getTargetGroupUUID());

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.add("x-csrf-token", csrfToken);
		headers.addAll("Cookie", cookie);
		HttpEntity<String> rebuildTargetGroupRequest = new HttpEntity<String>(headers);
		ResponseEntity<String> rebuildTargetGroupResponse = restTemplate.exchange(rebuildTargetGroupURL,
				HttpMethod.POST, rebuildTargetGroupRequest, String.class);
		return rebuildTargetGroupResponse;

	}

//	This method gets the x-csrf-token and cookie from the marketing system by calling the /$metadata endpoint of the API
//	that we are trying to use.
//	This call is authenticated using basic auth with the communication user details which is configured in the marketing system.
//  You have to add a header with key=x-csrf-token and value=fetch while making this call. This tells the server to send you back a csrf token.
	public void getCsrfTokenAndCookie(String authorization, String systemURL) {

		// Preparing the csrfTokenUrl according to the template
		String csrfTokenUrl = String.format(csrfTokenUrlTemplate, systemURL);
		
		// Bypassing SSL as we are calling a trusted marketing server. Do not do this in
		// production code.
		byPassSecureSocketLayer();

		// Adding the basic auth and x-csrf-token headers for the call.
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", authorization);
		headers.add("x-csrf-token", "fetch");
		HttpEntity<String> csrfTokenRequest = new HttpEntity<String>(headers);

		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> csrfTokenResponse = restTemplate.exchange(csrfTokenUrl, HttpMethod.HEAD,
				csrfTokenRequest, String.class);
		csrfToken = csrfTokenResponse.getHeaders().getFirst("x-csrf-token");
		cookie = csrfTokenResponse.getHeaders().get("set-cookie");

	}

	public static void byPassSecureSocketLayer() {
		try {
			HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			});
			SSLContext context = SSLContext.getInstance("TLS");
			context.init(null, new X509TrustManager[] { new X509TrustManager() {
				public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				}

				public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				}

				public X509Certificate[] getAcceptedIssuers() {
					return new X509Certificate[0];
				}
			} }, new SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
