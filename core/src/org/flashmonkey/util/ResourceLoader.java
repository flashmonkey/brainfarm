package org.flashmonkey.util;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

public class ResourceLoader {

    private ResourceLoader() {}
    private ResourceLoader(File dir) {
        this.directory = dir;
        
    }

    /**
     * Create a ResourceLoader for a specific file system location.
     * All JAR files and subdirectories in the location will be added
     * to the classpath
     */
    public static ClassLoader createForDirectory(File dir) {
        ResourceLoader loader = null;
        if (dir.isDirectory()) {
            loader = new ResourceLoader(dir);
        }
        loader.addJarsToPath();
        return loader.getClassLoader();
    }

    public static ClassLoader createForDirectory(String dir) {
        File f = new File(dir);
        return createForDirectory(f);
    }

    private File[] addJarsToPath() {
        File[] jarFiles = directory.listFiles();

        List<URL> urlList = new ArrayList<URL>();
        for(File f: jarFiles) {
            try {
                urlList.add(f.toURI().toURL());
            } catch (MalformedURLException ex) {
                //Logger.getLogger(ResourceLoader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        ClassLoader parentLoader = Thread.currentThread().getContextClassLoader();
        URL[] urls = new URL[urlList.size()];
        urls = urlList.toArray(urls);
        URLClassLoader classLoader = new URLClassLoader(urls, parentLoader);
        this.loader = classLoader;

        return jarFiles;
    }

    public ClassLoader getClassLoader() {
        return this.loader;
    }
    
    private File directory;
    private ClassLoader loader;
}

