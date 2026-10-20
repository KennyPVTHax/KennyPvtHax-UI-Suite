package com.kennypvthax.uisuite;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.text.InputType;
import android.view.Gravity;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

/** Native text editing/selection with existing input radius and focus colors. */
final class LicenseField extends EditText {
    private final GradientDrawable surface;
    private ValueAnimator focus;
    private float emphasis;
    LicenseField(Context c) {
        super(c);
        setTextSize(16); setTextColor(Tokens.TEXT); setHighlightColor(0x59ff5664); setHintTextColor(Tokens.SECONDARY);
        setHint(R.string.key_hint); setContentDescription(c.getString(R.string.key_hint));
        setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
        setTypeface(Typeface.create("sans-serif",Typeface.NORMAL));
        setSingleLine(true);
        // setSingleLine replaces the transformation; apply masking after that call.
        setTransformationMethod(android.text.method.PasswordTransformationMethod.getInstance());
        setSaveEnabled(false);
        setImportantForAutofill(IMPORTANT_FOR_AUTOFILL_NO_EXCLUDE_DESCENDANTS);
        setImeOptions(EditorInfo.IME_ACTION_DONE|EditorInfo.IME_FLAG_NO_PERSONALIZED_LEARNING);
        setTextDirection(TEXT_DIRECTION_LTR);
        setGravity(Gravity.CENTER_VERTICAL|Gravity.START);
        int pad=(int)KennyComponents.dp(c,14);setPadding(pad,0,pad,0);
        surface=KennyComponents.surface(c,Tokens.SUBTLE,Tokens.OUTLINE,8);setBackground(surface);
        setOnFocusChangeListener((v,hasFocus)->animateFocus(hasFocus?1:0));
    }
    private void animateFocus(float target) {
        if(focus!=null)focus.cancel();
        if(KennyComponents.reducedMotion){emphasis=target;paintBorder();return;}
        focus=ValueAnimator.ofFloat(emphasis,target);focus.setDuration(160);
        focus.addUpdateListener(a->{emphasis=(float)a.getAnimatedValue();paintBorder();});focus.start();
    }
    private void paintBorder(){surface.setStroke(Math.max(1,(int)KennyComponents.dp(getContext(),1)),KennyComponents.blend(Tokens.OUTLINE,0xffab6273,emphasis));}
    protected void onDetachedFromWindow(){if(focus!=null)focus.cancel();super.onDetachedFromWindow();}
}
