package se.puggan.gscantool;

import android.support.v4.app.Fragment;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
//import android.os.Build;

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
	
    static final int TIME_OUT = 20000;
//    static final int TIME_OUT = 1000;
//    static final int ERROR_TIME_OUT = 5000;
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
			ii.initiateScan();;
		}
	};
    
    public void show_message(String message)
    {
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
//		handler.sendEmptyMessageDelayed(MSG_DISMISS_DIALOG, ERROR_TIME_OUT);
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        project_name = s(R.string.app_name);
        setContentView(R.layout.activity_gscan_tool);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.gscan_tool, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_gscan_tool, container, false);
            return rootView;
        }
    }

    public void init_scan(View view)
    {
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
                    handler.post(
                    	new Runnable()
                    	{
                    		@Override
                    		public void run()
                    		{
                    			java.net.URL current_url = null;
                    			java.io.BufferedReader current_url_in = null;
                    			StringBuilder current_page = new StringBuilder();
                    			
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
	
// Java can't return null
//									if(current_url == null)
//	                    			{
//										show_error("Bad URL\n" + result);
//										return;
//	                    			}
									
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
										show_error("Connection failed\n" + current_url.toString() + "\n" + e.getMessage());
										return;
									}
									
									String current_line = null;
									try
									{
										while((current_line = current_url_in.readLine()) != null)
										{
											current_page.append(current_line);
										}
										
										parseResult(current_page.toString(), current_url);
										
										return;
									}
									catch(java.io.IOException e)
									{
										show_error("Reading failed\n" + current_url.toString() + "\n" + e.getMessage());
										return;
									}
									catch (org.json.JSONException e)
									{
										show_error("JSON parsing failed\n" + current_url.toString() + "\n" + e.getMessage() + "\n" + current_page.toString());
										return;
									}
	                    		}
                    			else
                    			{
                    				org.apache.http.client.methods.HttpPost post = null;
                    				java.util.List<org.apache.http.NameValuePair> post_data_list = null;
                    				org.apache.http.client.HttpClient client = null;
                    				org.apache.http.HttpResponse response = null;
                    				org.apache.http.HttpEntity entity = null;
                    				String responseText = null;
                    				
                    				try
                    				{
	                    				post = new org.apache.http.client.methods.HttpPost(posturl.toString());
	                    				
	                    				//switch(posttype)
                    					//case "plain":
	                    				if(posttype == "plain")
	                    				{
    										post.setEntity(new org.apache.http.entity.StringEntity(result));
                    						//break;
	                    				}
                    					//case "json":
	                    				else if(posttype == "json")
	                    				{
	                    					org.json.JSONObject json_request;
	                    					json_request = new org.json.JSONObject();
	                    					json_request.put("data", result);
	                    					json_request.put("type", result_type);
	                    					post.setEntity(new org.apache.http.entity.StringEntity(json_request.toString()));
                    						//break;
	                    				}
                    					//case "get":
	                    				else if(posttype == "get")
	                    				{
	                    					java.net.URL geturl;
	                    					geturl = new java.net.URL(posturl, 
	                    						"?type=" + 
   	                    						java.net.URLEncoder.encode(result_type, "UTF-8") +
   	                    						"&data=" + 
   	                    						java.net.URLEncoder.encode(result, "UTF-8")
	                    						);
		                    				post = new org.apache.http.client.methods.HttpPost(geturl.toString());
                    						//break;
	                    				}
                    					//case "post":
                    					//default:
	                    				else
	                    				{
		                    				post_data_list= new java.util.ArrayList<org.apache.http.NameValuePair>(2);

		                    				post_data_list.add(new org.apache.http.message.BasicNameValuePair("data", result));
    	                    				post_data_list.add(new org.apache.http.message.BasicNameValuePair("type", result_type));
    										post.setEntity(new org.apache.http.client.entity.UrlEncodedFormEntity(post_data_list));
                    						//break;
	                    				}

	                    				client = new org.apache.http.impl.client.DefaultHttpClient();
	                    				response = client.execute(post);
	                    				entity = response.getEntity();
	
	                    				responseText = org.apache.http.util.EntityUtils.toString(entity);
	                    				
										parseResult(responseText, posturl);
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
                    				catch (org.apache.http.client.ClientProtocolException e)
									{
										show_error("Protocol failed\n" + posturl.toString() + "\n" + e.getMessage());
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
		}
		else if(posturl == null)
		{
			posturl = current_url;
		}
		
		if(json_result.has("type"))
		{
			posttype = json_result.getString("type");
		}
		
		if(json_result.has("text"))
		{
			message = json_result.getString("text");
		}

		show_message(message);
    }
}
