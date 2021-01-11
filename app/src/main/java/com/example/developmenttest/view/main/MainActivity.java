package com.example.developmenttest.view.main;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.developmenttest.R;
import com.example.developmenttest.data.network.NetworkDataGetter;
import com.example.developmenttest.presenter.Presenter;
import com.example.developmenttest.view.fragment.MainFragment;

public class MainActivity extends AppCompatActivity implements NotifyOnParsingComplete, NotifyOnCallingFailed {

    Presenter presenter;

    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dialog = ProgressDialog.show(MainActivity.this,
                getString(R.string.dialog_title),
                getString(R.string.dialog_message),
                true);

        presenter = new Presenter(this);

        NetworkDataGetter networkDataGetter = new NetworkDataGetter(MainActivity.this, presenter, this);
    }

    @Override
    public void parseComplete() {
        dialog.dismiss();
        transitionToFragment();
    }

    @Override
    public void onParsingFailed(String message) {
        dialog.dismiss();
        Log.e(getString(R.string.retro_error), message);
        Toast.makeText(MainActivity.this , R.string.error , Toast.LENGTH_SHORT).show();
    }

    private void transitionToFragment() {
        MainFragment mainFragment = new MainFragment();

        Bundle bundle = new Bundle();

        bundle.putString(getString(R.string.first_value) , String.valueOf(presenter.getRSRP()));
        bundle.putString(getString(R.string.second_value) , String.valueOf(presenter.getRSRQ()));
        bundle.putString(getString(R.string.third_value) , String.valueOf(presenter.getSINR()));

        bundle.putString(getString(R.string.progress_one_color), presenter.getFirstProgressColor(presenter.getRSRP()));
        bundle.putString(getString(R.string.progress_two_color), presenter.getSecondProgressColor(presenter.getRSRQ()));
        bundle.putString(getString(R.string.progress_three_color), presenter.getThirdProgressColor(presenter.getSINR()));

        mainFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_host, mainFragment).commit();
    }
}