package com.kennypvthax.uisuite;
import android.content.Context;
import android.graphics.*;
import android.view.View;
/** Bounded presentation, scheduled only for a visible pending request. */
final class LoadingMark extends View {
    private final Paint paint = new Paint(3);
    private final RectF bounds = new RectF();
    private boolean running;
    private float angle;
    private long previous;
    LoadingMark(Context c) { super(c); setImportantForAccessibility(IMPORTANT_FOR_ACCESSIBILITY_NO); }
    void running(boolean value) { running=value; previous=0; invalidate(); }
    protected void onDraw(Canvas c) {
        float d=getResources().getDisplayMetrics().density;
        bounds.set(4*d,4*d,getWidth()-4*d,getHeight()-4*d);
        paint.setStyle(Paint.Style.STROKE); paint.setStrokeWidth(2*d);
        paint.setStrokeCap(Paint.Cap.ROUND); paint.setColor(0xff35252d); c.drawOval(bounds,paint);
        if(running&&!KennyComponents.reducedMotion) {
            long now=System.nanoTime();
            if(previous!=0) angle=(angle+180*AuroraModel.delta((now-previous)/1e9f))%360;
            previous=now;
            postInvalidateOnAnimation();
        }
        paint.setColor(Tokens.FIRE); c.drawArc(bounds,angle,95,false,paint);
    }
    protected void onDetachedFromWindow(){running=false;previous=0;super.onDetachedFromWindow();}
}
