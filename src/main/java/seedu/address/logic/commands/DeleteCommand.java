package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.commons.util.CommandUtil;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Caregiver;
import seedu.address.model.person.Senior;

/**
 * Deletes a person identified using it's displayed index from the address book.
 */
public class DeleteCommand extends Command {

    public static final String COMMAND_WORD = "delete";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the caregiver/senior identified "
            + "by the index number used"
            + " in the displayed caregiver/senior list.\n"
            + "Parameters: s/INDEX or c/INDEX "
            + "(must be a positive integer)\n"
            + "Examples:\n"
            + COMMAND_WORD + " c/1\n"
            + COMMAND_WORD + " c/2 s/1";

    public static final String MESSAGE_DELETE_PERSON_SUCCESS = "Deleted Person: %1$s";
    public static final String MESSAGE_INVALID_SENIOR_INDEX = "No such senior index exists.";
    public static final String MESSAGE_INVALID_CAREGIVER_INDEX = "No such caregiver index exists.";
    public static final String MESSAGE_NO_PERSONS = "No such senior and caregiver exist.";

    private final Integer seniorIndex;
    private final Integer caregiverIndex;

    /**
     * Creates a {@code DeleteCommand} deleting a specific person by id.
     * <p>
     * The command will delete exactly the person whose id matches {@code seniorIndex} and {@code caregiverIndex}.
     *
     * @param seniorIndex the id of the senior to pin;
     * @param caregiverIndex the id of the caregiver to pin;
     */
    public DeleteCommand(Integer seniorIndex, Integer caregiverIndex) {
        this.seniorIndex = seniorIndex;
        this.caregiverIndex = caregiverIndex;

        assert (seniorIndex == null || seniorIndex >= 0)
                : "Senior index must be non-negative or null";
        assert (caregiverIndex == null || caregiverIndex >= 0)
                : "Caregiver index must be non-negative or null";
        assert (seniorIndex != null || caregiverIndex != null)
                : "At least one of seniorIndex or caregiverIndex should be provided";
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        assert model.getAllSeniorList() != null
                : "Senior list in model should not be null";
        assert model.getAllCaregiverList() != null
                : "Caregiver list in model should not be null";

        List<Senior> fullSeniorList = model.getAllSeniorList();
        List<Caregiver> fullCaregiverList = model.getAllCaregiverList();

        // Find senior by seniorId
        Senior senior = CommandUtil.validateOptionalSeniorId(
                fullSeniorList, seniorIndex, MESSAGE_INVALID_SENIOR_INDEX);

        // Find caregiver by caregiverId
        Caregiver caregiver = CommandUtil.validateOptionalCaregiverId(
                fullCaregiverList, caregiverIndex, MESSAGE_INVALID_CAREGIVER_INDEX);

        assert (senior == null || fullSeniorList.contains(senior))
                : "Senior found must exist in the model senior list";
        assert (caregiver == null || fullCaregiverList.contains(caregiver))
                : "Caregiver found must exist in the model caregiver list";

        // If deleting a caregiver, clear references from seniors
        if (senior != null) {
            model.deleteSenior(senior);
        }
        if (caregiver != null) {
            model.getAddressBook().getSeniorList().stream()
                    .filter(s -> s.getCaregiver() != null && s.getCaregiver().isSamePerson(caregiver))
                    .forEach(s -> s.setCaregiver(null));
            model.deleteCaregiver(caregiver);
        }

        if (senior != null && caregiver == null) {
            return new CommandResult(String.format(MESSAGE_DELETE_PERSON_SUCCESS, Messages.formatSenior(senior)));
        } else if (senior == null && caregiver != null) {
            return new CommandResult(String.format(MESSAGE_DELETE_PERSON_SUCCESS, Messages.formatCaregiver(caregiver)));
        } else if (senior != null & caregiver != null) {
            return new CommandResult(
                    String.format(MESSAGE_DELETE_PERSON_SUCCESS, Messages.formatSenior(senior)) + " and "
                            + String.format(MESSAGE_DELETE_PERSON_SUCCESS, Messages.formatCaregiver(caregiver)));
        } else {
            throw new CommandException(MESSAGE_NO_PERSONS);
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DeleteCommand)) {
            return false;
        }

        DeleteCommand otherDeleteCommand = (DeleteCommand) other;
        return seniorIndex.equals(otherDeleteCommand.seniorIndex)
                && caregiverIndex.equals(otherDeleteCommand.caregiverIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("seniorIndex", seniorIndex)
                .add("caregiverIndex", caregiverIndex)
                .toString();
    }
}
