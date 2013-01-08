package live.wallpaper;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

public class LiveWallpaperPainting extends Thread implements Runnable{

        private SurfaceHolder surfaceHolder;
        private Context context;
        
        /** Состояние потоков */
        private boolean wait;
        private boolean run;
        
        /** Выосота и ширина сцены */
        private int width;
        private int height;
        
        /**Скоре, достижение :)*/
        private int score = 0;

        /**Список бульбашек который будет бесконечным*/
        public List<Bubble> bubble = new ArrayList<Bubble>();
        
        /**Это если захотишь сделать что бы выводило спрайт после взрыва*/
        //private List<Boms> temps = new ArrayList<Boms>();

        //private Bitmap blood;
        
        private float posX;
        private float posY;
        
        private Paint mScorePaint;
        
        private Bitmap bg;
        private Bitmap bubbles;
  
        public LiveWallpaperPainting(SurfaceHolder surfaceHolder, Context context, int radius) {
                this.surfaceHolder = surfaceHolder;
                this.context = context;

                this.wait = true;
                
                bubbles = BitmapFactory.decodeResource(context.getResources(), R.drawable.bubble);
                //blood = BitmapFactory.decodeResource(context.getResources(), R.drawable.blood1);
                bg = BitmapFactory.decodeResource(context.getResources(), R.drawable.bg);
                
             // стили для вывода счета
                mScorePaint = new Paint();
                mScorePaint.setTextSize(20);
                mScorePaint.setStrokeWidth(1);
                mScorePaint.setStyle(Style.FILL);
                mScorePaint.setColor(Color.WHITE);
        }

        /**
         * Pauses the livewallpaper animation
         */
        public void pausePainting() {
                this.wait = true;
                synchronized(this) {
                        this.notify();
                }
        }

        /**
         * Resume the livewallpaper animation
         */
        public void resumePainting() {
                this.wait = false;
                synchronized(this) {
                        this.notify();
                }
        }

        /**
         * Stop the livewallpaper animation
         */
        public void stopPainting() {
                this.run = false;
                synchronized(this) {
                        this.notify();
                }
        }

        @Override
        public void run() {
                this.run = true;
                Canvas c = null;
                while (run) {
                        try {
                                c = this.surfaceHolder.lockCanvas(null);
                                synchronized (this.surfaceHolder) {
                                        Thread.sleep(50);
                                        bubble.add(new Bubble(this, bubbles));
                                        doDraw(c);
                                }
                        } catch (InterruptedException e) {
							e.printStackTrace();
						} finally {
                                if (c != null) {
                                        this.surfaceHolder.unlockCanvasAndPost(c);
                                }
                        }
                        // pause if no need to animate
                        synchronized (this) {
                                if (wait) {
                                        try {
                                                wait();
                                        } catch (Exception e) {}
                                }
                        }
                }
        }

        /**
         * Invoke when the surface dimension change
         * 
         * @param width
         * @param height
         */
        public void setSurfaceSize(int width, int height) {
                this.width = width;
                this.height = height;
                synchronized(this) {
                        this.notify();
                        bg = Bitmap.createScaledBitmap(bg, width, height, true);
                }
        }
        
        /**
         * Invoke while the screen is touched
         * 
         * @param event
         */
        public boolean doTouchEvent(MotionEvent event) {
        	posX = event.getX();
            posY = event.getY();
            synchronized (surfaceHolder) {
                   for (int i = bubble.size() - 1; i >= 0; i--) {
                          Bubble sprite = bubble.get(i);
                          if (sprite.isCollition(posX, posY)) {
                        	    bubble.remove(sprite);
                        	    score++;
                                //temps.add(new Boms(temps, this, blood, posX, posY));
                                break;
                          }
                   }
            }
            return true;
      }
        
        /**
         * Do the actual drawing stuff
         * 
         * @param canvas
         */
        private void doDraw(Canvas canvas) {
        		canvas.drawColor(Color.WHITE);
        		canvas.drawBitmap(bg, 0,0, null);
        		/*for (int i = temps.size() - 1; i >= 0; i--) {
                    temps.get(i).onDraw(canvas);
        		}*/
        		
        		for(Bubble bub: bubble) {
        			if(bub.y >= 0 || bub.x <= 0)
        				bub.onDraw(canvas);
        			else
        				bubble.remove(this);
        		}
        		
        	    canvas.drawText("Score: " + score, 50, 70, mScorePaint);
        	    
        	    //System.out.println(bubble.size());
        }        
        
}