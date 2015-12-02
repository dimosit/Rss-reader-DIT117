package assignment1.android.hua.gr.assignment1;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends ActionBarActivity {
    // flag for Internet connection status
    Boolean isInternetPresent;
    // Connection detector class
    ConnectionDetector cd;

    private enum RSSXMLTag {
        TITLE, IGNORETAG, DESCRIPTION;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // creating connection detector class instance
        cd = new ConnectionDetector(getApplicationContext());
        AlertDialog  aler = null;

        final EditText enterUrl = (EditText) findViewById(R.id.editTextUrl);
        Button showButton  = (Button) findViewById(R.id.buttonShow);

        showButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //toast parameters
                Context context = getApplicationContext();
                CharSequence errorMessage = "ERROR: No internet connection detected! Please connect to " +
                        "internet and connect to internet and reopen the app";
                CharSequence okMessage = "You are connected to internet";
                int okDuration = Toast.LENGTH_SHORT;
                int erroDuration = Toast.LENGTH_LONG;


                // get Internet status
                isInternetPresent = cd.isConnectingToInternet();

                // Internet Connection is Present
                if (isInternetPresent == true ) {

                    //show ok message
                    Toast.makeText(context, errorMessage, okDuration).show();
                }
                // Internet connection is not present
                if(isInternetPresent == false){

                    // Ask user to connect to Internet error message
                    Toast.makeText(context, okMessage, erroDuration).show();
                }

                //get url from editText
                String url = enterUrl.getText().toString();

                XmlParserTask mytask = new XmlParserTask(MainActivity.this);
                //execute background task(download and parse xml)
                mytask.execute(url);

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //create a inner class to get and parser xml using asyncTask(threads!)
    public class XmlParserTask extends AsyncTask<String , Void, ArrayList<RssItems> >{
        private ProgressDialog dialog;
        private RSSXMLTag currentTag;
        /** application context. */
        private Context context;


        public XmlParserTask(MainActivity activity) {
            this.context = activity;
            dialog = new ProgressDialog(context);
        }


        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Loading please wait...!");
            //show dialog in main activity
            this.dialog.show();
        }

        @Override
        protected ArrayList<RssItems> doInBackground(String... arg0){
            // TODO Auto-generated method stub
            String urlStr = arg0[0];
            InputStream is = null;
            ArrayList<RssItems> postDataList = new ArrayList<RssItems>();
            try {
                URL url = new URL(urlStr);
                HttpURLConnection connection = (HttpURLConnection) url
                        .openConnection();
                connection.setReadTimeout(10 * 1000);
                connection.setConnectTimeout(10 * 1000);
                connection.setRequestMethod("GET");
                connection.setDoInput(true);
                connection.connect();
                int response = connection.getResponseCode();
                Log.d("debug", "The response is: " + response);
                is = connection.getInputStream();

                // parse xml
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(true);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(is, null);

                //Returns the type of the current event (START_TAG, END_TAG, TEXT, etc.) D.A
                int eventType = xpp.getEventType();
                RssItems pdData = null;

                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_DOCUMENT) {

                    } else if (eventType == XmlPullParser.START_TAG) {
                        Log.w("warming", xpp.getName());
                        if (xpp.getName().equals("item")) {
                            pdData = new RssItems();
                            currentTag = RSSXMLTag.IGNORETAG;
                        } else if (xpp.getName().equals("title")) {
                            currentTag = RSSXMLTag.TITLE;
                        }else if (xpp.getName().equals("description")){
                            currentTag = RSSXMLTag.DESCRIPTION;
                        }
                    } else if (eventType == XmlPullParser.END_TAG) {
                        if (xpp.getName().equals("item")) {
                            postDataList.add(pdData);
                        } else {
                            currentTag = RSSXMLTag.IGNORETAG;
                        }
                    } else if (eventType == XmlPullParser.TEXT) {
                        String content = xpp.getText();

                        //remove  spaces
                        content = content.trim();
                        Log.d("debug", content);
                        if (pdData != null) {
                            switch (currentTag) {
                                case TITLE:
                                    if (content.length() != 0){
                                        pdData.setPostTitle(content);
                                    }
                                    //debug
                                    System.out.println("--------------------------");
                                    Log.d("Title:", content);
                                    break;


                                case DESCRIPTION:
                                    if(content.length() != 0){
                                        pdData.setPostDescription(content);

                                    }
                                    //repeated elements bug!!!
                                    //postDataList.add(pdData);
                                    //debug
                                    System.out.println("--------------------------");
                                    System.out.println("--------------------------");
                                    System.out.println("--------------------------");
                                    Log.d("Description:", content);

                                default:
                                    break;
                            }
                        }
                    }

                    eventType = xpp.next();
                }
                Log.v("Size of ArrayList = ", String.valueOf((postDataList.size())));
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return postDataList;
        }



        @Override
        protected void onPostExecute(ArrayList<RssItems> rssObjects) {
            super.onPostExecute(rssObjects);
            Bundle bundle = new Bundle();
            //put data to bundle
            bundle.putSerializable("rssBundel", (Serializable) rssObjects);

            Intent i  = new Intent();
            //start the second activity with custom action
            i.setAction("gr.hua.android.assignment1.RSSpresentation");
            i.putExtras(bundle);

            //start SecondActivity
            startActivity(i);
            //end dialog
            dialog.dismiss();
        }
    }


}
