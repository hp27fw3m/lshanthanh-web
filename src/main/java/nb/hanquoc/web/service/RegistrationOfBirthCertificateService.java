package nb.hanquoc.web.service;

import nb.hanquoc.web.repository.RegistrationOfBirthCertificateRepository;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import nb.hanquoc.web.AppUltil;
import nb.hanquoc.web.VietNumber;
import nb.hanquoc.core.entity.RegistrationOfBirthCertificate;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegistrationOfBirthCertificateService {

    @Autowired
    private RegistrationOfBirthCertificateRepository repository;

    public List<RegistrationOfBirthCertificate> findAll() {
        return (List<RegistrationOfBirthCertificate>) repository.findAll();
    }

    public RegistrationOfBirthCertificate save(RegistrationOfBirthCertificate person) {
        return repository.save(person);
    }

    public Optional<RegistrationOfBirthCertificate> findById(Long id) {
        return repository.findById(id);
    }

    private static byte[] templateBinary;

    private static byte[] getTemplateBinary() throws IOException {
        if (templateBinary == null) {
            templateBinary = Files.readAllBytes(Paths.get("./conf/gcks.pdf"));
        }
        return templateBinary;
    }

    public ByteArrayOutputStream createPdfForm(RegistrationOfBirthCertificate birthRecord)
            throws ParseException, IOException {

        PDDocument document = PDDocument.load(getTemplateBinary());

        updatePage0(document, birthRecord);
        updatePage1(document, birthRecord);
        updatePage2(document, birthRecord);
        updatePage3(document, birthRecord);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        document.save(outputStream);
        document.close();
        return outputStream;
    }

    private void updatePage0(PDDocument document, RegistrationOfBirthCertificate birthRecord)
            throws IOException {
        PDPage page = document.getPage(0);
        PDPageContentStream contentStream = new PDPageContentStream(document, page,
                PDPageContentStream.AppendMode.APPEND, true, true);

        contentStream.setFont(PDType1Font.COURIER, 10);

        float line = 810;
        contentStream.beginText();
        contentStream.newLineAtOffset(26, line);
        contentStream.showText("[" + birthRecord.getId() + "-" + birthRecord.getDownloadCode() + "]");
        contentStream.endText();

        PDType0Font font = PDType0Font.load(document, new File("./conf/seguisb.ttf"));
        contentStream.setFont(font, 11);

        line -= 145;

        String requestPerson = birthRecord.getMotherFullName();
        String requestPersonResidence = birthRecord.getMotherResidence();
        String requestPersonPassportNumber = birthRecord.getMotherPassportNumber();
        String relationship = "Mẹ ruột";
        if (birthRecord.getFatherNationality().toLowerCase().indexOf("nam") > 0) {
            requestPerson = birthRecord.getFatherFullName();
            requestPersonResidence = birthRecord.getFatherResidence();
            requestPersonPassportNumber = birthRecord.getFatherPassportNumber();
            relationship = "Cha ruột";
        }

        contentStream.beginText();
        contentStream.newLineAtOffset(281, line);
        contentStream.showText(requestPerson);
        contentStream.endText();

        line -= 21;
        contentStream.beginText();
        contentStream.newLineAtOffset(181, line);
        contentStream.showText(requestPersonResidence + ", Hàn Quốc");
        contentStream.endText();

        line -= 42;
        contentStream.beginText();
        contentStream.newLineAtOffset(181, line);
        contentStream.showText("Hộ chiếu số " + requestPersonPassportNumber);
        contentStream.endText();

        line -= 38;
        contentStream.beginText();
        contentStream.newLineAtOffset(399, line);
        contentStream.showText(relationship);
        contentStream.endText();

        line -= 46;
        contentStream.beginText();
        contentStream.newLineAtOffset(181, line);
        contentStream.showText(birthRecord.getChildForeignFullName());
        contentStream.endText();

        line -= 20;

        contentStream.beginText();
        contentStream.newLineAtOffset(200, line);
        contentStream.showText(birthRecord.getChildDateOfBirth());
        contentStream.endText();

        line -= 18;
        LocalDate lDate = AppUltil.stringToDate(birthRecord.getChildDateOfBirth());
        contentStream.beginText();
        contentStream.newLineAtOffset(100, line);
        contentStream.showText(String.format("Ngày%s, tháng%s, năm%s", VietNumber.docso(lDate.getDayOfMonth()),
                VietNumber.docso(lDate.getMonthValue()), VietNumber.docso(lDate.getYear())));
        contentStream.endText();

        line -= 20;
        contentStream.beginText();
        contentStream.newLineAtOffset(130, line);
        contentStream.showText(birthRecord.getChildGender());
        contentStream.endText();

        contentStream.beginText();
        contentStream.newLineAtOffset(258, line);
        contentStream.showText(birthRecord.getChildEthnicGroup());
        contentStream.endText();

        contentStream.beginText();
        contentStream.newLineAtOffset(410, line);
        contentStream.showText("Việt Nam");
        contentStream.endText();

        line -= 22;
        contentStream.beginText();
        contentStream.newLineAtOffset(140, line);
        contentStream.showText(birthRecord.getChildPlaceOfBirth() + ", Hàn Quốc");
        contentStream.endText();

        line -= 20;
        contentStream.beginText();
        contentStream.newLineAtOffset(140, line);
        contentStream.showText(birthRecord.getChildPlaceOfOrigin());
        contentStream.endText();

        line -= 22;
        contentStream.beginText();
        contentStream.newLineAtOffset(240, line);
        contentStream.showText(birthRecord.getMotherFullName());
        contentStream.endText();

        line -= 20;
        lDate = AppUltil.stringToDate(birthRecord.getMotherDateOfBirth());
        contentStream.beginText();
        contentStream.newLineAtOffset(155, line);
        contentStream.showText(String.valueOf(lDate.getYear()));
        contentStream.endText();

        contentStream.beginText();
        contentStream.newLineAtOffset(278, line);
        contentStream.showText(birthRecord.getMotherEthnicGroup());
        contentStream.endText();

        contentStream.beginText();
        contentStream.newLineAtOffset(410, line);
        contentStream.showText(birthRecord.getMotherNationality());
        contentStream.endText();

        line -= 22;
        contentStream.beginText();
        contentStream.newLineAtOffset(155, line);
        contentStream.showText(birthRecord.getMotherResidence() + ", Hàn Quốc");
        contentStream.endText();

        line -= 39;
        contentStream.beginText();
        contentStream.newLineAtOffset(240, line);
        contentStream.showText(birthRecord.getFatherFullName());
        contentStream.endText();

        line -= 20;
        lDate = AppUltil.stringToDate(birthRecord.getFatherDateOfBirth());
        contentStream.beginText();
        contentStream.newLineAtOffset(155, line);
        contentStream.showText(String.valueOf(lDate.getYear()));
        contentStream.endText();

        contentStream.beginText();
        contentStream.newLineAtOffset(278, line);
        contentStream.showText(birthRecord.getFatherEthnicGroup());
        contentStream.endText();

        contentStream.beginText();
        contentStream.newLineAtOffset(410, line);
        contentStream.showText(birthRecord.getFatherNationality());
        contentStream.endText();

        line -= 22;
        contentStream.beginText();
        contentStream.newLineAtOffset(155, line);
        contentStream.showText(birthRecord.getFatherResidence() + ", Hàn Quốc");
        contentStream.endText();

        line -= 40;
        contentStream.beginText();
        contentStream.newLineAtOffset(245, line);
        contentStream.showText(birthRecord.getChildBirthCertificateIssuedPlace() + ", Korea");
        contentStream.endText();

        line -= 20;
        contentStream.beginText();
        contentStream.newLineAtOffset(181, line);
        contentStream.showText(birthRecord.getChildBirthCertificateNumber());
        contentStream.endText();

        contentStream.beginText();
        contentStream.newLineAtOffset(345, line);
        contentStream.showText(birthRecord.getChildBirthCertificateIssuedDate());
        contentStream.endText();

        lDate = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        line -= 53;

        contentStream.beginText();
        contentStream.newLineAtOffset(281, line);
        contentStream.showText(String.format("%s, ngày %02d tháng %02d năm %d ", requestPersonResidence,
                lDate.getDayOfMonth(), lDate.getMonthValue(), lDate.getYear()));
        contentStream.endText();

        line -= 100;
        contentStream.beginText();
        contentStream.newLineAtOffset(372, line);
        contentStream.showText(AppUltil.toTitleCase(requestPerson));
        contentStream.endText();

        contentStream.close();
    }

    private void updatePage1(PDDocument document, RegistrationOfBirthCertificate birthRecord)
            throws IOException {
        PDPage page = document.getPage(1);
        PDPageContentStream contentStream = new PDPageContentStream(document, page,
                PDPageContentStream.AppendMode.APPEND, true, true);

        contentStream.setFont(PDType1Font.COURIER, 10);
        float line = 810;
        contentStream.beginText();
        contentStream.newLineAtOffset(26, line);
        contentStream.showText("[" + birthRecord.getId() + "]");
        contentStream.endText();

        PDType0Font font = PDType0Font.load(document, new File("./conf/seguisb.ttf"));
        contentStream.setFont(font, 10);

        line -= 140;

        String requestPerson = birthRecord.getMotherFullName();
        String theOther = birthRecord.getFatherFullName();
        String requestPersonResidence = birthRecord.getMotherResidence();
        String requestPersonPassportNumber = birthRecord.getMotherPassportNumber();
        String relationship = "Mẹ ruột";
        if (birthRecord.getFatherNationality().toLowerCase().indexOf("nam") > 0) {
            requestPerson = birthRecord.getFatherFullName();
            requestPersonResidence = birthRecord.getFatherResidence();
            requestPersonPassportNumber = birthRecord.getFatherPassportNumber();
            relationship = "Cha ruột";
            theOther = birthRecord.getMotherFullName();
        }

        contentStream.beginText();
        contentStream.newLineAtOffset(281, line);
        contentStream.showText(requestPerson);
        contentStream.endText();

        line -= 34;
        contentStream.beginText();
        contentStream.newLineAtOffset(171, line);
        contentStream.showText(requestPersonResidence + ", Hàn Quốc");
        contentStream.endText();

        line -= 34;
        contentStream.beginText();
        contentStream.newLineAtOffset(201, line);
        contentStream.showText("Hộ chiếu số " + requestPersonPassportNumber);
        contentStream.endText();

        line -= 35;
        contentStream.beginText();
        contentStream.newLineAtOffset(231, line);
        contentStream.showText(relationship);
        contentStream.endText();

        line -= 17;
        contentStream.beginText();
        contentStream.newLineAtOffset(261, line);
        contentStream.showText("Thay đổi họ và tên");
        contentStream.endText();

        line -= 35;
        contentStream.beginText();
        contentStream.newLineAtOffset(201, line);
        contentStream.showText(birthRecord.getChildForeignFullName());
        contentStream.endText();

        line -= 17;

        contentStream.beginText();
        contentStream.newLineAtOffset(210, line);
        contentStream.showText(birthRecord.getChildDateOfBirth());
        contentStream.endText();

        line -= 17;
        contentStream.beginText();
        contentStream.newLineAtOffset(140, line);
        contentStream.showText(birthRecord.getChildGender());
        contentStream.endText();

        contentStream.beginText();
        contentStream.newLineAtOffset(248, line);
        contentStream.showText(birthRecord.getChildEthnicGroup());
        contentStream.endText();

        contentStream.beginText();
        contentStream.newLineAtOffset(455, line);
        contentStream.showText("Việt Nam");
        contentStream.endText();

        line -= 34;
        contentStream.beginText();
        contentStream.newLineAtOffset(171, line);
        contentStream.showText(requestPersonResidence + ", Hàn Quốc");
        contentStream.endText();

        line -= 34;
        contentStream.beginText();
        contentStream.newLineAtOffset(182, line);
        contentStream.showText("Trích lục Ghi chú khai sinh");
        contentStream.endText();

        line -= 34;
        contentStream.beginText();
        contentStream.newLineAtOffset(151, line);
        contentStream.showText("ĐSQ Việt Nam tại Hàn Quốc");
        contentStream.endText();

        //
        line -= 34;
        contentStream.beginText();
        contentStream.newLineAtOffset(191, line);
        contentStream.showText("Được thay đổi họ và tên");
        contentStream.endText();

        line -= 17;
        contentStream.beginText();
        contentStream.newLineAtOffset(201, line);
        contentStream.showText("Từ: " + birthRecord.getChildForeignFullName());
        contentStream.endText();

        line -= 17;
        contentStream.beginText();
        contentStream.newLineAtOffset(201, line);
        contentStream.showText("Thành: " + birthRecord.getChildVietnameseFullName());
        contentStream.endText();

        line -= 17;
        line -= 17;
        line -= 17;
        line -= 17;
        line -= 17;
        line -= 17;

        LocalDate lDate = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        line -= 8;
        // contentStream.setFont(font, 12);
        contentStream.beginText();
        contentStream.newLineAtOffset(281, line);
        contentStream.showText(String.format("%s, ngày %02d tháng %02d năm %d ", requestPersonResidence,
                lDate.getDayOfMonth(), lDate.getMonthValue(), lDate.getYear()));
        contentStream.endText();

        line -= 75;
        // contentStream.setFont(font, 13);
        contentStream.beginText();
        contentStream.newLineAtOffset(412, line);
        contentStream.showText(AppUltil.toTitleCase(requestPerson));
        contentStream.endText();

        line -= 17 * 3;
        contentStream.beginText();
        contentStream.newLineAtOffset(355, line);
        contentStream.showText("Tôi đã đọc và đồng ý với các nội dung");
        contentStream.endText();
        line -= 15;
        contentStream.beginText();
        contentStream.newLineAtOffset(381, line);
        contentStream.showText("mà người yêu cầu đã đưa ra.");
        contentStream.endText();
        line -= 17 * 2 + 8;
        contentStream.beginText();
        contentStream.newLineAtOffset(412, line);
        contentStream.showText(AppUltil.toTitleCase(theOther));
        contentStream.endText();

        // Tôi đồng ý thực hiện thay đổi họ, chữ đệm, tên theo yêu cầu
        contentStream.close();
    }

    private void updatePage2(PDDocument document,
            RegistrationOfBirthCertificate birthRecord)
            throws IOException {
        PDPage page = document.getPage(2);
        PDPageContentStream contentStream = new PDPageContentStream(document, page,
                PDPageContentStream.AppendMode.APPEND, true, true);

        contentStream.setFont(PDType1Font.COURIER, 10);

        float line = 810;
        contentStream.beginText();
        contentStream.newLineAtOffset(26, line);
        contentStream.showText("[" + birthRecord.getId() + "]");
        contentStream.endText();

        PDType0Font font = PDType0Font.load(document, new File("./conf/seguisb.ttf"));
        contentStream.setFont(font, 11);

        line -= 165;

        LocalDate lDate = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        contentStream.beginText();
        contentStream.newLineAtOffset(133, line);
        contentStream.showText(String.format("%02d", lDate.getDayOfMonth()));
        contentStream.endText();

        contentStream.beginText();
        contentStream.newLineAtOffset(183, line);
        contentStream.showText(String.format("%02d", lDate.getMonthValue()));
        contentStream.endText();

        contentStream.beginText();
        contentStream.newLineAtOffset(233, line);
        contentStream.showText(String.format("%02d", lDate.getYear()));
        contentStream.endText();

        line -= 30;
        contentStream.beginText();
        contentStream.newLineAtOffset(182, line);
        contentStream.showText(birthRecord.getFatherFullName());
        contentStream.endText();

        line -= 15;
        contentStream.beginText();
        contentStream.newLineAtOffset(111, line);
        contentStream.showText(birthRecord.getFatherDateOfBirth());
        contentStream.endText();
        line -= 15;
        line -= 15;
        contentStream.beginText();
        contentStream.newLineAtOffset(301, line);
        contentStream.showText(birthRecord.getFatherPassportNumber());
        contentStream.endText();

        line -= 15;
        contentStream.beginText();
        contentStream.newLineAtOffset(202, line);
        contentStream.showText(birthRecord.getFatherEthnicGroup());
        contentStream.endText();

        contentStream.beginText();
        contentStream.newLineAtOffset(412, line);
        contentStream.showText(birthRecord.getFatherNationality());
        contentStream.endText();

        line -= 15;
        contentStream.beginText();
        contentStream.newLineAtOffset(252, line);
        contentStream.showText(birthRecord.getFatherResidence() + ", Hàn Quốc");
        contentStream.endText();

        line -= 29;
        contentStream.beginText();
        contentStream.newLineAtOffset(182, line);
        contentStream.showText(birthRecord.getMotherFullName());
        contentStream.endText();

        line -= 15;
        contentStream.beginText();
        contentStream.newLineAtOffset(111, line);
        contentStream.showText(birthRecord.getMotherDateOfBirth());
        contentStream.endText();
        line -= 15;
        line -= 15;
        contentStream.beginText();
        contentStream.newLineAtOffset(301, line);
        contentStream.showText(birthRecord.getMotherPassportNumber());
        contentStream.endText();

        line -= 15;
        contentStream.beginText();
        contentStream.newLineAtOffset(202, line);
        contentStream.showText(birthRecord.getMotherEthnicGroup());
        contentStream.endText();

        contentStream.beginText();
        contentStream.newLineAtOffset(412, line);
        contentStream.showText(birthRecord.getMotherNationality());
        contentStream.endText();

        line -= 15;
        contentStream.beginText();
        contentStream.newLineAtOffset(252, line);
        contentStream.showText(birthRecord.getMotherResidence() + ", Hàn Quốc");
        contentStream.endText();

        line -= 15;
        line -= 15;
        line -= 15;
        contentStream.beginText();
        contentStream.newLineAtOffset(202, line);
        contentStream.showText(birthRecord.getChildForeignFullName());
        contentStream.endText();

        line -= 15;
        contentStream.beginText();
        contentStream.newLineAtOffset(111, line);
        contentStream.showText(birthRecord.getChildDateOfBirth());
        contentStream.endText();

        contentStream.beginText();
        contentStream.newLineAtOffset(286, line);
        contentStream.showText(birthRecord.getChildGender());
        contentStream.endText();

        line -= 15;
        line -= 15;

        contentStream.beginText();
        contentStream.newLineAtOffset(301, line);
        contentStream.showText(birthRecord.getChildBirthCertificateNumber());
        contentStream.endText();

        line -= 15;
        contentStream.beginText();
        contentStream.newLineAtOffset(202, line);
        contentStream.showText(birthRecord.getChildEthnicGroup());
        contentStream.endText();

        contentStream.beginText();
        contentStream.newLineAtOffset(401, line);
        contentStream.showText("Việt Nam");
        contentStream.endText();

        line -= 15 * 10;

        contentStream.beginText();
        contentStream.newLineAtOffset(135, line);
        contentStream.showText(AppUltil.toTitleCase(birthRecord.getMotherFullName()));
        contentStream.endText();

        contentStream.beginText();
        contentStream.newLineAtOffset(371, line);
        contentStream.showText(AppUltil.toTitleCase(birthRecord.getFatherFullName()));
        contentStream.endText();

        contentStream.close();
    }

    private void updatePage3(PDDocument document, RegistrationOfBirthCertificate p)
            throws IOException {
        PDPage page = document.getPage(3);

        PDPageContentStream contentStream = new PDPageContentStream(document, page,
                PDPageContentStream.AppendMode.APPEND, true, true);

        contentStream.setFont(PDType1Font.COURIER, 10);
        float line = 810;
        float lineHeight = 19;
        contentStream.beginText();
        contentStream.newLineAtOffset(26, line);
        contentStream.showText("[" + p.getId() + "]");
        contentStream.endText();

        PDType0Font font = PDType0Font.load(document, new File("./conf/seguisb.ttf"));
        contentStream.setFont(font, 11);

        // representative
        String rName = p.getMotherFullName();
        String rGender = "Nữ";
        String rPassportNumber = p.getMotherPassportNumber();
        String rResidence = p.getMotherResidence();
        String rRelationship = "Mẹ ruột";
        LocalDate lDate = AppUltil.stringToDate(p.getMotherDateOfBirth());
        if (p.getFatherNationality().toLowerCase().indexOf("nam") > 0) {
            rName = p.getFatherFullName();
            rGender = "Nam";
            rPassportNumber = p.getFatherPassportNumber();
            rResidence = p.getFatherResidence();
            rRelationship = "Cha ruột";
            lDate = AppUltil.stringToDate(p.getFatherDateOfBirth());
        }

        line -= 202;
        contentStream.beginText();
        contentStream.newLineAtOffset(108, line);
        contentStream.showText(rName);
        contentStream.endText();

        line -= lineHeight;        
        contentStream.beginText();
        contentStream.newLineAtOffset(108, line);
        contentStream.showText(
                String.format("%02d                       %02d                      %d", lDate.getDayOfMonth(),
                        lDate.getMonthValue(), lDate.getYear()));
        contentStream.endText();

        contentStream.beginText();
        contentStream.newLineAtOffset(410, line);
        contentStream.showText(rGender);
        contentStream.endText();

        // CMND/CCCD
        line -= lineHeight;
        int beginPos = 241;
        int nextPos = 0;

        if (rPassportNumber == null) {
            rPassportNumber = "";
        }

        for (int i = 0; i < rPassportNumber.length(); i++) {
            contentStream.beginText();
            contentStream.newLineAtOffset(beginPos + nextPos, line);
            contentStream.showText(String.valueOf(rPassportNumber.charAt(i)));
            contentStream.endText();
            if (i == 5 || i == 6)
                nextPos += 15;
            else
                nextPos += 14;
        }

        line -= lineHeight * 2;
        contentStream.beginText();
        contentStream.newLineAtOffset(180, line);
        contentStream.showText(rResidence + ", Hàn Quốc");
        contentStream.endText();
        line -= lineHeight * 2;
        contentStream.beginText();
        contentStream.newLineAtOffset(382, line);
        contentStream.showText(rRelationship);
        contentStream.endText();

        // Add the fullName text string
        line -= lineHeight * 2;
        String name = p.getChildVietnameseFullName();
        String firstName = "";
        String lastName = "";

        if (name.split("\\w+").length > 1) {
            firstName = name.substring(0, name.indexOf(" "));
            lastName = name.substring(name.indexOf(" ") + 1);
        } else {
            firstName = name;
        }

        contentStream.beginText();
        contentStream.newLineAtOffset(65, line);
        contentStream.showText(firstName);
        contentStream.endText();

        contentStream.beginText();
        contentStream.newLineAtOffset(65 + 180, line);
        contentStream.showText(lastName);
        contentStream.endText();

        // Add the Sex text string
        contentStream.beginText();
        contentStream.newLineAtOffset(490, line);
        contentStream.showText(p.getChildGender());
        contentStream.endText();

        // Add the dateOfBirth text string
        line -= lineHeight;
        lDate = AppUltil.stringToDate(p.getChildDateOfBirth());
        contentStream.beginText();
        contentStream.newLineAtOffset(108, line);
        contentStream.showText(String.format("%02d                   %02d                  %d", lDate.getDayOfMonth(),
                lDate.getMonthValue(), lDate.getYear()));
        contentStream.endText();

        // placeOfBirth
        contentStream.beginText();
        contentStream.newLineAtOffset(450, line);
        contentStream.showText("Hàn Quốc");
        contentStream.endText();

        line -= lineHeight * 2;
        contentStream.beginText();
        contentStream.newLineAtOffset(100, line);
        contentStream.showText("Việt Nam");
        contentStream.endText();

        contentStream.beginText();
        contentStream.newLineAtOffset(240, line);
        contentStream.showText(p.getChildEthnicGroup());
        contentStream.endText();

        line -= lineHeight * 2;
        // foreignAddress
        contentStream.beginText();
        contentStream.newLineAtOffset(180, line);
        contentStream.showText(p.getMotherResidence() + ", Hàn Quốc");
        contentStream.endText();

        line -= lineHeight * 2 + 15;
        contentStream.beginText();
        contentStream.newLineAtOffset(140, line);
        contentStream.showText(p.getFatherFullName());
        contentStream.endText();

        contentStream.beginText();
        contentStream.newLineAtOffset(455, line);
        contentStream.showText(p.getFatherDateOfBirth());
        contentStream.endText();

        line -= lineHeight;
        contentStream.beginText();
        contentStream.newLineAtOffset(140, line);
        contentStream.showText(p.getMotherFullName());
        contentStream.endText();

        contentStream.beginText();
        contentStream.newLineAtOffset(455, line);
        contentStream.showText(p.getMotherDateOfBirth());
        contentStream.endText();

        line -= lineHeight * 2;
        contentStream.beginText();
        contentStream.newLineAtOffset(180, line);
        contentStream.showText("Đề nghị cấp hộ chiếu lần đầu");
        contentStream.endText();

        line -= lineHeight * 2;
        lDate = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        contentStream.beginText();
        contentStream.newLineAtOffset(321, line);
        contentStream.showText(String.format("%s, ngày %02d tháng %02d năm %d ", p.getMotherResidence(),
                lDate.getDayOfMonth(), lDate.getMonthValue(), lDate.getYear()));
        contentStream.endText();

        line -= lineHeight * 5;
        //String requestPerson = p.getMotherFullName();
        //if (p.getFatherNationality().toLowerCase().indexOf("nam") > 0) {
        //    requestPerson = p.getFatherFullName();
        //}
        contentStream.beginText();
        contentStream.newLineAtOffset(393, line);
        contentStream.showText(AppUltil.toTitleCase(rName));
        contentStream.endText();

        // Close the content stream
        contentStream.close();
    }

}