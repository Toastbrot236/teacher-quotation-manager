package components;

import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import database.Comment;
import database.User;
import service.DataManager;

public class CommentBox extends VerticalLayout{
	
	public CommentBox(Comment comment, CommentList list) {
		setSpacing(false);
		
		HorizontalLayout headline = new HorizontalLayout();
		Span user = new Span(User.nameFormat(
				comment.getUserFirstName(), 
				comment.getUserLastName(),
				comment.getUserDisplayName()
				));
		user.getStyle().set("font-weight", "bold");
		user.getStyle().set("font-size", "var(--lumo-font-size-s)");
		Span time = new Span(comment.getPublished().toLocaleString());
		time.getStyle().set("font-size", "var(--lumo-font-size-xxs)");
		
		DeleteButton delete = new DeleteButton(comment, true);
		delete.removeThemeVariants(ButtonVariant.LUMO_TERTIARY);
		delete.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
		delete.getStyle().set("transform", "translateY(-4px)");
		delete.addDeletionListener(() -> list.updateList());
		
		headline.add(user, time);
				
		if (DataManager.canDelete())
			headline.add(delete);
		
		add(headline);
		

		add(new Span(comment.getText()));
	}

}
