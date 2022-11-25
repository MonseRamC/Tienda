package company.project;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class SimpleAlertDialog {


    public void show(Context context, String msg){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);


        builder.setMessage(msg);

        builder.setTitle("Operacion concluida");

        builder.setCancelable(false);

        builder.setPositiveButton("Hecho", (DialogInterface.OnClickListener) (dialog, which) -> {
        });


        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
