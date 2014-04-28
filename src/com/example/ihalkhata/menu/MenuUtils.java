package com.example.ihalkhata.menu;

import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;

import android.content.Context;
import android.content.res.XmlResourceParser;

public class MenuUtils {

	private static MenuUtils _instance = new MenuUtils();

	public static MenuUtils getInstance() {
		return _instance;
	}

	public ArrayList<MenuItem> parseXml(Context c, int menu) {

		ArrayList<MenuItem> menuItems = new ArrayList<MenuItem>();

		try {
			XmlResourceParser xpp = c.getResources().getXml(menu);

			xpp.next();
			int eventType = xpp.getEventType();

			while (eventType != XmlPullParser.END_DOCUMENT) {

				if (eventType == XmlPullParser.START_TAG) {

					String elemName = xpp.getName();

					if (elemName.equals("item")) {

						String textId = xpp.getAttributeValue(
								"http://schemas.android.com/apk/res/android",
								"title");
						String iconId = xpp.getAttributeValue(
								"http://schemas.android.com/apk/res/android",
								"icon");
						String resId = xpp.getAttributeValue(
								"http://schemas.android.com/apk/res/android",
								"id");

						MenuItem item = new MenuItem();
						item.id = Integer.valueOf(resId.replace("@", ""));
						item.text = resourceIdToString(c, textId);
						item.icon = Integer.valueOf(iconId.replace("@", ""));

						menuItems.add(item);

					}

				}

				eventType = xpp.next();
			}
			return menuItems;
		} catch (Exception e) {
			e.printStackTrace();
			return menuItems;
		}

	}

	private String resourceIdToString(Context c, String text) {

		if (!text.contains("@")) {
			return text;
		} else {

			String id = text.replace("@", "");

			return c.getResources().getString(Integer.valueOf(id));

		}

	}
}
