package components;

import java.util.concurrent.atomic.AtomicReference;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.theme.lumo.LumoIcon;

public interface HasHelp {
	
	public Component[] getPages();
	
	public default Dialog openHelp() {
		Component[] pages = getPages();
		
		AtomicReference<Integer> currPage = new AtomicReference<Integer>(0);
		int lastPage = pages.length - 1;
		
		Dialog dialog = new Dialog();
        
        dialog.setHeaderTitle("Willkommen");
        
        Button closeButton = new Button(LumoIcon.CROSS.create(), e -> dialog.close());
        closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        dialog.getHeader().add(closeButton);

        dialog.setWidth("90%");
        dialog.setMaxWidth("1000px");
        dialog.setHeight("80%");
        dialog.setMaxHeight("1500px");
        dialog.setModal(false);
        dialog.setVisible(true);
        dialog.setCloseOnOutsideClick(false);
       
        dialog.addComponentAsFirst(pages[currPage.get()]);
        
        HorizontalLayout footer = new HorizontalLayout();
        
        Button previousButton = new Button(VaadinIcon.ARROW_LEFT.create());
        previousButton.setEnabled(false);
        Span pageNumber = new Span((currPage.get()+1) + "/" + (lastPage+1));
        Button nextButton = new Button(VaadinIcon.ARROW_RIGHT.create());
        
        previousButton.addClickListener(e -> {
        	if (currPage.get() > 0) {
        		dialog.remove(pages[currPage.get()]);
        		currPage.set(currPage.get()-1);
        		dialog.addComponentAsFirst(pages[currPage.get()]);
        		pageNumber.setText((currPage.get()+1) + "/" + (lastPage+1));
        		nextButton.setEnabled(true);
        		if (currPage.get() == 0)
        			previousButton.setEnabled(false);
        	}
        });
        nextButton.addClickListener(e -> {
        	if (currPage.get() < lastPage) {
        		dialog.remove(pages[currPage.get()]);
        		currPage.set(currPage.get()+1);
        		dialog.addComponentAsFirst(pages[currPage.get()]);
        		pageNumber.setText((currPage.get()+1) + "/" + (lastPage+1));
        		previousButton.setEnabled(true);
        		if (currPage.get() == lastPage)
        			nextButton.setEnabled(false);
        	}
        });
        
        footer.add(previousButton, pageNumber, nextButton);
        footer.setWidthFull();
        footer.setJustifyContentMode(JustifyContentMode.CENTER);
        footer.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        
        dialog.getFooter().add(footer);
        
        dialog.open();
        
        return dialog;
	}

}