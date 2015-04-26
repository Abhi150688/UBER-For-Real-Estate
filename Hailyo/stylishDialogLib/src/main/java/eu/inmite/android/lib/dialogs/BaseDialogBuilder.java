package eu.inmite.android.lib.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

// TODO: Auto-generated Javadoc
/**
 * Internal base builder that holds common values for all dialog fragment builders.
 *
 * @param <T> the generic type
 * @author Tomas Vondracek
 */
abstract class BaseDialogBuilder<T extends BaseDialogBuilder<T>> {

	/** The arg request code. */
	public static String ARG_REQUEST_CODE = "request_code";
	
	/** The arg cancelable on touch outside. */
	public static String ARG_CANCELABLE_ON_TOUCH_OUTSIDE = "cancelable_oto";
	
	/** The default tag. */
	public static String DEFAULT_TAG = "simple_dialog";
	
	/** The default request code. */
	public static int DEFAULT_REQUEST_CODE = -42;

	/** The m context. */
	protected final Context mContext;
	
	/** The m fragment manager. */
	protected final FragmentManager mFragmentManager;
	
	/** The m class. */
	protected final Class<? extends BaseDialogFragment> mClass;

	/** The m target fragment. */
	private Fragment mTargetFragment;
	
	/** The m cancelable. */
	private boolean mCancelable = true;
	
	/** The m cancelable on touch outside. */
	private boolean mCancelableOnTouchOutside = true;

	/** The m tag. */
	private String mTag = DEFAULT_TAG;
	
	/** The m request code. */
	private int mRequestCode = DEFAULT_REQUEST_CODE;

	/**
	 * Instantiates a new base dialog builder.
	 *
	 * @param context the context
	 * @param fragmentManager the fragment manager
	 * @param clazz the clazz
	 */
	public BaseDialogBuilder(Context context, FragmentManager fragmentManager, Class<? extends BaseDialogFragment> clazz) {
		mFragmentManager = fragmentManager;
		mContext = context.getApplicationContext();
		mClass = clazz;
	}

	/**
	 * Self.
	 *
	 * @return the t
	 */
	protected abstract T self();

	/**
	 * Prepare arguments.
	 *
	 * @return the bundle
	 */
	protected abstract Bundle prepareArguments();

	/**
	 * Sets the cancelable.
	 *
	 * @param cancelable the cancelable
	 * @return the t
	 */
	public T setCancelable(boolean cancelable) {
		mCancelable = cancelable;
		return self();
	}
	
	/**
	 * Sets the cancelable on touch outside.
	 *
	 * @param cancelable the cancelable
	 * @return the t
	 */
	public T setCancelableOnTouchOutside(boolean cancelable) {
		mCancelableOnTouchOutside = cancelable;
		if (cancelable) {
			mCancelable = cancelable;
		}
		return self();
	}

	/**
	 * Sets the target fragment.
	 *
	 * @param fragment the fragment
	 * @param requestCode the request code
	 * @return the t
	 */
	public T setTargetFragment(Fragment fragment, int requestCode) {
		mTargetFragment = fragment;
		mRequestCode = requestCode;
		return self();
	}

	/**
	 * Sets the request code.
	 *
	 * @param requestCode the request code
	 * @return the t
	 */
	public T setRequestCode(int requestCode) {
		mRequestCode = requestCode;
		return self();
	}

	/**
	 * Sets the tag.
	 *
	 * @param tag the tag
	 * @return the t
	 */
	public T setTag(String tag) {
		mTag = tag;
		return self();
	}


	/**
	 * Show.
	 *
	 * @return the dialog fragment
	 */
	public DialogFragment show() {
		final Bundle args = prepareArguments();

		final BaseDialogFragment fragment = (BaseDialogFragment) Fragment.instantiate(mContext, mClass.getName(), args);
	
		args.putBoolean(ARG_CANCELABLE_ON_TOUCH_OUTSIDE, mCancelableOnTouchOutside);
		
		if (mTargetFragment != null) {
			fragment.setTargetFragment(mTargetFragment, mRequestCode);
		} else {
			args.putInt(ARG_REQUEST_CODE, mRequestCode);
		}
		fragment.setCancelable(mCancelable);
		fragment.show(mFragmentManager, mTag);
		
		return fragment;
	}
}
