package rs.etf.ba150210d.soccer.view.play;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import rs.etf.ba150210d.soccer.R;

public class PlayImageView extends AppCompatImageView {

    private Paint mPaint;
    private PlayData mData = null;
    private int mScoredPlayer = PlayMetadata.NO_PLAYER;

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

        Bitmap flag = BitmapFactory.decodeResource(getResources(), R.drawable.serbia_flag);
        Bitmap ball = BitmapFactory.decodeResource(getResources(), R.drawable.ball);

        this.setBackground(getResources().getDrawable(R.drawable.field_stealth));
        mData = new PlayData(getWidth(), getHeight(), flag, flag, ball);
    }

    public PlayData getData() {
        return mData;
    }

    public void setScoredPlayer(int scoredPlayer) {
        mScoredPlayer = scoredPlayer;
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

        if (w > 0 && h > 0) {
            mData.updateDims(w, h);
        }
    }
}
