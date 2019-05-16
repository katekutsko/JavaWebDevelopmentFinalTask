package by.epam.javatraining.webproject.customtag;

import org.apache.log4j.Logger;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;

public class ChangePagesTag extends SimpleTagSupport {

    private static Logger logger;

    static {
        logger = Logger.getRootLogger();
    }

    private int from;
    private int size;
    private int amount;
    private String command;

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        logger.debug("from = " + from);
        this.from = from;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        logger.debug("size = " + size);
        this.size = size;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        logger.debug("amount = " + amount);
        this.amount = amount;
    }

    private boolean isNext() {
        return (from + size < amount);
    }

    private boolean isPrevious() {
        return (from - size >= 0);
    }

    @Override
    public void doTag() throws JspException, IOException {

        JspWriter out = getJspContext().getOut();
        if (isNext()) {
            int newFrom = from + size;
            out.println("<a style=\"float: right; margin-top: 20px; margin-right: 80px;\" href=\"Hospital?command=" + command + "&from="+ newFrom +"\">");
            out.println(">></a>");
        }
        if (isPrevious()) {
            int newFrom = from - size;
            out.println("<a style=\"float: left; margin-top: 20px;\" href=\"Hospital?command=" + command + "&from="+ newFrom +"\">");
            out.println("<<</a>");
        }
    }
}
