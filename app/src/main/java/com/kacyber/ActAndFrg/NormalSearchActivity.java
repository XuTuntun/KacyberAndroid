package com.kacyber.ActAndFrg;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.kacyber.R;

public class NormalSearchActivity extends Activity implements View.OnClickListener{

    private EditText editText;
    private TextView textView;
    private AQuery aQuery;
    private Activity context;
    private View.OnClickListener onClickListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal_search);
        editText = (EditText) findViewById(R.id.search_hint);
        aQuery = new AQuery(this);
        aQuery.id(R.id.back_text).clickable(true).clicked(this);
        onClickListener = this;
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                aQuery.id(R.id.search_action).clickable(true).clicked(onClickListener);
                aQuery.id(R.id.search_action).visible();
                aQuery.id(R.id.search_text).text("Search '" + s + "'");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_text:
                this.finish();
                break;

            case R.id.search_action:

                Toast.makeText(this, "There are no results return from the server", Toast.LENGTH_LONG).show();

                break;




        }
    }
}
