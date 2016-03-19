package com.coco.wechat;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Looper;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

public class ChangeColorIconWithText extends View {
	private int mColor = 0xFF45C01A;
	private Bitmap mIconBitmap;
	private String mText = "";
	private int mTextSize = (int) TypedValue.applyDimension(
			TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics());

	private float mAlpha = 0.0f;
	private Bitmap mBitmap;
	private Canvas mCanvas;
	private Paint mPaint;
	private Paint mTextPaint;

	private Rect mIconRect;
	private Rect mTextBound;

	public ChangeColorIconWithText(Context context) {
		this(context, null);
	}

	public ChangeColorIconWithText(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ChangeColorIconWithText(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		TypedArray ta = context.obtainStyledAttributes(attrs,
				R.styleable.ChangeColorIconWithText);
		int indexCount = ta.getIndexCount();
		for (int i = 0; i < indexCount; i++) {
			int attr = ta.getIndex(i);
			switch (attr) {
			case R.styleable.ChangeColorIconWithText_icon:
				BitmapDrawable drawable = (BitmapDrawable) ta.getDrawable(attr);
				mIconBitmap = drawable.getBitmap();
				break;

			case R.styleable.ChangeColorIconWithText_color:
				mColor = ta.getColor(attr, mColor);
				break;
			case R.styleable.ChangeColorIconWithText_text:
				mText = ta.getString(attr);
				break;
			case R.styleable.ChangeColorIconWithText_textSize:
				mTextSize = (int) ta.getDimension(attr, mTextSize);
				break;
			}
		}
		ta.recycle();

		// 设置文字的信息
		mTextBound = new Rect();
		mTextPaint = new Paint();
		mTextPaint.setStyle(Style.FILL_AND_STROKE);
		mTextPaint.setStrokeWidth(0.8f);
		mTextPaint.setTextSize(mTextSize);
		mTextPaint.getTextBounds(mText, 0, mText.length(), mTextBound);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int iconWidth = Math.min(getMeasuredHeight() - getPaddingTop()
				- getPaddingBottom() - mTextBound.height(), getMeasuredWidth()
				- getPaddingLeft() - getPaddingRight());
		int left = (getMeasuredWidth() - iconWidth) >> 1;
		int top = (getMeasuredHeight() - iconWidth - mTextBound.height()) >> 1;

		mIconRect = new Rect(left, top, left + iconWidth, top + iconWidth);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawBitmap(mIconBitmap, null, mIconRect, null);

		int alpha = (int) Math.ceil(255 * mAlpha);
		// 在内存中准备mBitmap， setAlpha， 纯色， xfermode， 图标
		setupTargetBitmap(alpha);
		canvas.drawBitmap(mBitmap, 0, 0, null);

		// 绘制原文本
		drawSouceText(canvas, alpha);
		// 绘制变色文本
		drawTargetText(canvas, alpha);
	}

	private void drawSouceText(Canvas canvas, int alpha) {
		mTextPaint.setColor(0xFF333333);
		mTextPaint.setAlpha(255 - alpha);
		canvas.drawText(mText, (getMeasuredWidth() - mTextBound.width()) / 2,
				mIconRect.bottom + mTextBound.height(), mTextPaint);
	}

	private void drawTargetText(Canvas canvas, int alpha) {
		mTextPaint.setColor(mColor);
		mTextPaint.setAlpha(alpha);
		canvas.drawText(mText, (getMeasuredWidth() - mTextBound.width()) / 2,
				mIconRect.bottom + mTextBound.height(), mTextPaint);
	}

	/**
	 * 在内存中绘制可变色的Icon
	 */
	private void setupTargetBitmap(int alpha) {
		mBitmap = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(),
				Config.ARGB_8888);
		mCanvas = new Canvas(mBitmap);
		mPaint = new Paint();
		mPaint.setColor(mColor);
		mPaint.setAntiAlias(true);
		mPaint.setDither(true);
		mPaint.setAlpha(alpha);
		mCanvas.drawRect(mIconRect, mPaint);
		mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
		mPaint.setAlpha(255);
		mCanvas.drawBitmap(mIconBitmap, null, mIconRect, mPaint);

	}

	public void setAlpha(float alpha) {
		if (alpha > 1 || alpha < 0) {
			throw new IllegalArgumentException("alpha must be between [0 - 1]");
		}
		mAlpha = alpha;
		invalidateView();
	}

	private void invalidateView() {
		if (Looper.getMainLooper() == Looper.myLooper()) {
			invalidate();
		} else {
			postInvalidate();
		}
	}

	// Activity回收处理
	private static final String INSTANCE_STATUS = "instance_status";
	private static final String STATUS_ALPHA = "status_alpha";

	@Override
	protected void onRestoreInstanceState(Parcelable state) {
		if (state instanceof Bundle) {
			// 恢复View的自身状态
			super.onRestoreInstanceState(((Bundle) state)
					.getParcelable(INSTANCE_STATUS));
			// 恢复我们定义的状态
			mAlpha = ((Bundle) state).getFloat(STATUS_ALPHA);
			return;
		}
		super.onRestoreInstanceState(state);
	}

	@Override
	protected Parcelable onSaveInstanceState() {
		Bundle bundle = new Bundle();
		// 保存View的自身状态
		bundle.putParcelable(INSTANCE_STATUS, super.onSaveInstanceState());
		// 保存我们定义的状态
		bundle.putFloat(STATUS_ALPHA, mAlpha);
		return bundle;
	}
}
