package schulmanager.components;

import java.time.LocalDate;

import com.abi.quotes.views.beta_test.BetaTestView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.ThemableLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.TabSheet;

import database.Row;
import database.Table;
import database.TableReceiver;
import database.User;
import schulmanager.api.ActualLessonsRequest;
import schulmanager.api.Session;
import service.CalendarCalc;
import service.DataManager;

public class TimetableSharingDialog extends Dialog implements CalendarCalc {
	
	private Button originButton;
	
	private BetaTestView view;
	private Timetable timetable;
	private LocalDate startDate;
	
	public TimetableSharingDialog(Button originButton, BetaTestView view, Timetable timetable, LocalDate startDate) {
		if (DataManager.accountsConnected()) {
			this.originButton = originButton;
			this.view = view;
			this.timetable = timetable;
			this.startDate = startDate;
			TabSheet tabSheet = new TabSheet();
			tabSheet.add("Geteilte Pläne", createUserList(true));
			tabSheet.add("Freigabe", createUserList(false));
			add(tabSheet);
		}
		else {
			close();
			return;
		}
	}
	
	private Component createUserList(boolean ttUsers) {
		Scroller scroller = new Scroller();
		scroller.setHeight("70%");
		VerticalLayout layout = new VerticalLayout();
		layout.setSpacing(false);
		layout.setJustifyContentMode(JustifyContentMode.START);
		
		Table result;
		if (ttUsers) {
			try {
				result = new TableReceiver().runQueryAndGet(
						  "SELECT * FROM user \r\n"
						+ "JOIN smSharing ON smSharing_ttUser = user_id \r\n"
						+ "JOIN smCredentials ON smCredentials_id = user_smId\r\n"
						+ "WHERE smSharing_allowedUser = " + DataManager.getUserID());
				for (Row r : result.getRows()) {
						HorizontalLayout userBox = createUserBox(r);
						userBox.addClickListener(event -> {
							ActualLessonsRequest request = new ActualLessonsRequest(toString(startDate), toString(startDate.plusDays(4)));
					        Session session = new Session(
					        		r.get("smCredentials_username", String.class),
					        		r.get("smCredentials_password", String.class)
					        		);
					        request.execute(session);
					        view.setCurrUser(r.get("user_id", Integer.class), session);
					        view.timetable.setIsOwnTimetable(false);
					        view.timetable.changeValues(
					        		request.getLessons(),
					        		startDate,
					        		true,
					        		r.get("user_id", Integer.class)
					        		);
					        originButton.setText(r.get("user_firstName", String.class) + " " + r.get("user_lastName", String.class));
					        close();
						});
						layout.add(userBox);
				}
			} catch (Exception e) {e.printStackTrace(); return new Span("Fehler 301");};
		}
		else {
			
			try {
				Table result2 = new TableReceiver().runQueryAndGet(
						"""
						SELECT user_id, user_firstName, user_lastName FROM user WHERE user_smId IS NOT NULL;
						"""
						);
				
			MultiSelectComboBox<Row> selectBox = new MultiSelectComboBox<Row>();
			selectBox.setItems(result2.getRows());
			selectBox.setItemLabelGenerator(r -> 
				r.get("user_firstName", String.class) + " " + r.get("user_lastName", String.class)
			);
			layout.add(selectBox);
			
			Button submitButton = new Button("Freigeben");
			submitButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS, ButtonVariant.LUMO_PRIMARY);
			submitButton.setTooltipText("Stundenplan für die ausgewählten Benutzer freigeben?");
			submitButton.addClickListener(e -> {
				try {
					for (Row r : selectBox.getSelectedItems()) {
						new TableReceiver().runUpdate(String.format(
								"""
								INSERT INTO smSharing (smSharing_ttUser, smSharing_allowedUser)
								SELECT * FROM (SELECT %d, %d) AS tmp
								WHERE NOT EXISTS (
								    SELECT * FROM smSharing 
								    WHERE smSharing_ttUser = %d AND smSharing_allowedUser = %d
								) LIMIT 1;
								""",
								DataManager.getUserID(),
								r.get("user_id"),
								DataManager.getUserID(),
								r.get("user_id")
								));
						Notification.show("Du hast deinen Stundenplan erfolgreich für " + r.get("user_firstName") + " freigegeben!");
					}
					
				} catch (Exception e_) {e_.printStackTrace();}
			});
			layout.add(submitButton);
			
			} catch (Exception e) {e.printStackTrace(); return new Span("Fehler 302");};
		
			
			

			try {
				result = new TableReceiver().runQueryAndGet(
						  "SELECT * FROM user \r\n"
						+ "JOIN smSharing ON smSharing_allowedUser = user_id \r\n"
						+ "JOIN smCredentials ON smCredentials_id = user_smId\r\n"
						+ "WHERE smSharing_ttUser = " + DataManager.getUserID());
				for (Row r : result.getRows()) {
						layout.add(createUserBox(r));
				}
				
				} catch (Exception e) {e.printStackTrace(); return new Span("Fehler 301");};
		}
		
		scroller.setContent(layout);
		return scroller;
	}
	
	private HorizontalLayout createUserBox(Row row) {
		String firstName   = row.get("user_firstName",   String.class);
		String lastName    = row.get("user_lastName",    String.class);
		String displayName = row.get("user_displayName", String.class);
		Integer userID = row.get("user_id",   Integer.class);
		Integer smId   = row.get("user_smId", Integer.class);
		
		HorizontalLayout layout = new HorizontalLayout();
		layout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
		layout.getStyle().set("box-shadow", "0px 10px 6px -4px rgba(0,0,0,0.3)");
		layout.add(new Avatar(firstName + " " + lastName));
		layout.add(new Span(User.nameFormat(firstName, lastName, displayName)));
		layout.getStyle().set("margin-bottom", "5px");
		layout.getStyle().set("padding-right", "5px");
		layout.getStyle().set("padding-bottom", "3px");
		
		return layout;
	}

}
