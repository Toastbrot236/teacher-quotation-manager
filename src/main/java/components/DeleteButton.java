package components;

import java.sql.SQLException;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.theme.lumo.LumoIcon;

import database.Comment;
import database.Quote;
import database.TableReceiver;
import service.Toolkit;

public class DeleteButton extends Button {
	
	private DeletionListener deletionListener;

	protected DeleteButton(String buttonText, String dialogHeadline, String dialogText, String SQLQuery, boolean compactDesign) {
		
		setIcon(VaadinIcon.TRASH.create());
		addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_TERTIARY);
		
		if(!compactDesign) {
			setText(buttonText);
		}
		
		ConfirmDialog dialog = new ConfirmDialog();
		dialog.setHeader(dialogHeadline);
		dialog.setText(dialogText);
		
		dialog.setCancelable(true);
		dialog.addCancelListener(e -> dialog.close());
		
		dialog.setConfirmText("Löschen");
		dialog.setConfirmButtonTheme("error primary");
		dialog.addConfirmListener(e -> {
			try {
				new TableReceiver().runUpdate(SQLQuery);
				onDelete();
				dialog.close();
					} catch (SQLException exception) {
						exception.printStackTrace();
					}	
		});	
		dialog.setCancelText("Abbrechen");
	    		
		addClickListener(e -> dialog.open());
	}
	
	public DeleteButton(Quote quote, boolean compactDesign) {
		this(
				"Zitat löschen",
				"Zitat löschen?",
				"Bist du dir sicher, dass du das Zitat von " 
						+ Toolkit.formatTeacherName(quote.getTeachersGender(), quote.getTeachersName())
						+ " löschen möchtest?",
				"DELETE FROM quotes WHERE quotes_id = " + quote.getId(),
				compactDesign
				);
	}
	
	public DeleteButton(Comment comment, boolean compactDesign) {
		this(
				"Löschen",
				"Kommentar löschen?",
				"Bist du dir sicher, dass du den Kommentar von " 
						+ comment.getUserFirstName() + " " + comment.getUserLastName()
						+ " löschen möchtest?",
				"DELETE FROM comments WHERE comments_id = " + comment.getCommentId(),
				compactDesign
				);
	}
	
	public void setCompact(boolean compact) {
		if(compact)
			this.setText(null);
		else
			setText("Löschen");
	}
	
	public interface DeletionListener {
		public void onDelete();
	}
	
	public void addDeletionListener(DeletionListener deletionListener) {
		this.deletionListener = deletionListener;
	}
	
	private void onDelete() {
		if (deletionListener != null)
			deletionListener.onDelete();
	}

}
