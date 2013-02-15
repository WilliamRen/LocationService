/*
 * Copyright 2011 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package fr.gdelente.android.utils;

import android.content.Context;
import android.location.LocationListener;

/**
 * Factory class to create the correct instances of a variety of classes with
 * platform specific implementations.
 * 
 */
public class PlatformSpecificImplementationFactory {

	public static boolean SUPPORTS_GINGERBREAD = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.GINGERBREAD;
	public static boolean SUPPORTS_HONEYCOMB = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB;
	public static boolean SUPPORTS_FROYO = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.FROYO;
	public static boolean SUPPORTS_ECLAIR = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.ECLAIR;

	/**
	 * Create a new LastLocationFinder instance
	 * 
	 * @param context
	 *            Context
	 * @return LastLocationFinder
	 */
	public static ILastLocationFinder getLastLocationFinder(Context context,
			LocationListener locationListener) {
		return SUPPORTS_GINGERBREAD ? new GingerbreadLastLocationFinder(
				context, locationListener) : new LegacyLastLocationFinder(
				context, locationListener);
	}
}
