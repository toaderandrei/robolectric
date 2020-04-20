package org.robolectric.shadows;

import android.media.audiofx.DynamicsProcessing;
import android.media.audiofx.DynamicsProcessing.Config;
import android.media.audiofx.DynamicsProcessing.Eq;
import android.media.audiofx.DynamicsProcessing.EqBand;
import androidx.annotation.Nullable;
import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;

/**
 * Implements parts of the functionality of {@link DynamicsProcessing}, by modifying a {@link
 * DynamicsProcessing.Config} instance.
 */
@Implements(value = DynamicsProcessing.class, minSdk = 28)
public class ShadowDynamicsProcessing extends ShadowAudioEffect {
  private Config config;

  @Implementation
  protected void __constructor__(
      int priority, int audioSession, @Nullable DynamicsProcessing.Config cfg) {
    if (cfg == null) {
      cfg = createDefaultConfig();
    }
    this.config = cfg;
  }

  public Config getConfig() {
    return config;
  }

  @Implementation
  public Eq getPreEqByChannelIndex(int channelIndex) {
    return config.getPreEqByChannelIndex(channelIndex);
  }

  @Implementation
  public EqBand getPreEqBandByChannelIndex(int channelIndex, int band) {
    return config.getPreEqBandByChannelIndex(channelIndex, band);
  }

  @Implementation
  public void setPreEqBandAllChannelsTo(int band, EqBand preEqBand) {
    config.setPreEqBandAllChannelsTo(band, preEqBand);
  }

  private static Config createDefaultConfig() {
    return new Config.Builder(
            /* variant= */ DynamicsProcessing.VARIANT_FAVOR_FREQUENCY_RESOLUTION,
            /* channelCount= */ 2,
            /* preEqInUse= */ true,
            /* preEqBandCount= */ 6,
            /* mbcInUse= */ true,
            /* mbcBandCount= */ 6,
            /* postEqInUse= */ true,
            /* postEqBandCount= */ 6,
            /* limiterInUse= */ true)
        .build();
  }
}
