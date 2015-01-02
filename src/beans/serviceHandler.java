package beans;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import model.FileBodyData;
import model.StringBodyData;

public class serviceHandler {

	public final static int GET = 1;
	public final static int POST = 2;

	public serviceHandler() {
		// TODO Auto-generated constructor stub
	}

	public String makeServiceCall(String url, int method)
			throws ClientProtocolException, IOException {
		return makeServiceCall(url, method, null);
	}

	public String makeServiceCall(String url, int method,
			List<NameValuePair> params) throws ClientProtocolException,
			IOException {

		String response = null;

		HttpResponse httpResponse = null;
		HttpEntity httpEntity = null;

		DefaultHttpClient httpClient = new DefaultHttpClient();

		try {
			if (method == POST) {

				HttpPost httpPost = new HttpPost(url);
				if (params != null) {
					httpPost.setEntity(new UrlEncodedFormEntity(params));
				}
				httpResponse = httpClient.execute(httpPost);

			} else if (method == GET) {

				if (params != null) {
					String param = URLEncodedUtils.format(params, "utf-8");
					url += "?" + param;
				}

				HttpGet httpGet = new HttpGet(url);
				httpResponse = httpClient.execute(httpGet);
			}
		} catch (Exception e) {
			Log.i("HA", "" + e.getMessage());
		}

		httpEntity = httpResponse.getEntity();
		response = EntityUtils.toString(httpEntity);

		return response;

	}

	public String makeServiceCallFiles(String url,
			ArrayList<StringBodyData> stringBodyMap, ArrayList<FileBodyData> fileBodyMap)
			throws ClientProtocolException, IOException {

		String response = null;

		HttpResponse httpResponse = null;
		HttpEntity httpEntity = null;

		DefaultHttpClient httpClient = new DefaultHttpClient();

		//HttpClient httpClient = new DefaultHttpClient();

		try {

			HttpPost httpPost = new HttpPost(url);

			MultipartEntityBuilder builder = MultipartEntityBuilder.create();

			for (int i = 0; i < stringBodyMap.size(); i++) {
				builder.addPart(stringBodyMap.get(i).getKey(), stringBodyMap
						.get(i).getStringBody());
			}
			for (int i = 0; i < fileBodyMap.size(); i++) {
				builder.addPart(fileBodyMap.get(i).getKey(), fileBodyMap
						.get(i).getFileBody());
			}
			
			httpEntity = builder.build();
			
			httpPost.setEntity(httpEntity);
			
			httpResponse = httpClient.execute(httpPost);

		} catch (Exception e) {
			Log.i("HA", "" + e.getMessage());
		}

		httpEntity = httpResponse.getEntity();
		response = EntityUtils.toString(httpEntity);

		return response;

	}

}
