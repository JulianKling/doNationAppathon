package appathon.donation;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by Sascha on 11/8/2015.
 */
class CheckDonation extends AsyncTask<Object, Integer, Object>
{
    protected String doInBackground(Object... params)
    {
        SelectActivity selectActivity = (SelectActivity) params[0];

        callAPI(selectActivity);

        return null;
    }

    protected void callAPI(SelectActivity selectActivity) {

        String id = selectActivity.getId();

        if (!id.equals("tbd_by_api_callback")) {
            try {
                Uri builtUri = Uri.parse("http://labs.casaro.de/doNation/get.php")
                        .buildUpon()
                        .appendQueryParameter("id", id)
                        .build();
                URL url = new URL(builtUri.toString());
                URLConnection urlConnection = url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                //try parse the reader to a XML object
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = factory.newDocumentBuilder();
                Document dom = db.parse(in);

                Node taker = dom.getElementsByTagName("taker").item(0);
                //String taker = dom.getElementsByTagName("taker").item(0).getFirstChild().getTextContent();
                if (taker != null && !taker.getTextContent().isEmpty()) {
                    selectActivity.receiveThanks(selectActivity, taker.getTextContent());
                } else {
                    Thread.sleep(1000);
                    callAPI(selectActivity);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else
        {
            try {
                Thread.sleep(1000);
                callAPI(selectActivity);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}