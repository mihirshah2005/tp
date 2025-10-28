package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_PERSONS_LISTED_OVERVIEW;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;
import static seedu.address.testutil.TypicalPersons.DANIEL;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.NameContainsTagPredicate;
import seedu.address.model.tag.Tag;

/**
 * Contains integration tests (interaction with the Model) for {@code FindByTagCommand}.
 */
public class FindByTagCommandTest {
    private final Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private final Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void equals() {
        NameContainsTagPredicate firstPredicate = new NameContainsTagPredicate(
                Collections.singleton(new Tag(VALID_TAG_FRIEND)));
        NameContainsTagPredicate secondPredicate = new NameContainsTagPredicate(
                Collections.singleton(new Tag(VALID_TAG_HUSBAND)));

        FindByTagCommand firstCommand = new FindByTagCommand(firstPredicate);
        FindByTagCommand secondCommand = new FindByTagCommand(secondPredicate);

        // same object -> returns true
        assertTrue(firstCommand.equals(firstCommand));

        // same values -> return true
        FindByTagCommand firstCommandCopy = new FindByTagCommand(firstPredicate);
        assertTrue(firstCommand.equals(firstCommandCopy));

        // different types -> returns false
        assertFalse(firstCommand.equals(1));
        assertFalse(firstCommand.equals(firstPredicate));
        assertFalse(firstCommand.equals(
                new FindCommand(new NameContainsKeywordsPredicate(List.of(VALID_TAG_FRIEND)))));

        // null -> returns false
        assertFalse(firstCommand.equals(null));

        // different predicate -> returns false
        assertFalse(firstCommand.equals(secondCommand));
    }

    @Test
    public void execute_tagNotInList_noPersonFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 0);
        NameContainsTagPredicate predicate = new NameContainsTagPredicate(
                Collections.singleton(new Tag("noonehasthistag")));
        FindByTagCommand command = new FindByTagCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.emptyList(), model.getProcessedPersonList());
    }

    @Test
    public void execute_tagInList_personsFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 3);
        NameContainsTagPredicate predicate = new NameContainsTagPredicate(
                Collections.singleton(new Tag("friends")));
        FindByTagCommand command = new FindByTagCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(ALICE, BENSON, DANIEL), model.getProcessedPersonList());
    }

    @Test
    public void toStringMethod() {
        NameContainsTagPredicate predicate = new NameContainsTagPredicate(
                Collections.singleton(new Tag(VALID_TAG_FRIEND)));
        FindByTagCommand command = new FindByTagCommand(predicate);
        String expected = FindByTagCommand.class.getCanonicalName() + "{predicate=" + predicate + "}";
        assertEquals(expected, command.toString());
    }
}
