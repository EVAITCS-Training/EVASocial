package com.horrorcore.utils;

import java.util.HashMap;

/**
 * A tiny runtime service registry used to wire dependencies manually in the
 * application. This is intentionally minimal — it stores objects by name and
 * returns them on demand. It's not thread-safe and intended only for a simple
 * console demo app.
 *
 * Usage:
 * - setService("name", instance) to register
 * - getService("name") to retrieve and cast the stored object
 */
public class DependencyContainer {
    private static DependencyContainer INSTANCE;
    private HashMap<String, Object> container = new HashMap<String, Object>();

//    static {
//        INSTANCE = new DependencyContainer();
//    }

    private DependencyContainer() {
    }

    public static DependencyContainer getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new DependencyContainer();
        }
        return INSTANCE;
    }
    /**
     * Retrieve a previously-registered service by name.
     * Returns null if the name is not registered.
     * Note: the caller is responsible for casting to the expected type.
     */
    public Object getService(String name) {
        return container.get(name);
    }

    /**
     * Register or replace a service instance for the given name.
     * This simple method overwrites any existing value for the same name.
     */
    public void setService(String name, Object object) {
        container.put(name, object);
    }
}
