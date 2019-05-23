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
    private String name;
    private int cardId;

    private static final String NEXT_ARROW = ">>";
    private static final String PREVIOUS_ARROW = "<<";
    private static final String INDENT = "margin-right: 80px;";
    private static final String FLOAT_RIGHT = "right";
    private static final String FLOAT_LEFT = "left";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCardId() {
        return cardId;
    }

    public void setCardId(int cardId) {
        this.cardId = cardId;
    }

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
            createArrow(out, INDENT, FLOAT_RIGHT, NEXT_ARROW, newFrom);
        }
        if (isPrevious()) {
            int newFrom = from - size;
            createArrow(out, "", FLOAT_LEFT, PREVIOUS_ARROW, newFrom);
        }
    }

    private void createArrow(JspWriter out, String indent, String floatSide, String arrow, int newFrom) throws IOException {
        out.println("<a style=\"float: " + floatSide + "; margin-top: 20px;" + indent + "\" href=\"Hospital?command=" + command + "&from=" + newFrom);
        if (name != null && cardId != 0) {
            out.print(" name=" + name + " card_id=" + cardId);
        }
        out.print("\">");
        out.println(arrow + "</a>");
    }
}
