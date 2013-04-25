package no.ntnu.Battleship.graphics;

import java.lang.ref.WeakReference;

import android.graphics.Canvas;
import android.graphics.Point;
import android.view.View;
import android.view.View.DragShadowBuilder;

/**
 * Custom {@link DragShadowBuilder} for moving {@link PlatformView}. Shadows are
 * positioned with the bottom tile under the dragpoint.
 * @author Håvard
 *
 */
class PlatformShadowBuilder extends View.DragShadowBuilder{
	private final WeakReference<View> mView;
	private float tSize;

	public PlatformShadowBuilder(View v, float tSize) {
		mView = new WeakReference<View>(v);
		this.tSize = tSize;
	}


	@Override
	public void onProvideShadowMetrics (Point size, Point touch){
		final View view = mView.get();
		if (view != null) {
			size.set(view.getWidth(), view.getHeight());
			touch.set(size.x / 2, (int) (size.y - (tSize/2)));
		}
	}


	@Override
	public void onDrawShadow(Canvas canvas) {
		final View view = mView.get();
		if (view != null) {
			view.draw(canvas);
		}
	}

}