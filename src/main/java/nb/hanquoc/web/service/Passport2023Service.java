/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package nb.hanquoc.web.service;

import nb.hanquoc.web.repository.Passport2023Repository;
import nb.hanquoc.web.repository.PassportRepository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import nb.hanquoc.web.AppUltil;
import nb.hanquoc.core.entity.Passport;
import nb.hanquoc.core.entity.Passport2023;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

@Service
public class Passport2023Service {

    @Autowired
    private Passport2023Repository passport2023Repository;
    @Autowired
    private PassportRepository passportRepository;

    public List<Passport2023> getAllPassport() {
        return (List<Passport2023>) passport2023Repository.findAll();
    }

    
    public Passport2023 savePassport(Passport2023 person) {
        return passport2023Repository.save(person);
    }

    
    public void deletePassport(Long id) {
        passport2023Repository.deleteById(id);
    }

    
    public Passport2023 findPassportById(Long id) {
        Optional<Passport2023> otionalP2023 = passport2023Repository.findById(id);
        if (otionalP2023.isPresent()) {
            return otionalP2023.get();
        } else {            
            Optional<Passport> p = passportRepository.findById(id);
            if (p.isPresent()) {
                return convertPassportToPassport2023(p.get());
            } else {
                return new Passport2023();
            }
        }

    }

    private static HashMap<Integer, byte[]> templateMap = new HashMap<>();

    private static byte[] getTemplateBinary(int page) throws IOException {
        byte[] templateBinary = templateMap.get(page);
        if (templateBinary == null) {
            switch (page) {
                case 0:
                    templateBinary = Files.readAllBytes(Paths.get("./conf/tk02_1.pdf"));
                    break;
                case 1:
                    templateBinary = Files.readAllBytes(Paths.get("./conf/tk02a.pdf"));
                    break;
            }
            templateMap.put(page, templateBinary);
        }
        return templateBinary;
    }

    public ByteArrayOutputStream createPdfForm1(Passport2023 p) throws ParseException, IOException {

        PDDocument pdfTemplate = PDDocument.load(getTemplateBinary(0));

        LocalDate lDate;
        // Get the first page
        PDPage page = pdfTemplate.getPage(0);

        // Create a content stream for the page
        PDPageContentStream contentStream = new PDPageContentStream(pdfTemplate, page,
                PDPageContentStream.AppendMode.APPEND, true, true);

        contentStream.setFont(PDType1Font.COURIER, 10);
        String text = "[" + p.getId() + "-" + p.getDownloadCode() + "]";
        if (p.getP1AgencyName() != null && p.getP1AgencyName().length() > 0) {
            text = "[" + p.getId() + "-" + p.getDownloadCode() + "-" + p.getP1AgencyName() + "]";
        }
        float line = 815;
        // Add the fullName text string
        contentStream.beginText();
        contentStream.newLineAtOffset(355, line);
        contentStream.showText(text);
        contentStream.endText();

        // Set the font and font size
        PDType0Font font = PDType0Font.load(pdfTemplate, new File("./conf/seguisb.ttf"));
        contentStream.setFont(font, 11);

        // Add the fullName text string
        String name = p.getP1FullName();
        String firstName = "";
        String lastName = "";

        if (name.split("\\w+").length > 1) {
            firstName = name.substring(0, name.indexOf(" "));
            lastName = name.substring(name.indexOf(" ") + 1);
        } else {
            firstName = name;
        }

        line -= 205;
        contentStream.beginText();
        // contentStream.setFont(font, 12);
        contentStream.newLineAtOffset(81, line);
        contentStream.showText(firstName);
        contentStream.endText();

        contentStream.beginText();
        // contentStream.setFont(font, 12);
        contentStream.newLineAtOffset(82 + 150, line);
        contentStream.showText(lastName);
        contentStream.endText();

        // Add the Sex text string
        contentStream.beginText();
        contentStream.newLineAtOffset(82 + 150 + 265 + 10, line);
        contentStream.showText(p.getP1Gender());
        contentStream.endText();

        // Add the dateOfBirth text string
        // LocalDate localDate = AppUltil.stringToDate(p.getDateOfBirth());
        line -= 23;
        lDate = AppUltil.stringToDate(p.getP1DateOfBirth());

        int birthYear = lDate.getYear();
        String dob = p.getP1DateOfBirth();

        if (dob.contains("00/")) {
            birthYear++;
            contentStream.beginText();
            contentStream.newLineAtOffset(215 + 15, line);
            contentStream.showText("" + birthYear);
            contentStream.endText();

        } else {
            contentStream.beginText();
            contentStream.newLineAtOffset(108 + 10, line);
            contentStream.showText(String.format("%02d                 %02d              %d", lDate.getDayOfMonth(),
                    lDate.getMonthValue(), lDate.getYear()));
            contentStream.endText();
        }

        // placeOfBirth
        contentStream.beginText();
        contentStream.newLineAtOffset(400 + 15, line);
        contentStream.showText(p.getP1PlaceOfBirth());
        contentStream.endText();

        line -= 26;
        int beginPos = 160;
        int nextPos = 0;

        String identityNumber = p.getP1IdentityCardNumber();

        if (identityNumber == null) {
            identityNumber = "";
        }

        for (int i = 0; i < identityNumber.length(); i++) {
            contentStream.beginText();
            contentStream.newLineAtOffset(beginPos + nextPos, line);
            contentStream.showText(String.valueOf(identityNumber.charAt(i)));
            contentStream.endText();
            if (i == 5 || i == 6)
                nextPos += 15;
            else
                nextPos += 14;
        }
        nextPos += 9 * 14;

        // font = PDType0Font.load(document, new File("./conf/timesbi.ttf"));
        // contentStream.setFont(font, 12);

        contentStream.beginText();
        contentStream.newLineAtOffset(beginPos + 250, line);
        contentStream.showText(p.getP1IdentityCardIssuedDate());
        contentStream.endText();
        nextPos += 14;

        line -= 26;
        // ethinic
        contentStream.beginText();
        contentStream.newLineAtOffset(105, line);
        contentStream.showText(p.getP1Ethnic());
        contentStream.endText();

        // phoneNumber
        contentStream.beginText();
        contentStream.newLineAtOffset(450, line);
        contentStream.showText(p.getP1PhoneNumber());
        // contentStream.showText("010-5120-1982");
        contentStream.endText();

        line -= 44;
        // foreignAddress
        contentStream.beginText();
        contentStream.newLineAtOffset(80, line);
        contentStream.showText(p.getP1ForeignAddress() + ", Hàn Quốc");
        contentStream.endText();

        line -= 36;
        // getVietnameseAddress
        contentStream.beginText();
        contentStream.newLineAtOffset(80, line);
        contentStream.showText(p.getP1VietnameseAddress());
        contentStream.endText();

        line -= 34;
        // father
        contentStream.beginText();
        contentStream.newLineAtOffset(140, line);
        contentStream.showText(p.getP1FatherFullName());
        contentStream.endText();

        contentStream.beginText();
        contentStream.newLineAtOffset(440, line);
        contentStream.showText(p.getP1FatherDateOfBirth() == null ? "" : p.getP1FatherDateOfBirth().replace("00/", "__/"));
        contentStream.endText();

        line -= 18;
        // Mother
        contentStream.beginText();
        contentStream.newLineAtOffset(140, line);
        contentStream.showText(p.getP1MotherFullName());
        contentStream.endText();

        contentStream.beginText();
        contentStream.newLineAtOffset(440, line);
        contentStream.showText(p.getP1MotherDateOfBirth() == null ? "" : p.getP1MotherDateOfBirth().replace("00/", "__/"));
        contentStream.endText();

        line -= 36 * 2;
        // departure
        contentStream.beginText();
        contentStream.newLineAtOffset(201, line);
        contentStream.showText(p.getP1DepartureDate());
        contentStream.endText();

        contentStream.beginText();
        contentStream.newLineAtOffset(360, line);
        contentStream.showText(p.getP1DeparturePlace());
        contentStream.endText();

        line -= 18;
        // Last Passport
        contentStream.beginText();
        contentStream.newLineAtOffset(290, line);
        contentStream.showText(p.getP1PassportNumber());
        contentStream.endText();

        contentStream.beginText();
        contentStream.newLineAtOffset(440, line);
        contentStream.showText(p.getP1PassportIssuedDate());
        contentStream.endText();

        line -= 18;
        contentStream.beginText();
        contentStream.newLineAtOffset(180, line);
        contentStream.showText(AppUltil.isNullOrEmpty(p.getP1PassportNumber()) ? "" : "Đề nghị cấp lại hộ chiếu");
        contentStream.endText();

        line -= 18 * 3 + 3;

        lDate = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        contentStream.beginText();
        contentStream.newLineAtOffset(295, line);
        contentStream.showText(String.format("%s, ngày %02d tháng %02d năm %d ", p.getP1ForeignAddress(),
                lDate.getDayOfMonth(), lDate.getMonthValue(), lDate.getYear()));
        contentStream.endText();

        line -= 95;
        int curentYear = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getYear();
        if (curentYear - birthYear > 14) {
            contentStream.beginText();
            contentStream.newLineAtOffset(382, line);
            contentStream.showText(AppUltil.toTitleCase(p.getP1FullName()));
            contentStream.endText();
        }

        // Close the content stream
        contentStream.close();

        // Save the PDF to a file
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        pdfTemplate.save(outputStream);
        pdfTemplate.close();
        return outputStream;

    }

    public ByteArrayOutputStream createPdfForm2(Passport2023 p) throws ParseException, IOException {
        PDDocument document = PDDocument.load(getTemplateBinary(1));

        PDPage page = document.getPage(0);
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
        String rName = p.getP2FullName();

        String rGender = "father".equals(p.getP2Role()) ? "Nam" : "Nữ";

        String rPassportNumber = p.getP2PassportNumber();
        String rResidence = p.getP2CurrentPlaceOfResidence();
        String rRelationship = "father".equals(p.getP2Role()) ? "Cha ruột" : "Mẹ ruột";
        LocalDate lDate = AppUltil.stringToDate(p.getP2DateOfBirth());

        line -= 202;
        contentStream.beginText();
        contentStream.newLineAtOffset(108, line);
        contentStream.showText(rName);
        contentStream.endText();

        line -= lineHeight;
        contentStream.beginText();
        contentStream.newLineAtOffset(108, line);
        contentStream.showText(
                String.format("%02d                       %02d                      %d",
                        lDate.getDayOfMonth(),
                        lDate.getMonthValue(),
                        lDate.getYear()));
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

        contentStream.beginText();
        contentStream.newLineAtOffset(480, line);
        contentStream.showText(p.getP2PassportIssuedDate());
        contentStream.endText();

        line -= lineHeight * 2;
        contentStream.beginText();
        contentStream.newLineAtOffset(80, line);
        contentStream.showText(rResidence + ", Hàn Quốc");
        contentStream.endText();

        line -= lineHeight;
        contentStream.beginText();
        contentStream.newLineAtOffset(180, line);
        contentStream.showText(p.getP2PhoneNumber());
        contentStream.endText();

        line -= lineHeight;
        contentStream.beginText();
        contentStream.newLineAtOffset(382, line);
        contentStream.showText(rRelationship);
        contentStream.endText();

        // Add the fullName text string
        line -= lineHeight * 2;
        String name = p.getP1FullName();
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
        contentStream.newLineAtOffset(65 + 190, line);
        contentStream.showText(lastName);
        contentStream.endText();

        // Add the Sex text string
        contentStream.beginText();
        contentStream.newLineAtOffset(490, line);
        contentStream.showText(p.getP1Gender());
        contentStream.endText();

        // Add the dateOfBirth text string
        line -= lineHeight;
        lDate = AppUltil.stringToDate(p.getP1DateOfBirth());
        contentStream.beginText();
        contentStream.newLineAtOffset(108, line);
        contentStream.showText(
                String.format("%02d                   %02d                  %d", lDate.getDayOfMonth(),
                        lDate.getMonthValue(), lDate.getYear()));
        contentStream.endText();

        // placeOfBirth
        contentStream.beginText();
        contentStream.newLineAtOffset(450, line);
        contentStream.showText(p.getP1PlaceOfBirth());
        contentStream.endText();

        line -= lineHeight * 2;
        contentStream.beginText();
        contentStream.newLineAtOffset(100, line);
        contentStream.showText("Việt Nam");
        contentStream.endText();

        contentStream.beginText();
        contentStream.newLineAtOffset(240, line);
        contentStream.showText(p.getP1Ethnic());
        contentStream.endText();

        line -= lineHeight * 2;
        // foreignAddress
        contentStream.beginText();
        contentStream.newLineAtOffset(80, line);
        contentStream.showText(p.getP1ForeignAddress() + ", Hàn Quốc");
        contentStream.endText();

        line -= lineHeight * 2;
        // foreignAddress
        contentStream.beginText();
        contentStream.newLineAtOffset(80, line);
        contentStream.showText(p.getP1VietnameseAddress());
        contentStream.endText();

        line -= lineHeight;
        contentStream.beginText();
        contentStream.newLineAtOffset(140, line);
        contentStream.showText(p.getP1FatherFullName());
        contentStream.endText();

        contentStream.beginText();
        contentStream.newLineAtOffset(455, line);
        contentStream.showText(p.getP1FatherDateOfBirth());
        contentStream.endText();

        line -= lineHeight;
        contentStream.beginText();
        contentStream.newLineAtOffset(140, line);
        contentStream.showText(p.getP1MotherFullName());
        contentStream.endText();

        contentStream.beginText();
        contentStream.newLineAtOffset(455, line);
        contentStream.showText(p.getP1MotherDateOfBirth());
        contentStream.endText();

        line -= lineHeight;
        contentStream.beginText();
        contentStream.newLineAtOffset(300, line);
        contentStream.showText(p.getP1PassportNumber());
        contentStream.endText();

        contentStream.beginText();
        contentStream.newLineAtOffset(455, line);
        contentStream.showText(p.getP1PassportIssuedDate());
        contentStream.endText();

        line -= lineHeight;
        contentStream.beginText();
        contentStream.newLineAtOffset(180, line);
        contentStream.showText(AppUltil.isNullOrEmpty(p.getP1PassportNumber()) ? "Đề nghị cấp hộ chiếu lần đầu"
                : "Đề nghị cấp lại hộ chiếu");
        contentStream.endText();

        line -= lineHeight * 2;
        lDate = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        contentStream.beginText();
        contentStream.newLineAtOffset(321, line);
        contentStream.showText(String.format("%s, ngày %02d tháng %02d năm %d ", p.getP2CurrentPlaceOfResidence(),
                lDate.getDayOfMonth(), lDate.getMonthValue(), lDate.getYear()));
        contentStream.endText();

        line -= lineHeight * 5;

        contentStream.beginText();
        contentStream.newLineAtOffset(393, line);
        contentStream.showText(AppUltil.toTitleCase(rName));
        contentStream.endText();

        // Close the content stream
        contentStream.close();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        document.save(outputStream);
        document.close();
        return outputStream;
    }

    private Passport2023 convertPassportToPassport2023(Passport p) {

        Passport2023 p2023 = new Passport2023();

        p2023.setId(p.getId());
        p2023.setP1PassportNumber(p.getOldPassportNumber());
        p2023.setP1PassportIssuedDate(p.getOldPasswordIssuedDate());
        p2023.setP1PassportPlaceOfIssued(p.getOldPassportPlaceOfIssued());
        p2023.setP1PassportSigner(p.getOldPassportSigner());
        p2023.setP1PositonOfSigner(p.getPositonOfSigner());
        p2023.setP1FullName(p.getFullName());
        p2023.setP1Gender(p.getSex());
        p2023.setP1DateOfBirth(p.getDateOfBirth());
        p2023.setP1PlaceOfBirth(p.getPlaceOfBirth());
        p2023.setP1IdentityCardNumber(p.getIdentityCardNumber());
        p2023.setP1IdentityCardIssuedDate(p.getIdentityCardIssuedDate());
        p2023.setP1IdentityCardIssuedPlace(p.getIdentityCardIssuedPlace());
        p2023.setP1Ethnic(p.getEthnic());
        p2023.setP1PhoneNumber(p.getPhoneNumber());
        p2023.setP1AgencyName(p.getAgencyName());
        p2023.setP1VietnameseAddress(p.getVietnameseAddress());
        p2023.setP1ForeignAddress(p.getForeignAddress());
        p2023.setP1FatherFullName(p.getFatherName());
        p2023.setP1FatherDateOfBirth("00/00/" + p.getFatherYearOfBirth());
        p2023.setP1MotherFullName(p.getMotherName());
        p2023.setP1MotherDateOfBirth("00/00/" + p.getMotherYearOfBirth());
        p2023.setP1DepartureDate(p.getDepartureDate());
        p2023.setDownloadCode(p.getDownloadCode());
        p2023.setCreatedBy(p.getCreatedBy());
        p2023.setCreatedAt(p.getCreatedAt());

        return p2023;

    }

}