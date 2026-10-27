package app.gkai.ai.core.common.view;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import app.gkai.ai.R;


public class MyDirectToast {
	private static Toast toast; 
	public final static int fail = 1;
	public final static int success = 2;
	public final static int waring = 3;
	public final static int text = 4;

	@SuppressWarnings("static-access")
	public static void show(Context paramContext, int paramInt, String paramString) {
		if (paramContext == null)
			return;
		/*View localView = LayoutInflater.from(paramContext).inflate(R.layout.mydirecttoast,
				(ViewGroup) ((Activity) paramContext).findViewById(2131100292));*/
		//此处出BUG做一修改
		View localView = LayoutInflater.from(paramContext).inflate(R.layout.mydirecttoast,
				(ViewGroup) ((Activity) paramContext).findViewById(R.id.toast_layout_root));
		TextView localTextView = (TextView) localView.findViewById(R.id.text);
		ImageView localImageView = (ImageView) localView.findViewById(R.id.icon);
		localTextView.setText(paramString);

		switch (paramInt) {

		case fail:
			localImageView.setImageResource(R.drawable.operation_fail_icon);
			break;
		case success:
			localImageView.setImageResource(R.drawable.operation_success_icon);
			break;
		case waring:
			localImageView.setImageResource(R.drawable.operation_waring_icon);

			break;
		default:
			break;
		}
		if (toast == null)
			toast = new Toast(paramContext);
		toast.setDuration(toast.LENGTH_LONG);
		toast.setView(localView);
		toast.setGravity(17, 0, 100);
		toast.show();
	}

}
