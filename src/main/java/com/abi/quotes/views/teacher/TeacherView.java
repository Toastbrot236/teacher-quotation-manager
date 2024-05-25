package com.abi.quotes.views.teacher;

import java.sql.SQLException;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.virtuallist.VirtualList;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import components.TeacherBox;

import com.abi.quotes.views.MainLayout;
import com.vaadin.flow.component.*;

import database.Row;
import database.TableReceiver;

@PageTitle("Alle Lehrer")
@Route(value = "lehrer", layout = MainLayout.class)
/**
 * Page that shows all teachers and their quote stats (likes, dislikes, comment count)
 */
public class TeacherView extends VerticalLayout {
	
	private VirtualList<Row> list;
	
	public TeacherView() {
		list = new VirtualList<Row>();
		
		list.setItems(getRows());
		list.setRenderer(
				new ComponentRenderer<Component, Row>(
						r -> {
							return new TeacherBox(r);
						})
				);
		list.getElement().getStyle().setHeight("80vh");
		
		add(list);
	}
	
	private Row[] getRows() {
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
					+ "WHERE teachers_gender NOT LIKE 's'\r\n"
					+ "ORDER BY teachers_name;"
					).getRows();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

}
