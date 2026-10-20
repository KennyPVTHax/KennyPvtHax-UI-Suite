package com.kennypvthax.uisuite;

import android.content.Context;
import android.graphics.*;
import android.view.View;

/** Existing KennyIdentity::Emblem geometry, without Anime photograph assets. */
final class BrandMark extends View {
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final RectF arc = new RectF();
    private Shader surface;
    BrandMark(Context context) {
        super(context);
        setContentDescription("KennyPvtHax");
        setImportantForAccessibility(IMPORTANT_FOR_ACCESSIBILITY_NO);
    }
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        surface = new LinearGradient(0, 0, 0, h, 0xff321c27, 0xff0b090d, Shader.TileMode.CLAMP);
    }
    protected void onDraw(Canvas canvas) {
        float side = Math.min(getWidth(), getHeight());
        float scale = side / 42f, x = getWidth() / 2f, y = getHeight() / 2f;
        paint.setStyle(Paint.Style.FILL); paint.setShader(surface);
        canvas.drawCircle(x, y, 38 * scale * .48f, paint);
        paint.setShader(null); paint.setStyle(Paint.Style.STROKE); paint.setStrokeWidth(scale);
        paint.setColor(0x5294717d); canvas.drawCircle(x, y, 38 * scale * .49f, paint);
        float radius = 38 * scale * .49f;
        arc.set(x - radius, y - radius, x + radius, y + radius);
        paint.setColor(0xd9e78695); paint.setStrokeWidth(1.4f * scale);
        canvas.drawArc(arc, -126, 140, false, paint);
        paint.setStrokeWidth(2 * scale); paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setColor(Tokens.TEXT); canvas.drawLine(x - 6*scale,y - 10*scale,x - 6*scale,y + 10*scale,paint);
        canvas.drawLine(x - 5*scale,y,x + 8*scale,y - 10*scale,paint);
        paint.setColor(Tokens.SELECTED); canvas.drawLine(x - 5*scale,y,x + 8*scale,y + 10*scale,paint);
    }
}
