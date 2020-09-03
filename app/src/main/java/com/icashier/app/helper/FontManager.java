package com.icashier.app.helper;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class FontManager {

	public enum FontType {
		FONT_MONTSERRAT_BOLD("fonts/Montserrat-Bold"),
		FONT_MONTSERRAT_LIGHT("fonts/Montserrat-Light"),
		FONT_MONTSERRAT_MEDIUM("fonts/Montserrat-Medium"),
		FONT_MONTSERRAT_REGULAR("fonts/Montserrat-Regular");


		private String type;

		FontType(String type) {

			this.type = type;

		}

		public static String fromType(FontType fontType) {
			if (fontType != null) {
				for (FontType typeEnum : FontType.values()) {
					if (fontType == typeEnum) {
						return typeEnum.type;
					}
				}
			}
			return null;
		}

	}

	/**
	 * Apply font face to each textview, button, edittext element in the com.timetokill.app.activity view childeren
	 * Note: Should be called after setcontentview method
	 * */
	public static void applyFontRegular(final Context context, final View root) {
		final FontType fontType = FontType.FONT_MONTSERRAT_REGULAR;
		try {
			if (root instanceof ViewGroup) {
				ViewGroup viewGroup = (ViewGroup) root;
				for (int i = 0; i < viewGroup.getChildCount(); i++)
					applyFontRegular(context, viewGroup.getChildAt(i));
			} else if (root instanceof TextView)
				((TextView) root).setTypeface(Typeface.createFromAsset(context.getAssets(), FontType.fromType(fontType)));
			else if (root instanceof EditText)
				((EditText) root).setTypeface(Typeface.createFromAsset(context.getAssets(), FontType.fromType(fontType)));
			else if (root instanceof Button)
				((Button) root).setTypeface(Typeface.createFromAsset(context.getAssets(), FontType.fromType(fontType)));

		} catch (Exception e) {
			Log.e("Font Error", String.format("Error occured when trying to apply %s font for %s view", FontType.fromType(fontType), root));
			e.printStackTrace();
		}
	}

	public static void applyFontLight(final Context context, final View root) {
		final FontType fontType = FontType.FONT_MONTSERRAT_LIGHT;
		try {
			if (root instanceof ViewGroup) {
				ViewGroup viewGroup = (ViewGroup) root;
				for (int i = 0; i < viewGroup.getChildCount(); i++)
					applyFontRegular(context, viewGroup.getChildAt(i));
			} else if (root instanceof TextView)
				((TextView) root).setTypeface(Typeface.createFromAsset(context.getAssets(), FontType.fromType(fontType)));
			else if (root instanceof EditText)
				((EditText) root).setTypeface(Typeface.createFromAsset(context.getAssets(), FontType.fromType(fontType)));
			else if (root instanceof Button)
				((Button) root).setTypeface(Typeface.createFromAsset(context.getAssets(), FontType.fromType(fontType)));

		} catch (Exception e) {
			Log.e("Font Error", String.format("Error occured when trying to apply %s font for %s view", FontType.fromType(fontType), root));
			e.printStackTrace();
		}
	}

	public static void applyFontMedium(final Context context, final View root) {
		final FontType fontType = FontType.FONT_MONTSERRAT_MEDIUM;
		try {
			if (root instanceof ViewGroup) {
				ViewGroup viewGroup = (ViewGroup) root;
				for (int i = 0; i < viewGroup.getChildCount(); i++)
					applyFontRegular(context, viewGroup.getChildAt(i));
			} else if (root instanceof TextView)
				((TextView) root).setTypeface(Typeface.createFromAsset(context.getAssets(), FontType.fromType(fontType)));
			else if (root instanceof EditText)
				((EditText) root).setTypeface(Typeface.createFromAsset(context.getAssets(), FontType.fromType(fontType)));
			else if (root instanceof Button)
				((Button) root).setTypeface(Typeface.createFromAsset(context.getAssets(), FontType.fromType(fontType)));

		} catch (Exception e) {
			Log.e("Font Error", String.format("Error occured when trying to apply %s font for %s view", FontType.fromType(fontType), root));
			e.printStackTrace();
		}
	}


	public static void applyFontBold(final Context context, final View root) {
		final FontType fontType = FontType.FONT_MONTSERRAT_BOLD;
		try {
			if (root instanceof ViewGroup) {
				ViewGroup viewGroup = (ViewGroup) root;
				for (int i = 0; i < viewGroup.getChildCount(); i++)
					applyFontBold(context, viewGroup.getChildAt(i));
			} else if (root instanceof TextView)
				((TextView) root).setTypeface(Typeface.createFromAsset(context.getAssets(), FontType.fromType(fontType)));
			else if (root instanceof EditText)
				((EditText) root).setTypeface(Typeface.createFromAsset(context.getAssets(), FontType.fromType(fontType)));
			else if (root instanceof Button)
				((Button) root).setTypeface(Typeface.createFromAsset(context.getAssets(), FontType.fromType(fontType)));

		} catch (Exception e) {
			Log.e("Font Error", String.format("Error occured when trying to apply %s font for %s view", FontType.fromType(fontType), root));
			e.printStackTrace();
		}
	}

}
