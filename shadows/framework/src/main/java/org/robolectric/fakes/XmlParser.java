package org.robolectric.fakes;

import com.android.ide.common.rendering.api.ILayoutPullParser;
import com.android.ide.common.rendering.api.IProjectCallback;
import org.kxml2.io.KXmlParser;

/**
 * KXml-based parser that implements {@link ILayoutPullParser}.
 *
 */
public class XmlParser extends KXmlParser implements ILayoutPullParser {
  /**
   * @deprecated {@link IProjectCallback} replaces this.
   */
  @Deprecated
  public ILayoutPullParser getParser(String layoutName) {
    return null;
  }

  public Object getViewCookie() {
    return null;
  }
}
