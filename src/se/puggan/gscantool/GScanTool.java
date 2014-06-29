package se.puggan.gscantool;

import android.annotation.SuppressLint;
import android.util.Log;

public class GScanTool extends android.support.v7.app.ActionBarActivity
{
	final android.content.Context context = this;
	private com.google.zxing.integration.android.IntentIntegrator ii = new com.google.zxing.integration.android.IntentIntegrator(GScanTool.this);
	private android.app.AlertDialog alertDialog;
	private String project_name = null;
	private String result = null;
	private String result_type = null;
	private java.net.URL posturl = null;
	private String posttype = null;

	static final int TIME_OUT = 3000;
	static final int MSG_DISMISS_DIALOG = 0;

	public String s(int string_id)
	{
		return getResources().getString(string_id);
	}

	@SuppressLint("HandlerLeak")
	private android.os.Handler handler = new android.os.Handler()
	{
		public void handleMessage(android.os.Message msg)
		{
			switch (msg.what)
			{
				case MSG_DISMISS_DIALOG:
				{
					if (alertDialog != null && alertDialog.isShowing())
					{
						try
						{
							alertDialog.dismiss();
						}
						catch(IllegalArgumentException e)
						{

						}
						alertDialog = null;
						ii.initiateScan();
					}
					break;
				}

				default:
				{
					break;
				}
			}
		}
	};

	android.content.DialogInterface.OnClickListener clickListner = new android.content.DialogInterface.OnClickListener()
	{
		public void onClick(android.content.DialogInterface dialog, int id)
		{
			ii.initiateScan();
		}
	};

	public void show_message(String message)
	{
Log.d("GScanTool", "show message: " + message);
		android.app.AlertDialog.Builder alertDialogBuilder;

		if (alertDialog != null && alertDialog.isShowing())
		{
			alertDialog.dismiss();
			alertDialog = null;
		}

		alertDialogBuilder = new android.app.AlertDialog.Builder(context);
		alertDialogBuilder.setTitle(project_name);
		alertDialogBuilder.setMessage(message);
		alertDialogBuilder.setPositiveButton(R.string.messageButton, clickListner);
		alertDialog = alertDialogBuilder.create();
		alertDialog.show();
		handler.sendEmptyMessageDelayed(MSG_DISMISS_DIALOG, TIME_OUT);
	}

	public void show_error(String message)
	{
Log.d("GScanTool", "show error: " + message);
		android.app.AlertDialog.Builder alertDialogBuilder;

		if (alertDialog != null && alertDialog.isShowing())
		{
			alertDialog.dismiss();
			alertDialog = null;
		}

		alertDialogBuilder = new android.app.AlertDialog.Builder(context);
		alertDialogBuilder.setTitle(R.string.errorTitle);
		alertDialogBuilder.setMessage(message);
		alertDialogBuilder.setPositiveButton(R.string.errorButton, clickListner);

		alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}

	@Override
	protected void onCreate(android.os.Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		// super.onCreate(null);

		project_name = s(R.string.app_name);
		setContentView(R.layout.activity_gscan_tool);

		if (savedInstanceState == null)
		{
			getSupportFragmentManager().beginTransaction()
				.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	protected void onStart()
	{
		super.onStart();

		reset_view();
	}

	public void reset_view()
	{
		GScanSettings settings;
		settings = getsettings();

		android.widget.Button b = (android.widget.Button) findViewById(R.id.projectButton);
		android.widget.TextView tv = (android.widget.TextView) findViewById(R.id.textInstructionProject);

		if ((settings.name != null) && (settings.name.length() > 0))
		{
Log.w("GScanTool", "Setting name:" + settings.name);
			project_name = settings.name;
			b.setText(settings.name);
			tv.setText(R.string.about_project);
		}
		else
		{
			project_name = null;
			b.setText(R.string.no_project);
			tv.setText(R.string.about_no_project);
		}
	}


	@Override
	public boolean onCreateOptionsMenu(android.view.Menu menu)
	{
		getMenuInflater().inflate(R.menu.gscan_tool, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(android.view.MenuItem item)
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		//int id = item.getItemId();
//        if (id == R.id.action_settings) {
//            return true;
//        }
		return super.onOptionsItemSelected(item);
	}

	/**
	* A placeholder fragment containing a simple view.
	*/
	public static class PlaceholderFragment extends
			android.support.v4.app.Fragment
	{

		public PlaceholderFragment() {
		}

		@Override
		public android.view.View onCreateView(
				android.view.LayoutInflater inflater,
				android.view.ViewGroup container,
				android.os.Bundle savedInstanceState)
		{
			android.view.View rootView = inflater.inflate(
					R.layout.fragment_gscan_tool, container, false);
			return rootView;
		}
	}

	public void init_scan(android.view.View view)
	{
		ii.initiateScan();
	}

	public void reset_scan(android.view.View view)
	{
		setsettings("", "", "");
		reset_view();
		ii.initiateScan();
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, android.content.Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode)
		{
			case com.google.zxing.integration.android.IntentIntegrator.REQUEST_CODE:
			{
				com.google.zxing.integration.android.IntentResult scanResult;
				scanResult = com.google.zxing.integration.android.IntentIntegrator.parseActivityResult(
						requestCode,
						resultCode,
						data);
				if (scanResult == null)
				{
					return;
				}

				result = scanResult.getContents();
				result_type = scanResult.getFormatName();
				if (result != null)
				{
Log.d("GScanTool", "Scanned: " + result);
					handler.post(
						new Runnable()
						{
							@Override
							public void run()
							{
								java.net.URL current_url = null;

								GScanSettings settings;
								settings = getsettings();
								if ((settings.url != null) && (settings.url.length() > 0))
								{
									try
									{
										posturl = new java.net.URL(settings.url);
									}
									catch (java.net.MalformedURLException e)
									{
										posturl = null;
									}
								}
								if ((settings.type != null)
										&& (settings.type.length() > 0))
								{
									posttype = settings.type;
								}
								else
								{
									posttype = null;
								}

								if(posturl == null)
								{
									try
									{
										current_url = new java.net.URL(result);
									}
									catch (java.net.MalformedURLException e)
									{
										show_error("MalformedURLException\n" + e.getMessage());
										return;
									}

									new HttpGet().execute(current_url);
								}
								else
								{
									org.apache.http.client.methods.HttpPost post = null;
									String responseText = null;

									try
									{
										post = new org.apache.http.client.methods.HttpPost(posturl.toString());

										//switch(posttype)
										//case "plain":
										if (posttype != null && posttype.equals("plain"))
										{
											post.setEntity(new org.apache.http.entity.StringEntity(result));
											new HttpPost().execute(post);
											//break;
										}
										//case "json":
										else if (posttype != null && posttype.equals("json"))
										{
											org.json.JSONObject json_request;
											json_request = new org.json.JSONObject();
											json_request.put("data", result);
											json_request.put("type", result_type);
											post.setEntity(new org.apache.http.entity.StringEntity(json_request.toString()));
											new HttpPost().execute(post);
											//break;
										}
										//case "get":
										else if (posttype != null && posttype.equals("get"))
										{
											java.net.URL geturl;
											StringBuilder geturl_string = new StringBuilder(posturl.toString());

											if(posturl.getQuery() != null)
											{
												geturl_string.append('&');
											}
											else
											{
												geturl_string.append('?');
											}

											geturl_string.append(
												"type=" +
												java.net.URLEncoder.encode(result_type, "UTF-8") +
												"&" +
												"data=" +
												java.net.URLEncoder.encode(result, "UTF-8")
												);
											geturl = new java.net.URL(geturl_string.toString());
											//post = new org.apache.http.client.methods.HttpPost(geturl.toString());
											new HttpGet().execute(geturl);
											//break;
										}
										//case "post":
										//default:
										else
										{
											java.util.List<org.apache.http.NameValuePair> post_data_list = null;
											post_data_list= new java.util.ArrayList<org.apache.http.NameValuePair>(2);

											post_data_list.add(new org.apache.http.message.BasicNameValuePair("data", result));
											post_data_list.add(new org.apache.http.message.BasicNameValuePair("type", result_type));
											post.setEntity(new org.apache.http.client.entity.UrlEncodedFormEntity(post_data_list));
											new HttpPost().execute(post);
											//break;
										}
									}
									catch (org.json.JSONException e)
									{
										show_error("JSON parsing failed\n" + posturl.toString() + "\n" + e.getMessage() + "\n" + responseText);
										return;
									}
									catch (java.io.UnsupportedEncodingException e)
									{
										show_error("HTTP-Post encoding failed\n" + e.getMessage());
										return;
									}
									catch (java.io.IOException e)
									{
										show_error("Connection read failed\n" + posturl.toString() + "\n" + e.getMessage());
										return;
									}
								}
							}
						}
					);
				}
				break;
			}
			default:
			{
				break;
			}
		}
	}

	private void parseResult(String result, java.net.URL current_url) throws org.json.JSONException
	{
		Boolean settings_changed = false;
		String message = null;
		org.json.JSONObject json_result;
		json_result = new org.json.JSONObject(result);

		if(json_result.has("name"))
		{
			project_name = json_result.getString("name");
//			message = "Project " + project_name + " loaded";
			message = String.format(
				s(R.string.messageLoaded),
				project_name
				);
			settings_changed = true;
		}

		if(json_result.has("url"))
		{
			try
			{
				posturl = new java.net.URL(json_result.getString("url"));

				if(message == null)
				{
					//message = "GScanTool connected to " + json_result.getString("url");
					message = String.format(
							s(R.string.messageLoadedUrl),
							json_result.getString("url")
							);
				}
			}
			catch (java.net.MalformedURLException e)
			{
				show_error("MalformedURLException\n" + e.getMessage());
				return;
			}
			settings_changed = true;
		}
		else if(posturl == null)
		{
			posturl = current_url;
		}

		if(json_result.has("type"))
		{
			posttype = json_result.getString("type");
		}

		if(settings_changed)
		{
			setsettings(project_name, posturl.toString(), posttype);

			reset_view();
		}

		if(json_result.has("text"))
		{
			message = json_result.getString("text");
		}

		show_message(message);
	}

	public class BackgroundResponse
	{
		public java.net.URL url;
		public String text;
		public int mode; // 0 = ok, 1 = error

		public BackgroundResponse(java.net.URL new_url, String new_text, int new_mode)
		{
			url = new_url;
			text = new_text;
			mode = new_mode;
		}
	}

	private class HttpGet extends android.os.AsyncTask<java.net.URL, Integer, BackgroundResponse>
	{
		protected BackgroundResponse doInBackground(java.net.URL... current_urls)
		{
			java.net.URL current_url;

			current_url = current_urls[0];
Log.d("GScanTool", "HttpGet: " + current_url.toString());

			java.io.BufferedReader current_url_in = null;
			StringBuilder current_page = new StringBuilder();

			try
			{
				current_url_in =
					new java.io.BufferedReader(
						new java.io.InputStreamReader(
								current_url.openStream()
						)
					);
			}
			catch(java.io.IOException e)
			{
				return new BackgroundResponse(current_url, "Connection failed\n" + current_url.toString() + "\n" + e.getMessage(), 1);
			}

			String current_line = null;
			try
			{
				while((current_line = current_url_in.readLine()) != null)
				{
					current_page.append(current_line);
				}
			}
			catch(java.io.IOException e)
			{
				return new BackgroundResponse(current_url, "Reading failed\n" + current_url.toString() + "\n" + e.getMessage(), 1);
			}

			return new BackgroundResponse(current_url, current_page.toString(), 0);
		}

		// This is called when doInBackground() is finished
		protected void onPostExecute(BackgroundResponse response)
		{
			if(response.mode == 0)
			{
				try
				{
					parseResult(response.text, response.url);
				}
				catch (org.json.JSONException e)
				{
					show_error("JSON parsing failed\n" + response.url.toString() + "\n" + e.getMessage() + "\n" + response.text);
					return;
				}
			}
			else
			{
				show_error(response.text);
				return;
			}
		}
	}

	private class HttpPost extends android.os.AsyncTask<org.apache.http.client.methods.HttpPost, Integer, BackgroundResponse>
	{
		protected BackgroundResponse doInBackground(org.apache.http.client.methods.HttpPost... current_posts)
		{
try {
			org.apache.http.client.HttpClient client = null;
			org.apache.http.HttpResponse response = null;
			org.apache.http.HttpEntity entity = null;
			String responseText = null;
			java.net.URL current_url = null;
			org.apache.http.client.methods.HttpPost current_post;

			current_post = current_posts[0];

			try
			{
				current_url = current_post.getURI().toURL();
Log.d("GScanTool", "HttpPost: " + current_url.toString());
				client = new org.apache.http.impl.client.DefaultHttpClient();
				response = client.execute(current_post);
				entity = response.getEntity();

				responseText = org.apache.http.util.EntityUtils.toString(entity);
			}
			catch(java.io.IOException e)
			{
				return new BackgroundResponse(current_url, "Connection failed\n" + current_url.toString() + "\n" + e.getMessage(), 1);
			}

			return new BackgroundResponse(current_url, responseText, 0);
} catch (Exception e) { e.printStackTrace(); Log.e("GScanTool", "Exception A: " + e.getMessage()); return null;}
		}

		// This is called when doInBackground() is finished
		protected void onPostExecute(BackgroundResponse response)
		{
try {
			if(response.mode == 0)
			{
				try
				{
					parseResult(response.text, response.url);
				}
				catch (org.json.JSONException e)
				{
					show_error("JSON parsing failed\n" + response.url.toString() + "\n" + e.getMessage() + "\n" + response.text);
					return;
				}
			}
			else
			{
				show_error(response.text);
				return;
			}
} catch (Exception e) { e.printStackTrace(); Log.e("GScanTool", "Exception B: " + e.getMessage()); return;}
		}

	}

	public GScanSettings getsettings()
	{
		GScanSettings result = new GScanSettings("", "", "");

		android.content.SharedPreferences settings = getSharedPreferences(
				"GScanTool", 0);
		result.name = settings.getString("name", "");
		result.url = settings.getString("url", "");
		result.type = settings.getString("type", "");

		return result;
	}

	public void setsettings(String new_name, String new_url, String new_type)
	{
		android.content.SharedPreferences settings = getSharedPreferences(
				"GScanTool", 0);
		android.content.SharedPreferences.Editor set_ed = settings.edit();
		set_ed.putString("name", new_name);
		set_ed.putString("url", new_url);
		set_ed.putString("type", new_type);
		set_ed.commit();
	}

	public class GScanSettings
	{
		public String name;
		public String url;
		public String type;

		public GScanSettings(String new_url, String new_name, String new_type)
		{
			name = new_name;
			url = new_url;
			type = new_type;
		}
	}
}

