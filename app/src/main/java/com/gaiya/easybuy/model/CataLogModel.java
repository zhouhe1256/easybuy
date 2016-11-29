
package com.gaiya.easybuy.model;

import java.io.Serializable;

/**
 * Created by dengt on 15-10-9.
 */
public class CataLogModel implements Serializable {
    long id;
    String name;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        CataLogModel s = (CataLogModel) obj;
        return id == s.id;
    }

}
