package by.epam.javatraining.webproject.command;

public class CommandManager {

    public static Command getCommand(CommandType type){

        return type.getCommand();
    }
}
