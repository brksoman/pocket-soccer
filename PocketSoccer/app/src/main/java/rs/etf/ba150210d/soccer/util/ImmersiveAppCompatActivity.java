package rs.etf.ba150210d.soccer.util;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListPopupWindow;
import android.widget.PopupWindow;
import android.widget.Spinner;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;

/**
 * Abstract extension of AppCompatActivity which contains all the necessary additions for fullscreen
 * function. Also contains methods for forcing fullscreen on Spinners and EditTexts.
 */
public abstract class ImmersiveAppCompatActivity
        extends AppCompatActivity {

    private HideHandler mHideHandler;

    private SpinnerHideHandler mSpinnerHideHandler = null;
    private EditTextBackEvent.EditTextImeBackListener mEditTextListener = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // create a handler to set immersive mode on a delay
        mHideHandler = new HideHandler(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setToImmersiveMode();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus) {
            mHideHandler.removeMessages(0);
            mHideHandler.sendEmptyMessageDelayed(0, 300);
        }
        else {
            mHideHandler.removeMessages(0);
        }
    }

    private void setToImmersiveMode() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    private static class HideHandler extends Handler {
        private final WeakReference<ImmersiveAppCompatActivity> mActivity;

        HideHandler(ImmersiveAppCompatActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            ImmersiveAppCompatActivity activity = mActivity.get();
            if(activity != null) {
                activity.setToImmersiveMode();
            }
        }
    }

    public void avoidUiWithSpinner(Spinner spinner) {
        if (mSpinnerHideHandler == null) {
            mSpinnerHideHandler = new SpinnerHideHandler();
        }
        mSpinnerHideHandler.avoidUiWithSpinner(spinner);
    }

    public void avoidUiWithEditText(EditTextBackEvent editText) {
        if (mEditTextListener == null) {
            mEditTextListener = new EditTextBackEvent.EditTextImeBackListener() {
                @Override
                public void onImeBack(EditTextBackEvent ctrl, String text) {
                    setToImmersiveMode();
                }
            };
        }
        editText.setOnEditTextImeBackListener(mEditTextListener);
    }

    private class SpinnerHideHandler {

        public SpinnerHideHandler() {
        }

        public void avoidUiWithSpinner(Spinner spinner) {
            try {
                Field listPopupField = Spinner.class.getDeclaredField("mPopup");
                listPopupField.setAccessible(true);
                Object listPopup = listPopupField.get(spinner);
                if (listPopup instanceof ListPopupWindow) {
                    Field popupField = ListPopupWindow.class.getDeclaredField("mPopup");
                    popupField.setAccessible(true);
                    Object popup = popupField.get((ListPopupWindow) listPopup);
                    if (popup instanceof PopupWindow) {
                        ((PopupWindow) popup).setFocusable(false);
                    }
                }
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
