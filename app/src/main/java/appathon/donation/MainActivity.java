package appathon.donation;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import net.gini.android.vision.BitmapFuture;
import net.gini.android.vision.CaptureActivity;
import net.gini.android.vision.DocumentType;
import net.gini.android.vision.ScannerActivity;
import net.gini.android.vision.ScannerActivityDelegate;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    protected static final int IMAGE_REQUEST = 1;
    protected boolean shouldStoreOriginal = false;
    protected boolean shouldStoreRectified = false;

    private String getImageFilename() {
        return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:s", Locale.GERMANY).format(new Date());
    }

    protected String storeImage(final Bitmap image, final String filename) {
        final File storageDirectory = getExternalFilesDir(null);
        final File imageFile = new File(storageDirectory, filename);
        try {
            final FileOutputStream outputStream = new FileOutputStream(imageFile);
            image.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast toast = Toast.makeText(this, "Could not save image", Toast.LENGTH_LONG);
            toast.show();
        }
        return imageFile.toString();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        String ocrString = "test";
//        SelectActivity.hasDonated = false;
//        SelectActivity.productId = ocrString;
//        Intent intent = new Intent(this, SelectActivity.class);
//        startActivity(intent);

        // Call Gini scanner
        Intent scanIntent = new Intent(this, ScannerActivity.class);
        scanIntent.putExtra(ScannerActivity.EXTRA_STORE_ORIGINAL, shouldStoreOriginal);
        scanIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        final Bundle docTypeBundle = new Bundle();
        docTypeBundle.putParcelable(ScannerActivity.EXTRA_DOCTYPE, DocumentType.INVOICE);
        scanIntent.putExtra(ScannerActivity.EXTRA_DOCTYPE_BUNDLE, docTypeBundle);
        ScannerActivity.setUploadActivityExtra(scanIntent, this, UploadActivity.class);
        startActivityForResult(scanIntent, IMAGE_REQUEST);

        UploadActivity.doneOnce = false;
        this.finish();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                Intent mInHome = new Intent(MainActivity.this, SelectActivity.class);
                MainActivity.this.startActivity(mInHome);
                MainActivity.this.finish();
            }
        }, 7000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bundle documentBundle;
        BitmapFuture originalFuture = null;
        BitmapFuture rectifiedFuture = null;

        if (requestCode == IMAGE_REQUEST && data != null) {
            documentBundle = data.getBundleExtra(ScannerActivity.EXTRA_DOCUMENT_BUNDLE);
            if (documentBundle != null) {
                originalFuture = documentBundle.getParcelable(CaptureActivity.EXTRA_ORIGINAL);
                rectifiedFuture = documentBundle.getParcelable(CaptureActivity.EXTRA_DOCUMENT);
            }
        }

        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK) {
            final String imageFilename = getImageFilename();

            if (shouldStoreOriginal && originalFuture != null) {
                storeImage(originalFuture.get(), imageFilename + "_original.jpg");
            }
            if (shouldStoreRectified && rectifiedFuture != null) {
                storeImage(rectifiedFuture.get(), imageFilename + "_rectified.jpg");
            }

            String ocrString = data.getStringExtra(UploadActivity.EXTRA_OCR_STRING);
            SelectActivity.hasDonated = false;
            SelectActivity.productId = ocrString;
            Intent intent = new Intent(this, SelectActivity.class);
            startActivity(intent);
        } else if (requestCode == IMAGE_REQUEST && resultCode == ScannerActivity.RESULT_ERROR) {
            final ScannerActivityDelegate.Error error = data.getParcelableExtra(ScannerActivity.EXTRA_ERROR);
            final Toast toast = Toast.makeText(this, "Error! " + error.toString(), Toast.LENGTH_LONG);
            toast.show();
        } else if (requestCode == IMAGE_REQUEST && resultCode == UploadActivity.RESULT_UPLOAD_ERROR) {
            final String error = data.getStringExtra(UploadActivity.EXTRA_ERROR_STRING);
            final Toast toast = Toast.makeText(this, "Getting the extractions failed! " + error, Toast.LENGTH_LONG);
            toast.show();
        }
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

}
