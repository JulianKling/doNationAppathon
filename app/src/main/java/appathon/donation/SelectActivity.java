package appathon.donation;

import android.os.Handler;
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


public class SelectActivity extends AppCompatActivity {

    // Marks if the user has already donated. UI depends on this.
    public static boolean hasDonated = false;
    // The percentage of the product the user wants to donate.
    public int percentage = 0;
    // The money the product is worth.
    // TODO: Get this from the code in the scanned logo. Hardcoded only for demonstration.
    public static double money = 2.99;
    // The ID of the product.
    // TODO: Get this from the code in the scanned logo. Hardcoded only for demonstration.
    public static String productId = null;

    // The height of the image representing the product. Needed for UI.
    private int layout_height = 0;
    // The width of the image representing the product. Needed for UI.
    private int layout_width = 0;
    // An ID that is stored to make the "thank you" messages possible.
    private String id;

    private Handler repeatUpdateHandler = new Handler();
    private boolean mAutoIncrement = false;

    /**
     * Created by jkling on 07.11.15.
     * This class runs in a separate thread and provides the
     * possibility to press a button continuously.
     *
     * Based on: http://stackoverflow.com/questions/
     * 7938516/continuously-increase-integer-value-as-the-button-is-pressed
     */
    class RptUpdater implements Runnable {
        public void run() {
            if( mAutoIncrement ){
                doPlus((View)null);
                repeatUpdateHandler.postDelayed( new RptUpdater(),50);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        final ImageButton mBTIncrement = (ImageButton) findViewById(R.id.imageView_press);

        mBTIncrement.getContext();
        mBTIncrement.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mAutoIncrement = true;
                repeatUpdateHandler.post(new RptUpdater());
                return false;
            }
        });

        mBTIncrement.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if ((event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL)
                        && mAutoIncrement) {
                    mAutoIncrement = false;
                }
                return false;
            }
        });
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
            final ImageView image_powered = (ImageView) findViewById(R.id.imageView_powered);
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

            image_powered.setVisibility(View.VISIBLE);
            RelativeLayout.LayoutParams parms = (RelativeLayout.LayoutParams)image_powered.getLayoutParams();
            parms.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
            image_powered.requestLayout();

            // call API to donate
            id = "tbd_by_api_callback";
            new DoDonation().execute(this);
            new DoPayment().execute(this);

            // call API to check if donation was collected
            // will be replaced by push notification
            new CheckDonation().execute(this, id);

        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        updateSizeInfo();
        updateValue();
    }

    // Update the size of the relative layout, because this is needed to hide certain percentage of
    // the picture.
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

    // Adds 5% to the donated product.
    public void doPlus(View view) {
        if (percentage < 100) {
            percentage += 2;
        }

        updateValue();
    }

    // Sets the percentage of payed product to zero; giving the user an option for corrections.
    public void doReset(View view) {
        percentage = 0;
        updateValue();
    }


    // Opens a dialogue to let the user confirm the payment.
    // Not needed technically, but because the money should not be gone after one click only.
    public void goToPayment(View view) {
        DialogFragment dialogFragment = new AcceptPaymentDialog();
        dialogFragment.show(getSupportFragmentManager(), "AcceptDonation");
    }

    // Actualizes UI for change of percentage.
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

    // Receives a notification because someone collected the donated product.
    public void receiveThanks(final SelectActivity selectActivity, final String taker, final String product) {

        selectActivity.runOnUiThread(new Runnable() {
            public void run() {
                CharSequence text = "Your " + product + " was enjoyed by " + taker + ". You are too good!";
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
