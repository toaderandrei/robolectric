package org.robolectric.shadows;

import static android.os.Build.VERSION_CODES.LOLLIPOP;
import static android.os.Build.VERSION_CODES.M;
import static android.os.Build.VERSION_CODES.O;
import static org.robolectric.shadow.api.Shadow.directlyOn;

import android.content.Intent;
import android.os.Bundle;
import android.service.voice.VoiceInteractionSession;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;
import org.robolectric.annotation.RealObject;

/** Shadow implementation of {@link android.service.voice.VoiceInteractionSession}. */
@Implements(value = VoiceInteractionSession.class, minSdk = LOLLIPOP)
public class ShadowVoiceInteractionSession {

  private final List<Intent> assistantActivityIntents = new ArrayList<>();
  private final List<Intent> voiceActivityIntents = new ArrayList<>();

  private boolean isCreated = false;
  private boolean isHidden = false;
  private boolean isFinished = false;
  private boolean isShown = false;
  @Nullable private RuntimeException startVoiceActivityException;
  @RealObject private VoiceInteractionSession realSession;

  @Implementation(minSdk = M)
  protected void onCreate() {
    directlyOn(realSession, VoiceInteractionSession.class).onCreate();
    isCreated = true;
  }

  @Implementation(minSdk = M)
  protected void hide() {
    if (!isCreated) {
      throw new IllegalStateException("Can't call before onCreate()");
    }
    isHidden = true;
  }

  @Implementation(minSdk = M)
  protected void show(Bundle args, int flags) {
    if (!isCreated) {
      throw new IllegalStateException("Can't call before onCreate()");
    }
    isShown = true;
  }

  @Implementation
  protected void finish() {
    if (!isCreated) {
      throw new IllegalStateException("Can't call before onCreate()");
    }
    isFinished = true;
  }

  @Implementation(minSdk = O)
  protected void startAssistantActivity(Intent intent) {
    if (!isCreated) {
      throw new IllegalStateException("Can' call before onCreate()");
    }
    assistantActivityIntents.add(intent);
  }

  @Implementation(minSdk = M)
  protected void startVoiceActivity(Intent intent) {
    if (!isCreated) {
      throw new IllegalStateException("Can't call before onCreate()");
    }
    RuntimeException exception = startVoiceActivityException;
    if (exception != null) {
      throw exception;
    }
    voiceActivityIntents.add(intent);
  }

  /**
   * Returns the last {@link Intent} passed into {@link #startAssistantActivity(Intent)} or {@code
   * null} if there wasn't any.
   */
  @Nullable
  public Intent getLastAssistantActivityIntent() {
    return Iterables.getLast(assistantActivityIntents, /* defaultValue= */ null);
  }

  /**
   * Returns the list of {@link Intent} instances passed into {@link
   * #startAssistantActivity(Intent)} in invocation order.
   */
  public ImmutableList<Intent> getAssistantActivityIntents() {
    return ImmutableList.copyOf(assistantActivityIntents);
  }

  /**
   * Returns the last {@link Intent} passed into {@link #startVoiceActivity(Intent)} or {@code null}
   * if there wasn't any.
   */
  @Nullable
  public Intent getLastVoiceActivityIntent() {
    return Iterables.getLast(voiceActivityIntents, /* defaultValue= */ null);
  }

  /**
   * Returns the list of {@link Intent} instances passed into {@link #startVoiceActivity(Intent)} in
   * invocation order.
   */
  public ImmutableList<Intent> getVoiceActivityIntents() {
    return ImmutableList.copyOf(voiceActivityIntents);
  }

  /** Returns whether {@link #hide()} has been invoked. */
  public boolean isHidden() {
    return isHidden;
  }

  /** Returns whether {@link #finish()} has been invoked. */
  public boolean isFinished() {
    return isFinished;
  }

  /** Returns whether {@link #show(Bundle, int)} has been invoked. */
  public boolean isShown() {
    return isShown;
  }

  /**
   * Sets a {@link RuntimeException} that should be thrown when {@link #startVoiceActivity(Intent)}
   * is invoked.
   */
  public void setStartVoiceActivityException(RuntimeException exception) {
    startVoiceActivityException = exception;
  }
}
