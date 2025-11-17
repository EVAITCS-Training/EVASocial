package com.horrorcore.utils;

import java.util.HashMap;

public class DependencyContainer {
    private HashMap<String, Object> container = new HashMap<>();

    public Object getService(String name) {
        return container.get(name);
    }

    public void setService(String name, Object object) {
        container.put(name, object);
    }
}
