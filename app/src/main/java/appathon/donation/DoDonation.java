package appathon.donation;

import android.net.Uri;
import android.os.AsyncTask;

import org.w3c.dom.Document;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

// Sends information about the donated item to the server. The final list of available items will
// be shown online at http://labs.casaro.de/donation/overview.html.
// TODO: Each shop should only be able to see its own donated items.
class DoDonation extends AsyncTask<Object, Integer, Object>
{
    protected String doInBackground(Object... params)
    {
        // TODO Auto-generated method stub
        SelectActivity selectActivity = (SelectActivity) params[0];

        try
        {
            Uri builtUri = Uri.parse("http://labs.casaro.de/doNation/donate.php")
                    .buildUpon()
                    .appendQueryParameter("item", "Cappuccino")
                    .appendQueryParameter("percent", "100")
                    .appendQueryParameter("organization", "Münchner Cafe")
                    .appendQueryParameter("giver", "Steven")
                    .build();
            URL url = new URL(builtUri.toString());
            URLConnection urlConnection = url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());

            //try parse the reader to a XML object
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = factory.newDocumentBuilder();
            Document dom = db.parse(in);

            String id = dom.getElementsByTagName("id").item(0).getFirstChild().getTextContent();
            if (!id.isEmpty()) {
                selectActivity.setId(id);
            }
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }
}