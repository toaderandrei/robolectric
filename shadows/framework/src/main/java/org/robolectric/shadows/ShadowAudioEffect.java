package org.robolectric.shadows;

import static android.media.audiofx.AudioEffect.SUCCESS;
import static com.google.common.base.Preconditions.checkNotNull;

import android.media.audiofx.AudioEffect;
import android.media.audiofx.AudioEffect.OnControlStatusChangeListener;
import android.media.audiofx.AudioEffect.OnEnableStatusChangeListener;
import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;
import org.robolectric.annotation.RealObject;
import org.robolectric.annotation.Resetter;

/**
 * Implements parts of the functionality of {@link AudioEffect}, especially whether an effect is
 * enabled or not.
 */
@Implements(value = AudioEffect.class)
public class ShadowAudioEffect {
  private static final List<AudioEffect.Descriptor> descriptors = new ArrayList<>();
  private static final List<AudioEffect> audioEffects = new ArrayList<>();

  @RealObject AudioEffect audioEffect;

  private int priority;
  private int audioSession;
  private boolean isEnabled = false;
  private boolean isReleased = false;

  private OnControlStatusChangeListener controlStatusChangeListener;
  private OnEnableStatusChangeListener enableStatusChangeListener;

  @Implementation
  protected void __constructor__(UUID type, UUID uuid, int priority, int audioSession) {
    checkNotNull(type);
    checkNotNull(uuid);
    audioEffects.add(audioEffect);
    this.priority = priority;
    this.audioSession = audioSession;
  }

  @Implementation
  protected int setEnabled(boolean enabled) {
    checkNotReleased();
    isEnabled = enabled;
    return SUCCESS;
  }

  @Implementation
  public boolean getEnabled() {
    checkNotReleased();
    return isEnabled;
  }

  /**
   * Sets the control-status change listener, note that the given listener will NOT ever be called
   * from this class.
   */
  @Implementation
  public void setControlStatusListener(OnControlStatusChangeListener listener) {
    controlStatusChangeListener = listener;
  }

  /**
   * Sets the enable-status change listener, note that the given listener will NOT ever be called
   * from this class.
   */
  @Implementation
  public void setEnableStatusListener(OnEnableStatusChangeListener listener) {
    enableStatusChangeListener = listener;
  }

  public boolean hasControlStatusListener() {
    return controlStatusChangeListener != null;
  }

  public boolean hasEnableStatusListener() {
    return enableStatusChangeListener != null;
  }

  public int getPriority() {
    return priority;
  }

  public int getAudioSession() {
    return audioSession;
  }

  /**
   * Adds an effect represented by an {@link AudioEffect.Descriptor}, only to be queried from {@link
   * #queryEffects()}.
   */
  public static void addEffect(AudioEffect.Descriptor descriptor) {
    descriptors.add(descriptor);
  }

  /**
   * Returns the set of audio effects added through {@link #addEffect}.
   *
   * <p>Note: in the original {@link AudioEffect} implementation this method returns all the
   * existing unique AudioEffects created through an {@link AudioEffect} ctor. In this
   * implementation only the effects added through {@link #addEffect} are returned here.
   */
  @Implementation
  protected static AudioEffect.Descriptor[] queryEffects() {
    return descriptors.toArray(new AudioEffect.Descriptor[descriptors.size()]);
  }

  /** Returns all effects created with an {@code AudioEffect} constructor. */
  public static List<AudioEffect> getAudioEffects() {
    return new ArrayList<>(audioEffects);
  }

  /**
   * Removes this audio effect from the set of active audio effects, and marks the effect so certain
   * methods cannot be called after this method.
   */
  @Implementation
  public void release() {
    isReleased = true;
    audioEffects.remove(audioEffect);
  }

  private void checkNotReleased() {
    Preconditions.checkState(!isReleased);
  }

  @Resetter
  public static void reset() {
    descriptors.clear();
    audioEffects.clear();
  }
}
