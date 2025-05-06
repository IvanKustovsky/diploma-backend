package com.e2rent.rent_service.service.impl;

import com.e2rent.rent_service.dto.EquipmentResponseDto;
import com.e2rent.rent_service.dto.UserResponseDto;
import com.e2rent.rent_service.entity.Rental;
import com.lowagie.text.*;
import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class RentalPdfGenerator {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final Font TITLE_FONT = new Font(Font.HELVETICA, 16, Font.BOLD);
    private static final Font SECTION_FONT = new Font(Font.HELVETICA, 13, Font.BOLD);
    private static final Font LABEL_FONT = new Font(Font.HELVETICA, 11, Font.BOLD);
    private static final Font NORMAL_FONT = new Font(Font.HELVETICA, 11, Font.NORMAL);

    public byte[] generateRentalAgreementPdf(Rental rental, UserResponseDto renter, UserResponseDto owner, EquipmentResponseDto equipment) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4, 40, 40, 40, 40);

        try {
            PdfWriter writer = PdfWriter.getInstance(document, outputStream);
            writer.setPageEvent(new HeaderFooter());
            document.open();

            // Title
            Paragraph title = new Paragraph("Договір оренди №" + rental.getId(), TITLE_FONT);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20f);
            document.add(title);

            // Rental Information
            PdfPTable rentalTable = new PdfPTable(2);
            rentalTable.setWidthPercentage(100);
            rentalTable.setWidths(new float[]{1, 3});
            rentalTable.setSpacingBefore(10f);
            rentalTable.setSpacingAfter(10f);

            addTableRow(rentalTable, "Адреса доставки:", rental.getAddress());
            String period = rental.getStartDate().format(DATE_FORMATTER) + " - " + rental.getEndDate().format(DATE_FORMATTER);
            addTableRow(rentalTable, "Період оренди:", period);
            addTableRow(rentalTable, "Загальна вартість:", String.format("%.2f грн", rental.getTotalPrice()));
            addTableRow(rentalTable, "Статус запиту:", getStatusInUkrainian(rental));
            String formattedResponseDate = rental.getOwnerResponseAt() != null
                    ? rental.getOwnerResponseAt().format(DATE_TIME_FORMATTER)
                    : "Немає";
            addTableRow(rentalTable, "Дата відповіді власника:", formattedResponseDate);
            document.add(rentalTable);

            // Equipment Information
            document.add(new Paragraph("Обладнання", SECTION_FONT));
            PdfPTable equipmentTable = new PdfPTable(2);
            equipmentTable.setWidthPercentage(100);
            equipmentTable.setWidths(new float[]{1, 3});
            equipmentTable.setSpacingBefore(5f);
            equipmentTable.setSpacingAfter(10f);
            addTableRow(equipmentTable, "Назва обладнання:", equipment.getName());
            addTableRow(equipmentTable, "Стан:", getConditionInUkrainian(equipment));
            addTableRow(equipmentTable, "Ціна за добу:", String.format("%.2f грн", equipment.getPricePerDay()));
            document.add(equipmentTable);

            // Renter Information
            document.add(new Paragraph("Орендар", SECTION_FONT));
            PdfPTable renterTable = new PdfPTable(2);
            renterTable.setWidthPercentage(100);
            renterTable.setWidths(new float[]{1, 3});
            renterTable.setSpacingBefore(5f);
            renterTable.setSpacingAfter(10f);
            addTableRow(renterTable, "ПІБ:", renter.getFullName());
            addTableRow(renterTable, "Email:", renter.getEmail());
            addTableRow(renterTable, "Телефон:", renter.getMobileNumber());
            if (renter.getCompany() != null) {
                addTableRow(renterTable, "Компанія:", renter.getCompany().getName());
                addTableRow(renterTable, "Код компанії:", renter.getCompany().getCode());
                if (renter.getCompany().getAddress() != null) {
                    addTableRow(renterTable, "Адреса компанії:", renter.getCompany().getAddress());
                }
            }
            document.add(renterTable);

            // Owner Information
            document.add(new Paragraph("Власник", SECTION_FONT));
            PdfPTable ownerTable = new PdfPTable(2);
            ownerTable.setWidthPercentage(100);
            ownerTable.setWidths(new float[]{1, 3});
            ownerTable.setSpacingBefore(5f);
            ownerTable.setSpacingAfter(10f);
            addTableRow(ownerTable, "ПІБ:", owner.getFullName());
            addTableRow(ownerTable, "Email:", owner.getEmail());
            addTableRow(ownerTable, "Телефон:", owner.getMobileNumber());
            if (owner.getCompany() != null) {
                addTableRow(ownerTable, "Компанія:", owner.getCompany().getName());
                addTableRow(ownerTable, "Код компанії:", owner.getCompany().getCode());
                if (owner.getCompany().getAddress() != null) {
                    addTableRow(ownerTable, "Адреса компанії:", owner.getCompany().getAddress());
                }
            }
            document.add(ownerTable);

            // Owner Message
            if (rental.getOwnerMessage() != null && !rental.getOwnerMessage().isBlank()) {
                document.add(new Paragraph("Повідомлення власника:", SECTION_FONT));
                document.add(new Paragraph(rental.getOwnerMessage(), NORMAL_FONT));
                document.add(Chunk.NEWLINE);
            }

            // Generation Date and Signatures
            String currentDate = LocalDateTime.now().format(DATE_TIME_FORMATTER);
            document.add(new Paragraph("Дата генерації: " + currentDate, NORMAL_FONT));
            document.add(Chunk.NEWLINE);
            document.add(new Paragraph("Підписи сторін:", SECTION_FONT));
            document.add(new Paragraph("Орендар: " + renter.getFullName(), NORMAL_FONT));
            document.add(new Paragraph("Підпис: ____________________", NORMAL_FONT));
            document.add(new Paragraph("Власник: " + owner.getFullName(), NORMAL_FONT));
            document.add(new Paragraph("Підпис: ____________________", NORMAL_FONT));

            document.close();
        } catch (Exception e) {
            throw new RuntimeException("Не вдалося згенерувати PDF", e.getCause());
        }

        return outputStream.toByteArray();
    }

    private void addTableRow(PdfPTable table, String label, String value) {
        PdfPCell cell1 = new PdfPCell(new Phrase(label, LABEL_FONT));
        PdfPCell cell2 = new PdfPCell(new Phrase(value != null ? value : "-", NORMAL_FONT));
        cell1.setBorder(Rectangle.NO_BORDER);
        cell2.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell1);
        table.addCell(cell2);
    }

    private String getStatusInUkrainian(Rental rental) {
        return switch (rental.getStatus()) {
            case PENDING -> "Очікує на підтвердження";
            case APPROVED -> "Підтверджено";
            case CANCELLED -> "Скасовано";
            case IN_PROGRESS -> "В оренді";
            case DECLINED_BY_SYSTEM -> "Відхилено системою";
            case REJECTED -> "Відхилено";
            case COMPLETED -> "Завершено";
        };
    }

    private String getConditionInUkrainian(EquipmentResponseDto equipment) {
        return switch (equipment.getCondition()) {
            case "NEW" -> "Новий";
            case "USED" -> "Вживаний";
            case "REFURBISHED" -> "Відновлений";
            default -> equipment.getCondition();
        };
    }

    private static class HeaderFooter extends PdfPageEventHelper {
        Font footerFont = new Font(Font.HELVETICA, 10, Font.NORMAL);

        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            PdfPTable header = new PdfPTable(1);
            header.setTotalWidth(document.getPageSize().getWidth() - document.leftMargin() - document.rightMargin());
            header.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            header.writeSelectedRows(0, -1, document.leftMargin(), document.getPageSize().getHeight() - 10, writer.getDirectContent());

            PdfPTable footer = new PdfPTable(1);
            footer.setTotalWidth(document.getPageSize().getWidth() - document.leftMargin() - document.rightMargin());
            footer.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            footer.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
            footer.addCell(new Phrase("Сторінка " + writer.getPageNumber(), footerFont));
            footer.writeSelectedRows(0, -1, document.leftMargin(), 20, writer.getDirectContent());
        }
    }
}

