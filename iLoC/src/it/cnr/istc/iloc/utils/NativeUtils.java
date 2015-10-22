package it.cnr.istc.iloc.utils;

import java.lang.reflect.Field;
import java.util.Arrays;

/**
 *
 * @author Riccardo De Benedictis
 */
public class NativeUtils {

    /**
     * Adds the system dependant library path.
     *
     * @throws Exception
     */
    public static void addLibraryPath() throws Exception {
        String libraryPath = ".\\lib\\";

        String os_name = System.getProperty("os.name");
        if (os_name.equals("Linux")) {
            libraryPath += "linux";
        } else if (os_name.startsWith("Windows")) {
            libraryPath += "windows";
        } else if (os_name.equals("SunOS")) {
            libraryPath += "solaris";
        } else if (os_name.endsWith("OS X")) {
            libraryPath += "osx";
        }

        libraryPath += "\\";

        String os_arch = System.getProperty("os.arch");
        if (os_arch.endsWith("86")) {
            libraryPath += "x86";
        } else if (os_arch.equals("arm")) {
            libraryPath += "arm";
        } else if (os_arch.equals("amd64") || os_arch.equals("x86_64")) {
            libraryPath += "amd64";
        }

        addLibraryPath(libraryPath);
    }

    /**
     * Adds the specified path to the java library path.
     *
     * @param pathToAdd the path to add.
     * @throws Exception
     */
    public static void addLibraryPath(String pathToAdd) throws Exception {
        final Field usrPathsField = ClassLoader.class.getDeclaredField("usr_paths");
        usrPathsField.setAccessible(true);

        //get array of paths
        final String[] paths = (String[]) usrPathsField.get(null);

        //check if the path to add is already present
        for (String path : paths) {
            if (path.equals(pathToAdd)) {
                return;
            }
        }

        //add the new path
        final String[] newPaths = Arrays.copyOf(paths, paths.length + 1);
        newPaths[newPaths.length - 1] = pathToAdd;
        usrPathsField.set(null, newPaths);
    }

    private NativeUtils() {
    }
}
