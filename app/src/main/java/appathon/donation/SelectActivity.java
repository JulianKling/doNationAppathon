package appathon.donation;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Timer;
import java.util.TimerTask;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class SelectActivity extends AppCompatActivity {

    public static boolean hasDonated = false;
    public int percentage = 0;
    public static double money = 2.99;
    public static String productId = null;

    private boolean pressed = false;
    private int layout_height = 0;
    private int layout_width = 0;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        updateValue();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_select, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();  // Always call the superclass method first

        if (hasDonated) {
            final Button donateButton = (Button) findViewById(R.id.buttonDonate);
            final ImageButton resetButton = (ImageButton) findViewById(R.id.imageButton_arrow);
            final ImageView image_bw = (ImageView) findViewById(R.id.imageView_blackAndWhite);
            final ImageButton button3 = (ImageButton) findViewById(R.id.imageView_color);
            final RelativeLayout textView_thanks = (RelativeLayout) findViewById(R.id.relativeLayout_thanks);
            final LinearLayout linearLayout_payment = (LinearLayout) findViewById(R.id.linearLayout_payment);
            donateButton.setEnabled(false);
            donateButton.setVisibility(View.INVISIBLE);
            resetButton.setEnabled(false);
            linearLayout_payment.setVisibility(View.INVISIBLE);
            button3.setEnabled(false);
            donateButton.setText("you just donated");
            textView_thanks.setVisibility(View.VISIBLE);
            image_bw.setVisibility(View.INVISIBLE);
            //donateButton.setTextColor(getColorStateList());

            // call API to donate
            id = "tbd_by_api_callback";
            new DoDonation().execute(this);

            // call API to check if donation was collected
            // will be replaced by push notification
            new CheckDonation().execute(this, id);
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        // TODO Auto-generated method stub
        super.onWindowFocusChanged(hasFocus);
        updateSizeInfo();
        updateValue();
    }

    private void updateSizeInfo() {
        RelativeLayout rl_cards_details_card_area = (RelativeLayout) findViewById(R.id.imageLayout);
        layout_width = rl_cards_details_card_area.getWidth();
        layout_height = rl_cards_details_card_area.getHeight();
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

    public void doPlus(View view) {
        if (percentage < 100) {
            percentage += 5;
        }

        updateValue();
    }

    public void doReset(View view) {
        percentage = 0;
        updateValue();
    }


    public void goToPayment(View view) {

        DialogFragment dialogFragment = new AcceptPaymentDialog();
        dialogFragment.show(getSupportFragmentManager(), "AcceptDonation");

        //Intent intent = new Intent(this, PaymentActivity.class);
        //startActivity(intent);
    }

    private void updateValue() {

        updateSizeInfo();
        final ImageView bwImage = (ImageView) findViewById(R.id.imageView_blackAndWhite);
        int height =  (int) (layout_height * (100 - percentage) / 100);
        System.out.print("height: " + height);
        RelativeLayout.LayoutParams parms = (RelativeLayout.LayoutParams)bwImage.getLayoutParams();
        parms.height = height;
        bwImage.requestLayout();

        final TextView percentageText = (TextView) findViewById(R.id.textView);
        final TextView paymentText = (TextView) findViewById(R.id.textView_payment);
        percentageText.setText(percentage + "%");
        paymentText.setText("you pay " + String.format("%.2f", money * percentage / 100) + "€");
    }

    public void receiveThanks(final SelectActivity selectActivity, final String taker) {

        selectActivity.runOnUiThread(new Runnable() {
            public void run() {
                CharSequence text = "Your donation was enjoyed by " + taker + ". You are too good!";
                Toast.makeText(selectActivity.getBaseContext(), text, Toast.LENGTH_LONG).show();
            }
        });

        // resetting id
        this.id = "";
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
