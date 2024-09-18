package com.unlimiteddream.generate_pdf.service;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.html2pdf.resolver.font.DefaultFontProvider;
import com.itextpdf.io.font.FontProgram;
import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.layout.font.FontProvider;
import com.unlimiteddream.generate_pdf.entity.User;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;


@Service
@Slf4j
public class PdfService {

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private ServletContext servletContext;

    public void generateWarrantyPdf (HttpServletResponse response) throws IOException {
        Context context = new Context();
        User user = User.builder().id("user1").fullName("Nguyen Van A").address("Ha Noi Viet Nam").phone("0123456789").build();
        context.setVariable("user", user);
        ByteArrayInputStream byteArrayInputStream = generatePdfFromThymeleaf("user_pdf", context);
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=" + user.getId() + ".pdf");
        IOUtils.copy(byteArrayInputStream, response.getOutputStream());
    }

    private ByteArrayInputStream generatePdfFromThymeleaf(String templateName, Context context) throws IOException {
        String htmlContent = templateEngine.process(templateName, context);
        FontProvider fontProvider = new DefaultFontProvider(false, false, false);
        Arrays.asList("Roboto-Regular.ttf", "Roboto-Italic.ttf", "Roboto-Bold.ttf", "Roboto-BoldItalic.ttf").forEach(item -> {
            try {
                FontProgram fontProgram = FontProgramFactory.createFont(String.format("fonts/%s",item));
                fontProvider.addFont(fontProgram);
                log.info("Load font successfully");
            } catch (IOException e) {
                e.printStackTrace();
                log.warn("Error load font");
                throw new RuntimeException(e);
            }
        });
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ConverterProperties converterProperties = new ConverterProperties();
        converterProperties.setFontProvider(fontProvider);
        converterProperties.setBaseUri("http://localhost:8086");
        HtmlConverter.convertToPdf(htmlContent, outputStream, converterProperties);
        return new ByteArrayInputStream(outputStream.toByteArray());
    }

}
