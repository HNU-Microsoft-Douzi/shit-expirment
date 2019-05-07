package com.example.administrator.lightingplane;

import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Enemy extends Plane {
	int moveStyle;
	int STEPY = 10;
	public Enemy(Context context, int screenWidth, int screenHeight,
			Bitmap[] planePics) {
		super(context, screenWidth, screenHeight, planePics);
		// TODO Auto-generated constructor stub
		shotStyle = 1;
		health = 10;
		lives = 1;
		state = 2;
		STEP = -5;
		shotInterval = 20;
		//�ı��ӵ�������
		for(Bullet bullet:bullets){
			bullet.bulletPic = BitmapFactory.decodeResource(context.getResources(),R.drawable.enzd1);
			bullet.belongTo = false;
			bullet.step = -10;
		}
	}

	public void impact() {
		for(Plane enemy:enemys){
			if(enemy.state == 1 && state == 1){
				if((nowX > enemy.nowX && nowX < (enemy.nowX + enemy.width) && nowY > enemy.nowY && nowY < (enemy.nowY + enemy.height))
						|| ((nowX+width) > enemy.nowX && (nowX+width) < (enemy.nowX + enemy.width) && (nowY + height) > enemy.nowY && (nowY + height) < (enemy.nowY + enemy.height))){
					health -= 10;
					enemy.health -= 10;
					if(enemy.health <= 0 && FinalPlaneActivity.soundFlag){
						FinalPlaneActivity.bombMusic.start();
					}
				}
			}
		}
	}
	
	@Override
	public void move(Canvas canvas, Paint paint, int moveToX, int moveToY) {
		// TODO Auto-generated method stub
		impact();//��ײ���
		if(health <= 0){
			if(!animation.isEnd)
				animation.start(canvas, paint, nowX, nowY);
			else{
				state = 2;
				animation.isEnd = false;
				//reset();
			}
		}else{
			switch(moveStyle){
			case 0://ֱ�ߴ���������
				nowY -= STEP;
				break;
			case 1://����������
				if(nowX <= 0 || nowX >= screenWidth - width){
					STEPY = -STEPY;
				}
				nowY -= STEP;
				nowX += STEPY;
				break;
//			case 2://��ɱʽս��
//				
//					if(Math.abs(moveToX-nowX)<STEP){
//						nowX = moveToX;
//					}
//					else if(moveToX > nowX && moveToX < screenWidth && moveToX > 0){
//						nowX += STEP;
//					}
//					else if(moveToX < nowX && moveToX < screenWidth && moveToX > 0){
//						nowX -= STEP;
//					}
//					
//					if(Math.abs(moveToY-nowY)<STEP){
//						nowY = moveToY;
//					}
//					else if(moveToY > nowY && moveToY < screenHeight && moveToY > 0)
//						nowY += STEP;
//					else if(moveToY < nowY && moveToY < screenHeight && moveToY > 0)
//						nowY -= STEP;
//					break;
			}

			canvas.drawBitmap(planePics[0], nowX, nowY, paint);
			}
			
		
		
		//�л�����
		if(nowX < -width/2 || nowX > screenWidth || nowY > screenHeight){
			state = 2;
		}
		//�ӵ��ƶ�
		bulletsMove(canvas,paint);
	}
	
	/* (non-Javadoc)
	 * ���õл�
	 * @see cn.com.wygame.Plane#reset()
	 */
	@Override
	public void reset(){
		Random random = new Random();
		nowY = -screenHeight;
		nowX = Math.abs(random.nextInt()%(screenWidth+1-width));
		state = 1;
		health = 10;
	}

}