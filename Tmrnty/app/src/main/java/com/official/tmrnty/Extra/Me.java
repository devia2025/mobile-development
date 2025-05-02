package com.official.tmrnty.Extra;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.official.tmrnty.R;

public class Me extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me);

        YoYo.with(Techniques.BounceInDown).duration(1000).playOn(findViewById(R.id.rellay1));// after start,just click mTarget view, rope is not init
        YoYo.with(Techniques.BounceInDown).duration(1000).playOn(findViewById(R.id.rellay2));// after start,just click mTarget view, rope is not init

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //preventing default implementation previous to android.os.Build.VERSION_CODES.ECLAIR
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void back(View view) {
        Intent intent = new Intent(this, Main.class);
        startActivity(intent);
    }
}