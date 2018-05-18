package org.studyslug.www.studyslug;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CourseTest {

  private Course testCourse;
  private static final String TAG = "CourseTest: ";
  private static final String TEST_NUMBER = "Test Number";
  private static final String TEST_DEPARTMENT = "Test Department";
  private static final String TEST_SECTION = "Test Section";
  private static final String STUDENT_1_NAME = "Student1";
  private static final String STUDENT_2_NAME = "Student2";
  private static final String STUDENT_1_EMAIL = "student2@ucsc.edu";
  private static final String STUDENT_2_EMAIL = "student2@ucsc.edu";
  private User student1;
  private User student2;

  @Before
  public void setUp() {
    testCourse = new Course();
    testCourse.setSection(TEST_SECTION);
    testCourse.setDepartment(TEST_DEPARTMENT);
    testCourse.setNumber(TEST_NUMBER);
  }

  @Test
  public void course_test_getters_setters() {
    // Tests that the getters work correctly
    Assert.assertEquals(testCourse.getSection(), TEST_SECTION);
    Assert.assertEquals(testCourse.getNumber(), TEST_NUMBER);
    Assert.assertEquals(testCourse.getDepartment(), TEST_DEPARTMENT);
  }

  @Test
  public void course_test_noStudents() {
    Assert.assertEquals(testCourse.getStudents().isEmpty(), true);
  }

  @Test
  public void course_test_firstStudent() {
    student1 = new User();
    student1.setName(STUDENT_1_NAME);
    student1.setEmail(STUDENT_1_EMAIL);
    testCourse.addStudent(student1);
    Assert.assertEquals(testCourse.getStudents().isEmpty(), false);
    Assert.assertEquals(testCourse.getStudents().containsKey("ID 0"), true);
    Assert.assertEquals(testCourse.getStudents().get("ID 0"), student1.getEmail());
  }

  @Test
  public void course_test_secondStudent() {
    student1 = new User();
    student1.setName(STUDENT_1_NAME);
    student1.setEmail(STUDENT_1_EMAIL);
    testCourse.addStudent(student1);
    student2 = new User();
    student2.setName(STUDENT_2_NAME);
    student2.setEmail(STUDENT_2_EMAIL);
    testCourse.addStudent(student2);
    Assert.assertEquals(testCourse.getStudents().isEmpty(), false);
    Assert.assertEquals(testCourse.getStudents().containsKey("ID 1"), true);
    Assert.assertEquals(testCourse.getStudents().get("ID 1"), student2.getEmail());
  }
}