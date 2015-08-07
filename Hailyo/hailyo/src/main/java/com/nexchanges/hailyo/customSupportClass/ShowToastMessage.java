package com.nexchanges.hailyo.customSupportClass;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.nexchanges.hailyo.R;

/**
 * Created by TP on 07/08/15.
 */
public class ShowToastMessage {

    public void displayToast(Context context, String Text) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View layout = inflater.inflate(R.layout.custom_toast_new_bid,null);

        // set a message
        TextView text = (TextView) layout.findViewById(R.id.toasttext);
        text.setText(Text);

        // Toast...
        Toast toast = new Toast(context);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }
}
