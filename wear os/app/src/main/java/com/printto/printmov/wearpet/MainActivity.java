package com.printto.printmov.wearpet;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.widget.Button;
import android.widget.Toast;
import android.widget.EditText;
import android.content.Context;
import android.view.View;
import android.text.InputType;
import android.view.View.OnClickListener;
import android.util.Log;
import android.content.res.Configuration;
import android.content.DialogInterface;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import com.printto.printmov.wearpet.R;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity {
   String msg = "Android : ";
   RenderView view;
   Button feedButton;
   Button careButton;
   Button battleButton;
   Button statusButton;

    /** Called when the activity is first created. */
   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      setContentView(R.layout.activity_main);

      view = (RenderView)findViewById(R.id.view);
      feedButton = (Button)findViewById(R.id.feedButton);
      careButton = (Button)findViewById(R.id.careButton);
      battleButton = (Button)findViewById(R.id.battleButton);
      statusButton = (Button)findViewById(R.id.statsButton);
      Log.d(msg, "The onCreate() event");
   }

   public void feedBtnClicked(View v) {
       if(view.isEgg()) {
           String text = "Wait for the egg to hatch";
           int duration = 40*1000; //Toast.LENGTH_SHORT;

           Toast toast = Toast.makeText(MainActivity.this, (CharSequence)text, duration);
           toast.show();
           return;
       }
       // Code here executes on main thread after user presses button
       view.feedMonster();
   }

   public void careBtnClicked(View v){
       if(view.isEgg()) {
           String text = "Wait for the egg to hatch";
           int duration = 40*1000; //Toast.LENGTH_SHORT;

           Toast toast = Toast.makeText(MainActivity.this, (CharSequence)text, duration);
           toast.show();
           return;
       }
       // Code here executes on main thread after user presses button
       view.careMonster();
   }

   public void statusBtnClicked(View v){
       Intent intent = new Intent(MainActivity.this, StatsActivity.class);
       intent.putExtra("data", view.getStatusInfo());
       startActivity(intent);
   }

   public void battleBtnClicked(View v){
       if(view.isEgg()) {
           String text = "This egg is completely defenseless";
           int duration = 40*1000; //Toast.LENGTH_SHORT;

           Toast toast = Toast.makeText(MainActivity.this, (CharSequence)text, duration);
           toast.show();
           return;
       }
       // Let the user choose to host or join and delegate that
       final CharSequence battleTypes[] = new CharSequence[] {"Host", "Join", "Train"};
       Builder builder = new Builder(MainActivity.this);
       builder.setTitle("Choose Battle");
       builder.setItems(battleTypes, new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialog, int which) {
               // the user clicked on builder[which]
               if(battleTypes[which].equals("Host")) {
                   // Toast the IP address...

                   // insert at 0 == prepend a string
                   String text = "Monster battle code is " + BattleCode.pack(RenderView.getLocalIpAddress());
                   int duration = 40*1000; //Toast.LENGTH_SHORT;

                   Toast toast = Toast.makeText(MainActivity.this, (CharSequence)text, duration);
                   toast.show();

                   view.hostBattle();
               } else if(battleTypes[which].equals("Join")) {
                   // Request an IP address...

                   Builder builder = new Builder(MainActivity.this);
                   builder.setTitle("Join a P2P battle");

                   // Set up the input
                   final EditText input = new EditText(MainActivity.this);
                   // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                   input.setInputType(InputType.TYPE_CLASS_TEXT);
                   builder.setView(input);

                   // Set up the buttons
                   builder.setPositiveButton("FIGHT!", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialog, int which) {
                           String bc = input.getText().toString();
                           String ip = BattleCode.unpack(bc);
                           view.joinBattle(ip);
                       }
                   });
                   builder.setNegativeButton("Nope", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialog, int which) {
                           dialog.cancel();
                       }
                   });

                   builder.show();

               } else if(battleTypes[which].equals("Train")) {
                   // Do training...
                   view.train();

                   String text = "Training available in the next update!";
                   int duration = 40*1000; //Toast.LENGTH_SHORT;

                   Toast toast = Toast.makeText(MainActivity.this, (CharSequence)text, duration);
                   toast.show();
               }
           }
       });
       builder.show();
   }

   /** Called when the activity is about to become visible. */
   @Override
   protected void onStart() {
      super.onStart();
    //  view.loadMonster();
      Log.d(msg, "The onStart() event");
   }

   /** Called when the activity has become visible. */
   @Override
   protected void onResume() {
    super.onResume();
    view.onResume();
    Log.d(msg, "The onResume() event");
   }

   /** Called when another activity is taking focus. */
   @Override
   protected void onPause() {
    super.onPause();
    view.saveMonster();
    view.onPause();
    Log.d(msg, "The onPause() event");
   }

   /** Called when the activity is no longer visible. */
   @Override
   protected void onStop() {
    super.onStop();
    view.saveMonster();
    Log.d(msg, "The onStop() event");
   }

   /** Called just before the activity is destroyed. */
   @Override
   public void onDestroy() {
    view.saveMonster();
    super.onDestroy();
    Log.d(msg, "The onDestroy() event");
   }
}
