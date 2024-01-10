package components;

import java.sql.SQLException;

import com.abi.quotes.views.teacher_detail.TeacherDetailView;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;

import database.Quote;
import database.Row;
import database.TableReceiver;
import database.User;
import service.DataManager;
import service.Toolkit;

public class TeacherBox extends VerticalLayout {
	
	private Row teacher;
	
	private int id;
	private String name;
	private long quotes, likes, dislikes, comments;
	
	public TeacherBox(Row teacher) {
		this.teacher = teacher;
		
		addClassName("hover-effect");
		
		getStyle().set("box-shadow", "0px 8px 9px -2px rgba(0,0,0,0.42)");
		getStyle().set("padding", "16px 16px 8px 16px");
		getStyle().setMargin("5px 0px 5px 0px");
		setSpacing(false);
		
		id = teacher.get("teachers_id", Integer.class);
		name = Toolkit.formatTeacherName(teacher.get("teachers_gender", String.class), teacher.get("teachers_name", String.class));
		quotes = teacher.get("quotes", Long.class);
		likes = teacher.get("likes", Long.class);
		dislikes = teacher.get("dislikes", Long.class);
		comments = teacher.get("comments", Long.class);
		
		H3 heading = new H3(name);
		heading.getStyle().set("color", "var(--lumo-primary-color)");
		add(heading);

		HorizontalLayout quotesLayout = new HorizontalLayout();
		quotesLayout.add(new Html("<div><b>" + quotes + "</b> Zitate</div>"));
		add(quotesLayout);
		
		HorizontalLayout likesLayout = new HorizontalLayout();
		likesLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
		likesLayout.getStyle().set("gap", "10px");
		likesLayout.add(new Html("<div><b>" + likes + "</b></div>"));
		Icon i = VaadinIcon.THUMBS_UP.create();
		i.setSize("20px");
		likesLayout.add(i);
		
		HorizontalLayout dislikesLayout = new HorizontalLayout();
		dislikesLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
		dislikesLayout.getStyle().set("gap", "10px");
		dislikesLayout.add(new Html("<div><b>" + dislikes + "</b></div>"));
		Icon j = VaadinIcon.THUMBS_DOWN.create();
		j.setSize("20px");
		dislikesLayout.add(j);
		
		HorizontalLayout commentsLayout = new HorizontalLayout();
		commentsLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
		commentsLayout.getStyle().set("gap", "10px");
		commentsLayout.add(new Html("<div><b>" + comments + "</b> Kommentare</div>"));
		Icon k = VaadinIcon.COMMENT.create();
		k.setSize("20px");
		commentsLayout.add(k);
		
		add(new HorizontalLayout(likesLayout, dislikesLayout, commentsLayout));
		
		addClickListener(e -> {
			DataManager.setTeacherDetail(id);
			DataManager.setTeacherDetailRow(teacher);
			getUI().ifPresent(ui -> ui.navigate(TeacherDetailView.class));
		});
		
	}
	
	public TeacherBox(int teacherId) {
		this(getTeacher(teacherId));
	}
	
	public static Row getTeacher(int id) {
		try {
			return new TableReceiver().runQueryAndGet(
					"SELECT \r\n"
					+ "	DISTINCT teachers_id, teachers_gender, teachers_name, \r\n"
					+ "    (SELECT COUNT(quotes_id) FROM quotes WHERE quotes_originator = teachers_id) AS quotes,\r\n"
					+ "    (SELECT COUNT(likes_id) FROM likes JOIN quotes ON likes_quote = quotes_id WHERE quotes_originator = teachers_id) AS likes,\r\n"
					+ "	(SELECT COUNT(dislikes_id) FROM dislikes JOIN quotes ON dislikes_quote = quotes_id WHERE quotes_originator = teachers_id) AS dislikes,\r\n"
					+ "    (SELECT COUNT(comments_id) FROM comments JOIN quotes ON comments_quote = quotes_id WHERE quotes_originator = teachers_id) AS comments\r\n"
					+ "FROM teachers\r\n"
					+ "LEFT JOIN quotes ON quotes_originator = teachers_id\r\n"
					+ "WHERE teachers_id = " + id + "\r\n"
					+ "ORDER BY teachers_name;"
					).getRows()[0];
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

}
