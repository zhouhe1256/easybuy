package com.gaiya.easybuy.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gaiya.easybuy.R;

/**
 * Created by dengt on 15-9-24.
 */
public class BaseFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = null;
        rootView = inflater.inflate(R.layout.fragment_base, container);
        return rootView;
    }
}
