package eu.inmite.android.lib.dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

// TODO: Auto-generated Javadoc
/**
 * Simple progress dialog that shows indeterminate progress bar together with message and dialog title (optional).<br/>
 * <p>
 * To show the dialog, start with {@link #createBuilder(android.content.Context, android.support.v4.app.FragmentManager)}.
 * </p>
 * <p>
 * Dialog can be cancelable - to listen to cancellation, activity or target fragment must implement {@link ISimpleDialogCancelListener}
 * </p>
 *
 * @author Tomas Vondracek
 */
public class ProgressDialogFragment extends BaseDialogFragment {

	/** The arg message. */
	protected static String ARG_MESSAGE = "message";
	
	/** The arg title. */
	protected static String ARG_TITLE = "title";

	/** The m request code. */
	protected int mRequestCode;

	/**
	 * Creates the builder.
	 *
	 * @param context the context
	 * @param fragmentManager the fragment manager
	 * @return the progress dialog builder
	 */
	public static ProgressDialogBuilder createBuilder(Context context, FragmentManager fragmentManager) {
		return new ProgressDialogBuilder(context, fragmentManager);
	}

	/* (non-Javadoc)
	 * @see eu.inmite.android.lib.dialogs.BaseDialogFragment#build(eu.inmite.android.lib.dialogs.BaseDialogFragment.Builder)
	 */
	@Override
	protected Builder build(Builder builder) {
		final int defaultMessageTextColor = getResources().getColor(R.color.sdl_message_text_dark);
		final TypedArray a = getActivity().getTheme().obtainStyledAttributes(null, R.styleable.DialogStyle, R.attr.sdlDialogStyle, 0);
		final int messageTextColor = a.getColor(R.styleable.DialogStyle_messageTextColor, defaultMessageTextColor);
		a.recycle();

		final LayoutInflater inflater = builder.getLayoutInflater();
		final View view = inflater.inflate(R.layout.dialog_part_progress, null, false);
		final TextView tvMessage = (TextView) view.findViewById(R.id.sdl__message);
		tvMessage.setText(getArguments().getString(ARG_MESSAGE));
		tvMessage.setTextColor(messageTextColor);

		builder.setView(view);

		builder.setTitle(getArguments().getString(ARG_TITLE));

		return builder;
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.DialogFragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if (getArguments() == null) {
			throw new IllegalArgumentException("use ProgressDialogBuilder to construct this dialog");
		}
		final Fragment targetFragment = getTargetFragment();
		mRequestCode = targetFragment != null ?
				getTargetRequestCode() : getArguments().getInt(BaseDialogBuilder.ARG_REQUEST_CODE, 0);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.DialogFragment#onCancel(android.content.DialogInterface)
	 */
	@Override
	public void onCancel(DialogInterface dialog) {
		super.onCancel(dialog);
		ISimpleDialogCancelListener listener = getCancelListener();
		if (listener != null) {
			listener.onCancelled(mRequestCode);
		}
	}

	/**
	 * Gets the cancel listener.
	 *
	 * @return the cancel listener
	 */
	protected ISimpleDialogCancelListener getCancelListener() {
		final Fragment targetFragment = getTargetFragment();
		if (targetFragment != null) {
			if (targetFragment instanceof ISimpleDialogCancelListener) {
				return (ISimpleDialogCancelListener) targetFragment;
			}
		} else {
			if (getActivity() instanceof ISimpleDialogCancelListener) {
				return (ISimpleDialogCancelListener) getActivity();
			}
		}
		return null;
	}

	/**
	 * The Class ProgressDialogBuilder.
	 */
	public static class ProgressDialogBuilder extends BaseDialogBuilder<ProgressDialogBuilder> {

		/** The m title. */
		private String mTitle;
		
		/** The m message. */
		private String mMessage;

		/**
		 * Instantiates a new progress dialog builder.
		 *
		 * @param context the context
		 * @param fragmentManager the fragment manager
		 */
		protected ProgressDialogBuilder(Context context, FragmentManager fragmentManager) {
			super(context, fragmentManager, ProgressDialogFragment.class);
		}

		/* (non-Javadoc)
		 * @see eu.inmite.android.lib.dialogs.BaseDialogBuilder#self()
		 */
		@Override
		protected ProgressDialogBuilder self() {
			return this;
		}

		/**
		 * Sets the title.
		 *
		 * @param titleResourceId the title resource id
		 * @return the progress dialog builder
		 */
		public ProgressDialogBuilder setTitle(int titleResourceId) {
			mTitle = mContext.getString(titleResourceId);
			return this;
		}


		/**
		 * Sets the title.
		 *
		 * @param title the title
		 * @return the progress dialog builder
		 */
		public ProgressDialogBuilder setTitle(String title) {
			mTitle = title;
			return this;
		}

		/**
		 * Sets the message.
		 *
		 * @param messageResourceId the message resource id
		 * @return the progress dialog builder
		 */
		public ProgressDialogBuilder setMessage(int messageResourceId) {
			mMessage = mContext.getString(messageResourceId);
			return this;
		}

		/**
		 * Sets the message.
		 *
		 * @param message the message
		 * @return the progress dialog builder
		 */
		public ProgressDialogBuilder setMessage(String message) {
			mMessage = message;
			return this;
		}

		/* (non-Javadoc)
		 * @see eu.inmite.android.lib.dialogs.BaseDialogBuilder#prepareArguments()
		 */
		@Override
		protected Bundle prepareArguments() {
			Bundle args = new Bundle();
			args.putString(SimpleDialogFragment.ARG_MESSAGE, mMessage);
			args.putString(SimpleDialogFragment.ARG_TITLE, mTitle);

			return args;
		}
	}
}
