package components;

import com.abi.quotes.views.teacher_detail.TeacherDetailView;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import database.Quote;
import database.User;
import service.DataManager;

public class QuoteBox extends VerticalLayout {

	private Quote quote;
	
	private RatingBar ratingBar;
	private Button teacherName, publisher;
	
	public QuoteBox(Quote quote) {
		this.quote = quote;
		
		addClassName("hover-effect");
		
		getStyle().set("box-shadow", "0px 8px 9px -2px rgba(0,0,0,0.42)");
		getStyle().set("padding", "16px 16px 8px 16px");
		getStyle().setMargin("5px 0px 5px 0px");
		setSpacing(false);
		
		add(createHeader());
		add(createMainPart());
		add(createRatingBar());
		
	}
	
	@SuppressWarnings("deprecation")
	private HorizontalLayout createHeader() {
		HorizontalLayout header = new HorizontalLayout();
		header.setWidthFull();
		
		
		teacherName = new Button();
		teacherName.setText(((quote.getTeachersGender().equals("m")) ? "Herr" : "Frau") + " " + quote.getTeachersName());
		teacherName.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
		teacherName.addClickListener(e -> {
			DataManager.setTeacherDetail(quote.getTeachersId());
			getUI().ifPresent(ui -> ui.navigate(TeacherDetailView.class));
		});
		
		VerticalLayout publication = new VerticalLayout();
		publication.setDefaultHorizontalComponentAlignment(Alignment.END);
		Span date = new Span(quote.getPublished().toLocaleString());
		date.getStyle().set("font-size", "10px");
		publisher = new Button(User.nameFormat(quote.getUserFirstName(), quote.getUserLastName(), quote.getUserDisplayName()));
		publisher.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
		publisher.getStyle().setColor("var(--lumo-body-text-color)");
		publication.setPadding(false);
		publication.setSpacing(false);
		publication.add(date, publisher);
		publication.setWidthFull();
		
		header.add(teacherName, publication);
		return header;
	}
	
	private VerticalLayout createMainPart() {
		VerticalLayout main = new VerticalLayout();
		main.setPadding(false);
		main.setWidthFull();
		main.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
		
		HorizontalLayout l = new HorizontalLayout();
		l.setPadding(false); l.setMargin(false); l.setSpacing(false);
		l.setJustifyContentMode(JustifyContentMode.CENTER);
		l.setWidthFull();
		
		Html text = new Html("<span>" + quote.getText() + "</span");
		text.getStyle().setWidth("85%");
		l.addClickListener(e -> {
        	DataManager.setQuoteDetail(quote.getId());
        	getUI().ifPresent(ui ->
        		ui.navigate("quote-detail"));
        });
		l.add(text);
		
		main.add(l);
		//main.add(text);
		
		return main;
	}
	
	private HorizontalLayout createRatingBar() {
		ratingBar = new RatingBar(quote);
		return (ratingBar);
	}

	public Quote getQuote() {
		return quote;
	}
	
	public void setCommentButtonVisible(boolean visible) {
		ratingBar.setCommentButtonVisible(visible);
	}
	
	public void setRatingBarSmall() {
		ratingBar.setSmall();
		teacherName.getStyle().setMargin("0px");
		publisher.getStyle().set("font-size", "12px");
	}
	
}
