package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.person.NameContainsTagPredicate;

/**
 * Finds and lists all the persons who are tagged with the given tag.
 * Tag searching is case insensitive.
 */
public class FindByTagCommand extends Command {

    public static final String COMMAND_WORD = "findtag";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all persons who are tagged with"
            + "the specified tag (case-insensitive) and displays them as a list with index numbers.\n"
            + "Parameters: TAG_NAME\n"
            + "Example: " + COMMAND_WORD + " mathematics\n"
            + "Note that only one tag can be provided.";

    private final NameContainsTagPredicate predicate;

    /**
     * Creates a FindByTagCommand to search for {@code Person}s that fulfil the given
     * {@code NameContainsTagPredicate}.
     * @param predicate Tests if a given {@code Person} is tagged with the given tag.
     * @throws NullPointerException If {@code predicate} is null.
     */
    public FindByTagCommand(NameContainsTagPredicate predicate) {
        requireNonNull(predicate);
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredPersonList(predicate);
        return new CommandResult(
                String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW, model.getFilteredPersonList().size()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof FindByTagCommand)) {
            return false;
        }

        FindByTagCommand otherFindByTagCommand = (FindByTagCommand) other;
        return predicate.equals(otherFindByTagCommand.predicate);
    }

    @Override
    public int hashCode() {
        return predicate.hashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("predicate", predicate)
                .toString();
    }
}
