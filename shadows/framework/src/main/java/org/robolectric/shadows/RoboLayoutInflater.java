package org.robolectric.shadows;

import android.R.integer;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import org.robolectric.RuntimeEnvironment;
import org.robolectric.util.ReflectionHelpers;
import org.robolectric.util.ReflectionHelpers.ClassParameter;

/**
 * Robolectric implementation of {@link android.view.LayoutInflater}.
 */
public class RoboLayoutInflater extends LayoutInflater {
  private static final String[] sClassPrefixList = {
      "android.widget.",
      "android.webkit."
  };

  /**
   * Instead of instantiating directly, you should retrieve an instance
   * through {@link android.content.Context#getSystemService}
   *
   * @param context The Context in which in which to find resources and other
   *                application-specific things.
   *
   * @see android.content.Context#getSystemService
   */
  public RoboLayoutInflater(Context context) {
    super(context);
  }

  RoboLayoutInflater(LayoutInflater original, Context newContext) {
    super(original, newContext);
  }

  /** Override onCreateView to instantiate names that correspond to the
   widgets known to the Widget factory. If we don't find a match,
   call through to our super class.
   */
  @Override protected View onCreateView(String name, AttributeSet attrs) throws ClassNotFoundException {
    for (String prefix : sClassPrefixList) {
      try {
        View view = createView(name, prefix, attrs);
        if (RuntimeEnvironment.isRendering()) {
          Class shadowViewClass = Class.forName("org.robolectric.shadows.ShadowView");
          Object shadowView = ReflectionHelpers.getField(view, "__robo_data__");
          ReflectionHelpers.callInstanceMethod(shadowViewClass, shadowView, "updateViewId",
              ClassParameter.from(AttributeSet.class, attrs),
              ClassParameter.from(int.class, 0));
        }
        if (view != null) {
          return view;
        }
      } catch (ClassNotFoundException e) {
        // In this case we want to let the base class take a crack
        // at it.
      }
    }

    return super.onCreateView(name, attrs);
  }

  @Override public LayoutInflater cloneInContext(Context newContext) {
    return new RoboLayoutInflater(this, newContext);
  }
}
