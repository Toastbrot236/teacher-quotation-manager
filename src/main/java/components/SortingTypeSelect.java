package components;

import com.abi.processing.SortingType;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;

public class SortingTypeSelect extends Select<SortingType> {

	private QuoteList list;
	
	public SortingTypeSelect(QuoteList list) {
		this.list = list;
        setLabel("Sortieren");
        setRenderer(new ComponentRenderer<Component, SortingType>(this::createEntry));
        setItems(SortingType.values());
        
        this.setValue(list.getSortingType());

        addValueChangeListener(event -> {
            list.setSortingType(event.getValue());
        });
    }
	
	private Component createEntry(SortingType type) {
		HorizontalLayout box = new HorizontalLayout();
		box.setMargin(false);
		box.setPadding(false);
		box.setSpacing(false);
		
		box.add(type.getIconOne());
		box.add(type.getIconTwo());
		Div text = new Div();
		text.setText(type.getDescription());
		box.add(text);
		
		return box;
	}
	
}
