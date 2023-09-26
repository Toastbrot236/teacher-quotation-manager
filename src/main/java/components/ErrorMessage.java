package components;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.html.Paragraph;

public class ErrorMessage extends Div {
	
	public ErrorMessage(String title, String description) {
		addClassName("error-message");
		
		H5 heading = new H5(title);
		heading.addClassName("error-title");
		add(heading);
		
		Paragraph text = new Paragraph(description);
		text.addClassName("error-text");
		add(text);
	}

}
