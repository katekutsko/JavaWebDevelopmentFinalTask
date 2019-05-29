package by.epam.javatraining.webproject.customtag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;

public class SelectTag extends SimpleTagSupport {

    private String[] options;
    private String name;

    @Override
    public void doTag() throws JspException, IOException {

        JspWriter out = getJspContext().getOut();
        out.println("<select name=\"" + name +"\">");

        for (String option : options) {
            out.println("<option>" + option + "</option>");
        }

        out.println("</select>");
    }
}
