package com.soiadmahedi.blurbackgroundlayout;

import android.os.*;
import android.view.*;
import android.view.View.*;

import androidx.activity.EdgeToEdge;
import androidx.core.view.ViewCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {
	
	private Toolbar _toolbar;
	private BlurBackgroundLayout linear_blurbackgroundlayout;
	
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		//enable edge to edge
		EdgeToEdge.enable(this);
		//add padding for status bar and navigation bar
		setContentView(R.layout.main);

		ViewCompat.setOnApplyWindowInsetsListener(getWindow().getDecorView(), (v, insets) -> {
			v.setPadding(
				insets.getSystemWindowInsetLeft(),
				insets.getSystemWindowInsetTop(),
				insets.getSystemWindowInsetRight(),
				insets.getSystemWindowInsetBottom());
			return insets.consumeSystemWindowInsets();
		});

		initialize(_savedInstanceState);
		initializeLogic();
	}
	
	private void initialize(Bundle _savedInstanceState) {
		_toolbar = findViewById(R.id._toolbar);
		setSupportActionBar(_toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		_toolbar.setNavigationOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View _v) {
				onBackPressed();
			}
		});
		linear_blurbackgroundlayout = findViewById(R.id.linear_blurbackgroundlayout);
	}
	
	private void initializeLogic() {
		linear_blurbackgroundlayout.setBackgroundImage(R.drawable.image_soiadmahedi);
		linear_blurbackgroundlayout.enableBlur(true);
	}
	
}
