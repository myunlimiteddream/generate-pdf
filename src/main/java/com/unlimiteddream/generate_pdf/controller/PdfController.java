package com.unlimiteddream.generate_pdf.controller;

import com.unlimiteddream.generate_pdf.service.PdfService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller
@RequestMapping("/api/pdf")
@CrossOrigin
@Slf4j
public class PdfController {

    @Autowired
    PdfService pdfService;

    @GetMapping
    public void generatePdf(HttpServletResponse response) throws IOException {
        pdfService.generateWarrantyPdf(response);
    }
}
