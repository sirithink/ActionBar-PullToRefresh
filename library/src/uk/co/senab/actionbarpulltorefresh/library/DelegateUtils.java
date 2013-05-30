/*
 * Copyright 2013 Chris Banes
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

package uk.co.senab.actionbarpulltorefresh.library;


import android.content.Context;
import android.util.Log;
import android.view.View;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

class DelegateUtils {

    private static final String LOG_TAG = "DelegateUtils";

    private static final Class<?>[] DELEGATE_CONSTRUCTOR_SIGNATURE = new Class[] {};

    private static final HashMap<Class, Class> BUILT_IN_DELEGATES;
    static {
        BUILT_IN_DELEGATES = new HashMap<Class, Class>();
        BUILT_IN_DELEGATES.put(AbsListViewDelegate.SUPPORTED_VIEW_CLASS, AbsListViewDelegate.class);
        BUILT_IN_DELEGATES.put(ScrollViewDelegate.SUPPORTED_VIEW_CLASS, ScrollViewDelegate.class);
    }

    static PullToRefreshAttacher.Delegate getBuiltInDelegateForView(final View view) {
        final Set<Map.Entry<Class, Class>> entries = BUILT_IN_DELEGATES.entrySet();
        for (final Map.Entry<Class, Class> entry : entries) {
            if (entry.getKey().isInstance(view)) {
                return DelegateUtils.newInstance(view.getContext(), entry.getValue());
            }
        }
        return null;
    }

    static <T> T newInstance(Context context, String className, Object... arguments) {
        try {
            Class<?> clazz = context.getClassLoader().loadClass(className);
            return newInstance(context, clazz, arguments);
        } catch (Exception e) {
            Log.w(LOG_TAG, "Cannot instantiate class: " + className, e);
        }
        return null;
    }

    private static <T> T newInstance(Context context, Class clazz, Object... arguments) {
        try {
            Constructor<?> constructor = clazz.getConstructor(DELEGATE_CONSTRUCTOR_SIGNATURE);
            return (T) constructor.newInstance(arguments);
        } catch (Exception e) {
            Log.w(LOG_TAG, "Cannot instantiate class: " + clazz.getName(), e);
        }
        return null;
    }

}