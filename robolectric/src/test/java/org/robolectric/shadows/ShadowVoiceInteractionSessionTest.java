package org.robolectric.shadows;

import static android.os.Build.VERSION_CODES.Q;
import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static com.google.common.truth.Truth.assertThat;

import android.content.Intent;
import android.os.Bundle;
import android.service.voice.VoiceInteractionSession;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;
import org.robolectric.shadow.api.Shadow;

/** Tests for {@link ShadowVoiceInteractionSession}. */
@RunWith(AndroidJUnit4.class)
@Config(sdk = Q)
public class ShadowVoiceInteractionSessionTest {

  private VoiceInteractionSession session;
  private ShadowVoiceInteractionSession shadowSession;

  @Before
  public void setUp() {
    session = new VoiceInteractionSession(getApplicationContext());
    shadowSession = Shadow.extract(session);
  }

  @Test(expected = IllegalStateException.class)
  public void hide_beforeOnCreate_throws() {
    session.hide();
  }

  @Test
  public void hide_notInvoked_isNotHidden() {
    assertThat(shadowSession.isHidden()).isFalse();
  }

  @Test
  public void hide_invoked_isHidden() {
    session.onCreate();

    session.hide();

    assertThat(shadowSession.isHidden()).isTrue();
  }

  @Test(expected = IllegalStateException.class)
  public void show_beforeOnCreate_throws() {
    session.show(new Bundle(), /* flags= */ 0);
  }

  @Test
  public void show_notInvoked_isNotShown() {
    assertThat(shadowSession.isShown()).isFalse();
  }

  @Test
  public void show_invoked_isShown() {
    session.onCreate();

    session.show(new Bundle(), /* flags= */ 0);

    assertThat(shadowSession.isShown()).isTrue();
  }

  @Test(expected = IllegalStateException.class)
  public void finish_beforeOnCreate_throws() {
    session.finish();
  }

  @Test
  public void finish_notInvoked_isNotFinished() {
    assertThat(shadowSession.isFinished()).isFalse();
  }

  @Test
  public void finish_invoked_isFinished() {
    session.onCreate();

    session.finish();

    assertThat(shadowSession.isFinished()).isTrue();
  }

  @Test(expected = IllegalStateException.class)
  public void startAssistantActivity_beforeOnCreate_throws() {
    session.startAssistantActivity(new Intent());
  }

  @Test
  public void startAssistantActivity_invokedTwice_lastIntentRegistered() {
    session.onCreate();
    Intent intent1 = new Intent("foo action");
    Intent intent2 = new Intent("bar action");

    session.startAssistantActivity(intent1);
    session.startAssistantActivity(intent2);

    assertThat(shadowSession.getLastAssistantActivityIntent()).isEqualTo(intent2);
  }

  @Test
  public void startAssistantActivity_invokedTwice_allIntentsRegisteredInOrder() {
    session.onCreate();
    Intent intent1 = new Intent("foo action");
    Intent intent2 = new Intent("bar action");

    session.startAssistantActivity(intent1);
    session.startAssistantActivity(intent2);

    assertThat(shadowSession.getAssistantActivityIntents())
        .containsExactly(intent1, intent2)
        .inOrder();
  }

  @Test
  public void startAssistantActivity_notInvoked_noRegisteredIntents() {
    assertThat(shadowSession.getAssistantActivityIntents()).isEmpty();
  }

  @Test
  public void startAssistantActivity_notInvoked_lastRegisteredIntentIsNull() {
    assertThat(shadowSession.getLastAssistantActivityIntent()).isNull();
  }

  @Test(expected = IllegalStateException.class)
  public void startVoiceActivity_beforeOnCreate_throws() {
    session.startVoiceActivity(new Intent());
  }

  @Test(expected = SecurityException.class)
  public void startVoiceActivity_exceptionSet_throws() {
    session.onCreate();

    shadowSession.setStartVoiceActivityException(new SecurityException());

    session.startVoiceActivity(new Intent());
  }

  @Test
  public void startVoiceActivity_invokedTwice_lastIntentRegistered() {
    session.onCreate();
    Intent intent1 = new Intent("foo action");
    Intent intent2 = new Intent("bar action");

    session.startVoiceActivity(intent1);
    session.startVoiceActivity(intent2);

    assertThat(shadowSession.getLastVoiceActivityIntent()).isEqualTo(intent2);
  }

  @Test
  public void startVoiceActivity_invokedTwice_allIntentsRegisteredInOrder() {
    session.onCreate();
    Intent intent1 = new Intent("foo action");
    Intent intent2 = new Intent("bar action");

    session.startVoiceActivity(intent1);
    session.startVoiceActivity(intent2);

    assertThat(shadowSession.getVoiceActivityIntents()).containsExactly(intent1, intent2).inOrder();
  }

  @Test
  public void startVoiceActivity_notInvoked_noRegisteredIntents() {
    assertThat(shadowSession.getVoiceActivityIntents()).isEmpty();
  }

  @Test
  public void startVoiceActivity_notInvoked_lastRegisteredIntentIsNull() {
    assertThat(shadowSession.getVoiceActivityIntents()).isEmpty();
  }
}
