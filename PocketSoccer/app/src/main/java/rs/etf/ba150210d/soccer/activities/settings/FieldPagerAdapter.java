package rs.etf.ba150210d.soccer.activities.settings;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import rs.etf.ba150210d.soccer.datastructures.Field;

public class FieldPagerAdapter extends PagerAdapter {
    public static final int FIELD_COUNT = 4;

    private Context mContext;

    public FieldPagerAdapter(Context context) {
        super();
        mContext = context;
    }

    @Override
    public int getCount() {
        return FIELD_COUNT;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        Field field = new Field(mContext, position);
        ImageView image = new ImageView(mContext);

        image.setImageResource(field.getId());
        container.addView(image);
        return image;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ImageView)object);
    }

}
