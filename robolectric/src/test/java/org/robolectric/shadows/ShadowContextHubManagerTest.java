package org.robolectric.shadows;

import static com.google.common.truth.Truth.assertThat;

import android.content.Context;
import android.hardware.location.ContextHubClient;
import android.hardware.location.ContextHubInfo;
import android.hardware.location.ContextHubManager;
import android.os.Build;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;

/** Tests for {@link ShadowContextHubManager}. */
@RunWith(AndroidJUnit4.class)
@Config(minSdk = Build.VERSION_CODES.P)
public class ShadowContextHubManagerTest {
  // Do not reference a non-public field in a test, because those get loaded outside the Robolectric
  // sandbox
  // DO NOT DO: private ContextHubManager contextHubManager;

  private Context context;

  @Before
  public void setUp() {
    context = ApplicationProvider.getApplicationContext();
  }

  @Test
  public void getContextHubs_returnsValidList() {
    ContextHubManager contextHubManager =
        (ContextHubManager) context.getSystemService(Context.CONTEXTHUB_SERVICE);
    List<ContextHubInfo> contextHubInfoList = contextHubManager.getContextHubs();
    assertThat(contextHubInfoList).isNotNull();
    assertThat(contextHubInfoList).isNotEmpty();
  }

  @Test
  public void createClient_returnsValidClient() {
    ContextHubManager contextHubManager =
        (ContextHubManager) context.getSystemService(Context.CONTEXTHUB_SERVICE);
    ContextHubClient contextHubClient = contextHubManager.createClient(null, null);
    assertThat(contextHubClient).isNotNull();
  }
}
