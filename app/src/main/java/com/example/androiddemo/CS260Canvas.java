package com.example.androiddemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CS260Canvas extends View implements View.OnTouchListener
{
  private ShapeDrawable mShape;
  private ScheduledExecutorService mDrawLoop;
  private Paint mLinePaint;
  private Paint mTextPaint;

  public CS260Canvas (Context context)
  {
    super (context);
    setup();
  }

  public CS260Canvas (Context context, @Nullable AttributeSet attrs)
  {
    super (context, attrs);
    setup();
  }

  public CS260Canvas (Context context, @Nullable AttributeSet attrs,
      int defStyleAttr)
  {
    super (context, attrs, defStyleAttr);
    setup();
  }

  public CS260Canvas (Context context, @Nullable AttributeSet attrs,
      int defStyleAttr, int defStyleRes)
  {
    super (context, attrs, defStyleAttr, defStyleRes);
    setup();
  }

  public void setup()
  {
    mShape = new ShapeDrawable(new OvalShape());
    mShape.getPaint().setColor(
        ContextCompat.getColor(getContext(), R.color.blue));
    mShape.setBounds(100, 100, 100 + 20, 100 + 20);

    this.setOnTouchListener(this);

    mLinePaint = new Paint(Color.RED);
    mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    mTextPaint.setTextSize(75);
    mTextPaint.setTextAlign(Paint.Align.CENTER);
  }

  public void start()
  {
    mDrawLoop = Executors.newScheduledThreadPool(1);
    mDrawLoop.scheduleAtFixedRate(this::update,
        0, 1000/24, TimeUnit.MILLISECONDS);
  }

  public void stop()
  {
    mDrawLoop.shutdown();
  }

  @Override
  public boolean onTouch(View view, MotionEvent motionEvent)
  {
    //Log.d("GRAPHICS", "onTouch" + motionEvent.getX() + " , " +
        //motionEvent.getY());
    int x, y;
    if(motionEvent.getActionMasked() == MotionEvent.ACTION_DOWN);
    {
      x = (int) motionEvent.getX();
      y = (int) motionEvent.getY();

      mShape.setBounds(x, y, x + 100, y + 100);
      //invalidate();
    }

    return false;
  }

  public void onDraw(Canvas canvas)
  {
    if(mShape != null)
    {
      mShape.draw(canvas);
    }

    if(mLinePaint != null)
    {
      canvas.drawLine(0, 0, getWidth(), getHeight(), mLinePaint);
    }
    if(mTextPaint != null)
    {
      canvas.drawText("Hello CS 260", getWidth()/2.0f,
          getHeight()/2.0f, mTextPaint);
    }
  }

  public void update()
  {
    int delta = 2;
    Rect localRect;

    /*move items*/
    if(mShape != null)
    {
      localRect = mShape.getBounds();
      localRect.offset(delta, delta);

      if(localRect.bottom > getHeight())
      {
        localRect.offset(0, -getHeight());
      }

      if(localRect.right > getWidth())
      {
        localRect.offset(-getWidth(), 0);
      }

      mShape.setBounds(localRect);
    }

    invalidate();
  }
}