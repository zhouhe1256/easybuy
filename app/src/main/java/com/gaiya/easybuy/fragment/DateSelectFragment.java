package com.gaiya.easybuy.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gaiya.easybuy.R;

/**
 * Created by dengt on 15-10-8.
 */
public class DateSelectFragment extends DialogFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.DialogTheme);
        Bundle bundle=getArguments();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       View rootView = inflater.inflate(R.layout.dialog_select_date, container);
        return rootView;
    }
}
