package com.abi.quotes.views.quote_detail;

import java.sql.SQLException;

import com.abi.quotes.views.MainLayout;
import database.*;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin;

import components.CommentList;
import components.DeleteButton;
import components.EditQuoteDialog;
import components.ErrorMessage;
import components.NotLoggedInScreen;
import components.QuoteBox;
import service.DataManager;

@PageTitle("Zitat")
@Route(value = "quote-detail", layout = MainLayout.class)
public class QuoteDetailView extends VerticalLayout {
	
	private Quote quote;
	private QuoteBox quoteBox;
	
	private H2 commentHeadline;
	
	private CommentList list;

    public QuoteDetailView() {
        setSpacing(false);
        setSizeFull();
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        getStyle().set("text-align", "center");
        
        Button backButton = new Button("Zurück zur Zitate-Übersicht");
        backButton.setIcon(VaadinIcon.ARROW_BACKWARD.create());
        backButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_SMALL);
        backButton.getStyle().setMarginBottom("0");
        this.setHorizontalComponentAlignment(Alignment.START, backButton);
        backButton.addClickListener(e -> {
			getUI().ifPresent(ui -> ui.navigate("zitate"));
		});
        add(backButton);
        
        if (DataManager.getLoggedIn() == null || !DataManager.getLoggedIn()) {
        	add(new NotLoggedInScreen());
        } else {
        	
        	try {
        		
        		Row[] rows = new TableReceiver().runQueryAndGet(""
    					+ "SELECT\r\n"
    					+ "	quotes_id,\r\n"
    					+ "	quotes_category,\r\n"
    					+ "	teachers_gender,\r\n"
    					+ "	teachers_name,\r\n"
    					+ " teachers_id, \r\n"
    					+ "	quotes_published,\r\n"
    					+ "	user_firstName,\r\n"
    					+ "	user_lastName,\r\n"
    					+ "	user_displayName,\r\n"
    					+ "	quotes_text,\r\n"
    					+ "	quotes_lastEdited,\r\n"
    					+ "	quotes_lastEditedBy,\r\n"
    					+ "	(SELECT COUNT(likes_id) FROM likes WHERE likes_quote = quotes_id) AS likes,\r\n"
    					+ "	(SELECT COUNT(dislikes_id) FROM dislikes WHERE dislikes_quote = quotes_id) AS dislikes,\r\n"
    					+ " (SELECT COUNT(comments_id) FROM comments WHERE comments_quote = quotes_id) AS comments,\r\n"
    					+ "	EXISTS(SELECT * FROM stars WHERE stars_user = '" + DataManager.getUserID() + "' AND stars_quote = quotes_id) AS isStar,\r\n"
    					+ " EXISTS(SELECT * FROM likes WHERE likes_quote = quotes_id AND likes_user = " + DataManager.getUserID() + ") AS isLiked,\r\n"
    					+ " EXISTS(SELECT * FROM dislikes WHERE dislikes_quote = quotes_id AND dislikes_user = " + DataManager.getUserID() + ") AS isDisliked \r\n"
    					+ "FROM quotes\r\n"
    					+ "JOIN teachers ON\r\n"
    					+ "	teachers_id = quotes_originator\r\n"
    					+ "JOIN user ON\r\n"
    					+ "	user_id = quotes_user"
    					+ " WHERE quotes_id = " + DataManager.getQuoteDetail()
    					).getRows();
        		
				quote = Quote.fromRow(rows[0]);
				
				quoteBox = new QuoteBox(quote);
				quoteBox.setCommentButtonVisible(false);
				add(quoteBox);
				
				HorizontalLayout managementButtons = new HorizontalLayout();
				
				
				if (DataManager.canEdit()) {
					Button editButton = new Button("Zitat bearbeiten");
					editButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ICON);
					editButton.setIcon(VaadinIcon.PENCIL.create());
					editButton.addClickListener(e -> {
						new EditQuoteDialog(quote).open();
					});
					managementButtons.add(editButton);
				}
				
				if (DataManager.canDelete()) {
					DeleteButton deleteButton = new DeleteButton(quote, false);
					managementButtons.add(deleteButton);
				}
				
				add(managementButtons);
				setHorizontalComponentAlignment(Alignment.END, managementButtons);

				createHeadline();
				
				list = new CommentList(quote, this);
				add(list);
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
        	
        }
        
    }
    
    private void createHeadline() {
    	HorizontalLayout layout = new HorizontalLayout();
    	layout.setWidthFull();
    	layout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
		layout.getStyle().set("margin-top", "20px");
    	
    	commentHeadline = new H2("Kommentare (" + quote.getComments() + ")");
		
		Button refreshButton = new Button(VaadinIcon.REFRESH.create());
		refreshButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_CONTRAST);
		refreshButton.addClickListener(e -> {
			list.updateList();
		});
		
		HorizontalLayout inner = new HorizontalLayout();
		inner.setDefaultVerticalComponentAlignment(Alignment.CENTER);
		inner.add(commentHeadline, refreshButton);
		
		layout.setJustifyContentMode(JustifyContentMode.EVENLY);
		layout.add(new Div(), inner, new Div(), new Div());
		
		add(layout);
    }
    
    public H2 getH2() {
    	return commentHeadline;
    }

}
