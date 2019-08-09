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

    @Override
    public String getName() {
        StringBuilder sb = new StringBuilder();

        for (int fromIndex = 0; fromIndex < mName.length(); ) {
            sb.append(Character.toUpperCase(mName.charAt(fromIndex)));

            int spaceIndex = mName.indexOf("_", fromIndex);
            if (spaceIndex == -1) {
                sb.append(mName.substring(fromIndex + 1));
                break;
            } else {
                sb.append(mName.substring(fromIndex + 1, spaceIndex - 1) + " ");
            }
            fromIndex = spaceIndex + 1;
        }

        return sb.toString();
    }
}
