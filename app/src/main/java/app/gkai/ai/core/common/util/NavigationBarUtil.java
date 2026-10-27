package app.gkai.ai.core.common.util;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;

/** Makes the bottom system navigation area transparent while keeping its gesture indicator. */
public final class NavigationBarUtil {

    private NavigationBarUtil() {
    }

    public static void showGestureBarOnContent(Activity activity) {
        if (activity == null) {
            return;
        }

        Window window = activity.getWindow();
        View decorView = window.getDecorView();

        // Keep the top status bar. The app draws behind the transparent navigation area.
        window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // Without this, the transparent navigation bar reveals the black window background
            // instead of the page because the content is still inset above the gesture area.
            window.setDecorFitsSystemWindows(false);
        }

        window.setNavigationBarColor(Color.TRANSPARENT);

        int systemUiVisibility = decorView.getSystemUiVisibility()
                & View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        systemUiVisibility |= View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // A light navigation bar uses a dark gesture indicator on the white page.
            systemUiVisibility |= View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
        }
        decorView.setSystemUiVisibility(systemUiVisibility);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Prevent Android from adding a contrast scrim/black background behind the indicator.
            window.setNavigationBarContrastEnforced(false);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowInsetsController controller = window.getInsetsController();
            if (controller != null) {
                controller.show(WindowInsets.Type.statusBars() | WindowInsets.Type.navigationBars());
                controller.setSystemBarsAppearance(
                        WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS,
                        WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS);
            }
        }
    }
}
