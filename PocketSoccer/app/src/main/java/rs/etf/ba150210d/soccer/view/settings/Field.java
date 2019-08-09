package rs.etf.ba150210d.soccer.view.settings;

import android.content.Context;
import android.graphics.drawable.Drawable;

import java.util.List;

import rs.etf.ba150210d.soccer.R;

public class Field {

    private Context mContext;
    private int mIndex;
    private int mId;
    private String mName;

    public Field(Context context, int index) {
        mContext = context;
        mIndex = index;

        mName = mContext.getResources().getStringArray(R.array.field_names)[mIndex];
        mId = mContext.getResources().getIdentifier(mName, "drawable",
                mContext.getPackageName());
    }

    public int getIndex() {
        return mIndex;
    }

    public int getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public Drawable getImage() {
        return mContext.getDrawable(mId);
    }
}
