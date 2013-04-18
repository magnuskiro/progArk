package de.vogella.android.draganddrop;

import android.app.Activity;
import android.content.ClipData;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.Settings.System;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class DragActivity extends Activity {

	/** Called when the activity is first created. */
	int screenheight;
	int screenwidht;
	int gridsize;
	long time;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		findViewById(R.id.myimage2).setOnTouchListener(new MyTouchListener());
		findViewById(R.id.myimage21).setOnTouchListener(new MyTouchListener());
		findViewById(R.id.myimage22).setOnTouchListener(new MyTouchListener());
		//		findViewById(R.id.myimage21).setScaleX((float) 2);
		//		findViewById(R.id.myimage21).setScaleY((float) 2);
		findViewById(R.id.topleft).setOnDragListener(new MyDragListener());
		View v = findViewById(R.id.topleft);

		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		screenheight=dm.heightPixels;
		screenwidht= dm.widthPixels;
		gridsize= screenwidht/10;

		time = 0;


		Log.d("size", ""+screenwidht);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(screenwidht, screenwidht);
		v.setLayoutParams(params);


		//    Canvas canvas = new Canvas();
		//    int width = v.getWidth();
		//	Paint background = new Paint();
		//	background.setColor(android.graphics.Color.BLUE);
		//	canvas.drawRect(0, 0, v.getWidth(), v.getHeight(), background);
		//	Paint dark = new Paint();
		//	dark.setColor(android.graphics.Color.BLACK);
		//	for (int i = 0; i <= 10; i++) {
		//		canvas.drawLine(0, i * 4, width, i * 4, dark);
		//		canvas.drawLine(i * 4, 0, i * 4, width, dark);
		//	}
		//	v.draw(canvas);
		//    System.out.println(findViewById(R.id.myimage1).getX());

	}

	private final class MyTouchListener implements OnTouchListener {
		public boolean onTouch(View view, MotionEvent motionEvent) {
			if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
				time = motionEvent.getDownTime();
				ClipData data = ClipData.newPlainText("", "");
				DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
				view.startDrag(data, shadowBuilder, view, 0);
//				view.setVisibility(View.INVISIBLE);
				return true;
			} else {
				return false;
			}
		}
	}



	class MyDragListener implements OnDragListener {
		Drawable enterShape = getResources().getDrawable(R.drawable.shape_droptarget);
		Drawable normalShape = getResources().getDrawable(R.drawable.shape);

		@Override
		public boolean onDrag(View v, DragEvent event) {
			int action = event.getAction();
			View view = (View) event.getLocalState();
			ViewGroup owner = (ViewGroup) view.getParent();
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(gridsize,gridsize*3);
			RelativeLayout container = (RelativeLayout) v;
			switch (action) {
			case DragEvent.ACTION_DRAG_STARTED:
				// Do nothing
				break;
			case DragEvent.ACTION_DRAG_ENTERED:
				Log.d("movement", "drag entered");
				v.setBackgroundDrawable(enterShape);
				break;
			case DragEvent.ACTION_DRAG_EXITED:
				Log.d("movement", "drag exited");
				v.setBackgroundDrawable(normalShape);
				owner.removeView(view);

				Log.d("movement","event x: " + event.getX() + " y: " + event.getY());
				Log.d("movement","event grid x: " + (int)event.getX()/gridsize + " y: " + (int)event.getY()/gridsize);

				params.leftMargin = Math.max((int)( event.getX() - event.getX()%gridsize), 0);
				params.topMargin = Math.min(Math.max((int)( event.getY() - event.getY()%gridsize), 0), 10*gridsize - view.getHeight());

				Log.d("movement",""+params.leftMargin);
				Log.d("movement",""+params.topMargin);

				container.addView(view, params);
				view.setVisibility(View.VISIBLE);
				break;
			case DragEvent.ACTION_DROP:
				// Dropped, reassign View to ViewGroup
				owner.removeView(view);

				Log.d("movement","event x: " + event.getX() + " y: " + event.getY());
				Log.d("movement","event grid x: " + (int)event.getX()/gridsize + " y: " + (int)event.getY()/gridsize);

				params.leftMargin = Math.max((int)( event.getX() - event.getX()%gridsize), 0);
				params.topMargin = Math.min(Math.max((int)( event.getY() - event.getY()%gridsize), 0), 10*gridsize - view.getHeight());

				Log.d("movement",""+params.leftMargin);
				Log.d("movement",""+params.topMargin);

				container.addView(view, params);
				view.setVisibility(View.VISIBLE);
				break;
			case DragEvent.ACTION_DRAG_ENDED:
				if(view.getDrawingTime() - time < 200){
					Log.d("movement","eventtime minus drawingtime: " +(view.getDrawingTime() - time));
					if(view.getRotation() == 0){
						view.setRotation(90);
					}else{
						view.setRotation(0);
					}
				}
				view.setVisibility(View.VISIBLE);

				v.setBackgroundDrawable(normalShape);
				Log.d("movement", "drag ended");
			default:
				break;
			}
			return true;
		}
	}
} 