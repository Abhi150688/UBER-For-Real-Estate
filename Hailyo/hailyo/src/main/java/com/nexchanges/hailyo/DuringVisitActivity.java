package com.nexchanges.hailyo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nexchanges.hailyo.model.SharedPrefs;

/**
 * Created by AbhishekWork on 22/06/15.
 */
public class DuringVisitActivity extends Activity {

    TextView spec;
    String spec_code;
    Button endBut;
    Context context;
    ImageView myphoto;
    String photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.during_visit);
        context = this;

        spec = (TextView)findViewById(R.id.spec);
       spec_code =  SharedPrefs.getString(this, SharedPrefs.CURRENT_SPEC, "No_Name");

        spec.setText(spec_code);

       endBut = (Button) findViewById(R.id.endvisit);

        myphoto = (ImageView)findViewById(R.id.myphoto);

        photo = SharedPrefs.getString(this, SharedPrefs.PHOTO_KEY);

        myphoto.setImageBitmap(BitmapFactory.decodeFile(photo));



        endBut.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(R.string.app_name);
                builder.setMessage("Do you want to End Site Visit?");
                //builder.setIcon(R.drawable.ic_launcher);
                builder.setPositiveButton("Yes-End Now", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();

                        Intent GiveRatingActivity=new Intent(context, GiveRatingActivity.class);
                        context.startActivity(GiveRatingActivity);

                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();


            }
        });


    }

    @Override
    public void onBackPressed() {
        //do nothing
    }

}