/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.creepid.jusbrelay.util;

import java.io.Closeable;
import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Logger;

/**
 *
 * @author rusakovich
 */
public class NativeHelper {

    private static final Logger log = Logger.getLogger(NativeHelper.class.getName());
    private static final int MILLIS_PER_DAY = 86400000;

    private static final class TempDLLFileFilter implements FileFilter {

        private final String tempfilePrefix;

        public TempDLLFileFilter(String tempfilePrefix) {
            this.tempfilePrefix = tempfilePrefix;
        }

        @Override
        public boolean accept(File pathname) {
            String name = pathname.getName();
            return pathname.isFile()
                    && name.startsWith(tempfilePrefix)
                    && name.endsWith(DLL_EXTENSION);
        }
    }
    public static final String LIB_DIR_OVERRIDE = "natives_lib_dir";
    static final String DLL_EXTENSION = ".dll";
    static final String DEFAULT_LIB_FOLDER = "lib";

    static File findLibFile(String libnameBase, String tempfilePrefix) throws IOException {
        String libName = buildLibName(libnameBase);
        File libFile = getOverrideLibFile(libName);
        if (libFile == null || libFile.exists() == false) {
            libFile = getDefaultLibFile(libName);
        }
        if (libFile == null || libFile.exists() == false) {
            libFile = extractToTempFile(libName, tempfilePrefix);
        }
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
            log.severe("Error cleaning up temporary dll files. " + e.getMessage());
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

    static File extractToTempFile(String libName, String tempfilePrefix) throws IOException {
        InputStream source = NativeHelper.class.getResourceAsStream("/" + DEFAULT_LIB_FOLDER + "/" + libName);

        String tempFolder = System.getProperty("java.io.tmpdir")
                + File.separator
                + LIB_DIR_OVERRIDE
                + File.separator;

        File tempFile = new File(tempFolder +  tempfilePrefix + DLL_EXTENSION);
        tempFile.getParentFile().mkdir();
        tempFile.createNewFile();

        FileOutputStream destination = new FileOutputStream(tempFile);
        copy(source, destination);

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
        String arch = "w32";
        if (!System.getProperty("os.arch").equals("x86")) {
            arch = System.getProperty("os.arch");
        }
        return libnameBase + arch + DLL_EXTENSION;
    }

    public static void load(String libnameBase, String tempfilePrefix) {
        try {
            File libFile = NativeHelper.findLibFile(libnameBase, tempfilePrefix);
            System.load(libFile.getAbsolutePath());
            NativeHelper.cleanupTempFiles(tempfilePrefix);
        } catch (IOException e) {
            throw new RuntimeException("Error loading dll" + e.getMessage(), e);
        }
    }

}
