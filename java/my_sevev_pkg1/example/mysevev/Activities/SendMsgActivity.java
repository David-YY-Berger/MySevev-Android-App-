package my_sevev_pkg1.example.mysevev.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import my_sevev_pkg1.example.mysevev.Objects.Sevev;
import my_sevev_pkg1.example.mysevev.Objects.Person;
import com.example.mysevev.R;

public class SendMsgActivity extends AppCompatActivity {

    static final String EXTRA_MSG_TO_SEND = "com.example.mysevev.Activities.SendMsgActivity - msgToSend";
    String msgToSend;

    Button butReturn;
    Button butSendWhatsapp;
    Button butCopyToClip;

    TextView tvDisplayMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_msg);

        extractDataFromIntent();
        tvDisplayMsg = (TextView) findViewById(R.id.tvDisplayMessage);
        tvDisplayMsg.setText(msgToSend);
        tvDisplayMsg.setTypeface(null, Typeface.BOLD);

        //ASSIGN OBJECTS
        butReturn = (Button) findViewById(R.id.butReturn);
        butSendWhatsapp = (Button) findViewById(R.id.butShare);
        butCopyToClip = (Button) findViewById(R.id.butCopyClipboard);

        butReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(SendMsgActivity.this, "hi!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        butCopyToClip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", msgToSend);
                clipboard.setPrimaryClip(clip);
            }
        });
        butSendWhatsapp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                PackageManager pm=getPackageManager();
//                try {
//
//                    Intent waIntent = new Intent(Intent.ACTION_SEND);
//                    waIntent.setType("text/plain");
//                    String text = msgToSend;
//
//                    PackageInfo info=pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
//                    //Check if package exists or not. If not then code
//                    //in catch block will be called
//                    waIntent.setPackage("com.whatsapp");
//
//                    waIntent.putExtra(Intent.EXTRA_TEXT, text);
//                    startActivity(Intent.createChooser(waIntent, "Share with"));
//
//                } catch (PackageManager.NameNotFoundException e) {
//                    displayErrMsg(v, "Sorry! \n" +
//                            "We can't connect to whatsapp... \n" +
//                            "But you can copy to your clipboard.");
//                }
//            }
@Override
public void onClick(View v){
    Intent myIntent = new Intent(Intent.ACTION_SEND);
    myIntent.setType("text/plain");
    String body = msgToSend;
    String sub = "Hi!";
    myIntent.putExtra(Intent.EXTRA_SUBJECT,sub);
    myIntent.putExtra(Intent.EXTRA_TEXT,body);
    startActivity(Intent.createChooser(myIntent, "Share Using"));

}
        });

    }

    private void extractDataFromIntent() {
        Intent intent = getIntent();
        this.msgToSend = intent.getStringExtra(EXTRA_MSG_TO_SEND);
    }

    /**
     * Factory Method, generates this object via the caller context
     * @param callerContext
     * @return
     */
    public static Intent genIntent(Context callerContext,
                                   Person person_to_send_to, Sevev sevev){
        Intent intent = new Intent(callerContext, SendMsgActivity.class);
        return intent;
    }
    public static Intent genIntent(Context callerContext, String msgToSend){
        Intent intent = new Intent(callerContext, SendMsgActivity.class);
        intent.putExtra(EXTRA_MSG_TO_SEND, msgToSend);
        return intent;
    }

    private void displayErrMsg(View view, String msg){
        AlertDialog alertDialog = new AlertDialog.Builder(SendMsgActivity.this).create();
        alertDialog.setTitle("Error");
        alertDialog.setMessage(msg);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }
}