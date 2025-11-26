package com.example.whatsapp.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.whatsapp.R;


public class CallsFragment extends Fragment {

    public CallsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View view= inflater.inflate(R.layout.fragment_calls, container, false);

        View callBtn = view.findViewById(R.id.goToCall);

        // Set click listener
        callBtn.setOnClickListener(v -> {
            // Open dialer
            Intent intent = new Intent(Intent.ACTION_DIAL);
            // Optional: pre-fill a number
            intent.setData(Uri.parse("tel:"));
            startActivity(intent);
        });
        return view;
    }
}