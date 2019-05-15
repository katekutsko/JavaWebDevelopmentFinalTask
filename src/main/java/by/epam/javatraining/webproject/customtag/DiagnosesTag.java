package by.epam.javatraining.webproject.customtag;

import by.epam.javatraining.webproject.model.entity.Diagnosis;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;

public class DiagnosesTag extends SimpleTagSupport {

    private String[] diagnoses;

    {
        Diagnosis[] values = Diagnosis.values();
        diagnoses = new String[values.length];
        int i = 0;
        for (Diagnosis diagnosis : values){
            diagnoses[i] = diagnosis.name();
            i++;
        }
    }

    @Override
    public void doTag() throws JspException, IOException {

        JspWriter out = getJspContext().getOut();
        out.println("<select name=\"final_diagnosis\">");

        for (String diagnosis : diagnoses) {
            out.println("<option>" + diagnosis + "</option>");
        }

        out.println("</select>");
    }
}
