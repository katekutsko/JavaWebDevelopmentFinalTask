package by.epam.javatraining.webproject.command;

import by.epam.javatraining.webproject.controller.ActionType;

import javax.servlet.http.HttpServletRequest;

public class NoCommand implements Command {
    @Override
    public String execute(HttpServletRequest request, ActionType type) {
        return null;
    }
}
