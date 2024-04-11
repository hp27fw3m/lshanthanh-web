package nb.hanquoc.web.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nb.hanquoc.web.AppUltil;
import nb.hanquoc.core.entity.Marriage;
import nb.hanquoc.web.repository.MarriageRepository;

@Service
public class MarriageService {
    @Autowired
    private MarriageRepository marriageRepository;

    public List<Marriage> getAllPassport() {
        return (List<Marriage>) marriageRepository.findAll();
    }

    @SuppressWarnings("null")
    public Marriage save(Marriage person) {
        return marriageRepository.save(person);
    }

    @SuppressWarnings("null")
    public Optional<Marriage> findById(Long id) {
        return marriageRepository.findById(id);
    }

    private static HashMap<Integer, byte[]> templateMap = new HashMap<>();

    private static byte[] getTemplateBinary(int pageIndex) throws IOException {

        byte[] templateBinary = templateMap.get(pageIndex);
        if (templateBinary == null) {
            switch (pageIndex) {
                case 0:
                    templateBinary = Files.readAllBytes(Paths.get("./conf/blank.pdf"));
                    break;
                case 1:
                    templateBinary = Files.readAllBytes(Paths.get("./conf/marriage_cn.pdf"));
                    break;
            }
            templateMap.put(pageIndex, templateBinary);
        }
        return templateBinary;
    }

    public ByteArrayOutputStream createPDFPage(Marriage p)
            throws ParseException, IOException {

        switch (p.getFormType()) {
            case "CN":
                return createCNPage(p);
            default:
                return null;
        }

    }

    private ByteArrayOutputStream createCNPage(Marriage p)
            throws ParseException, IOException {

        PDDocument document = PDDocument.load(getTemplateBinary(1));
        PDPage page = document.getPage(0);

        PDPageContentStream contentStream = new PDPageContentStream(document, page,
                PDPageContentStream.AppendMode.APPEND, true, true);

        contentStream.setFont(PDType1Font.COURIER, 10);
        float lineHeight = 22;
        float line = 810;
        line -= 36;
        contentStream.beginText();
        contentStream.newLineAtOffset(28, line);
        contentStream.showText("[" + p.getId() + "-" + p.getDownloadCode() + "]");
        contentStream.endText();

        PDType0Font font = PDType0Font.load(document, new File("./conf/seguisb.ttf"));
        contentStream.setFont(font, 11);

        line -= 100;
        line -= lineHeight + 12;
        contentStream.beginText();
        contentStream.newLineAtOffset(210, line);
        contentStream.showText(p.getP1FullName());
        contentStream.endText();

        contentStream.beginText();
        contentStream.newLineAtOffset(460, line);
        contentStream.showText(p.getP1Gender());
        contentStream.endText();

        line -= lineHeight;
        contentStream.beginText();
        contentStream.newLineAtOffset(160, line);
        contentStream.showText(p.getP1DateOfBirth());
        contentStream.endText();

        contentStream.beginText();
        contentStream.newLineAtOffset(300, line);
        contentStream.showText(p.getP1Ethnic());
        contentStream.endText();

        contentStream.beginText();
        contentStream.newLineAtOffset(460, line);
        contentStream.showText(p.getP1Nationality());
        contentStream.endText();

        line -= lineHeight;
        contentStream.beginText();
        contentStream.newLineAtOffset(160, line);
        contentStream.showText(p.getP1PlaceOfBirth() + ", Việt Nam");
        contentStream.endText();

        line -= lineHeight;
        contentStream.beginText();
        contentStream.newLineAtOffset(160, line);
        contentStream.showText(p.getP1PassportNumber());
        contentStream.endText();

        contentStream.beginText();
        contentStream.newLineAtOffset(370, line);
        contentStream.showText(p.getP1PassportIssuedDate());
        contentStream.endText();

        line -= lineHeight;
        contentStream.beginText();
        contentStream.newLineAtOffset(210, line);
        contentStream.showText(p.getP1PassportIssuedPlace());
        contentStream.endText();

        line -= lineHeight;
        contentStream.beginText();
        contentStream.newLineAtOffset(182, line);
        contentStream.showText(p.getP1PlaceOfResidenceInVietnamBeforeDeparture());
        contentStream.endText();

        line += 2;
        line -= lineHeight;
        line -= lineHeight;
        line -= lineHeight;
        line -= lineHeight;
        contentStream.beginText();
        contentStream.newLineAtOffset(281, line);
        contentStream.showText(p.getP1MaritalStatus());
        contentStream.endText();

        line -= lineHeight;
        line -= lineHeight + 13;

        contentStream.beginText();
        contentStream.newLineAtOffset(140, line);
        contentStream.showText(p.getP2FullName());
        contentStream.endText();

        contentStream.beginText();
        contentStream.newLineAtOffset(320, line);
        contentStream.showText(p.getP2DateOfBirth());
        contentStream.endText();

        contentStream.beginText();
        contentStream.newLineAtOffset(460, line);
        contentStream.showText(p.getP2Nationality());
        contentStream.endText();

        line -= lineHeight;
        contentStream.beginText();
        contentStream.newLineAtOffset(160, line);
        contentStream.showText(p.getP2PassportNumber());
        contentStream.endText();

        contentStream.beginText();
        contentStream.newLineAtOffset(370, line);
        contentStream.showText(p.getP2PassportIssuedDate());
        contentStream.endText();

        line -= lineHeight;
        contentStream.beginText();
        contentStream.newLineAtOffset(210, line);
        contentStream.showText(p.getP2PassportIssuedPlace());
        contentStream.endText();

        line -= lineHeight;
        contentStream.beginText();
        contentStream.newLineAtOffset(210, line);
        contentStream.showText(p.getP2CurrentPlaceOfResidence());
        contentStream.endText();

        line -= lineHeight;
        line -= lineHeight;
        line -= lineHeight;
        line -= 14;

        LocalDate lDate = LocalDate.now();
        contentStream.beginText();
        contentStream.newLineAtOffset(390, line);
        contentStream.showText(String.format("%02d", lDate.getDayOfMonth()));
        contentStream.endText();

        contentStream.beginText();
        contentStream.newLineAtOffset(445, line);
        contentStream.showText(String.format("%02d", lDate.getMonthValue()));
        contentStream.endText();

        contentStream.beginText();
        contentStream.newLineAtOffset(490, line);
        contentStream.showText("" + lDate.getYear());
        contentStream.endText();

        line -= lineHeight * 4 + 10;
        contentStream.beginText();
        contentStream.newLineAtOffset(350, line);
        contentStream.showText(AppUltil.toTitleCase(p.getP1FullName()));
        contentStream.endText();

        contentStream.close();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        document.save(outputStream);
        document.close();
        return outputStream;
    }
}
