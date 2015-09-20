/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package by.creepid.jusbrelay.util;

import by.creepid.jusbrelay.TestUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author mirash
 */
public class NativeHelperTest {
    
    @AfterClass
    public static void tearDownClass() {
        File libTempDir = new File(System.getProperty("java.io.tmpdir") + File.separator + "natives_lib_dir");

        if (libTempDir.exists()) {
            File[] files = libTempDir.listFiles();
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                file.delete();
            }
        }

        if (libTempDir.exists()) {
            libTempDir.delete();
        }
    }

    /**
     * Test of findLibFile method, of class NativeHelper.
     */
    @Test
    public void testFindLibFile() throws Exception {
        System.out.println("**** findLibFile *****");
        String path = TestUtil.TEST_DATA_FOLDER + PlatformSupport.getPlatform();
        System.setProperty(NativeHelper.LIB_DIR_OVERRIDE, path);
        File actual = NativeHelper.findLibFile("usbrelay_", "usbrelay");
        assertTrue(actual.getAbsolutePath().contains("usbrelay" + PlatformSupport.getPlatform().getExtension()));
    }

    /**
     * Test of cleanupTempFiles method, of class NativeHelper.
     */
    @Test
    @Ignore // We now only clean files older than one day
    public void testCleanupTempFiles() throws IOException {
        System.out.println("***** cleanupTempFiles *****");
        File f1 = File.createTempFile("usbrelay" + "_ABC", PlatformSupport.getPlatform().getExtension());
        assertTrue(f1.exists());
        NativeHelper.cleanupTempFiles("usbrelay_");
        assertFalse(f1.exists());
    }

    /**
     * Test of extractToTempFile method, of class NativeHelper.
     */
    @Test
    public void testExtractToTempFile() throws Exception {
        System.out.println("**** extractToTempFile *****");
        String path = TestUtil.TEST_DATA_FOLDER + PlatformSupport.getPlatform();
        System.setProperty(NativeHelper.LIB_DIR_OVERRIDE, path);

        String libName = "usbrelay_" + PlatformSupport.getArch() + PlatformSupport.getPlatform().getExtension();
        String tempfilePrefix = "usbrelay_";
        FileInputStream fis = new FileInputStream(new File(path + File.separator + libName));

        File result = NativeHelper.extractToTempFile(fis, "usbrelay");
        result.getParentFile().deleteOnExit();
        result.deleteOnExit();

        assertNotNull(result);
        assertTrue(result.exists());
    }

    /**
     * Test of copy method, of class NativeHelper.
     */
    @Test
    public void testCopy() throws Exception {
        System.out.println("**** copy *****");
        File originalFile = new File(TestUtil.TEST_DATA_FOLDER
                + PlatformSupport.getPlatform() + File.separator
                + "usbrelay_" + PlatformSupport.getArch() + PlatformSupport.getPlatform().getExtension());

        File tempFile = File.createTempFile("usbrelay_" + PlatformSupport.getArch().toString(), PlatformSupport.getPlatform().getExtension());

        NativeHelper.copy(new FileInputStream(originalFile), new FileOutputStream(tempFile));
        assertTrue(tempFile.exists() && tempFile.length() == originalFile.length());
        tempFile.delete();
    }

    /**
     * Test of load method, of class NativeHelper.
     */
    @Test
    public void testLoad() {
        System.out.println("**** load *****");
        String path = TestUtil.TEST_DATA_FOLDER + PlatformSupport.getPlatform();
        System.setProperty(NativeHelper.LIB_DIR_OVERRIDE, path);

        NativeHelper.load("usb_relay_device_", "usb_relay_device");
        NativeHelper.load("usbrelay_", "usbrelay");
    }
}