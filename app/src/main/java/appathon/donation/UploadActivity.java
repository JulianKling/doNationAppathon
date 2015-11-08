package appathon.donation;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import net.gini.android.DocumentTaskManager;
import net.gini.android.Gini;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Sends pictures to Gini service.
public class UploadActivity extends net.gini.android.vision.UploadActivity {

    public static boolean doneOnce = false;

    private static final Logger LOG = LoggerFactory.getLogger(UploadActivity.class);
    public static final String EXTRA_ERROR_STRING = "error";
    public static final String EXTRA_OCR_STRING = "ocr_result";
    public static final int RESULT_UPLOAD_ERROR = RESULT_FIRST_USER;

    private DocumentTaskManager documentTaskManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Gini gini = GiniService.INSTANCE.startService(this);
        documentTaskManager = gini.getDocumentTaskManager();
    }

    public void uploadDocument(final Bitmap document) {
        // todo: we would get the layout information from the Gini SDK as a
        // JSONObject here. For the demo let's assume it worked and the OCR did a
        // good job.
        final String ocrResult = "#07289020#";
//        final Intent result = new Intent();
//        result.putExtra(EXTRA_OCR_STRING, ocrResult);
//        setResult(RESULT_OK, result);
        if (!doneOnce) {
            doneOnce = true;
            LOG.info("ocr recognized tag number: " + ocrResult);
            SelectActivity.hasDonated = false;
            SelectActivity.productId = ocrResult;
            Intent intent = new Intent(this, SelectActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            this.finish();
        }
    }
}
