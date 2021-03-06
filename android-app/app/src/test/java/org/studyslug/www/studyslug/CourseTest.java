package org.studyslug.www.studyslug;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CourseTest {

  private Course testCourse;
  private static final String TEST_NUMBER = "Test Number";
  private static final String TEST_NAME = "Test Name";
  private static final String TEST_DEPARTMENT = "Test Department";
  private static final String TEST_SECTION = "Test Section";
  private static final String MOCK_USER_1_NAME = "Student1";
  private static final String MOCK_USER_2_NAME = "Student2";
  private static final String MOCK_USER_1_EMAIL = "mockUser1@ucsc.edu";
  private static final String MOCK_USER_2_EMAIL = "mockUser2@ucsc.edu";
  private static final String TEST_KEY = TEST_DEPARTMENT + " " +
                                         TEST_NUMBER + " " +
                                         TEST_SECTION + " " +
                                         TEST_NAME + " " +
                                         TEST_SECTION;
  private User mockUser1 = mock(User.class);
  private User mockUser2 = mock(User.class);

  @Before
  public void setup() {
    when(mockUser1.getDisplayName()).thenReturn(MOCK_USER_1_NAME);
    when(mockUser1.getEmail()).thenReturn(MOCK_USER_1_EMAIL);
    when(mockUser1.getUserName()).thenReturn(MOCK_USER_1_NAME);
    when(mockUser2.getDisplayName()).thenReturn(MOCK_USER_2_NAME);
    when(mockUser2.getEmail()).thenReturn(MOCK_USER_2_EMAIL);
    when(mockUser2.getUserName()).thenReturn(MOCK_USER_2_NAME);
  }
  @Test
  public void testEmptyConstructor() {
    Assert.assertNull(testCourse);
    testCourse = new Course();
    Assert.assertNotNull(testCourse);
  }

  @Test
  public void testFullConstructor() {
    Assert.assertNull(testCourse);
    HashMap<String, HashMap<String, String>> testStudents = new HashMap<>();
    HashMap<String, String> newStudentEntry1 = new HashMap<>();
    HashMap<String, String> newStudentEntry2 = new HashMap<>();
    newStudentEntry1.put("displayName", mockUser1.getDisplayName());
    newStudentEntry1.put("email", mockUser1.getEmail());
    newStudentEntry1.put("uri", mockUser1.getURI());
    newStudentEntry2.put("displayName", mockUser2.getDisplayName());
    newStudentEntry2.put("email", mockUser2.getEmail());
    newStudentEntry2.put("uri", mockUser2.getURI());
    testStudents.put(mockUser1.getUserName(), newStudentEntry1);
    testStudents.put(mockUser2.getUserName(), newStudentEntry2);
    testCourse = new Course(TEST_DEPARTMENT, TEST_NAME, TEST_NUMBER, TEST_SECTION, testStudents);
    Assert.assertNotNull(testCourse);

  }

  @Test
  public void testNumberSetter() {
    testCourse = new Course();
    Assert.assertNull(testCourse.getNumber());
    testCourse.setNumber(TEST_NUMBER);
    Assert.assertEquals(testCourse.getNumber(), TEST_NUMBER);
  }

  @Test
  public void testDepartmentSetter() {
    testCourse = new Course();
    Assert.assertNull(testCourse.getDepartment());
    testCourse.setDepartment(TEST_DEPARTMENT);
    Assert.assertEquals(testCourse.getDepartment(), TEST_DEPARTMENT);
  }

  @Test
  public void testNameSetter() {
    testCourse = new Course();
    Assert.assertNull(testCourse.getName());
    testCourse.setName(TEST_NAME);
    Assert.assertEquals(testCourse.getName(), TEST_NAME);
  }

  @Test
  public void testSectionSetter() {
    testCourse = new Course();
    Assert.assertNull(testCourse.getSection());
    testCourse.setSection(TEST_SECTION);
    Assert.assertEquals(testCourse.getSection(), TEST_SECTION);
  }

  @Test
  public void testKeySetter() {
    testCourse = new Course();
    testCourse.setKey(TEST_KEY);
    Assert.assertEquals(testCourse.getKey(), TEST_KEY);
  }

  @Test
  public void course_test_noStudents() {
    testCourse = new Course();
    Assert.assertEquals(testCourse.getStudents().isEmpty(), true);
  }

  @Test
  public void course_test_firstStudent() {
    testCourse = new Course();
    mockUser1 = new User();
    mockUser1.setDisplayName(MOCK_USER_1_NAME);
    mockUser1.setEmail(MOCK_USER_1_EMAIL);
    testCourse.addStudent(mockUser1);
    Assert.assertFalse(testCourse.getStudents().isEmpty());
    Assert.assertTrue(testCourse.getStudents().containsKey(mockUser1.getUserName()));
  }

  @Test
  public void course_test_secondStudent() {
    testCourse = new Course();
    mockUser1 = new User();
    mockUser1.setDisplayName(MOCK_USER_1_NAME);
    mockUser1.setEmail(MOCK_USER_1_EMAIL);
    testCourse.addStudent(mockUser1);
    mockUser2 = new User();
    mockUser2.setDisplayName(MOCK_USER_2_NAME);
    mockUser2.setEmail(MOCK_USER_2_EMAIL);
    testCourse.addStudent(mockUser2);
    Assert.assertFalse(testCourse.getStudents().isEmpty());
    Assert.assertTrue(testCourse.getStudents().containsKey(mockUser2.getUserName()));
  }

  @Test
  public void course_test_setStudents() {
    testCourse = new Course();
    Assert.assertTrue(testCourse.getStudents().isEmpty());
    HashMap<String, HashMap<String, String>> testStudents = new HashMap<>();
    HashMap<String, String> newStudentEntry1 = new HashMap<>();
    HashMap<String, String> newStudentEntry2 = new HashMap<>();
    newStudentEntry1.put("displayName", mockUser1.getDisplayName());
    newStudentEntry1.put("email", mockUser1.getEmail());
    newStudentEntry1.put("uri", mockUser1.getURI());
    newStudentEntry2.put("displayName", mockUser2.getDisplayName());
    newStudentEntry2.put("email", mockUser2.getEmail());
    newStudentEntry2.put("uri", mockUser2.getURI());
    testStudents.put(mockUser1.getUserName(), newStudentEntry1);
    testStudents.put(mockUser2.getUserName(), newStudentEntry2);
    testCourse.setStudents(testStudents);
    Assert.assertFalse(testCourse.getStudents().isEmpty());
    Assert.assertTrue(testCourse.getStudents().containsKey(mockUser1.getUserName()));
    Assert.assertTrue(testCourse.getStudents().containsKey(mockUser2.getUserName()));
  }

  @Test
  public void course_test_equals() {
    HashMap<String, HashMap<String, String>> testStudents = new HashMap<>();
    Course testCourse1 = new Course(TEST_DEPARTMENT, TEST_NAME, TEST_NUMBER, TEST_SECTION, testStudents);
    Course testCourse2 = new Course();
    Assert.assertFalse(testCourse1.equals(testCourse2));
    testCourse2 = new Course(TEST_DEPARTMENT, TEST_NAME, TEST_NUMBER, TEST_SECTION, testStudents);
    Assert.assertTrue(testCourse1.equals(testCourse2));
  }
}