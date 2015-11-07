package appathon.donation;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import net.gini.android.DocumentTaskManager;
import net.gini.android.Gini;
import net.gini.android.models.Document;
import net.gini.android.vision.DocumentType;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bolts.Continuation;
import bolts.Task;


public class UploadActivity extends net.gini.android.vision.UploadActivity {

    private static final Logger LOG = LoggerFactory.getLogger(UploadActivity.class);
    public static final String EXTRA_ERROR_STRING = "error";
    public static final String EXTRA_OCR_STRING = "appathon/donation/gini";
    public static final int RESULT_UPLOAD_ERROR = RESULT_FIRST_USER;

    private DocumentTaskManager documentTaskManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Gini gini = GiniService.INSTANCE.startService(this);
        documentTaskManager = gini.getDocumentTaskManager();
    }

    public void uploadDocument(final Bitmap document) {
        final Intent intent = getIntent();
        final Bundle documentTypeBundle = intent.getBundleExtra(EXTRA_DOCTYPE_BUNDLE);
        final DocumentType documentType = documentTypeBundle.getParcelable(EXTRA_DOCTYPE);
        documentTaskManager.createDocument(document, null, documentType.getApiDocTypeHint(), 50)
                .onSuccessTask(new Continuation<Document, Task<Document>>() {
                    @Override
                    public Task<Document> then(Task<Document> task) throws Exception {
                        final Document document = task.getResult();
                        documentId = document.getId();
                        return documentTaskManager.pollDocument(document);
                    }
                })
                .onSuccessTask(new Continuation<Document, Task<JSONObject>>() {
                    @Override
                    public Task<JSONObject> then(Task<Document> task) throws Exception {
                        return documentTaskManager.getLayout(task.getResult());
                    }
                })
                .onSuccess(new Continuation<JSONObject, Object>() {
                    @Override
                    public Object then(Task<JSONObject> task) throws Exception {
                        return null;
                    }
                })
                .continueWith(new Continuation<Object, Object>() {
                    @Override
                    public Object then(Task<Object> task) throws Exception {
                        // todo: we would get the layout information from the Gini SDK as a
                        // JSONObject here. For the demo let's assume it worked and the OCR did a
                        // good job.
                        final String ocrResult = "#07289020#";

                        LOG.info("ocr recognized tag number: " + ocrResult);

                        final Intent result = new Intent();
                        result.putExtra(EXTRA_OCR_STRING, ocrResult);
                        setResult(RESULT_OK, result);
                        return null;
                    }
                });
    }
}
