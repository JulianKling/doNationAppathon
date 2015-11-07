package appathon.donation;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
 * Created by jkling on 07.11.15.
 * This dialog is used to prevent unwanted payments by
 * accidentally pressing the 'donate' button and forces the user to confirm the donation.
 */
public class AcceptPaymentDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.confirm_donation)
                .setPositiveButton(R.string.accept_donation, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SelectActivity.hasDonated = true;
                        dialog.dismiss();
                        ((SelectActivity) getContext()).onResume();
                    }
                })
                .setNegativeButton(R.string.cancel_donation, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
