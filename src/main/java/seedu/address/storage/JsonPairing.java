package seedu.address.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

class JsonPairing {
    // keep names i/j to match the JSON you showed
    public final int i;
    public final int j;

    @JsonCreator
    JsonPairing(@JsonProperty("i") Integer i,
             @JsonProperty("j") Integer j) {
        // defensive defaults if someone writes nulls
        this.i = i == null ? -1 : i;
        this.j = j == null ? -1 : j;
    }
}
