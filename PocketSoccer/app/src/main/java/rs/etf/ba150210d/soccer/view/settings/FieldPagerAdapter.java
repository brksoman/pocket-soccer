package rs.etf.ba150210d.soccer.view.settings;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import rs.etf.ba150210d.soccer.R;

public class FieldPagerAdapter extends PagerAdapter {
    public static final int FIELD_COUNT = 4;

    private Context mContext;
    private List<String> mFieldNames;

    @Override
    public int getCount() {
        return FIELD_COUNT;
    }

    public void setContext(Context context) {
        mContext = context;
        String[] fieldNames = mContext.getResources().getStringArray(R.array.field_names);
        mFieldNames = Arrays.asList(fieldNames);
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
