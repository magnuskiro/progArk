package no.ntnu.Battleship.graphics;

import no.ntnu.Battleship.Platform;
import no.ntnu.Battleship.R;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

/**
 * View that shows the user where one of his platforms will be placed
 * @author Håvard
 *
 */
public class PlatformView extends ImageView{

	Platform platform;
	float tileSize;
	Resources res;


	/**
	 * main constructor
	 * @param context - the application context
	 * @param plat - the platform to represent
	 * @param res - resources enabling loading of appropriate image
	 * @param tileSize - size of a single square of the board in pixels
	 */
	public PlatformView(Context context, Platform plat, Resources res, float tileSize) {
		super(context);
		this.tileSize = tileSize;
		this.res	= res;
		setPlatform(plat);

		Log.d("movement", "pivX: " + tileSize/2 + " pivY: " +  ((plat.getlength()*tileSize) -(tileSize/2)));
	}

	/**
	 * 	 * @return the platform this view is representing
	 */
	public Platform getPlatform(){
		return platform;
	}

	/**
	 * Set a new platform to represent 
	 * @param plat
	 */
	public void setPlatform(Platform plat){
		platform = plat;

		switch(plat.getlength()){
		case 2:
			this.setImageBitmap(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(res, R.drawable.ship2_96),
					(int)tileSize, (int)tileSize * 2, false));
			break;
		case 3:
			this.setImageBitmap(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(res, R.drawable.ship3_96),
					(int)tileSize, (int)tileSize * 3, false));
			break;
		case 4:
			this.setImageBitmap(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(res, R.drawable.ship4_96),
					(int)tileSize, (int)tileSize * 4, false));
			break;
		case 5:
			this.setImageBitmap(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(res, R.drawable.ship5_96),
					(int)tileSize, (int)tileSize * 5, false));
			break;
		}
		setPivotX(tileSize/2);
		setPivotY(plat.getlength()*tileSize - tileSize/2);
	}
}
