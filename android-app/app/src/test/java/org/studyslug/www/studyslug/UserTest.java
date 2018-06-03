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
  private static final String TEST_NAME = "Test Name";
  private static final String TEST_EMAIL = "Test Email";
  private static final String TEST_BIO = "Test Bio";
  private static final String TEST_URI_STRING = "https://test.uri";
  private static final String TEST_COURSE_1 = "Test Course 1";
  private static final String TEST_COURSE_2 = "Test Course 2";
  private Client mockClient = mock(Client.class);
  private Uri mockUri = mock(Uri.class, TEST_URI_STRING);

  @Before
  public void setup() {
    when(mockClient.getDisplayName()).thenReturn(TEST_NAME);
    when(mockClient.getEmail()).thenReturn(TEST_EMAIL);
    when(mockClient.getPhotoUri()).thenReturn(mockUri);
    when(mockClient.getUserName()).thenReturn(TEST_EMAIL.split("@")[0]);
  }

  @Test
  public void testEmptyConstructor() {
    Assert.assertNull(testUser);
    testUser = new User();
    Assert.assertNotNull(testUser);
  }

  @Test
  public void testFirstConstructor() {
    Assert.assertNull(testUser);
    testUser = new User(TEST_NAME, TEST_EMAIL);
    Assert.assertNotNull(testUser);
    Assert.assertEquals(testUser.getDisplayName(), TEST_NAME);
    Assert.assertEquals(testUser.getEmail(), TEST_EMAIL);
  }

  @Test
  public void testSecondConstructor() {
    Assert.assertNull(testUser);
    testUser = new User(TEST_NAME, TEST_EMAIL, TEST_BIO, mockUri);
    Assert.assertNotNull(testUser);
    Assert.assertEquals(testUser.getDisplayName(), TEST_NAME);
    Assert.assertEquals(testUser.getBio(), TEST_BIO);
    Assert.assertEquals(testUser.getEmail(), TEST_EMAIL);
    Assert.assertEquals(testUser.getURI(), mockUri.toString());
  }
  @Test
  public void testThirdConstructor() {
    Assert.assertNull(testUser);
    testUser = new User(TEST_NAME, TEST_EMAIL, TEST_BIO, TEST_URI_STRING);
    Assert.assertEquals(testUser.getDisplayName(), TEST_NAME);
    Assert.assertEquals(testUser.getEmail(), TEST_EMAIL);
    Assert.assertEquals(testUser.getBio(), TEST_BIO);
    Assert.assertEquals(testUser.getURI(), TEST_URI_STRING);
  }

  @Test
  public void testFourthConstructor() {
    Assert.assertNull(testUser);
    testUser = new User(mockClient);
    Assert.assertEquals(testUser.getDisplayName(), TEST_NAME);
    Assert.assertEquals(testUser.getBio(), TEST_BIO);
    Assert.assertEquals(testUser.getEmail(), TEST_EMAIL);
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
    testUser.setDisplayName(TEST_NAME);
    Assert.assertEquals(testUser.getDisplayName(), TEST_NAME);
  }

  @Test
  public void testEmailSetter() {
    testUser = new User();
    Assert.assertNull(testUser.getEmail());
    testUser.setEmail(TEST_EMAIL);
    Assert.assertEquals(testUser.getEmail(), TEST_EMAIL);
  }
  @Test
      public void testBioSetter() {
    testUser = new User();
    Assert.assertNull(testUser.getBio());
    testUser.setBio(TEST_BIO);
    Assert.assertEquals(testUser.getBio(), TEST_BIO);
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
}


