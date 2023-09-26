package components;

import java.util.concurrent.TimeUnit;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.shared.Registration;

public class SettingBar extends HorizontalLayout {
	
	private QuoteList list;
	
	HorizontalLayout addQuoteLayout;
	Button moreButton;
	
	TextField searchField; //neu
    private Registration valueChangeListenerRegistration; // Neu hinzugefügt

	
	public SettingBar(QuoteList list) {
		this.list = list;
		setWidthFull();
		
		moreButton = new Button(VaadinIcon.FILTER.create());
    	moreButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST, ButtonVariant.LUMO_PRIMARY);
    	ContextMenu menu = new ContextMenu();
    	menu.setOpenOnClick(true);
    	menu.add(new SortingTypeSelect(list));
    	menu.setTarget(moreButton);
    	add(moreButton);
    	
    	 searchField = new TextField();
    	 searchField.setValueChangeMode(ValueChangeMode.EAGER);
    	 searchField.setWidth("30%");
    	 searchField.setMaxWidth("200px");
         searchField.setPlaceholder("Suche");
         searchField.setClearButtonVisible(true);
         add(searchField);

         valueChangeListenerRegistration = searchField.addValueChangeListener(
             event -> handleDelayedSearch(event.getValue(), 500)
         );
		
		addQuoteLayout = new HorizontalLayout();
		addQuoteLayout.setWidthFull();
    	addQuoteLayout.add(new Span("Zitat hinzufügen"));
    	NewQuoteButton plusButton = new NewQuoteButton();
    	addQuoteLayout.add(plusButton);
    	addQuoteLayout.setJustifyContentMode(JustifyContentMode.END);
    	plusButton.setQuoteCreationListener(() -> list.update());
    	add(addQuoteLayout);
    	
	}
	
	public void setMoreVisible(boolean visible) {
		moreButton.setVisible(visible);
	}
	
	public void setAddVisible(boolean visible) {
		addQuoteLayout.setVisible(visible);
	}
	
	public void setSerachFieldVisible(boolean visible) {
		searchField.setVisible(visible);
	}
	
	private void handleDelayedSearch(String searchValue, int delayMillis) {
        if (valueChangeListenerRegistration != null) {
            valueChangeListenerRegistration.remove();
        }

        valueChangeListenerRegistration = searchField.addValueChangeListener(event -> {
            try {
                TimeUnit.MILLISECONDS.sleep(delayMillis);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            handleSearch(event.getValue());
        });
    }

    private void handleSearch(String searchValue) {
    	list.setSearchValue(searchValue);
        //Notification.show("Suche: " + searchValue);
    }


}
