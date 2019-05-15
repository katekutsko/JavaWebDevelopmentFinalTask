package by.epam.javatraining.webproject.command;

public enum CommandType {

    NO_COMMAND(new NoCommand()),
    REGISTER(new RegisterCommand()),
    LOGIN(new LoginCommand()),
    VIEW_PROFILE(new ViewProfileCommand()),
    VIEW_ALL_PATIENTS(new ViewAllPatientsCommand()),
    MAKE_PRESCRIPTION(new MakePrescriptionCommand()),
    DISCHARGE(new DischargeCommand()),
    SELECT_PATIENT(new SelectPatientCommand()),
    VIEW_HISTORY(new ViewPatientHistoryCommand()),
    ADD_RECORD(new AddCaseCommand()),
    EDIT_PROFILE(new EditProfileCommand()),
    LOGOUT(new LogOutCommand()),
    VIEW_DOCTORS(new ViewDoctorsCommand()),
    EDIT_CASE(new EditCaseCommand()),
    VIEW_ALL_USERS(new ViewAllUsersCommand()),
    DELETE_USER(new DeleteUserCommand()),
    VIEW_PRESCRIPTIONS(new ViewPrescriptionsCommand()),
    SEARCH(new SearchCommand());

    private Command command;

    private CommandType(Command command){
        this.command = command;
    }

    public Command getCommand() {
        return command;
    }
}
