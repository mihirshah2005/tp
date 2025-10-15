package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class EntryTypeTest {

    @Test
    public void enumValues_stableOrder() {
        // Guards against accidental reordering that could break persistence assumptions
        EntryType[] values = EntryType.values();
        assertEquals(EntryType.STUDENT, values[0]);
        assertEquals(EntryType.VOLUNTEER, values[1]);
    }

    @Test
    public void name_matchesExpected() {
        assertEquals("STUDENT", EntryType.STUDENT.name());
        assertEquals("VOLUNTEER", EntryType.VOLUNTEER.name());
    }
}
