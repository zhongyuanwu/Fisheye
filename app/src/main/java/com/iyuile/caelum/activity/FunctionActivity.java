package com.iyuile.caelum.activity;

import android.os.Bundle;

import com.iyuile.caelum.R;
import com.iyuile.caelum.fragment.MyFunctionFragment;

import me.yokeyword.fragmentation.SupportActivity;

/**
 *
 */
public class FunctionActivity extends SupportActivity {

    private static final String TAG = "FunctionActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_root_function);
        if(savedInstanceState==null) {
            loadRootFragment(R.id.fl_root_function_container, MyFunctionFragment.newInstance());
        }
    }
}
