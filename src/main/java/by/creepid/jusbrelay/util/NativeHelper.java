/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.creepid.jusbrelay.util;

import java.io.*;
import java.lang.reflect.Field;
import java.util.logging.Logger;
import static by.creepid.jusbrelay.util.PlatformSupport.*;

/**
 *
 * @author rusakovich
 */
public class NativeHelper {

    private static final Logger log = Logger.getLogger(NativeHelper.class.getName());
    private static final int MILLIS_PER_DAY = 86400000;
    private static final PlatformSupport.Platform PLATFORM = PlatformSupport.getPlatform();
    private static final PlatformSupport.Arch ARCH = PlatformSupport.getArch();

    private static final class TempDLLFileFilter implements FileFilter {

        private final String tempfilePrefix;

        public TempDLLFileFilter(String tempfilePrefix) {
            this.tempfilePrefix = tempfilePrefix;
        }

        public boolean accept(File pathname) {
            String name = pathname.getName();
            return pathname.isFile()
                    && name.startsWith(tempfilePrefix)
                    && name.endsWith(PLATFORM.getExtension());
        }
    }
    public static final String LIB_DIR_OVERRIDE = "natives_lib_dir";
    static final String DEFAULT_LIB_FOLDER = "lib";
    static final String EXTRACT_FOLDER = System.getProperty("java.io.tmpdir")
            + File.separator
            + LIB_DIR_OVERRIDE;

    static File findLibFile(String libnameBase, String tempfilePrefix) throws IOException {
        String libName = buildLibName(libnameBase);
        File libFile = getOverrideLibFile(libName);
        if (libFile == null || libFile.exists() == false) {
            libFile = getDefaultLibFile(libName);
        }

        InputStream source = null;
        if (libFile == null || libFile.exists() == false) {
            source = NativeHelper.class.getResourceAsStream("/" + DEFAULT_LIB_FOLDER
                    + "/" + PLATFORM.toString()
                    + "/" + libName);
        } else {
            source = new FileInputStream(libFile);
        }

        if (source == null) {
            throw new IllegalStateException("I/O error while reading [" + libName + "] file");
        }

        libFile = extractToTempFile(source, tempfilePrefix);
        return libFile;
    }

    public static void cleanupTempFiles(String tempfilePrefix) {
        try {
            String tempFolder = System.getProperty("java.io.tmpdir")
                    + File.separator
                    + LIB_DIR_OVERRIDE;

            if (tempFolder == null || tempFolder.trim().length() == 0) {
                return;
            }

            File fldr = new File(tempFolder);
            File[] oldFiles = fldr.listFiles(new TempDLLFileFilter(tempfilePrefix));
            if (oldFiles == null) {
                return;
            }

            for (File tmp : oldFiles) {
                if ((System.currentTimeMillis() - tmp.lastModified()) > MILLIS_PER_DAY) {
                    tmp.delete();
                }
            }

        } catch (Exception e) {
            log.severe("Error cleaning up temporary library files. " + e.getMessage());
        }
    }

    private static File getDefaultLibFile(String libName) {
        return new File(DEFAULT_LIB_FOLDER, libName);
    }

    private static File getOverrideLibFile(String libName) {
        String libDir = System.getProperty(LIB_DIR_OVERRIDE);
        if (libDir == null || libDir.trim().length() == 0) {
            return null;
        }
        return new File(libDir, libName);
    }

    static File extractToTempFile(InputStream source, String tempfilePrefix) throws IOException {
        if (PLATFORM == Platform.UNKNOWN) {
            throw new IllegalStateException("Your platform [" + PlatformSupport.OS + "] , "
                    + "[" + PlatformSupport.ARCH + "] not supported yet");
        }

        File tempFile = new File(EXTRACT_FOLDER + File.separator + tempfilePrefix + PLATFORM.getExtension());
        tempFile.getParentFile().mkdir();
        boolean isTempFileDeleted = true;
        if (tempFile.exists()) {
            isTempFileDeleted = tempFile.delete();
        } 
        
        if (isTempFileDeleted) {
            FileOutputStream destination = new FileOutputStream(tempFile);
            copy(source, destination);
        }

        //Add folder to lib path
        addLibraryPath(tempFile.getParentFile().getAbsolutePath());

        return tempFile;
    }

    private static void closeStream(Closeable c) {
        try {
            if (c != null) {
                c.close();
            }
        } catch (IOException e) {
            // Ignore cleanup errors
        }
    }

    static void copy(InputStream source, OutputStream dest)
            throws IOException {
        try {
            byte[] buffer = new byte[1024];
            int read = 0;
            while (read >= 0) {
                dest.write(buffer, 0, read);
                read = source.read(buffer);
            }
            dest.flush();
        } finally {
            closeStream(source);
            closeStream(dest);
        }
    }

    private static String buildLibName(String libnameBase) {

        if (PLATFORM == Platform.UNKNOWN) {
            throw new IllegalStateException("Your platform [" + PlatformSupport.OS + "] , "
                    + "[" + PlatformSupport.ARCH + "] not supported yet");
        }

        return libnameBase + ARCH.toString() + PLATFORM.getExtension();
    }

    public static void load(String libnameBase, String tempfilePrefix) {
        try {
            File libFile = NativeHelper.findLibFile(libnameBase, tempfilePrefix);
            System.load(libFile.getAbsolutePath());
            NativeHelper.cleanupTempFiles(tempfilePrefix);
        } catch (IOException e) {
            throw new RuntimeException("Error loading library " + e.getMessage(), e);
        }
    }

    static void addLibraryPath(String libPath) {
        String currLibsPath = System.getProperty("java.library.path");
        if (currLibsPath != null && currLibsPath.contains(libPath)) {
            return;
        }

        if (currLibsPath != null && !currLibsPath.trim().isEmpty()) {
            System.setProperty("java.library.path", currLibsPath + File.pathSeparator + libPath);
        } else {
            System.setProperty("java.library.path", libPath);
        }

        try {
            Field fieldSysPath = ClassLoader.class.getDeclaredField("sys_paths");
            fieldSysPath.setAccessible(true);
            fieldSysPath.set(null, null);
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
    }
}
