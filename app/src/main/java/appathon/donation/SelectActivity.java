package appathon.donation;

import android.content.Intent;
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

public class SelectActivity extends AppCompatActivity {

    public static boolean hasDonated = false;
    public int percentage = 0;
    public static double money = 2.99;

    private boolean pressed = false;
    private int layout_height = 0;
    private int layout_width = 0;

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
            final ImageButton button3 = (ImageButton) findViewById(R.id.imageView_color);
            final Button button4 = (Button) findViewById(R.id.button4);
            donateButton.setEnabled(false);
            button3.setEnabled(false);
            button4.setEnabled(false);
            donateButton.setText("You just donated :)");
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

        Intent intent = new Intent(this, PaymentActivity.class);
        startActivity(intent);
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
        percentageText.setText(percentage + "%; " + String.format("%.2f", money*percentage/100) + "€/" + money + "€");
    }
}
