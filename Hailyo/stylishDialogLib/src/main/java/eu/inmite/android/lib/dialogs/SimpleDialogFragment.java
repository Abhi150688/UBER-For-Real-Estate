/*
 * Copyright 2013 Inmite s.r.o. (www.inmite.eu).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package eu.inmite.android.lib.dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Html;
import android.text.SpannedString;
import android.text.TextUtils;
import android.view.View;

// TODO: Auto-generated Javadoc
/**
 * Dialog for displaying simple message, message with title or message with title and two buttons. Implement {@link
 * ISimpleDialogListener} in your Fragment or Activity to rect on positive and negative button clicks. This class can
 * be extended and more parameters can be added in overridden build() method.
 *
 * @author David Vávra (david@inmite.eu)
 */
public class SimpleDialogFragment extends BaseDialogFragment {

	/** The arg message. */
	protected static String ARG_MESSAGE = "message";
	
	/** The arg title. */
	protected static String ARG_TITLE = "title";
	
	/** The arg icon. */
	protected static String ARG_ICON = "icon";
	
	/** The arg positive button. */
	protected static String ARG_POSITIVE_BUTTON = "positive_button";
	
	/** The arg negative button. */
	protected static String ARG_NEGATIVE_BUTTON = "negative_button";

	/** The m request code. */
	protected int mRequestCode;

	/**
	 * Creates the builder.
	 *
	 * @param context the context
	 * @param fragmentManager the fragment manager
	 * @return the simple dialog builder
	 */
	public static SimpleDialogBuilder createBuilder(Context context, FragmentManager fragmentManager) {
		return new SimpleDialogBuilder(context, fragmentManager, SimpleDialogFragment.class);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.DialogFragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		final Fragment targetFragment = getTargetFragment();
		if (targetFragment != null) {
			mRequestCode = getTargetRequestCode();
		} else {
			Bundle args = getArguments();
			if (args != null) {
				mRequestCode = args.getInt(BaseDialogBuilder.ARG_REQUEST_CODE, 0);
			}
		}
	}

	/**
	 * Children can extend this to add more things to base builder.
	 *
	 * @param builder the builder
	 * @return the base dialog fragment. builder
	 */
	@Override
	protected BaseDialogFragment.Builder build(BaseDialogFragment.Builder builder) {
		final String title = getTitle();
		if (!TextUtils.isEmpty(title)) {
			if(getIcon()==0)
				builder.setTitle(title);
			else
				builder.setTitle(title,getIcon());
		}

		final CharSequence message = getMessage();
		if (!TextUtils.isEmpty(message)) {
			builder.setMessage(message);
		}

		final String positiveButtonText = getPositiveButtonText();
		if (!TextUtils.isEmpty(positiveButtonText)) {
			builder.setPositiveButton(positiveButtonText, new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					ISimpleDialogListener listener = getDialogListener();
					if (listener != null) {
						listener.onPositiveButtonClicked(mRequestCode);
					}
					dismiss();
				}
			});
		}

		final String negativeButtonText = getNegativeButtonText();
		if (!TextUtils.isEmpty(negativeButtonText)) {
			builder.setNegativeButton(negativeButtonText, new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					ISimpleDialogListener listener = getDialogListener();
					if (listener != null) {
						listener.onNegativeButtonClicked(mRequestCode);
					}
					dismiss();
				}
			});
		}
		return builder;
	}

	/**
	 * Gets the message.
	 *
	 * @return the message
	 */
	protected CharSequence getMessage() {
		return getArguments().getCharSequence(ARG_MESSAGE);
	}

	/**
	 * Gets the title.
	 *
	 * @return the title
	 */
	protected String getTitle() {
		return getArguments().getString(ARG_TITLE);
	}
	
	/**
	 * Gets the icon.
	 *
	 * @return the icon
	 */
	protected int getIcon() {
		return getArguments().getInt(ARG_ICON);
	}

	/**
	 * Gets the positive button text.
	 *
	 * @return the positive button text
	 */
	protected String getPositiveButtonText() {
		return getArguments().getString(ARG_POSITIVE_BUTTON);
	}

	/**
	 * Gets the negative button text.
	 *
	 * @return the negative button text
	 */
	protected String getNegativeButtonText() {
		return getArguments().getString(ARG_NEGATIVE_BUTTON);
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
	 * Gets the dialog listener.
	 *
	 * @return the dialog listener
	 */
	protected ISimpleDialogListener getDialogListener() {
		final Fragment targetFragment = getTargetFragment();
		if (targetFragment != null) {
			if (targetFragment instanceof ISimpleDialogListener) {
				return (ISimpleDialogListener) targetFragment;
			}
		} else if (getParentFragment() != null
				&& getParentFragment() instanceof ISimpleDialogListener) {
			return (ISimpleDialogListener) getParentFragment();
		} else {
			if (getActivity() instanceof ISimpleDialogListener) {
				return (ISimpleDialogListener) getActivity();
			}
		}
		return null;
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
		} else if (getParentFragment() != null
				&& getParentFragment() instanceof ISimpleDialogCancelListener) {
			return (ISimpleDialogCancelListener) getParentFragment();
		} else {
			if (getActivity() instanceof ISimpleDialogCancelListener) {
				return (ISimpleDialogCancelListener) getActivity();
			}
		}
		return null;
	}

	/**
	 * The Class SimpleDialogBuilder.
	 */
	public static class SimpleDialogBuilder extends BaseDialogBuilder<SimpleDialogBuilder> {

		/** The m title. */
		private String mTitle;
		
		/** The icon. */
		private int icon;
		
		/** The m message. */
		private CharSequence mMessage;
		
		/** The m positive button text. */
		private String mPositiveButtonText;
		
		/** The m negative button text. */
		private String mNegativeButtonText;

		/** The m show default button. */
		private boolean mShowDefaultButton = true;

		/**
		 * Instantiates a new simple dialog builder.
		 *
		 * @param context the context
		 * @param fragmentManager the fragment manager
		 * @param clazz the clazz
		 */
		protected SimpleDialogBuilder(Context context, FragmentManager fragmentManager, Class<? extends SimpleDialogFragment> clazz) {
			super(context, fragmentManager, clazz);
		}

		/* (non-Javadoc)
		 * @see eu.inmite.android.lib.dialogs.BaseDialogBuilder#self()
		 */
		@Override
		protected SimpleDialogBuilder self() {
			return this;
		}

		/**
		 * Sets the title.
		 *
		 * @param titleResourceId the title resource id
		 * @return the simple dialog builder
		 */
		public SimpleDialogBuilder setTitle(int titleResourceId) {
			mTitle = mContext.getString(titleResourceId);
			return this;
		}
		
		/**
		 * Sets the title.
		 *
		 * @param titleResourceId the title resource id
		 * @param icon the icon
		 * @return the simple dialog builder
		 */
		public SimpleDialogBuilder setTitle(int titleResourceId,int icon) {
			mTitle = mContext.getString(titleResourceId);
			this.icon=icon;
			return this;
		}


		/**
		 * Sets the title.
		 *
		 * @param title the title
		 * @return the simple dialog builder
		 */
		public SimpleDialogBuilder setTitle(String title) {
			mTitle = title;
			return this;
		}

		/**
		 * Sets the message.
		 *
		 * @param messageResourceId the message resource id
		 * @return the simple dialog builder
		 */
		public SimpleDialogBuilder setMessage(int messageResourceId) {
			mMessage = mContext.getText(messageResourceId);
			return this;
		}

		/**
		 * Allow to set resource string with HTML formatting and bind %s,%i.
		 * This is workaround for https://code.google.com/p/android/issues/detail?id=2923
		 *
		 * @param resourceId the resource id
		 * @param formatArgs the format args
		 * @return the simple dialog builder
		 */
		public SimpleDialogBuilder setMessage(int resourceId, Object... formatArgs){
			mMessage = Html.fromHtml(String.format(Html.toHtml(new SpannedString(mContext.getText(resourceId))), formatArgs));
			return this;
		}

		/**
		 * Sets the message.
		 *
		 * @param message the message
		 * @return the simple dialog builder
		 */
		public SimpleDialogBuilder setMessage(CharSequence message) {
			mMessage = message;
			return this;
		}

		/**
		 * Sets the html message.
		 *
		 * @param htmlMessageResourceId the html message resource id
		 * @return the simple dialog builder
		 */
		public SimpleDialogBuilder setHtmlMessage(int htmlMessageResourceId) {
			mMessage = mContext.getText(htmlMessageResourceId);
			return this;
		}

		/**
		 * Sets the html message.
		 *
		 * @param htmlMessage the html message
		 * @return the simple dialog builder
		 */
		public SimpleDialogBuilder setHtmlMessage(String htmlMessage) {
			mMessage = Html.fromHtml(htmlMessage);
			return this;
		}

		/**
		 * Sets the positive button text.
		 *
		 * @param textResourceId the text resource id
		 * @return the simple dialog builder
		 */
		public SimpleDialogBuilder setPositiveButtonText(int textResourceId) {
			mPositiveButtonText = mContext.getString(textResourceId);
			return this;
		}


		/**
		 * Sets the positive button text.
		 *
		 * @param text the text
		 * @return the simple dialog builder
		 */
		public SimpleDialogBuilder setPositiveButtonText(String text) {
			mPositiveButtonText = text;
			return this;
		}

		/**
		 * Sets the negative button text.
		 *
		 * @param textResourceId the text resource id
		 * @return the simple dialog builder
		 */
		public SimpleDialogBuilder setNegativeButtonText(int textResourceId) {
			mNegativeButtonText = mContext.getString(textResourceId);
			return this;
		}

		/**
		 * Sets the negative button text.
		 *
		 * @param text the text
		 * @return the simple dialog builder
		 */
		public SimpleDialogBuilder setNegativeButtonText(String text) {
			mNegativeButtonText = text;
			return this;
		}

		/**
		 * When there is neither positive nor negative button, default "close" button is created if it was enabled.<br/>
		 * Default is true.
		 *
		 * @param hide the hide
		 * @return the simple dialog builder
		 */
		public SimpleDialogBuilder hideDefaultButton(boolean hide) {
			mShowDefaultButton = !hide;
			return this;
		}

		/* (non-Javadoc)
		 * @see eu.inmite.android.lib.dialogs.BaseDialogBuilder#prepareArguments()
		 */
		@Override
		protected Bundle prepareArguments() {
			if (mShowDefaultButton && mPositiveButtonText == null && mNegativeButtonText == null) {
				mPositiveButtonText = mContext.getString(R.string.dialog_close);
			}

			Bundle args = new Bundle();
			args.putCharSequence(SimpleDialogFragment.ARG_MESSAGE, mMessage);
			args.putString(SimpleDialogFragment.ARG_TITLE, mTitle);
			args.putInt(SimpleDialogFragment.ARG_ICON, icon);
			args.putString(SimpleDialogFragment.ARG_POSITIVE_BUTTON, mPositiveButtonText);
			args.putString(SimpleDialogFragment.ARG_NEGATIVE_BUTTON, mNegativeButtonText);

			return args;
		}
	}
}
