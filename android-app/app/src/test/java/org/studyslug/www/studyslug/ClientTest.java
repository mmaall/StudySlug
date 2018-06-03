package org.studyslug.www.studyslug;

import android.net.Uri;

import com.google.firebase.auth.FirebaseUser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ClientTest {
  private static final String TEST_EMAIL = "Test Email";
  private static final String TEST_USERNAME = "Test Username";
  private static final String TEST_DISPLAYNAME = "Test Displayname";
  private static final String TEST_URI_STRING = "http://test.url.string";
  private static final String TEST_UID = "Test UID";
  private FirebaseUser mockUser = mock(FirebaseUser.class);
  private Uri mockUri = mock(Uri.class, TEST_URI_STRING);
  private Client testClient;

  @Before
  public void setup() throws Exception {
    when(mockUser.getEmail()).thenReturn(TEST_EMAIL);
    when(mockUser.getDisplayName()).thenReturn(TEST_DISPLAYNAME);
    when(mockUser.getPhotoUrl()).thenReturn(mockUri);
  }

  @Test
  public void testEmptyConstructor() {
    Assert.assertNull(testClient);
    testClient = new Client();
    Assert.assertNotNull(testClient);
  }

  @Test
  public void testNonEmptyConstructor() {
    Assert.assertNull(testClient);
    testClient = new Client(mockUser);
    Assert.assertNotNull(testClient);
  }

  @Test
  public void testUserSetter() {
    testClient = new Client();
    Assert.assertNull(testClient.getUser());
    testClient.setUser(mockUser);
    Assert.assertEquals(testClient.getUser(), mockUser);
  }

  @Test
  public void testEmailSetter() {
    testClient = new Client();
    Assert.assertNull(testClient.getEmail());
    testClient.setEmail(TEST_EMAIL);
    Assert.assertEquals(testClient.getEmail(),TEST_EMAIL);
  }

  @Test
  public void testUserNameSetter() {
    testClient = new Client();
    Assert.assertNull(testClient.getUserName());
    testClient.setUserName(TEST_USERNAME);
    Assert.assertEquals(testClient.getUserName(),TEST_USERNAME);
  }

  @Test
  public void testDisplayNameSetter() {
    testClient = new Client();
    Assert.assertNull(testClient.getDisplayName());
    testClient.setDisplayName(TEST_DISPLAYNAME);
    Assert.assertEquals(testClient.getDisplayName(), TEST_DISPLAYNAME);
  }

  @Test
  public void testPhotoUriSetter() {
    testClient = new Client();
    Assert.assertNull(testClient.getPhotoUri());
    testClient.setPhotoUri(mockUri);
    Assert.assertEquals(testClient.getPhotoUri(), mockUri);
  }

  @Test
  public void testUID() {
    testClient = new Client();
    Assert.assertNull(testClient.getUID());
    testClient.setUID(TEST_UID);
    Assert.assertEquals(testClient.getUID(), TEST_UID);
  }
}