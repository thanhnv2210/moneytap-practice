package com.mtvn;

import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
@SelectPackages("com.mtvn.rest.controller")
//@SelectClasses({MoEngageHookControllerTest.class, DropdownControllerTest.class, DropdownControllerMockMvcTest.class})
public class AllUnitTest {
}
