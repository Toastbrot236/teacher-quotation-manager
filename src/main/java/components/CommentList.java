package components;

import java.sql.SQLException;
import java.util.Arrays;

import com.abi.quotes.views.quote_detail.QuoteDetailView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.messages.MessageInput;
import com.vaadin.flow.component.messages.MessageInputI18n;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.virtuallist.VirtualList;
import com.vaadin.flow.data.renderer.ComponentRenderer;

import database.Comment;
import database.Quote;
import database.Row;
import database.TableReceiver;
import service.DataManager;
import service.WordBlacklist;

public class CommentList extends VerticalLayout{
	
	private QuoteDetailView view;
	
	private Quote quote;
	
	private MessageInput input;
	private VirtualList<Comment> list;
	
	public CommentList(Quote quote, QuoteDetailView view) {
		this.view = view;
		
		this.quote = quote;
		setPadding(false);
		
		input = new MessageInput();
		MessageInputI18n i18n = new MessageInputI18n();
		i18n.setMessage("Kommentar");
		i18n.setSend("Senden");
		input.setI18n(i18n);
		input.setWidth("100%");
		input.setMaxWidth("600px");
		add(input);
		
		input.addSubmitListener(e -> {
			createComment(e.getValue(), quote.getId());
		});
		
		updateList();
	}
	
	private void createComment(String text, int quote) {
		try {
			for (String word : WordBlacklist.getBlacklist()) {
				text = text.replace(word, word.charAt(0) + new String(new char[word.length()-2]).replace("\0", "*") + word.charAt(word.length()-1));
			}
			
			new TableReceiver().runUpdate(
					String.format(
							"INSERT INTO comments (comments_text, comments_published, comments_author, comments_quote) VALUES \r\n"
									+ "	(\"%s\", now(), %d, %d)",
							text, DataManager.getUserID(), quote
							)
					);
			updateList();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void updateList() {
		Row[] rows;
		try {
			try {
				remove(list);
			} catch (Exception e) {} //TODO Schlechter Stil
			
			rows = new TableReceiver().runQueryAndGet(
					"SELECT comments_id, comments_text, comments_published, comments_author, user_firstName, user_lastName, user_displayName\r\n"
					+ "	FROM comments\r\n"
					+ "    JOIN user\r\n"
					+ "		ON comments_author = user_id\r\n"
					+ "	WHERE comments_quote = " + quote.getId()
					+ " ORDER BY comments_published DESC"
					).getRows();
			
			list = new VirtualList<Comment>();
			Comment[] comments = new Comment[rows.length];
			for (int i = 0; i < rows.length; i++) {
				comments[i] = Comment.fromRow(rows[i]);
			}
			list.setItems(comments);
			list.setRenderer(new ComponentRenderer<Component, Comment>(c -> new CommentBox(c, this)));
			add(list);
			
			view.getH2().setText("Kommentare (" + comments.length + ")");
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
