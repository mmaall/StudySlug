package org.studyslug.www.studyslug;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CourseTest {

    private Course testCourse;
    private static final String TAG = "CourseTest: ";
    private static final String testNumber = "Test Number";
    private static final String testDepartment = "Test Department";
    private static final String testSection = "Test Section";
    private User testUser;

    @Before
    public void setUp() throws Exception {
        testCourse = new Course();
        testCourse.setSection(testSection);
        testCourse.setDepartment(testDepartment);
        testCourse.setNumber(testNumber);
    }

    @Test
    public void course_unit_test() {
        testUser = new User();
        testUser.setEmail("test_email@test.com");
        testUser.setName("Testy McTesterface");
        testUser.setStatus("Test Status");
        testCourse.addStudent(testUser);
        Assert.assertEquals(testCourse.getSection(), testSection);
        Assert.assertEquals(testCourse.getNumber(), testNumber);
        Assert.assertEquals(testCourse.getDepartment(), testDepartment);

    }

}