package com.abi.quotes.views.uploadtest;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.abi.quotes.views.MainLayout;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.component.upload.receivers.MultiFileBuffer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@PageTitle("File-Processor")
@Route(value="file-processor", layout= MainLayout.class)
@CssImport(value = "themes/zitate-sammlung/file-viewer.css")
/**
 * Test page where you can apparently upload files to somewhere
 */
public class FileProcessorView extends VerticalLayout {

    public FileProcessorView() {
        MemoryBuffer buffer = new MemoryBuffer();
        Upload upload = new Upload(buffer);
        upload.setUploadButton(new Button("Upload File"));
        upload.setMaxFiles(5);
        upload.setMaxFileSize(67108864);

        upload.addSucceededListener(event -> {
            String mimeType = event.getMIMEType();
            String originalFileName = event.getFileName();
            InputStream inputStream = buffer.getInputStream();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            byte[] bufferArray = new byte[8192]; // Puffergröße wählen
            int bytesRead;
            try {
                while ((bytesRead = inputStream.read(bufferArray)) != -1) {
                    outputStream.write(bufferArray, 0, bytesRead);
                }
                byte[] processedFileBytes = outputStream.toByteArray();

                // Erstellen der Data-URL basierend auf dem MIME-Typ
                String base64Data = Base64.getEncoder().encodeToString(processedFileBytes);
                String dataURL = "data:" + mimeType + ";base64," + base64Data;

                // Erstellen des Download-Links mit der Data-URL
                Anchor downloadLink = new Anchor();
                downloadLink.setText("Download " + originalFileName);
                downloadLink.setHref(dataURL);
                downloadLink.getElement().setAttribute("download", originalFileName);
                add(downloadLink); // Füge den Download-Link zum Layout hinzu
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        add(upload);
    }
}