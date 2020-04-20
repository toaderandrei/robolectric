package org.robolectric.shadows;

import static com.google.common.truth.Truth.assertThat;

import android.media.audiofx.AudioEffect;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import java.util.List;
import java.util.UUID;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Shadows;

/** Tests for {@link ShadowAudioEffect}. */
@RunWith(AndroidJUnit4.class)
public class ShadowAudioEffectTest {
  private static final UUID EFFECT_TYPE_NULL =
      UUID.fromString("ec7178ec-e5e1-4432-a3f4-4657e6795210");

  @Test public void queryEffects() {

    AudioEffect.Descriptor descriptor = new AudioEffect.Descriptor();
    descriptor.type = AudioEffect.EFFECT_TYPE_AEC;
    ShadowAudioEffect.addEffect(descriptor);

    AudioEffect.Descriptor[] descriptors = AudioEffect.queryEffects();

    assertThat(descriptors).asList().hasSize(1);
    assertThat(descriptors[0].type).isEqualTo(AudioEffect.EFFECT_TYPE_AEC);
  }

  @Test
  public void getAudioEffects_noAudioEffects_returnsNoEffects() {
    assertThat(ShadowAudioEffect.getAudioEffects()).isEmpty();
  }

  @Test
  public void getAudioEffects_newAudioEffect_returnsAudioEffect() {
    int priority = 100;
    int audioSession = 500;
    new AudioEffect(
        AudioEffect.EFFECT_TYPE_AEC, /* uuid= */ EFFECT_TYPE_NULL, priority, audioSession);

    List<AudioEffect> actualEffects = ShadowAudioEffect.getAudioEffects();

    assertThat(actualEffects.size()).isEqualTo(1);
    ShadowAudioEffect actualEffect = Shadows.shadowOf(actualEffects.get(0));
    assertThat(actualEffect.getPriority()).isEqualTo(priority);
    assertThat(actualEffect.getAudioSession()).isEqualTo(audioSession);
  }

  @Test
  public void getPriority_returnsPriorityFromCtor() {
    int priority = 100;
    AudioEffect audioEffect =
        new AudioEffect(
            AudioEffect.EFFECT_TYPE_AEC, EFFECT_TYPE_NULL, priority, /* audioSession= */ 0);

    assertThat(Shadows.shadowOf(audioEffect).getPriority()).isEqualTo(priority);
  }

  @Test
  public void getAudioSession_returnsAudioSessionFromCtor() {
    int audioSession = 100;
    AudioEffect audioEffect =
        new AudioEffect(
            AudioEffect.EFFECT_TYPE_AEC, EFFECT_TYPE_NULL, /* priority= */ 0, audioSession);

    assertThat(Shadows.shadowOf(audioEffect).getAudioSession()).isEqualTo(audioSession);
  }

  @Test
  public void getEnabled_returnsFalseByDefault() {
    AudioEffect audioEffect = createAudioEffect();

    assertThat(audioEffect.getEnabled()).isFalse();
  }

  @Test
  public void getEnabled_setEnabledTrue_returnsTrue() {
    AudioEffect audioEffect = createAudioEffect();

    audioEffect.setEnabled(true);

    assertThat(audioEffect.getEnabled()).isTrue();
  }

  @Test
  public void getEnabled_setEnabledTrueThenFalse_returnsFalse() {
    AudioEffect audioEffect = createAudioEffect();

    audioEffect.setEnabled(true);
    audioEffect.setEnabled(false);

    assertThat(audioEffect.getEnabled()).isFalse();
  }

  @Test
  public void hasControlStatusListener_returnsFalseByDefault() {
    AudioEffect audioEffect = createAudioEffect();

    assertThat(Shadows.shadowOf(audioEffect).hasControlStatusListener()).isFalse();
  }

  @Test
  public void hasControlStatusListener_setControlStatusListenerNonNull_returnsTrue() {
    AudioEffect audioEffect = createAudioEffect();

    audioEffect.setControlStatusListener((effect, controlGranted) -> {});

    assertThat(Shadows.shadowOf(audioEffect).hasControlStatusListener()).isTrue();
  }

  @Test
  public void hasControlStatusListener_setControlStatusListenerNull_returnsFalse() {
    AudioEffect audioEffect = createAudioEffect();

    audioEffect.setControlStatusListener((effect, controlGranted) -> {});
    audioEffect.setControlStatusListener(null);

    assertThat(Shadows.shadowOf(audioEffect).hasControlStatusListener()).isFalse();
  }

  @Test
  public void hasEnableStatusListener_returnsFalseByDefault() {
    AudioEffect audioEffect = createAudioEffect();

    assertThat(Shadows.shadowOf(audioEffect).hasEnableStatusListener()).isFalse();
  }

  @Test
  public void hasEnableStatusListener_setEnableStatusListenerNonNull_returnsTrue() {
    AudioEffect audioEffect = createAudioEffect();

    audioEffect.setEnableStatusListener((effect, enabled) -> {});

    assertThat(Shadows.shadowOf(audioEffect).hasEnableStatusListener()).isTrue();
  }

  @Test
  public void hasEnableStatusListener_setEnableStatusListenerNull_returnsFalse() {
    AudioEffect audioEffect = createAudioEffect();

    audioEffect.setEnableStatusListener((effect, enabled) -> {});
    audioEffect.setEnableStatusListener(null);

    assertThat(Shadows.shadowOf(audioEffect).hasEnableStatusListener()).isFalse();
  }

  @Test(expected = IllegalStateException.class)
  public void release_callSetEnabledAfterwards_throwsException() {
    AudioEffect audioEffect = createAudioEffect();
    audioEffect.release();

    audioEffect.setEnabled(true);
  }

  @Test(expected = IllegalStateException.class)
  public void release_callGetEnabledAfterwards_throwsException() {
    AudioEffect audioEffect = createAudioEffect();
    audioEffect.release();

    audioEffect.getEnabled();
  }

  @Test(expected = NullPointerException.class)
  public void ctor_nullType_throwsException() {
    new AudioEffect(/* type= */ null, EFFECT_TYPE_NULL, /* priority= */ 0, /* audioSession= */ 0);
  }

  @Test(expected = NullPointerException.class)
  public void ctor_nullUuid_throwsException() {
    new AudioEffect(
        AudioEffect.EFFECT_TYPE_AEC, /* uuid= */ null, /* priority= */ 0, /* audioSession= */ 0);
  }

  private static AudioEffect createAudioEffect() {
    return new AudioEffect(
        AudioEffect.EFFECT_TYPE_AEC, EFFECT_TYPE_NULL, /* priority= */ 0, /* audioSession= */ 0);
  }
}
