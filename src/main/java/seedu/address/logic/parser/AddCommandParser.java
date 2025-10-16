package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.ArrayList;
import java.util.stream.Stream;

import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.EntryType;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Student;
import seedu.address.model.person.Volunteer;

/**
 * Parses input arguments and creates a new AddCommand object
 */
public class AddCommandParser implements Parser<AddCommand> {


    private final EntryType fixedType;
    private final String messageUsage;

    /**
     * Constructs a parser that always produces an {@link AddCommand} for the given type.
     *
     * @param fixedType the concrete entry type to create (must be {@link EntryType#STUDENT}
     *                  or {@link EntryType#VOLUNTEER})
     * @param messageUsage the usage string to surface on invalid input (e.g., the addstu/addvol usage text)
     */
    public AddCommandParser(EntryType fixedType, String messageUsage) {
        this.fixedType = java.util.Objects.requireNonNull(fixedType);
        this.messageUsage = java.util.Objects.requireNonNull(messageUsage);
    }

    /**
     * Parses the given {@code String} of arguments in the context of the AddCommand
     * and returns an AddCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS, PREFIX_TAG);

        if (!arePrefixesPresent(argMultimap, PREFIX_NAME)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, messageUsage));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS);
        Name name = ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get());

        Person.PersonBuild<?> builder;
        switch (fixedType) {
        case STUDENT:
            builder = new Student.StudentBuild(name);
            break;
        case VOLUNTEER:
            builder = new Volunteer.VolunteerBuild(name);
            break;
        default:
            // Should never happen since we removed PERSON
            throw new ParseException("Unsupported type: " + fixedType);
        }

        if (argMultimap.getValue(PREFIX_PHONE).isPresent()) { // phone
            builder.phone(ParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE).get()));
        }

        if (argMultimap.getValue(PREFIX_EMAIL).isPresent()) { // email
            builder.email(ParserUtil.parseEmail(argMultimap.getValue(PREFIX_EMAIL).get()));
        }

        if (argMultimap.getValue(PREFIX_ADDRESS).isPresent()) { // address
            builder.address(ParserUtil.parseAddress(argMultimap.getValue(PREFIX_ADDRESS).get()));
        }

        builder.tags(ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG))); // tagList
        builder.pairedPersons(new ArrayList<>());
        return new AddCommand(builder.build());
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

}
