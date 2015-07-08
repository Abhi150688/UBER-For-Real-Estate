package com.nexchanges.hailyo;

import android.accounts.Account;
import android.accounts.AccountManager;
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
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.nexchanges.hailyo.model.SharedPrefs;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.List;

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
    TextView edit,fbdata;
    //CallbackManager callbackManager;
    //ImageButton fb;
    //LoginManager loginManager;
    //String fbfirstname, fblastname,fbpic;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_role);



        context = this;
        name = (EditText) findViewById(R.id.etname);
        email = (EditText) findViewById(R.id.etemail);
       // fbdata = (TextView)findViewById(R.id.fbtext);
//        fb = (ImageButton)findViewById(R.id.fb);


        clientBut = (Button) findViewById(R.id.iamclient);
        brokerBut = (Button) findViewById(R.id.iambroker);

        myphoto = (ImageButton) findViewById(R.id.myphoto);

        edit = (TextView)findViewById(R.id.edit);



    //    FacebookSdk.sdkInitialize(context);
  //      callbackManager = CallbackManager.Factory.create();
  //      FacebookSdk.addLoggingBehavior(LoggingBehavior.REQUESTS);
    //    loginManager = LoginManager.getInstance();
//        loginManager.setDefaultAudience(DefaultAudience.EVERYONE);


  /*fb.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
          fblogn();
      }
  });

        fbdata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fblogn();
            }
        });
*/


        String acc_email = getEmail(context);
        email.setText(acc_email);

        Sname = name.getText().toString();



        clientBut.setOnClickListener(new View.OnClickListener()

                                             {

                                                 @Override
                                                 public void onClick(View v) {
                                                     if(name.getText().toString().trim().equalsIgnoreCase("")) {
                                                         name.setError("Please enter name");
                                                         return;
                                                     }


                                                     Semail = email.getText().toString();

                                                     if(email.getText().toString().trim().equalsIgnoreCase("")) {
                                                         email.setError("Please enter email-id");
                                                         return;
                                                     }

                                                     name.addTextChangedListener(new TextWatcher() {

                                                         @Override
                                                         public void onTextChanged(CharSequence s, int start, int before, int count) {
                                                             name.setError(null);

                                                         }

                                                         @Override
                                                         public void beforeTextChanged(CharSequence s, int start, int count,
                                                                                       int after) {
                                                             // TODO Auto-generated method stub

                                                         }

                                                         @Override
                                                         public void afterTextChanged(Editable s) {
                                                             name.setError(null);

                                                         }
                                                     });



                                                     email.addTextChangedListener(new TextWatcher() {
                                                         @Override
                                                         public void onTextChanged(CharSequence s, int start, int before, int count) {
                                                             email.setError(null);

                                                         }

                                                         @Override
                                                         public void beforeTextChanged(CharSequence s, int start, int count,
                                                                                       int after) {
                                                             // TODO Auto-generated method stub

                                                         }

                                                         @Override
                                                         public void afterTextChanged(Editable s) {
                                                             email.setError(null);

                                                         }
                                                     });



                                                     if ((name.getText().toString().length()>0) &&
                                                             ( email.getText().toString().length()>0))
                                                     {
                                                         // TODO Auto-generated method stub
                                                         clientBut.setBackgroundColor(Color.parseColor("#FFA500"));
                                                         clientBut.setTextColor(Color.WHITE);
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


                                                 }
                                             }

                );


            brokerBut.setOnClickListener(new View.OnClickListener()

            {

                @Override
                public void onClick (View v){

                    if(name.getText().toString().trim().equalsIgnoreCase("")) {
                        name.setError("Please enter name");
                        return;
                    }


                    Semail = email.getText().toString();

                    if(email.getText().toString().trim().equalsIgnoreCase("")) {
                        email.setError("Please enter email-id");
                        return;
                    }

                    name.addTextChangedListener(new TextWatcher() {

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            name.setError(null);

                        }

                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count,
                                                      int after) {
                            // TODO Auto-generated method stub

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            name.setError(null);

                        }
                    });



                    email.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            email.setError(null);

                        }

                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count,
                                                      int after) {
                            // TODO Auto-generated method stub

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            email.setError(null);

                        }
                    });



                    if ((name.getText().toString().length()>0) &&
                            ( email.getText().toString().length()>0))
                    {
                        // TODO Auto-generated method stub
                        brokerBut.setBackgroundColor(Color.parseColor("#FFA500"));
                        brokerBut.setTextColor(Color.WHITE);


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

            }
            }

            );

            edit.setOnClickListener(new View.OnClickListener()

            {

                @Override
                public void onClick (View v){
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                // photoPickerIntent.putExtra("crop", "true");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);


            }
            }

            );

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

    static String getEmail(Context context) {
        AccountManager accountManager = AccountManager.get(context);
        Account account = getAccount(accountManager);
        if (account == null) {
            return null;
        } else {
            return account.name;
        }
    }
    private static Account getAccount(AccountManager accountManager) {
        Account[] accounts = accountManager.getAccountsByType("com.google");
        Account account;
        if (accounts.length > 0) {
            account = accounts[0];
        } else {
            account = null;
        } return account;
    }

}




