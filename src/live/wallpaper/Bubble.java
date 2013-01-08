package live.wallpaper;

import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Bubble
{
	public int x;
	public int y;
	
	public int widht;
	public int height;
	
	public int speed;
	
	public double angle;
	
	Bitmap bmp;
	LiveWallpaperPainting pm;
	
	public Bubble(LiveWallpaperPainting pm, Bitmap bmp) {
		this.pm = pm;
		this.bmp = bmp;
		
		Random rnd = new Random(System.currentTimeMillis());
		this.y = 1000;
		this.x = rnd.nextInt(800);
		
		this.speed = rnd.nextInt(15 - 5) + 15;
		
		this.widht = 75;
		this.height = 75;
		
		angle = getRandomAngle();
	}
	
	public void update() {
		y -= Math.abs(speed * Math.cos(angle));
		x -= Math.abs(speed * Math.sin(angle));
	}
	
	private int getRandomAngle() {
        Random rnd = new Random(System.currentTimeMillis());
        return rnd.nextInt(1) * 90 + 90 / 2 + rnd.nextInt(15) + 5;
    }
	
	public void onDraw(Canvas c) {
		update();
		c.drawBitmap(bmp, x, y, null);
	}
	
	/**Проверка на столкновения*/
    public boolean isCollition(float x2, float y2) {
          return x2 > x && x2 < x + widht && y2 > y && y2 < y + height;
    }
}
