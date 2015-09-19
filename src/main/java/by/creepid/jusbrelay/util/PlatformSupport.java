package by.creepid.jusbrelay.util;

/**
 * Created by creepid on 18.09.15.
 */
public class PlatformSupport {

    static String OS = System.getProperty("os.name").toLowerCase();
    static String ARCH = System.getProperty("os.arch").toLowerCase();

    private PlatformSupport(){
    }

    public static enum Platform{
        WIN(".dll"),
        NIX(".so"),
        MAC(".dylib"),
        SOLARIS(".so"),
        UNKNOWN("");

        private final String extension;

        Platform(String extension) {
            this.extension = extension;
        }

        @Override
        public String toString() {
            return super.toString().toLowerCase();
        }

        public String getExtension() {
            return extension;
        }
    }

    public static enum Arch{
        W32,
        I386,
        UNKNOWN;

        @Override
        public String toString() {
            return super.toString().toLowerCase();
        }
    }

    public static boolean isWindows() {
        return (OS.indexOf("win") >= 0);
    }

    public static boolean isMac() {
        return (OS.indexOf("mac") >= 0);
    }

    public static boolean isUnix() {
        return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0 );
    }

    public static boolean isSolaris() {
        return (OS.indexOf("sunos") >= 0);
    }

    public static boolean isWin32() {
        return (ARCH.indexOf("x86") >= 0);
    }

    public static boolean isI386() {
        return (ARCH.indexOf("i386") >= 0);
    }

    public static Platform getPlatform(){
        if (isWindows()) {
            return Platform.WIN;
        } else if (isMac()) {
            return Platform.MAC;
        } else if (isUnix()) {
            return Platform.NIX;
        } else if (isSolaris()) {
            return Platform.SOLARIS;
        } else {
            return Platform.UNKNOWN;
        }
    }

    public static Arch getArch(){
        if (isWin32()){
            return Arch.W32;
        }else if (isI386()){
            return Arch.I386;
        } else{
            return Arch.UNKNOWN;
        }
    }
}
