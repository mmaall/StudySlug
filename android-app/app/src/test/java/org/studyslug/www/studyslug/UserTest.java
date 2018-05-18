package org.studyslug.www.studyslug;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class UserTest {
    private User testUser;
    private static final String TAG = "User Test: ";
    private static final String TEST_NAME = "Test Name";
    private static final String TEST_EMAIL = "Test Email";

    @Before
    public void setUp() {
        testUser = new User();
        testUser.setName(TEST_NAME);
        testUser.setEmail(TEST_EMAIL);
    }

    @Test
    public void user_getter_test(){
        Assert.assertEquals(testUser.getName(), TEST_NAME);
        Assert.assertEquals(testUser.getEmail(), TEST_EMAIL);
    }
}


