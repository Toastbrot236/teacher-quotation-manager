package components;

import java.sql.SQLException;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.dependency.JsModule;

import database.DatabaseConnector;
import database.TableReceiver;
import service.DataManager;

@CssImport(value = "themes/zitate-sammlung/favorite-button.css")
@JsModule("./favoritebutton.js")
public class FavoriteButton extends Html {
	
	private int id;
	private boolean isActive;

	public FavoriteButton(int id, boolean isActive) {
		super(String.format(
				"""
				<div>
					<div class="fw-container">
					    <button id="favorite-button%d" class="favorite-button%s" onload="window.initFW(%d)">
					      <span id="star%d" class="star">&#973%d;</span> <!-- Stern Icon -->
					    </button>
					    <div id="firework%d" class="firework"></div> <!-- Feuerwerk -->
					</div>
				</div>
				""",
				id,
				isActive ? " active" : "",
				id,id,
				isActive ? 3 : 4,
				id
				));
		this.id = id;
		this.isActive = isActive;
		provideElementToJs();
	}
	
	public void setSmall() {
		this.setHtmlContent(String.format(
				"""
				<div style="align-content: center">
					<div class="fw-container">
					    <button id="favorite-button%d" class="favorite-button%s" style="font-size: 1rem">
					      <span id="star%d" class="star">&#973%d;</span> <!-- Stern Icon -->
					    </button>
					    <div id="firework%d" class="firework"></div> <!-- Feuerwerk -->
					</div>
				</div>
				""",
				id,
				isActive ? " active" : "",
				id,
				isActive ? 3 : 4,
				id
				));
	}
	
	public void onAttach(AttachEvent event) {
		getElement().executeJs("document.addEventListener('mousemove', function() {" /*console.log('Initiated FW" + id +  "');*/ + "window.initFW(" + id + "); } , false);");
	}
	
	private void provideElementToJs() {
	     getElement().executeJs("provideElement($0)", getElement());
	}
	
	@ClientCallable
	public void toggleFavorite(int id, boolean status) {
		System.out.println("Favorite with id " + id + " set to " + status);
		try {
			if (status) {
				new TableReceiver().runUpdate(String.format(
						"INSERT INTO stars (stars_user, stars_quote, stars_time) VALUES (%d, %d, now());",
						DataManager.getUserID(),
						id
						));
			}
			else {
				new TableReceiver().runUpdate(String.format(
						"DELETE FROM stars WHERE stars_user = %d AND stars_quote = %d",
						DataManager.getUserID(),
						id
						));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
