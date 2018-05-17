package org.studyslug.www.studyslug;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CourseTest {

    private Course testCourse;
    private static final String TAG = "CourseTest: ";
    private static final String TEST_NUMBER = "Test Number";
    private static final String TEST_DEPARTMENT = "Test Department";
    private static final String testSection = "Test Section";
    private stati
    private User testUser;

    @Before
    public void setUp() throws Exception {
        testCourse = new Course();
        testCourse.setSection(testSection);
        testCourse.setDepartment(TEST_DEPARTMENT);
        testCourse.setNumber(TEST_NUMBER);
    }

    @Test
    public void course_unit_test() {
        testUser = new User();
        testUser.setEmail("test_email@test.com");
        testUser.setName("Testy McTesterface");
        testUser.setStatus("Test Status");
        testCourse.addStudent(testUser);
        Assert.assertEquals(testCourse.getSection(), testSection);
        Assert.assertEquals(testCourse.getNumber(), TEST_NUMBER);
        Assert.assertEquals(testCourse.getDepartment(), TEST_DEPARTMENT);

    }

}