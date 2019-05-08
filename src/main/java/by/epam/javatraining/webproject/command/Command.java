package by.epam.javatraining.webproject.command;

import by.epam.javatraining.webproject.controller.ActionType;

import javax.servlet.http.HttpServletRequest;

public interface Command {
    String execute(HttpServletRequest request, ActionType type);
}
