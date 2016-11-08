package com.kacyber.ActAndFrg;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.kacyber.R;
import com.kacyber.adapter.ContactAdapter;
import com.kacyber.adapter.UserItemClickListener;
import com.kacyber.event.FriendSearchResult;
import com.kacyber.model.User;
import com.kacyber.network.http.KacyberRestClientUsage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

public class NormalSearchActivity extends Activity implements View.OnClickListener, UserItemClickListener {

    private static String TAG = NormalSearchActivity.class.getName();
    private EditText editText;
    private TextView textView;
    private AQuery aQuery;
    private Activity context;
    private View.OnClickListener onClickListener;
    private static String searchAction = "all";

    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal_search);
        EventBus.getDefault().register(this);
        String hint = getIntent().getStringExtra("hint");
        searchAction = getIntent().getStringExtra("searchAction");
        editText = (EditText) findViewById(R.id.search_hint);
        if (hint!=null) {
            editText.setHint(hint);
        }
        aQuery = new AQuery(this);
        aQuery.id(R.id.back_text).clickable(true).clicked(this);
        recyclerView = (RecyclerView) findViewById(R.id.search_result);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

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

                Log.e(TAG, "search action is " + searchAction);
                switch (searchAction) {
                    case "friends":
                        Log.e(TAG, "search friends");
                        KacyberRestClientUsage.getInstance().searchFriends(editText.getText().toString());

                }

                break;




        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onFriendSearchResult(FriendSearchResult friendSearchResult) {
        User user = friendSearchResult.user;
        ArrayList<User> userList = new ArrayList<>();
        userList.add(user);
        ContactAdapter adapter = new ContactAdapter(this, R.layout.user_item_layout, userList);
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);


    }

    @Override
    public void onItemClick(View view, int position) {

    }
}
