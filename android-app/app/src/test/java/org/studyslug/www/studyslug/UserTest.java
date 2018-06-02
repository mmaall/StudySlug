package org.studyslug.www.studyslug;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

public class UserTest {
  private User testUser;
  private static final String TAG = "User Test: ";
  private static final String TEST_NAME = "Test Name";
  private static final String TEST_EMAIL = "Test Email";
  private static final String TEST_BIO = "Test Bio";
  private static final String TEST_PHOTO = "Test URI";
  private static final String TEST_COURSE_1 = "Test Course 1";
  private static final String TEST_COURSE_2 = "Test Course 2";

  @Test
  public void testEmptyConstructor() {
    testUser = new User();
    Assert.assertNotNull(testUser);
  }

  @Test
  public void testFirstConstructor() {
    testUser = new User(TEST_NAME, TEST_EMAIL);
    Assert.assertEquals(testUser.getName(), TEST_NAME);
    Assert.assertEquals(testUser.getEmail(), TEST_EMAIL);
  }

  @Test
  public void testSecondConstructor() {
    testUser = new User(TEST_NAME, TEST_EMAIL, TEST_BIO, TEST_PHOTO);
    Assert.assertEquals(testUser.getName(), TEST_NAME);
    Assert.assertEquals(testUser.getEmail(), TEST_EMAIL);
    Assert.assertEquals(testUser.getBio(), TEST_BIO);
    Assert.assertEquals(testUser.getURI(), TEST_PHOTO);
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
    Assert.assertNull(testUser.getName());
    testUser.setName(TEST_NAME);
    Assert.assertEquals(testUser.getName(), TEST_NAME);
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
    testUser.setURI(TEST_PHOTO);
    Assert.assertEquals(testUser.getURI(),TEST_PHOTO);
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


