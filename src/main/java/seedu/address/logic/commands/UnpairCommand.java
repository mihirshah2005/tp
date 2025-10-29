package seedu.address.logic.commands;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.logic.Messages.INDEX_PERSON_LIST_TO_STRING_CONVERTER;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javafx.util.Pair;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Changes the remark of an existing person in the address book.
 */
public class UnpairCommand extends Command {

    public static final String COMMAND_WORD = "unpair";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Unpairs person identified "
            + "by the index number used in the displayed person list "
            + "to other persons by the index number used in the displayed person list\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "<INDEXES>\n"
            + "Example: " + COMMAND_WORD + " 1 3 4 5 ";

    public static final String MESSAGE_EDIT_PERSON_SUCCESS = "Unpaired: %s from %s";
    public static final String MESSAGE_PAIRING_DOES_NOT_EXIST_YET = "%d: %s is not paired to these person(s) "
            + "in the address book yet: {%s}";

    private final Index index;
    private final List<Index> indicesToUnpair;

    /**
     * @param index           of the person in the filtered person list
     * @param indicesToUnpair of the person(s) in the filtered person list to pair
     *                        them to
     */
    public UnpairCommand(Index index, List<Index> indicesToUnpair) {
        requireAllNonNull(index, indicesToUnpair);

        this.index = index;
        this.indicesToUnpair = indicesToUnpair;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_FIRST_PERSON_DISPLAYED_INDEX);
        }

        Person person = lastShownList.get(index.getZeroBased());

        Set<Person> personsToUnpair = new HashSet<>();
        Set<Index> invalidIndices = new HashSet<>();
        Set<Pair<Index, Person>> personsNotYetPaired = new HashSet<>();
        for (Index indexToUnpair : indicesToUnpair) {
            if (indexToUnpair.getZeroBased() >= lastShownList.size()) {
                invalidIndices.add(indexToUnpair);
                continue;
            }
            Person personToUnpair = lastShownList.get(indexToUnpair.getZeroBased());
            if (personsToUnpair.contains(personToUnpair)) {
                continue; // ignore duplicates first. Warning message will be added later in code.
            }
            if (!model.getAddressBook().isPaired(person, personToUnpair)) {
                personsNotYetPaired.add(new Pair<>(indexToUnpair, personToUnpair));
                continue;
            }

            personsToUnpair.add(personToUnpair);
        }

        StringBuilder successMessage = new StringBuilder();
        ArrayList<String> errorMessages = new ArrayList<>();

        Set<Integer> uniqueIndices = new HashSet<>(indicesToUnpair.stream().map(Index::getZeroBased)
                .collect(Collectors.toList()));
        if (uniqueIndices.size() != indicesToUnpair.size()) {
            successMessage.append(Messages.MESSAGE_DUPLICATE_INDEX).append("\n");
            errorMessages.add(Messages.MESSAGE_DUPLICATE_INDEX);
        }

        boolean isError = false;
        if (!invalidIndices.isEmpty()) {
            errorMessages.add(String.format(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDICES,
                    invalidIndices
                            .stream()
                            .map(index -> String.valueOf(index.getOneBased()))
                            .collect(Collectors.joining(", "))));
            isError = true;
        }

        if (!personsNotYetPaired.isEmpty()) {
            errorMessages.add(String.format(MESSAGE_PAIRING_DOES_NOT_EXIST_YET, index.getOneBased(), person.getName(),
                    INDEX_PERSON_LIST_TO_STRING_CONVERTER.apply(personsNotYetPaired)));
            isError = true;
        }

        if (isError) {
            throw new CommandException(String.join("\n", errorMessages));
        }

        for (Person personToUnpair : personsToUnpair) {
            model.unpair(person, personToUnpair);
            model.setPerson(personToUnpair, personToUnpair); // update GUI
        }

        model.setPerson(person, person); // update GUI
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        successMessage.append(String.format(MESSAGE_EDIT_PERSON_SUCCESS, person.getName().toString(),
                "{" + uniqueIndices.stream().map(
                                index -> lastShownList.get(index).getName().toString())
                        .collect(Collectors.joining(", ")) + "}"));

        return new CommandResult(successMessage.toString());
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof UnpairCommand)) {
            return false;
        }

        // state check
        UnpairCommand e = (UnpairCommand) other;
        return index.equals(e.index)
                && indicesToUnpair.equals(e.indicesToUnpair);
    }
}
