package by.epam.javatraining.webproject.command;

import by.epam.javatraining.webproject.controller.ActionType;
import by.epam.javatraining.webproject.util.Pages;

import javax.servlet.http.HttpServletRequest;

public class NoCommand implements Command {
    @Override
    public String execute(HttpServletRequest request, ActionType type) {
        return Pages.ERROR_PAGE;
    }
}
