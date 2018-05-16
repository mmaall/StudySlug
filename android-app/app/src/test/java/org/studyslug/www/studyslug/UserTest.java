package org.studyslug.www.studyslug;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class UserTest {
    private User testUser;
    private static final String TAG = "User Test: ";
    private static final String testName = "Test Name";
    private static final String testEmail = "Test Email";
    private static final String testStatus = "Test Status";

    @Before
    public void setUp() throws Exception{
        testUser = new User();
        testUser.setName(testName);
        testUser.setEmail(testEmail);
        testUser.setStatus(testStatus);

    }
    @Test
    public void userName_unit_test(){
        Assert.assertEquals(testUser.getName(),testName);

    }

    @Test
    public void userEmail_unit_test(){
        Assert.assertEquals(testUser.getEmail(),testEmail);
    }

    @Test
    public void userStatus_unit_test(){
        Assert.assertEquals(testUser.getStatus(),testStatus);
    }
    

    }


