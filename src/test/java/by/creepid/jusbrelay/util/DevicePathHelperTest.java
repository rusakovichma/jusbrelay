/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.creepid.jusbrelay.util;

import org.junit.Test;
import static org.junit.Assert.*;

public class DevicePathHelperTest {

    public DevicePathHelperTest() {
    }

    /**
     * Test of getVID method, of class DevicePathHelper.
     */
    @Test
    public void testGetVID() {
        System.out.println("***** testGetVID ******");
        String path = "\\\\?\\hid#vid_16c0&pid_05df#6&e6f7797&0&0000#{4d1e55b2-f16f-11cf-88cb-001111000030}";
        String actual = DevicePathHelper.getVID(path);
        String expected = "16c0";
        assertEquals(actual, expected);
    }

    /**
     * Test of getPID method, of class DevicePathHelper.
     */
    @Test
    public void testGetPID() {
        System.out.println("***** tedstGetPID ******");
        String path = "\\\\?\\hid#vi_16c0&pid_05df#6&e6f7797&0&0000#{4d1e55b2-f16f-11cf-88cb-001111000030}";
        String actual = DevicePathHelper.getPID(path);
        String expected = "05df";
        assertEquals(actual, expected);
    }

    /**
     * Test of getDriverGUID method, of class DevicePathHelper.
     */
    @Test
    public void testGetDriverGUID() {
        System.out.println("**** getDriverGUID *****");
        String path = "\\\\?\\hid#vi_16c0&pid_05df#6&e6f7797&0&0000#{4d1e55b2-f16f-11cf-88cb-001111000030}";
        String actual = DevicePathHelper.getDriverGUID(path);
        String expected = "4d1e55b2-f16f-11cf-88cb-001111000030";
        assertEquals(actual, expected);

        path = "\\\\?\\hid#vi_16c0&pid_05df#6&e6f7797&0&0000";
        actual = DevicePathHelper.getDriverGUID(path);
        assertNull(actual);
    }
}
