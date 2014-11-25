package be.fastrada.components;

import android.content.Context;
import android.preference.EditTextPreference;
import android.util.AttributeSet;

public class NumberPreference extends EditTextPreference {
    public NumberPreference(Context context) {
        super(context);
    }

    public NumberPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NumberPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected String getPersistedString(String defaultReturnValue) {
        return String.valueOf(getPersistedInt(-1));
    }

    @Override
    protected boolean persistString(String value) {
        return persistInt(Integer.valueOf(value));
    }
}