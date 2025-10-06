package seedu.address.logic.commands;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.List;
import java.util.stream.Collectors;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Changes the remark of an existing person in the address book.
 */
public class PairCommand extends Command {

    public static final String COMMAND_WORD = "pair";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Pairs person identified "
            + "by the index number used in the displayed person list. "
            + "to other persons by the index number used in the displayed person list\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "<INDEXES>\n"
            + "Example: " + COMMAND_WORD + " 1 3 4 5 ";

    public static final String MESSAGE_EDIT_PERSON_SUCCESS = "Paired: %s to %s";
    //public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_PERSON = "This pairing already exists in the address book.";

    private final Index index;
    private final List<Index> pairedIndices;

    /**
     * @param index of the person in the filtered person list
     * @param pairedIndices of the person(s) in the filtered person list to pair them to
     */
    public PairCommand(Index index, List<Index> pairedIndices) {
        requireAllNonNull(index, pairedIndices);

        this.index = index;
        this.pairedIndices = pairedIndices;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person person = lastShownList.get(index.getZeroBased());
        for (Index pairedIndex : pairedIndices) {
            if (pairedIndex.getZeroBased() >= lastShownList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
            }
            Person person2 = lastShownList.get(pairedIndex.getZeroBased());
            try {
                person.addPerson(person2);
            } catch (IllegalValueException e) {
                throw new CommandException(MESSAGE_DUPLICATE_PERSON);
            }
        }

        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        return new CommandResult(String.format(MESSAGE_EDIT_PERSON_SUCCESS, person.getName().toString(),
                "{" + pairedIndices.stream().map(
                        index -> lastShownList.get(index.getZeroBased()).getName().toString()
                ).collect(Collectors.joining(", ")) + "}"));
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof PairCommand)) {
            return false;
        }

        // state check
        PairCommand e = (PairCommand) other;
        return index.equals(e.index)
                && pairedIndices.equals(e.pairedIndices);
    }
}