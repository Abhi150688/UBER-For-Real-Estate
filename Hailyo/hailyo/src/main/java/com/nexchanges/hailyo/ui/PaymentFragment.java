package com.nexchanges.hailyo.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nexchanges.hailyo.R;

/**
 * Created by Abhishek on 28/04/15.
 */
public class PaymentFragment extends Fragment {
    public PaymentFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_payment, container, false);

        return rootView;
    }
}
