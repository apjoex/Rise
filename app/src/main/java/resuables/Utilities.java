package resuables;

import android.content.Context;
import android.graphics.Typeface;
import android.support.design.widget.TabLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DecimalFormat;

/**
 * Created by AKINDE-PETERS on 7/7/2016.
 */
public class Utilities {

    public static void changeTabsFont(Context context, TabLayout tabs) {

        ViewGroup vg = (ViewGroup) tabs.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    ((TextView) tabViewChild).setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Montserrat-Light.otf"));
                }
            }
        }
    }

    public static int roundUpDivide(long num, long divisor)
    {
        return (int) ((num + divisor - 1) / divisor);
    }

    public static int roundUpDivideDouble(double num, long divisor)
    {
        return (int) ((num + divisor - 1) / divisor);
    }

    public static int roundUp(double d)
    {
        DecimalFormat twoDForm = new DecimalFormat("#");
        return Integer.valueOf(twoDForm.format(d));
    }

    public static double roundUpTo2dp(double d)
    {
        DecimalFormat twoDForm = new DecimalFormat("#.##");
        return Double.parseDouble(twoDForm.format(d));
    }

    public static double roundUpTo1dp(double d)
    {
        DecimalFormat twoDForm = new DecimalFormat("#.#");
        return Double.parseDouble(twoDForm.format(d));
    }

}
