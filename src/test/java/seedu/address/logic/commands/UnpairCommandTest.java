package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;

import java.util.ArrayList;
import java.util.Collections;

public class UnpairCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validIndexes_success() throws Exception {
        Index tutorIndex = INDEX_FIRST_PERSON;
        Index tuteeIndex = INDEX_SECOND_PERSON;
        UnpairCommand pairCommand = new UnpairCommand(tutorIndex, Collections.singletonList(tuteeIndex));

        Person tutor = model.getFilteredPersonList().get(tutorIndex.getZeroBased());
        Person tutee = model.getFilteredPersonList().get(tuteeIndex.getZeroBased());
        tutor.addPerson(tutee);
        String expectedMessage = String.format(UnpairCommand.MESSAGE_EDIT_PERSON_SUCCESS, tutor.getName().toString(),
                "{" + tutee.getName().toString() + "}");
        CommandResult commandResult = pairCommand.execute(model);
        assertEquals(expectedMessage, commandResult.getFeedbackToUser());
    }

    @Test
    public void execute_invalidIndex_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        ArrayList<Index> indexes = new ArrayList<>();
        indexes.add(INDEX_FIRST_PERSON);
        UnpairCommand pairCommand = new UnpairCommand(outOfBoundIndex, indexes);

        assertThrows(CommandException.class, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX, () ->
                pairCommand.execute(model));
    }

    @Test
    public void equals() {
        UnpairCommand pairFirstCommand = new UnpairCommand(INDEX_FIRST_PERSON, Collections.singletonList(INDEX_SECOND_PERSON));
        UnpairCommand pairSecondCommand = new UnpairCommand(INDEX_SECOND_PERSON, Collections.singletonList(INDEX_FIRST_PERSON));

        // same object -> returns true
        assertTrue(pairFirstCommand.equals(pairFirstCommand));

        // same values -> returns true
        UnpairCommand pairFirstCommandCopy = new UnpairCommand(INDEX_FIRST_PERSON, Collections.singletonList(INDEX_SECOND_PERSON));
        assertTrue(pairFirstCommand.equals(pairFirstCommandCopy));

        // different types -> returns false
        assertFalse(pairFirstCommand.equals(1));

        // null -> returns false
        assertFalse(pairFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(pairFirstCommand.equals(pairSecondCommand));
    }
}
