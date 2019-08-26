package rs.etf.ba150210d.soccer.datastructures;

import android.content.Context;

import rs.etf.ba150210d.soccer.R;

public class Field extends ChoiceImage {

    public Field(Context context, int index) {
        super(context, index);
    }

    @Override
    protected int getResourceId() {
        return R.array.field_names;
    }
}
