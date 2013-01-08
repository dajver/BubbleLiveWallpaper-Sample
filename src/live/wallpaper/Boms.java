package live.wallpaper;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Boms {
	LiveWallpaperPainting pm;
	Bitmap bmp;
	
	public float x;
	public float y;
	
	public int widht;
	public int height;
	
	Timer timer;
	
	private int life = 30;
    private List<Boms> temps;
	
	public Boms(List<Boms> temps, LiveWallpaperPainting pm, Bitmap bmp, float x, float y) {
		this.pm = pm;
		this.bmp = bmp;
		
		this.x = x;
		this.y = y;
	}
	
	public void onDraw(Canvas c) {
		update();
		c.drawBitmap(bmp, x, y, null);
	}
	
	private void update() {
		/*if(--life < 1) {
			temps.remove(this);
		}*/
	}
}
