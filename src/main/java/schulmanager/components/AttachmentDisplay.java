package schulmanager.components;

import java.util.Base64;

import com.google.gson.JsonObject;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class AttachmentDisplay extends VerticalLayout {

	private String requestUrl, fileType, fileName;
	private int id;
	
	public AttachmentDisplay (JsonObject attachment) {
		setPadding(false);
		setSpacing(false);
		setMargin (false);
		
		id = attachment.get("id").getAsInt();
		
		String fileString = attachment.get("file").getAsString();
		
		requestUrl = "https://login.schulmanager-online.de/download-file/" 
								+ Base64.getEncoder().encodeToString(fileString.getBytes());
		
		String[] parts = fileString.split(",");
        fileType = parts[4].replaceAll("\"", "");
        fileName = parts[6].replaceAll("\"\\]", "");
		
        switch (fileType) {
        
        case "image/apng": 
        case "image/avif":
        case "image/gif":
        case "image/jpeg":
        case "image/png":
        case "image/svg":
        case "image/webp":
        	image();
        	break;
        case "application/pdf":
        	pdf();
        	break;
        default:
        	other();
        	break;
        
        }
        	
	}
	
	private void image() {
		Image img = new Image(requestUrl, fileName);
		img.addClassName("chat-image");
		img.setId("myImg" + id);
		add(img);
		System.out.println("----F; " + fileName);
		add(new Html(
				String.format(
				"""
				<div id="%s" class="modal">
				  <span class="close" id="%s">&times;</span>
				  <img class="modal-content" id="%s">
				  <div id="%s"></div>
				</div>
				""",
				"modal" + id,
				"close" + id,
				"a" + id,
				"caption" + id
				)));
		add(new Html(
				String.format(
				"""
				<script>
				// Get the modal
				var modal = document.getElementById("%s");
				
				// Get the image and insert it inside the modal - use its "alt" text as a caption
				var img = document.getElementById("%s");
				var modalImg = document.getElementById("%s");
				var captionText = document.getElementById("%s");
				img.onclick = function(){
				  modal.style.display = "block";
				  modalImg.src = this.src;
				  captionText.innerHTML = this.alt;
				}
				
				// Get the <span> element that closes the modal
				var span = document.getElementById("%s");
				
				// When the user clicks on <span> (x), close the modal
				span.onclick = function() {
				  modal.style.display = "none";
				}
				</script>
				""",
				"modal" + id,
				"myImg" + id,
				"a" + id,
				"caption" + id,
				"close" + id
				)));
	}
	
	private void pdf() {
		other();
	}
	
	private void other() {
		
		HorizontalLayout layout = new HorizontalLayout();
		layout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
		
		String type = fileType.split("/")[1];
		if (type.equals("vnd.openxmlformats-officedocument. wordprocessingml.document") || type.equals("msword"))
			type = "doc";
		
		String imageUrl = "images/" + type + ".svg";
		
		Anchor a = new Anchor(requestUrl);
		Image img = new Image(imageUrl, "Unb. Dateityp");
		img.setHeight("45px");
		layout.add(img, new Span(fileName + "\""));
		a.add(layout);
		
		add(a);
	}
	
}
