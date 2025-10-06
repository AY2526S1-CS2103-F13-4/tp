package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NOTE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Senior;

/**
 * Adds a Senior to the address book.
 * Command word: {@code add-snr}
 * Format: {@code add-snr n/NAME t/RISK_TAG p/PHONE a/ADDRESS [n/NOTES] [c/CAREGIVER_ID]}
 */
public class AddSeniorCommand extends Command {

    public static final String COMMAND_WORD = "add-snr";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a senior to the address book. "
            + "Parameters: "
            + PREFIX_NAME + "NAME "
            + PREFIX_TAG + "RISK_TAG "
            + PREFIX_PHONE + "PHONE "
            + PREFIX_ADDRESS + "ADDRESS "
            + "[" + PREFIX_NOTE + "NOTES] "
            + "[" + PREFIX_CID + "CAREGIVER_ID]\n"
            + "Valid risk tags: High Risk, Medium Risk, Low Risk\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NAME + "Lim Ah Kow "
            + PREFIX_TAG + "High Risk "
            + PREFIX_PHONE + "91234567  "
            + PREFIX_ADDRESS + "Blk 123 Bedok North Rd #02-45 "
            + PREFIX_NOTE + "Has dementia "
            + PREFIX_CID + "201";

    public static final String MESSAGE_SUCCESS = "New senior added: %1$s";
    public static final String MESSAGE_DUPLICATE_SENIOR = "Senior already exists. Please amend your entry.";
    public static final String MESSAGE_INVALID_TAG = "Invalid risk tag. Risk tag must either be `High Risk`, `Medium Risk`, or `Low Risk`.";


    private final Senior toAdd;

    /**
     * Creates an AddSeniorCommand to add the specified {@code Senior}.
     */
    public AddSeniorCommand(Senior senior) {
        requireNonNull(senior);
        toAdd = senior;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (model.hasPerson(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_SENIOR);
        }

        model.addPerson(toAdd);
        return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.formatSenior(toAdd)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddSeniorCommand)) {
            return false;
        }

        AddSeniorCommand otherAddSeniorCommand = (AddSeniorCommand) other;
        return toAdd.equals(otherAddSeniorCommand.toAdd);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("toAdd", toAdd)
                .toString();
    }

}
