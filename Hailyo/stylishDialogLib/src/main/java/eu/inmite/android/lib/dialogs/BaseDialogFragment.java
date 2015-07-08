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

import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

// TODO: Auto-generated Javadoc
/**
 * Base dialog fragment for all your dialogs, stylable and same design on Android 2.2+.
 *
 * @author David Vávra (david@inmite.eu)
 */
public abstract class BaseDialogFragment extends DialogFragment {

	/* (non-Javadoc)
	 * @see android.support.v4.app.DialogFragment#onCreateDialog(android.os.Bundle)
	 */
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Dialog dialog = new Dialog(getActivity(), R.style.SDL_Dialog);
		// custom dialog background
		final TypedArray a = getActivity().getTheme().obtainStyledAttributes(null, R.styleable.DialogStyle, R.attr.sdlDialogStyle, 0);
		Drawable dialogBackground = a.getDrawable(R.styleable.DialogStyle_dialogBackground);
		a.recycle();
		dialog.getWindow().setBackgroundDrawable(dialogBackground);
		Bundle args = getArguments();
		if (args != null) {
			dialog.setCanceledOnTouchOutside(args.getBoolean(BaseDialogBuilder.ARG_CANCELABLE_ON_TOUCH_OUTSIDE));
		}
		return dialog;
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Builder builder = new Builder(this, getActivity(), inflater, container);
		return build(builder).create();
	}

	/**
	 * Builds the.
	 *
	 * @param initialBuilder the initial builder
	 * @return the builder
	 */
	protected abstract Builder build(Builder initialBuilder);

	/* (non-Javadoc)
	 * @see android.support.v4.app.DialogFragment#onDestroyView()
	 */
	@Override
	public void onDestroyView() {
		// bug in the compatibility library
		if (getDialog() != null && getRetainInstance()) {
			getDialog().setDismissMessage(null);
		}
		super.onDestroyView();
	}

	/**
	 * Gets the positive button.
	 *
	 * @return the positive button if specified and the view is created, null otherwise
	 */
	protected Button getPositiveButton() {
		if (getView() != null) {
			return (Button) getView().findViewById(R.id.sdl__positive_button);
		} else {
			return null;
		}
	}

	/**
	 * Gets the negative button.
	 *
	 * @return the negative button if specified and the view is created, null otherwise
	 */
	protected Button getNegativeButton() {
		if (getView() != null) {
			return (Button) getView().findViewById(R.id.sdl__negative_button);
		} else {
			return null;
		}
	}

	/**
	 * Gets the neutral button.
	 *
	 * @return the neutral button if specified and the view is created, null otherwise
	 */
	protected Button getNeutralButton() {
		if (getView() != null) {
			return (Button) getView().findViewById(R.id.sdl__neutral_button);
		} else {
			return null;
		}
	}

	/**
	 * Custom dialog builder.
	 */
	protected static class Builder {

		/** The m dialog fragment. */
		private final DialogFragment mDialogFragment;
		
		/** The m context. */
		private final Context mContext;
		
		/** The m container. */
		private final ViewGroup mContainer;
		
		/** The m inflater. */
		private final LayoutInflater mInflater;

		/** The m title. */
		private CharSequence mTitle = null;
		
		/** The icon. */
		private int icon = 0;
		
		/** The m positive button text. */
		private CharSequence mPositiveButtonText;
		
		/** The m positive button listener. */
		private View.OnClickListener mPositiveButtonListener;
		
		/** The m negative button text. */
		private CharSequence mNegativeButtonText;
		
		/** The m negative button listener. */
		private View.OnClickListener mNegativeButtonListener;
		
		/** The m neutral button text. */
		private CharSequence mNeutralButtonText;
		
		/** The m neutral button listener. */
		private View.OnClickListener mNeutralButtonListener;
		
		/** The m message. */
		private CharSequence mMessage;
		
		/** The m view. */
		private View mView;
		
		/** The m view spacing specified. */
		private boolean mViewSpacingSpecified;
		
		/** The m view spacing left. */
		private int mViewSpacingLeft;
		
		/** The m view spacing top. */
		private int mViewSpacingTop;
		
		/** The m view spacing right. */
		private int mViewSpacingRight;
		
		/** The m view spacing bottom. */
		private int mViewSpacingBottom;
		
		/** The m list adapter. */
		private ListAdapter mListAdapter;
		
		/** The m list checked item idx. */
		private int mListCheckedItemIdx;
		
		/** The m on item click listener. */
		private AdapterView.OnItemClickListener mOnItemClickListener;
		
		/** Styling: *. */
		private int mTitleTextColor;
		
		/** The m title separator color. */
		private int mTitleSeparatorColor;
		
		/** The m message text color. */
		private int mMessageTextColor;
		
		/** The m button text color. */
		private ColorStateList mButtonTextColor;
		
		/** The m button separator color. */
		private int mButtonSeparatorColor;
		
		/** The m button background color normal. */
		private int mButtonBackgroundColorNormal;
		
		/** The m button background color pressed. */
		private int mButtonBackgroundColorPressed;
		
		/** The m button background color focused. */
		private int mButtonBackgroundColorFocused;

		/**
		 * Instantiates a new builder.
		 *
		 * @param dialogFragment the dialog fragment
		 * @param context the context
		 * @param inflater the inflater
		 * @param container the container
		 */
		public Builder(DialogFragment dialogFragment, Context context, LayoutInflater inflater, ViewGroup container) {
			this.mDialogFragment = dialogFragment;
			this.mContext = context;
			this.mContainer = container;
			this.mInflater = inflater;
		}

		/**
		 * Gets the layout inflater.
		 *
		 * @return the layout inflater
		 */
		public LayoutInflater getLayoutInflater() {
			return mInflater;
		}

		/**
		 * Sets the title.
		 *
		 * @param titleId the title id
		 * @return the builder
		 */
		public Builder setTitle(int titleId) {
			this.mTitle = mContext.getText(titleId);
			return this;
		}
		
		/**
		 * Sets the title.
		 *
		 * @param titleId the title id
		 * @param icon the icon
		 * @return the builder
		 */
		public Builder setTitle(int titleId,int icon) {
			this.mTitle = mContext.getText(titleId);
			this.icon=icon;
			return this;
		}

		/**
		 * Sets the title.
		 *
		 * @param title the title
		 * @return the builder
		 */
		public Builder setTitle(CharSequence title) {
			this.mTitle = title;
			return this;
		}
		
		/**
		 * Sets the title.
		 *
		 * @param title the title
		 * @param icon the icon
		 * @return the builder
		 */
		public Builder setTitle(CharSequence title,int icon) {
			this.mTitle = title;
			this.icon=icon;
			return this;
		}

		/**
		 * Sets the positive button.
		 *
		 * @param textId the text id
		 * @param listener the listener
		 * @return the builder
		 */
		public Builder setPositiveButton(int textId, final View.OnClickListener listener) {
			mPositiveButtonText = mContext.getText(textId);
			mPositiveButtonListener = listener;
			return this;
		}

		/**
		 * Sets the positive button.
		 *
		 * @param text the text
		 * @param listener the listener
		 * @return the builder
		 */
		public Builder setPositiveButton(CharSequence text, final View.OnClickListener listener) {
			mPositiveButtonText = text;
			mPositiveButtonListener = listener;
			return this;
		}

		/**
		 * Sets the negative button.
		 *
		 * @param textId the text id
		 * @param listener the listener
		 * @return the builder
		 */
		public Builder setNegativeButton(int textId, final View.OnClickListener listener) {
			mNegativeButtonText = mContext.getText(textId);
			mNegativeButtonListener = listener;
			return this;
		}

		/**
		 * Sets the negative button.
		 *
		 * @param text the text
		 * @param listener the listener
		 * @return the builder
		 */
		public Builder setNegativeButton(CharSequence text, final View.OnClickListener listener) {
			mNegativeButtonText = text;
			mNegativeButtonListener = listener;
			return this;
		}

		/**
		 * Sets the neutral button.
		 *
		 * @param textId the text id
		 * @param listener the listener
		 * @return the builder
		 */
		public Builder setNeutralButton(int textId, final View.OnClickListener listener) {
			mNeutralButtonText = mContext.getText(textId);
			mNeutralButtonListener = listener;
			return this;
		}

		/**
		 * Sets the neutral button.
		 *
		 * @param text the text
		 * @param listener the listener
		 * @return the builder
		 */
		public Builder setNeutralButton(CharSequence text, final View.OnClickListener listener) {
			mNeutralButtonText = text;
			mNeutralButtonListener = listener;
			return this;
		}

		/**
		 * Sets the message.
		 *
		 * @param messageId the message id
		 * @return the builder
		 */
		public Builder setMessage(int messageId) {
			mMessage = mContext.getText(messageId);
			return this;
		}

		/**
		 * Sets the message.
		 *
		 * @param message the message
		 * @return the builder
		 */
		public Builder setMessage(CharSequence message) {
			mMessage = message;
			return this;
		}

		/**
		 * Set list.
		 *
		 * @param listAdapter the list adapter
		 * @param checkedItemIdx Item check by default, -1 if no item should be checked
		 * @param listener the listener
		 * @return the builder
		 */
		public Builder setItems(ListAdapter listAdapter, int checkedItemIdx, final AdapterView.OnItemClickListener listener) {
			mListAdapter = listAdapter;
			mOnItemClickListener = listener;
			mListCheckedItemIdx = checkedItemIdx;
			return this;
		}

		/**
		 * Sets the view.
		 *
		 * @param view the view
		 * @return the builder
		 */
		public Builder setView(View view) {
			mView = view;
			mViewSpacingSpecified = false;
			return this;
		}

		/**
		 * Sets the view.
		 *
		 * @param view the view
		 * @param viewSpacingLeft the view spacing left
		 * @param viewSpacingTop the view spacing top
		 * @param viewSpacingRight the view spacing right
		 * @param viewSpacingBottom the view spacing bottom
		 * @return the builder
		 */
		public Builder setView(View view, int viewSpacingLeft, int viewSpacingTop,
		                       int viewSpacingRight, int viewSpacingBottom) {
			mView = view;
			mViewSpacingSpecified = true;
			mViewSpacingLeft = viewSpacingLeft;
			mViewSpacingTop = viewSpacingTop;
			mViewSpacingRight = viewSpacingRight;
			mViewSpacingBottom = viewSpacingBottom;
			return this;
		}

		/**
		 * Creates the.
		 *
		 * @return the view
		 */
		public View create() {
			final Resources res = mContext.getResources();
			final int defaultTitleTextColor = res.getColor(R.color.sdl_title_text_dark);
			final int defaultTitleSeparatorColor = res.getColor(R.color.sdl_title_separator_dark);
			final int defaultMessageTextColor = res.getColor(R.color.sdl_message_text_dark);
			final ColorStateList defaultButtonTextColor = res.getColorStateList(R.color.sdl_button_text_dark);
			final int defaultButtonSeparatorColor = res.getColor(R.color.sdl_button_separator_dark);
			final int defaultButtonBackgroundColorNormal = res.getColor(R.color.sdl_button_normal_dark);
			final int defaultButtonBackgroundColorPressed = res.getColor(R.color.sdl_button_pressed_dark);
			final int defaultButtonBackgroundColorFocused = res.getColor(R.color.sdl_button_focused_dark);

			final TypedArray a = mContext.getTheme().obtainStyledAttributes(null, R.styleable.DialogStyle, R.attr.sdlDialogStyle, 0);
			mTitleTextColor = a.getColor(R.styleable.DialogStyle_titleTextColor, defaultTitleTextColor);
			mTitleSeparatorColor = a.getColor(R.styleable.DialogStyle_titleSeparatorColor, defaultTitleSeparatorColor);
			mMessageTextColor = a.getColor(R.styleable.DialogStyle_messageTextColor, defaultMessageTextColor);
			mButtonTextColor = a.getColorStateList(R.styleable.DialogStyle_buttonTextColor);
			if (mButtonTextColor == null) {
				mButtonTextColor = defaultButtonTextColor;
			}
			mButtonSeparatorColor = a.getColor(R.styleable.DialogStyle_buttonSeparatorColor, defaultButtonSeparatorColor);
			mButtonBackgroundColorNormal = a.getColor(R.styleable.DialogStyle_buttonBackgroundColorNormal, defaultButtonBackgroundColorNormal);
			mButtonBackgroundColorPressed = a.getColor(R.styleable.DialogStyle_buttonBackgroundColorPressed, defaultButtonBackgroundColorPressed);
			mButtonBackgroundColorFocused = a.getColor(R.styleable.DialogStyle_buttonBackgroundColorFocused, defaultButtonBackgroundColorFocused);
			a.recycle();

			View v = getDialogLayoutAndInitTitle();

			LinearLayout content = (LinearLayout) v.findViewById(R.id.sdl__content);

			if (mMessage != null) {
				View viewMessage = mInflater.inflate(R.layout.dialog_part_message, content, false);
				TextView tvMessage = (TextView) viewMessage.findViewById(R.id.sdl__message);
				tvMessage.setTextColor(mMessageTextColor);
				tvMessage.setText(mMessage);
				content.addView(viewMessage);
			}

			if (mView != null) {
				FrameLayout customPanel = (FrameLayout) mInflater.inflate(R.layout.dialog_part_custom, content, false);
				FrameLayout custom = (FrameLayout) customPanel.findViewById(R.id.sdl__custom);
				custom.addView(mView, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
				if (mViewSpacingSpecified) {
					custom.setPadding(mViewSpacingLeft, mViewSpacingTop, mViewSpacingRight, mViewSpacingBottom);
				}
				content.addView(customPanel);
			}

			if (mListAdapter != null) {
				ListView list = (ListView) mInflater.inflate(R.layout.dialog_part_list, content, false);
				list.setAdapter(mListAdapter);
				list.setOnItemClickListener(mOnItemClickListener);
				if (mListCheckedItemIdx != -1) {
					list.setSelection(mListCheckedItemIdx);
				}
				content.addView(list);
			}

			addButtons(content);

			return v;
		}

		/**
		 * Gets the dialog layout and init title.
		 *
		 * @return the dialog layout and init title
		 */
		private View getDialogLayoutAndInitTitle() {
			View v = mInflater.inflate(R.layout.dialog_part_title, mContainer, false);
			TextView tvTitle = (TextView) v.findViewById(R.id.sdl__title);
			View viewTitleDivider = v.findViewById(R.id.sdl__titleDivider);
			if (mTitle != null) {
				tvTitle.setText(mTitle);
				tvTitle.setTextColor(mTitleTextColor);
				if(icon!=0)
				{
					tvTitle.setCompoundDrawablesWithIntrinsicBounds(icon, 0, 0, 0);
					tvTitle.setCompoundDrawablePadding(10);
					tvTitle.setGravity(Gravity.CENTER_VERTICAL);
				}
				viewTitleDivider.setBackgroundDrawable(new ColorDrawable(mTitleSeparatorColor));
			} else {
				tvTitle.setVisibility(View.GONE);
				viewTitleDivider.setVisibility(View.GONE);
			}
			return v;
		}

		/**
		 * Adds the buttons.
		 *
		 * @param llListDialog the ll list dialog
		 */
		private void addButtons(LinearLayout llListDialog) {
			if (mNegativeButtonText != null || mNeutralButtonText != null || mPositiveButtonText != null) {
				View viewButtonPanel = mInflater.inflate(R.layout.dialog_part_button_panel, llListDialog, false);
				LinearLayout llButtonPanel = (LinearLayout) viewButtonPanel.findViewById(R.id.dialog_button_panel);
				viewButtonPanel.findViewById(R.id.dialog_horizontal_separator).setBackgroundDrawable(new ColorDrawable(mButtonSeparatorColor));

				boolean addDivider = false;

				if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
					addDivider = addPositiveButton(llButtonPanel, addDivider);
				} else {
					addDivider = addNegativeButton(llButtonPanel, addDivider);
				}
				addDivider = addNeutralButton(llButtonPanel, addDivider);

				if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
					addNegativeButton(llButtonPanel, addDivider);
				} else {
					addPositiveButton(llButtonPanel, addDivider);
				}

				llListDialog.addView(viewButtonPanel);
			}
		}

		/**
		 * Adds the negative button.
		 *
		 * @param parent the parent
		 * @param addDivider the add divider
		 * @return true, if successful
		 */
		private boolean addNegativeButton(ViewGroup parent, boolean addDivider) {
			if (mNegativeButtonText != null) {
				if (addDivider) {
					addDivider(parent);
				}
				Button btn = (Button) mInflater.inflate(R.layout.dialog_part_button, parent, false);
				btn.setId(R.id.sdl__negative_button);
				btn.setText(mNegativeButtonText);
				btn.setTextColor(mButtonTextColor);
				btn.setBackgroundDrawable(getButtonBackground());
				btn.setOnClickListener(mNegativeButtonListener);
				parent.addView(btn);
				return true;
			}
			return addDivider;
		}

		/**
		 * Adds the positive button.
		 *
		 * @param parent the parent
		 * @param addDivider the add divider
		 * @return true, if successful
		 */
		private boolean addPositiveButton(ViewGroup parent, boolean addDivider) {
			if (mPositiveButtonText != null) {
				if (addDivider) {
					addDivider(parent);
				}
				Button btn = (Button) mInflater.inflate(R.layout.dialog_part_button, parent, false);
				btn.setId(R.id.sdl__positive_button);
				btn.setText(mPositiveButtonText);
				btn.setTextColor(mButtonTextColor);
				btn.setBackgroundDrawable(getButtonBackground());
				btn.setOnClickListener(mPositiveButtonListener);
				parent.addView(btn);
				return true;
			}
			return addDivider;
		}

		/**
		 * Adds the neutral button.
		 *
		 * @param parent the parent
		 * @param addDivider the add divider
		 * @return true, if successful
		 */
		private boolean addNeutralButton(ViewGroup parent, boolean addDivider) {
			if (mNeutralButtonText != null) {
				if (addDivider) {
					addDivider(parent);
				}
				Button btn = (Button) mInflater.inflate(R.layout.dialog_part_button, parent, false);
				btn.setId(R.id.sdl__neutral_button);
				btn.setText(mNeutralButtonText);
				btn.setTextColor(mButtonTextColor);
				btn.setBackgroundDrawable(getButtonBackground());
				btn.setOnClickListener(mNeutralButtonListener);
				parent.addView(btn);
				return true;
			}
			return addDivider;
		}

		/**
		 * Adds the divider.
		 *
		 * @param parent the parent
		 */
		private void addDivider(ViewGroup parent) {
			View view = mInflater.inflate(R.layout.dialog_part_button_separator, parent, false);
			view.findViewById(R.id.dialog_button_separator).setBackgroundDrawable(new ColorDrawable(mButtonSeparatorColor));
			parent.addView(view);
		}

		/**
		 * Gets the button background.
		 *
		 * @return the button background
		 */
		private StateListDrawable getButtonBackground() {
			int[] pressedState = {android.R.attr.state_pressed};
			int[] focusedState = {android.R.attr.state_focused};
			int[] defaultState = {android.R.attr.state_enabled};
			ColorDrawable colorDefault = new ColorDrawable(mButtonBackgroundColorNormal);
			ColorDrawable colorPressed = new ColorDrawable(mButtonBackgroundColorPressed);
			ColorDrawable colorFocused = new ColorDrawable(mButtonBackgroundColorFocused);
			StateListDrawable background = new StateListDrawable();
			background.addState(pressedState, colorPressed);
			background.addState(focusedState, colorFocused);
			background.addState(defaultState, colorDefault);
			return background;
		}
	}
}
