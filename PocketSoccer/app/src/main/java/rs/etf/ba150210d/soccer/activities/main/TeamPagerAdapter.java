package rs.etf.ba150210d.soccer.activities.main;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import rs.etf.ba150210d.soccer.datastructures.Team;

public class TeamPagerAdapter extends PagerAdapter {
    public static final int TEAM_COUNT = 20;

    private Context mContext;

    @Override
    public int getCount() {
        return TEAM_COUNT;
    }

    public void setContext(Context context) {
        mContext = context;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        Team team = new Team(mContext, position);
        ImageView image = new ImageView(mContext);

        image.setImageResource(team.getId());
        container.addView(image);
        return image;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ImageView)object);
    }


}
