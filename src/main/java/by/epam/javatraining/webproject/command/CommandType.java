package by.epam.javatraining.webproject.command;

public enum CommandType {

    NO_COMMAND(new NoCommand()),
    REGISTER(new RegisterCommand()),
    LOGIN(new LoginCommand()),
    VIEW_PROFILE(new ViewProfileCommand()),
    VIEW_ALL_PATIENTS(new ViewAllPatientsCommand()),
    MAKE_PRESCRIPTION(new MakePrescriptionCommand()),
    MAKE_APPOINTMENT(new MakeAppointmentCommand()),
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
    BLOCK_USER(new BlockUserCommand()),
    VIEW_PRESCRIPTIONS(new ViewPrescriptionsCommand()),
    VIEW_APPOINTMENTS(new ViewAppointmentsCommand()),
    CANCEL_APPOINTMENT(new CancelAppointmentCommand()),
    DELETE_CASE(new DeleteCaseCommand()),
    SEARCH(new SearchCommand());

    private Command command;

    private CommandType(Command command){
        this.command = command;
    }

    public Command getCommand() {
        return command;
    }
}
