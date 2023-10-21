package com.timeszone.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.lowagie.text.DocumentException;
import com.timeszone.model.dto.InvoiceDTO;

@Service
public class PdfService {
	
	@Autowired
	private PurchaseOrderService purchaseOrderService;
	
	@Autowired
	private TemplateEngine templateEngine;
	
	public byte[] createPdf(Integer orderId) throws DocumentException, IOException {
		Context context = new Context();
		InvoiceDTO invoice = purchaseOrderService.createInvoice(orderId);
		context.setVariable("invoice",invoice);
		
		String processHtml = templateEngine.process("invoice",context);
		
		ByteArrayOutputStream pdfOutputStream = new ByteArrayOutputStream();
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(processHtml);
        renderer.layout();
        renderer.createPDF(pdfOutputStream, false);
        renderer.finishPDF();
        // Get the PDF content as a byte array
        return pdfOutputStream.toByteArray();
		
	}
}
