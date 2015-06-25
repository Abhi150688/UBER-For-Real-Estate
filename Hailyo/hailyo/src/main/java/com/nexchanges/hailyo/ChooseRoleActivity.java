package com.nexchanges.hailyo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.nexchanges.hailyo.model.SharedPrefs;

import java.io.ByteArrayOutputStream;

/**
 * Created by AbhishekWork on 22/06/15.
 */
public class ChooseRoleActivity extends Activity {

    //declare variables
    private String Semail, Sname, Sphoto,C = "Customer",B ="Broker";
    Button clientBut, brokerBut;

    Context context;
    EditText name, email;
    ImageButton myphoto;
    private static final int SELECT_PHOTO = 1;
    TextView edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_role);

        context = this;
        name = (EditText) findViewById(R.id.etname);
        email = (EditText) findViewById(R.id.etemail);

        clientBut = (Button) findViewById(R.id.iamclient);
        brokerBut = (Button) findViewById(R.id.iambroker);

        myphoto = (ImageButton) findViewById(R.id.myphoto);

        edit = (TextView)findViewById(R.id.edit);




        clientBut.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                clientBut.setBackgroundColor(Color.WHITE);
                clientBut.setTextColor(Color.BLACK);
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                Sname = name.getText().toString();
                Semail = email.getText().toString();


                    SharedPrefs.save(context, SharedPrefs.MY_ROLE_KEY, C);
                    SharedPrefs.save(context, SharedPrefs.NAME_KEY, Sname);
                    SharedPrefs.save(context, SharedPrefs.EMAIL_KEY, Semail);
                    Intent MainActivity = new Intent(context, MainActivity.class);
                    startActivity(MainActivity);
                finish();




            }
        });


        brokerBut.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                brokerBut.setBackgroundColor(Color.WHITE);
                brokerBut.setTextColor(Color.BLACK);


                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                Sname = name.getText().toString();
                Semail = email.getText().toString();


                    SharedPrefs.save(context, SharedPrefs.NAME_KEY, Sname);
                    SharedPrefs.save(context, SharedPrefs.MY_ROLE_KEY, B);
                    SharedPrefs.save(context, SharedPrefs.EMAIL_KEY, Semail);
                   // editor.putString(MyPhoto, encodeTobase64(bitphoto));
                    Intent MainBrokerActivity = new Intent(context, MainBrokerActivity.class);
                    startActivity(MainBrokerActivity);
                finish();



            }
        });

        edit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
               // photoPickerIntent.putExtra("crop", "true");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);


            }
        });

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SELECT_PHOTO && resultCode == RESULT_OK && null!= data) {

                Uri selectedImageUri = data.getData();
                String[] FilePathColumn = {MediaStore.Images.Media.DATA };

            Cursor SelectedCursor = getContentResolver().query(selectedImageUri, FilePathColumn, null, null, null);
            SelectedCursor.moveToFirst();

            int columnIndex = SelectedCursor.getColumnIndex(FilePathColumn[0]);
            String picturePath = SelectedCursor.getString(columnIndex);
            SelectedCursor.close();

            myphoto.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            SharedPrefs.save(context, SharedPrefs.PHOTO_KEY, picturePath);


        }
    }

}




