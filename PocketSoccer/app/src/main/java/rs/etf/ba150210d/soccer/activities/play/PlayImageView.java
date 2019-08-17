package rs.etf.ba150210d.soccer.activities.play;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import rs.etf.ba150210d.soccer.datastructures.PlayData;

public class PlayImageView extends AppCompatImageView {

    private Paint mPaint;
    private PlayData mData = null;

    public PlayImageView(Context context) {
        super(context);
        init();
    }

    public PlayImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PlayImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
    }

    public void setData(PlayData data) {
        mData = data;
        mData.updateDims(getWidth(), getHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mData != null) {
            mData.draw(canvas, mPaint);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);

        if (w > 0 && h > 0 && mData != null) {
            mData.updateDims(w, h);
        }
    }
}
