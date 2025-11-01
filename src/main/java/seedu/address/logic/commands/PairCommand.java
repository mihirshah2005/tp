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
public class PairCommand extends Command {

    public static final String COMMAND_WORD = "pair";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Pairs person identified "
            + "by the index number used in the displayed person list "
            + "to other persons by the index number used in the displayed person list\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "<INDEXES>\n"
            + "Example: " + COMMAND_WORD + " 1 3 4 5 ";

    public static final String MESSAGE_EDIT_PERSON_SUCCESS = "Paired: %s to %s";
    public static final String MESSAGE_PAIRING_ALREADY_EXISTS = "%d: %s is already paired to these person(s) "
            + "in the address book: {%s}";
    public static final String MESSAGE_SAME_CLASS = "%d: %s and {%s} are all %4$ss. %4$ss cannot be paired to %4$ss.";

    private final Index index;
    private final List<Index> indicesToPair;

    /**
     * @param index         of the person in the filtered person list
     * @param indicesToPair of the person(s) in the filtered person list to pair
     *                      them to
     */
    public PairCommand(Index index, List<Index> indicesToPair) {
        requireAllNonNull(index, indicesToPair);

        this.index = index;
        this.indicesToPair = indicesToPair;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        List<Person> lastShownList = model.getProcessedPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_FIRST_PERSON_DISPLAYED_INDEX);
        }

        Person person = lastShownList.get(index.getZeroBased());

        Set<Person> personsToPair = new HashSet<>();
        Set<Index> invalidIndices = new HashSet<>();
        Set<Pair<Index, Person>> personsAlreadyPaired = new HashSet<>();
        Set<Pair<Index, Person>> personsSameClass = new HashSet<>();
        for (Index indexToPair : indicesToPair) {
            if (indexToPair.getZeroBased() >= lastShownList.size()) {
                invalidIndices.add(indexToPair);
                continue;
            }
            Person personToPair = lastShownList.get(indexToPair.getZeroBased());
            if (personsToPair.contains(personToPair)) {
                continue; // ignore duplicates first. Warning message will be added later in code.
            }
            if (model.getAddressBook().isPaired(person, personToPair)) {
                personsAlreadyPaired.add(new Pair<>(indexToPair, personToPair));
                continue;
            }

            if (!person.getType().equals(personToPair.getType())) {
                personsToPair.add(personToPair);
            } else {
                personsSameClass.add(new Pair<>(indexToPair, personToPair));
            }
        }

        StringBuilder successMessage = new StringBuilder();
        ArrayList<String> errorMessages = new ArrayList<>();

        Set<Integer> uniqueIndices = new HashSet<>(indicesToPair.stream().map(Index::getZeroBased)
                .collect(Collectors.toList()));
        if (uniqueIndices.size() != indicesToPair.size()) {
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
        if (!personsSameClass.isEmpty()) {
            errorMessages.add(String.format(MESSAGE_SAME_CLASS, index.getOneBased(), person.getName(),
                    INDEX_PERSON_LIST_TO_STRING_CONVERTER.apply(personsSameClass),
                    person.getType()));
            isError = true;
        }

        if (!personsAlreadyPaired.isEmpty()) {
            errorMessages.add(String.format(MESSAGE_PAIRING_ALREADY_EXISTS, index.getOneBased(), person.getName(),
                    INDEX_PERSON_LIST_TO_STRING_CONVERTER.apply(personsAlreadyPaired)));
            isError = true;
        }

        if (isError) {
            throw new CommandException(String.join("\n", errorMessages));
        }


        for (Person personToPair : personsToPair) {
            model.pair(person, personToPair);
            model.setPerson(personToPair, personToPair); // update GUI
        }

        model.setPerson(person, person); // update GUI
        model.filterPersonList(PREDICATE_SHOW_ALL_PERSONS);

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
        if (!(other instanceof PairCommand)) {
            return false;
        }

        // state check
        PairCommand e = (PairCommand) other;
        return index.equals(e.index) && indicesToPair.equals(e.indicesToPair);
    }
}
