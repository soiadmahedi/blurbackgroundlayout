package com.soiadmahedi.blurbackgroundlayout;

import android.view.*;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;

/*
This Project made by Soiad Mahedi 
Version : 1.0 Beta
*/

public class BlurBackgroundLayout extends FrameLayout {
	
	private ImageView backgroundImage;
	private LinearLayout contentLayout;
	private boolean isBlurred = false;
	private float blurRadius = 25f;
	private Bitmap originalBitmap = null;
	
	public BlurBackgroundLayout(Context context) {
		super(context);
		init(context);
	}
	
	public BlurBackgroundLayout(Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	
	public BlurBackgroundLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		if (backgroundImage != null) {
			backgroundImage.setVisibility(View.GONE);
		}

		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		if (backgroundImage != null) {
			backgroundImage.setVisibility(View.VISIBLE);
			backgroundImage.measure(
			MeasureSpec.makeMeasureSpec(getMeasuredWidth(), MeasureSpec.EXACTLY),
			MeasureSpec.makeMeasureSpec(getMeasuredHeight(), MeasureSpec.EXACTLY)
			);
		}
	}
	
	private void init(Context context) {
		// Background ImageView
		backgroundImage = new ImageView(context);
		backgroundImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
		backgroundImage.setAdjustViewBounds(false); // ইমেজের সাইজ অনুযায়ী view বড় হবে না
		LayoutParams bgParams = new LayoutParams(
		LayoutParams.MATCH_PARENT,
		LayoutParams.MATCH_PARENT
		);
		addView(backgroundImage, 0, bgParams);
		
		// Foreground LinearLayout (content container)
		contentLayout = new LinearLayout(context);
		contentLayout.setOrientation(LinearLayout.VERTICAL);
		LayoutParams contentParams = new LayoutParams(
		LayoutParams.MATCH_PARENT,
		LayoutParams.WRAP_CONTENT
		);
		addView(contentLayout, contentParams);
	}
	
	public LinearLayout getContentLayout() {
		return contentLayout;
	}
	
	public void setBackgroundImage(@DrawableRes int resId) {
		originalBitmap = BitmapFactory.decodeResource(getResources(), resId);
		backgroundImage.setImageBitmap(originalBitmap);
		if (isBlurred) applyBlur();
	}
	
	public void enableBlur(boolean enable) {
		isBlurred = enable;
		if (enable) {
			applyBlur();
		} else {
			clearBlur();
		}
	}
	
	private void applyBlur() {
		if (originalBitmap == null) return;
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
			backgroundImage.setRenderEffect(
			android.graphics.RenderEffect.createBlurEffect(
			blurRadius,
			blurRadius,
			android.graphics.Shader.TileMode.CLAMP
			)
			);
		} else {
			Bitmap blurredBitmap = blurBitmap(getContext(), originalBitmap, blurRadius);
			backgroundImage.setImageBitmap(blurredBitmap);
		}
	}
	
	private void clearBlur() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
			backgroundImage.setRenderEffect(null);
		}
		if (originalBitmap != null) {
			backgroundImage.setImageBitmap(originalBitmap);
		}
	}
	
	private Bitmap blurBitmap(Context context, Bitmap bitmap, float radius) {
		if (radius <= 0) return bitmap;
		
		Bitmap outputBitmap = Bitmap.createBitmap(bitmap);
		RenderScript rs = RenderScript.create(context);
		Allocation input = Allocation.createFromBitmap(rs, bitmap);
		Allocation output = Allocation.createFromBitmap(rs, outputBitmap);
		ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
		script.setRadius(radius); // 0 < radius <= 25
		script.setInput(input);
		script.forEach(output);
		output.copyTo(outputBitmap);
		rs.destroy();
		return outputBitmap;
	}
}
