package components;

import java.sql.SQLException;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.virtuallist.VirtualList;
import com.vaadin.flow.data.provider.DataKeyMapper;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.data.renderer.Rendering;
import com.vaadin.flow.dom.Element;
import com.abi.processing.SortingType;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.html.Span;

import database.Quote;
import database.Row;
import database.TableReceiver;
import service.DataManager;

public class QuoteList extends VerticalLayout {
	
	private VirtualList<Quote> list;
	private int amountQuotes = 0;
	
	private SortingType sort;
	
	private String searchValue;
	
	private int teacherId;
	
	private int limit;
	
	private boolean showStudentQuotes;
	
	public QuoteList(int limit, boolean smallRatingBars) {
		this(limit, smallRatingBars, -1, false);
	}
	
	public QuoteList(int limit, boolean smallRatingBars, int teacherId, boolean showStudentQuotes) {
		
		
		
		this.teacherId = teacherId;
		sort = SortingType.NEWEST;
		searchValue = "";
		this.limit = limit;
		this.showStudentQuotes = showStudentQuotes;
		
		list = new VirtualList<Quote>();
		list.getStyle().set("border-top", "2px solid");
		list.getStyle().set("border-color", " var(--lumo-body-text-color)");
		update();
		list.setRenderer(new ComponentRenderer<Component, Quote>(quote ->{
			QuoteBox box = new QuoteBox(quote);
			if (smallRatingBars)
				box.setRatingBarSmall();
			quote.setAsscociatedBox(box);
			if (!searchValue.equals("")) {
				markFoundTextParts(quote);
			}
			return box;
		}));
		
		
		
		if (!DataManager.canRead()) {
			add(new Html("<span>Dir wurde anscheinend der Lesezugriff genommen. Hast du vielleicht die Schule verlassen? Sollte es sich um einen Fehler handeln, kontaktiere einen Administrator.</span>"));
			return;
		}
		
		
		
		add(list);
	}
	
	public QuoteList() {
		this(-1, false);
	}
	
	public void update() {
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
					+ whereClause()
					+ sortingClause()
					+ limitClause()
					).getRows();
			Quote[] quotes = new Quote[rows.length];
			for (int i = 0; i < rows.length; i++) {
				quotes[i] = Quote.fromRow(rows[i]);
			}
			amountQuotes = rows.length;
			list.setItems(quotes);
			
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public VirtualList<Quote> getList() {
		return list;
	}
	
	public void setSortingType(SortingType type) {
		this.sort = type;
		update();
	}
	
	public void setSearchValue(String searchValue) {
		this.searchValue = searchValue;
		update();
	}
	
	public SortingType getSortingType() {
		return sort;
	}
	
	public void setLimit(int limit) {
		this.limit = limit;
	}
	
	public int getLimit() {
		return limit;
	}
	
	private String whereClause() {
		if (teacherId > 0) {
			if (searchValue.equals(""))
				return " WHERE teachers_id = " + teacherId;
			else
				return " WHERE teachers_id = " + teacherId + " AND quotes_text LIKE '%" + searchValue + "%'";
		}
		if (!searchValue.equals(""))
			return " WHERE quotes_text LIKE '%" + searchValue + "%' AND NOT teachers_gender = 's'";
		if (showStudentQuotes)
			return "";
		return " WHERE NOT teachers_gender = 's'";
	}
	
	private String sortingClause() {
		return " ORDER BY " + sort.getQuery();
	}
	
	private String limitClause() {
		return ((limit < 0) ? "" : " LIMIT " + limit);
	}
	
	public int amountQuotes() {
		return amountQuotes;
	}
	
	private void markFoundTextParts(Quote q) {
		q.getAsscociatedBox().mark(searchValue);
	}

}
