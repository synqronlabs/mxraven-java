package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

/**
 * One synthesized header in a simulation fixture.
 *
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class SimulationHeader {
    private final String name;
    private final String value;

    /** header name */
    public String name() {
        return name;
    }

    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SimulationHeader that = (SimulationHeader) o;
        return Objects.equals(this.name, that.name) && Objects.equals(this.value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.value);
    }

    @Override
    public String toString() {
        return "SimulationHeader[" + "name=" + this.name + ", " + "value=" + this.value + "]";
    }

    /**
     * Creates a new SimulationHeader.
     *
     * @param name header name
     * @param value value
     */
    @JsonCreator
    public SimulationHeader(String name, String value) {
        this.name = name;
        this.value = value;
    }
}
