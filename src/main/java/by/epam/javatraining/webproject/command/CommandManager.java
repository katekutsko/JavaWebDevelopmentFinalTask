package by.epam.javatraining.webproject.command;

public class CommandManager {

    private static String[] commandNames;

    static {
        CommandType[] commands = CommandType.values();
        commandNames = new String[commands.length];
        int i = 0;
        for (CommandType command : commands){
            commandNames[i++] = command.name();
        }
    }

    public static Command getCommand(String commandName){
        CommandType type = CommandType.NO_COMMAND;
        if (commandExists(commandName)){
            type = CommandType.valueOf(commandName.toUpperCase());
        }
        return type.getCommand();
    }

    private static boolean commandExists(String commandName){
        boolean exists = false;
        for (String name : commandNames){
            if (name.equalsIgnoreCase(commandName)){
                exists = true;
                break;
            }
        }
        return exists;
    }
}
