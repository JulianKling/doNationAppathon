package appathon.donation;

import android.content.Context;
import android.os.AsyncTask;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

// Sends information about the donated item to the server. The final list of available items will
// be shown online at http://labs.casaro.de/donation/overview.html.
// TODO: Each shop should only be able to see its own donated items.
class DoPayment extends AsyncTask<Object, Integer, Object>
{
    protected String doInBackground(Object... params)
    {
        // TODO Auto-generated method stub
        SelectActivity selectActivity = (SelectActivity) params[0];
        final String accountId = "4711951501";

        try
        {

            Context context = selectActivity.getApplicationContext();
            RequestQueue queue = Volley.newRequestQueue(context);
            StringRequest sr = new StringRequest(Request.Method.POST,"https://api.figo.me/rest/accounts/" + "4711951501" + "/payments", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    // todo
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // todo
                }
            }){
                @Override
                protected Map<String,String> getParams(){
                    Map<String,String> params = new HashMap<String, String>();
                    params.put("account_number", "4711951501");
                    params.put("amount", "2.99");
                    params.put("bank_code", "90090042");
                    params.put("name", "figo");
                    params.put("purpose", "doGood");
                    params.put("type", "Transfer");

                    return params;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String,String> params = new HashMap<String, String>();
                    params.put("Content-Type","application/x-www-form-urlencoded");
                    return params;
                }
            };
            queue.add(sr);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }
}