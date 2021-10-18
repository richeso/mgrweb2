package com.mapr.mgrweb.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RoleMap implements Serializable {

    private String id;
    private String role;

    public RoleMap() {}

    @JsonProperty("_id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("role")
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        StringBuilder rval = new StringBuilder();

        rval.append("{\"_id\":\"").append(id).append("\",\"role\":\"").append(role);
        return rval.toString();
    }
}
