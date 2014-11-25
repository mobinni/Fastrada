package be.fastrada.components;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * TextView with custom Font
 */
public class DigitalTextView extends TextView {
    public DigitalTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public DigitalTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DigitalTextView(Context context) {
        super(context);
        init();
    }

    private void init() {
        final Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/DS-DIGI.otf");
        setTypeface(tf);
    }
}
