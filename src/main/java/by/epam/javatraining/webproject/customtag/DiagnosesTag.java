package by.epam.javatraining.webproject.customtag;

import by.epam.javatraining.webproject.model.entity.Diagnosis;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class DiagnosesTag extends SimpleTagSupport {

    private int ordinal;
    private Diagnosis[] diagnoses;

    {
       diagnoses = Diagnosis.values();
    }

    public int getOrdinal() {
        return ordinal;
    }

    public void setOrdinal(int ordinal) {
        this.ordinal = ordinal;
    }

    @Override
    public void doTag() throws JspException, IOException {

        JspWriter out = getJspContext().getOut();
        out.println("<select name=\"final_diagnosis\">");

        String selected;
        for (Diagnosis diagnosis : diagnoses) {
            selected = "";
            if (ordinal == diagnosis.ordinal()) {
                selected = "selected=\"selected\"";
            }
            out.println("<option " + selected + ">" + diagnosis + "</option>");
        }
    }
}
