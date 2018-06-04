package org.studyslug.www.studyslug;

import android.net.Uri;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserTest {
  private User testUser;
  private static final String TAG = "User Test: ";
  private static final String TEST_DISPLAYNAME = "Test DisplayName";
  private static final String TEST_USERNAME = "Test UserName";
  private static final String TEST_EMAIL = "Test Email";
  private static final String TEST_URI_STRING = "https://test.uri";
  private static final String TEST_COURSE_1 = "Test Course 1";
  private static final String TEST_COURSE_2 = "Test Course 2";
  private Client mockClient = mock(Client.class);
  private Uri mockUri = mock(Uri.class, TEST_URI_STRING);

  @Before
  public void setup() {
    when(mockClient.getDisplayName()).thenReturn(TEST_DISPLAYNAME);
    when(mockClient.getEmail()).thenReturn(TEST_EMAIL);
    when(mockClient.getUserName()).thenReturn(TEST_USERNAME);
    when(mockClient.getPhotoUri()).thenReturn(mockUri);
  }

  @Test
  public void testEmptyConstructor() {
    Assert.assertNull(testUser);
    testUser = new User();
    Assert.assertNotNull(testUser);
  }

  @Test
  public void testConstructor() {
    Assert.assertNull(testUser);
    testUser = new User(mockClient);
    Assert.assertEquals(testUser.getDisplayName(), TEST_DISPLAYNAME);
    Assert.assertEquals(testUser.getUserName(), TEST_USERNAME);
    // I don't know why but this test thinks the string TEST_BIO is null so it fails
    // TODO figure this out I guess
    Assert.assertEquals(testUser.getURI(), TEST_URI_STRING);
  }

  @Test
  public void testCoursesSetter() {
    testUser = new User();
    HashMap<String, String> testCourses = new HashMap<>();
    testCourses.put(TEST_COURSE_1, "0");
    testCourses.put(TEST_COURSE_2, "0");
    testUser.setCourses(testCourses);
    Assert.assertFalse(testUser.getCourses().isEmpty());
    Assert.assertTrue(testUser.getCourses().containsKey(TEST_COURSE_1));
    Assert.assertTrue(testUser.getCourses().containsKey(TEST_COURSE_2));
  }

  @Test
  public void testNameSetter() {
    testUser = new User();
    Assert.assertNull(testUser.getDisplayName());
    testUser.setDisplayName(TEST_DISPLAYNAME);
    Assert.assertEquals(testUser.getDisplayName(), TEST_DISPLAYNAME);
  }

  @Test
  public void testUserNameSetter() {
    testUser = new User();
    Assert.assertNull(testUser.getUserName());
    testUser.setUserName(TEST_USERNAME);
    Assert.assertEquals(testUser.getUserName(), TEST_USERNAME);
  }

  @Test
  public void testEmailSetter() {
    testUser = new User();
    Assert.assertNull(testUser.getEmail());
    testUser.setEmail(TEST_EMAIL);
    Assert.assertEquals(testUser.getEmail(), TEST_EMAIL);
  }

  @Test
  public void testUserPhotoSetter() {
    testUser = new User();
    Assert.assertNull(testUser.getURI());
    testUser.setURI(TEST_URI_STRING);
    Assert.assertEquals(testUser.getURI(), TEST_URI_STRING);
  }

  @Test
  public void testCoursesWithNoEntries() {
    testUser = new User();
    Assert.assertNotNull(testUser.getCourses());
    Assert.assertTrue(testUser.getCourses().isEmpty());
  }

  @Test
  public void testCoursesAddFirstEntry() {
    testUser = new User();
    testUser.addCourse(TEST_COURSE_1);
    Assert.assertFalse(testUser.getCourses().isEmpty());
    Assert.assertTrue(testUser.getCourses().containsKey(TEST_COURSE_1));
  }

  @Test
  public void testCoursesAddSecondEntry() {
    testUser = new User();
    testUser.addCourse(TEST_COURSE_1);
    testUser.addCourse(TEST_COURSE_2);
    Assert.assertFalse(testUser.getCourses().isEmpty());
    Assert.assertTrue(testUser.getCourses().containsKey(TEST_COURSE_2));
  }

  @Test
  public void testCourseEntry() {
    testUser = new User(mockClient);
    Assert.assertEquals(testUser.getCourseEntry().get("displayName"), TEST_DISPLAYNAME);
    Assert.assertEquals(testUser.getCourseEntry().get("email"), TEST_EMAIL);
    Assert.assertEquals(testUser.getCourseEntry().get("uri"), TEST_URI_STRING);
  }
}