package components;

import java.sql.SQLException;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.shared.Registration;
import com.vaadin.flow.theme.lumo.LumoIcon;

import database.Quote;
import database.TableReceiver;
import service.DataManager;

public class RatingBar extends HorizontalLayout {
	
	private Quote quote;
	
	private Button like, dislike, commentButton;
	private boolean liked, disliked;
	
	private HorizontalLayout cornerLayout;
	
	public RatingBar(Quote quote) {
		this.quote = quote;
		
		HorizontalLayout inner = new HorizontalLayout();
		inner.setJustifyContentMode(JustifyContentMode.EVENLY);
		
		//Like-Button
		
		like = new Button();
		like.setText(String.valueOf(quote.getLikes()));
		like.setIcon(VaadinIcon.THUMBS_UP.create());
		like.setIconAfterText(true);
		like.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_SUCCESS, ButtonVariant.LUMO_TERTIARY);
		like.getStyle().set("border", "1px solid");
		like.getStyle().set("border-radius", "40px");
		like.getStyle().set("border-color", "var(--lumo-contrast)");
		inner.add(like);
		
		if(quote.isLiked()) {
			liked = true;
			setLikeDesign();
		}
		
		Registration likeReg = like.addClickListener(e -> {
			
			if (liked) {
				removeLike();
			}
			else {
				if(disliked)
					removeDislike();
				addLike();
			}
			
			like.addThemeVariants(ButtonVariant.LUMO_ICON);
			
		});
		
		//Dislike-Button
		
        dislike = new Button(); // Dislike-Button initialisieren
        dislike.setText(String.valueOf(quote.getDislikes())); // Text und Icon anpassen
        dislike.setIcon(VaadinIcon.THUMBS_DOWN.create());
        dislike.setIconAfterText(true);
        dislike.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_TERTIARY);
        dislike.getStyle().set("border", "1px solid");
        dislike.getStyle().set("border-radius", "40px");
        dislike.getStyle().set("border-color", "var(--lumo-contrast)");
        inner.add(dislike);

        if (quote.isDisliked()) { // Prüfen, ob der Dislike-Button bereits aktiviert ist
            disliked = true;
            setDislikeDesign();
        }

        Registration dislikeReg = dislike.addClickListener(e -> { // Event-Listener für Dislike-Button
            if (disliked) {
                removeDislike();
            } else {
            	if (liked)
            		removeLike();
                addDislike();
            }
            dislike.addThemeVariants(ButtonVariant.LUMO_ICON);
        });
        
        //Comment-Button
        
        commentButton = new Button();
        commentButton.setText(String.valueOf(quote.getComments()));
        commentButton.setIcon(VaadinIcon.COMMENT.create());
        commentButton.setIconAfterText(true);
        commentButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_CONTRAST, ButtonVariant.LUMO_TERTIARY);
        commentButton.getStyle().set("border", "1px solid");
        commentButton.getStyle().set("border-radius", "40px");
        commentButton.getStyle().set("border-color", "var(--lumo-contrast)");
        commentButton.addClickListener(e -> {
        	DataManager.setQuoteDetail(quote.getId());
        	getUI().ifPresent(ui ->
        		ui.navigate("quote-detail"));
        });
        inner.add(commentButton);
        
        if (!DataManager.canRate()) {
        	likeReg.remove();
        	dislikeReg.remove();
        	like.addClickListener(e -> Notification.show("Du bist nicht berechtigt, Zitate zu bewerten!"));
        	dislike.addClickListener(e -> Notification.show("Du bist nicht berechtigt, Zitate zu bewerten!"));
        }
        
        add(inner);
        
        createCorner();
	}
	
	//Like-Button:
	
	private void setLikeDesign() {
		like.removeThemeVariants(ButtonVariant.LUMO_SUCCESS, ButtonVariant.LUMO_TERTIARY);
		like.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_SUCCESS, ButtonVariant.LUMO_PRIMARY);
	}
	
	private void removeLikeDesign() {
		like.removeThemeVariants(ButtonVariant.LUMO_SUCCESS, ButtonVariant.LUMO_PRIMARY);
		like.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_SUCCESS, ButtonVariant.LUMO_TERTIARY);
	}
	
	private void addLike() {
		setLikeDesign();
		
		try {
			new TableReceiver().runUpdate(
					String.format("INSERT INTO likes (likes_user, likes_quote, likes_time) VALUES (%d, %d, now());",
							DataManager.getUserID(),
							quote.getId())
					);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		like.setText(String.valueOf(likeAmount()));
		liked = true;
	}
	
	private void removeLike() {
		removeLikeDesign();

		try {
			new TableReceiver().runUpdate(
					String.format("DELETE FROM likes WHERE likes_user = %d AND likes_quote = %d;",
							DataManager.getUserID(),
							quote.getId())
					);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		like.setText(String.valueOf(likeAmount()));
		liked = false;
	}
	
	private boolean isLiked() {
		try {
			return new TableReceiver().runQueryAndGetSingleValue(
					String.format(
							"SELECT EXISTS(SELECT * FROM likes WHERE likes_quote = %d AND likes_user = %d);",
							quote.getId(),
							DataManager.getUserID()
							)
					, Long.class) > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private long likeAmount() {
		try {
			return new TableReceiver().runQueryAndGetSingleValue(
							"SELECT COUNT(likes_id) FROM likes WHERE likes_quote = " + quote.getId(),
							Long.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -42;
	}
	
	//Dislike-Button:
	
    private void setDislikeDesign() {
        dislike.removeThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_TERTIARY);
        dislike.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_PRIMARY);
    }

    private void removeDislikeDesign() {
        dislike.removeThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_PRIMARY);
        dislike.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_TERTIARY);
    }

    private void addDislike() {
        setDislikeDesign();

        try {
            new TableReceiver().runUpdate(
                String.format("INSERT INTO dislikes (dislikes_user, dislikes_quote, dislikes_time) VALUES (%d, %d, now());",
                        DataManager.getUserID(),
                        quote.getId())
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }

        dislike.setText(String.valueOf(dislikeAmount()));
        disliked = true;
    }

    private void removeDislike() {
        removeDislikeDesign();

        try {
            new TableReceiver().runUpdate(
                String.format("DELETE FROM dislikes WHERE dislikes_user = %d AND dislikes_quote = %d;",
                        DataManager.getUserID(),
                        quote.getId())
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }

        dislike.setText(String.valueOf(dislikeAmount()));
        disliked = false;
    }

    private boolean isDisliked() {
        try {
            return new TableReceiver().runQueryAndGetSingleValue(
                String.format(
                    "SELECT EXISTS(SELECT * FROM dislikes WHERE dislikes_quote = %d AND dislikes_user = %d);",
                    quote.getId(),
                    DataManager.getUserID()
                ),
                Long.class) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private long dislikeAmount() {
        try {
            return new TableReceiver().runQueryAndGetSingleValue(
                "SELECT COUNT(dislikes_id) FROM dislikes WHERE dislikes_quote = " + quote.getId(),
                Long.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -42;
    }
    
    public void setCommentButtonVisible(boolean visible) {
    	commentButton.setVisible(visible);
    }
    
    public void setSmall() {
    	like.addThemeVariants(ButtonVariant.LUMO_SMALL);
    	dislike.addThemeVariants(ButtonVariant.LUMO_SMALL);
    	commentButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
    	
    	addDeleteButton();
    }
    
    private void addDeleteButton() {
    	if(DataManager.canDelete()) {
    		setWidthFull();
    		
    		VerticalLayout delLayout = new VerticalLayout();
    		delLayout.setSpacing(false);
    		delLayout.setPadding(false);
    		delLayout.setMargin(false);
    		delLayout.setDefaultHorizontalComponentAlignment(Alignment.END);
    		
    		DeleteButton delButton = new DeleteButton(quote, true);
    		delLayout.add(delButton);
    		
    		cornerLayout.addComponentAsFirst(delButton);
    	}
    }
    
    private void createCorner() {
    	
    	cornerLayout = new HorizontalLayout();
    	cornerLayout.setSpacing(false);
    	cornerLayout.setPadding(false);
    	cornerLayout.setMargin(false);
    	cornerLayout.setWidthFull();
    	cornerLayout.setJustifyContentMode(JustifyContentMode.END);
    	
    	if (quote.getLastEdited() != null) {
    		
    		setWidthFull();
    		
    		VerticalLayout layout = new VerticalLayout();
    		layout.setSpacing(false);
    		layout.setPadding(false);
    		layout.setMargin(false);
    		layout.setDefaultHorizontalComponentAlignment(Alignment.END);
    		layout.setJustifyContentMode(JustifyContentMode.END);
    		
    		Icon icon = VaadinIcon.PENCIL.create();
    		icon.setSize("12px");
    		icon.getTooltip().setManual(true);
    		icon.addClickListener(e -> {
    			icon.setTooltipText("Zuletzt bearbeitet am " + quote.getLastEdited().toLocaleString() + " von " + getName());
    			icon.getTooltip().setOpened(!icon.getTooltip().isOpened());
    		});
    		
    		layout.add(icon);
    		
    		cornerLayout.add(icon);
    		cornerLayout.setVerticalComponentAlignment(Alignment.END, icon);
    	}
    	
    	add(cornerLayout);
    }
    
    private String getName() {
    	try {
			return new TableReceiver().runQueryAndGetSingleValue(
					"SELECT CONCAT(user_firstName, \" \", user_lastName) AS name FROM user WHERE user_id = " + quote.getLastEditedBy(), 
					String.class
					);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return "";
    }

}
