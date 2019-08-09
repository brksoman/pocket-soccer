package rs.etf.ba150210d.soccer.datastructures;

import android.content.Context;
import android.graphics.drawable.Drawable;

import rs.etf.ba150210d.soccer.R;

public class Team extends ChoiceImage {

    public Team(Context context, int index) {
        super(context, index);
    }

    @Override
    protected int getResourceId() {
        return R.array.country_names;
    }
}
