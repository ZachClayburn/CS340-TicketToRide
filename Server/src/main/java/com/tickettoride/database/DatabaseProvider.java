package com.tickettoride.database;

import com.tickettoride.database.interfaces.IDatabase;
import exceptions.DatabaseException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

public class DatabaseProvider {

    protected static Class<? extends IDatabase> databaseFactory = null;
    private static ClassLoader classLoader = null;

    public static void intiDatabasePlugin(String pluginJarName) throws DatabaseException {

        if (!pluginJarName.endsWith(".jar"))
            throw new DatabaseException(pluginJarName + " is not a path to a valid jar file!");

        var jarFile = new File(pluginJarName);
        if (!jarFile.isFile() || !jarFile.canRead())
            throw new DatabaseException(pluginJarName + " does not exist or cannot be read!");

        processJarFile(jarFile);
    }

    private static void processJarFile(File jarFile) throws DatabaseException {
        try (var jarReader = new JarInputStream(new FileInputStream(jarFile))) {
            classLoader = new URLClassLoader(new URL[] {jarFile.toURI().toURL()}, DatabaseProvider.class.getClassLoader());

            JarEntry entry = jarReader.getNextJarEntry();
            while (entry != null){
                processJarEntry(entry);
                entry = jarReader.getNextJarEntry();
            }
            if (databaseFactory == null) {
                throw new DatabaseException("No class implementing IDatabase found");
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new DatabaseException(e);
        }
    }

    private static void processJarEntry(JarEntry entry) throws ClassNotFoundException, DatabaseException {
        String className = entry.getRealName();
        if (className.endsWith(".class")){
            className = className.
                    substring(0, className.lastIndexOf("."))
                    . replaceAll("/", "\\.");

            Class<?> clazz = classLoader.loadClass(className);

            if (IDatabase.class.isAssignableFrom(clazz)) {
                if (databaseFactory == null)
                    databaseFactory = clazz.asSubclass(IDatabase.class);
                else
                    throw new DatabaseException("Multiple Classes implementing IDatabase found");
            }
        }
    }

    public static IDatabase getDatabase() throws DatabaseException {
        if (databaseFactory == null)
            throw new DatabaseException("DatabaseProvider has not been initialized! Call DatabaseProvider.initDatabasePlugin(String) first!");
        try {
            return databaseFactory.getConstructor().newInstance();
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new DatabaseException("Could not construct the Database!", e);
        }
    }
}
