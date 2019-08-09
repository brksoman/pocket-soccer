package rs.etf.ba150210d.soccer.datastructures;

import android.content.Context;
import android.graphics.drawable.Drawable;

import rs.etf.ba150210d.soccer.R;

public abstract class ChoiceImage {

    protected Context mContext;
    protected int mIndex;
    protected int mId;
    protected String mName;

    public ChoiceImage(Context context, int index) {
        mContext = context;
        mIndex = index;

        mName = mContext.getResources().getStringArray(getResourceId())[mIndex];
        mId = mContext.getResources().getIdentifier(mName, "drawable",
                mContext.getPackageName());
    }

    protected abstract int getResourceId();

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
