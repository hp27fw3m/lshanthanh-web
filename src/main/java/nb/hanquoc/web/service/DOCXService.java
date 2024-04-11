package nb.hanquoc.web.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.springframework.stereotype.Service;

import nb.hanquoc.web.AppUltil;
import nb.hanquoc.core.entity.RegistrationOfBirthCertificate;
import nb.hanquoc.core.entity.RenunciationOfCitizenship;

@Service
public class DOCXService {
    public ByteArrayOutputStream createCurriculumVitae2(RenunciationOfCitizenship record)
            throws ParseException, IOException {
        String template = "./conf/curriculum-vitae-2.docx";

        // Mở tài liệu mẫu
        FileInputStream inputStream = new FileInputStream(new File(template));
        XWPFDocument doc = new XWPFDocument(inputStream);

        // Thay thế các giá trị đại diện bằng dữ liệu thực tế
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("p2FullName", record.getP2FullName());
        placeholders.put("p2Gender", record.getP2Gender());
        placeholders.put("p2DateOfBirth", record.getP2DateOfBirth());
        placeholders.put("p2PlaceOfBirth", record.getP2PlaceOfBirth());
        placeholders.put("p2BirthCertificateIssuedPlace", record.getP2BirthCertificateIssuedPlace());
        placeholders.put("p2Nationality", record.getP2Nationality());
        placeholders.put("p2PassportNumber", record.getP2PassportNumber());
        placeholders.put("p2PassportIssuedPlace", record.getP2PassportIssuedPlace());
        placeholders.put("p2PassportIssuedDate", record.getP2PassportIssuedDate());
        placeholders.put("p2CurrentPlaceOfResidence", record.getP2CurrentPlaceOfResidence());
        placeholders.put("p2PlaceOfResidenceInVietnamBeforeDeparture",
                record.getP2PlaceOfResidenceInVietnamBeforeDeparture());

        updateDocument(doc, placeholders);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        doc.write(outputStream);
        outputStream.close();
        doc.close();

        return outputStream;

    }

    public ByteArrayOutputStream createCurriculumVitae1(RenunciationOfCitizenship record)
            throws ParseException, IOException {
        String template = "./conf/curriculum-vitae-1.docx";

        // Mở tài liệu mẫu
        FileInputStream inputStream = new FileInputStream(new File(template));
        XWPFDocument doc = new XWPFDocument(inputStream);

        // Thay thế các giá trị đại diện bằng dữ liệu thực tế
        Map<String, String> map = new HashMap<>();

        map.put("p1FullName", record.getP1FullName());
        map.put("p1Gender", record.getP1Gender());
        map.put("[title]", record.getP1Gender().toUpperCase().equals("NAM") ? "Ông" : "Bà");
        map.put("p1DateOfBirth", record.getP1DateOfBirth());
        map.put("p1PlaceOfBirth", record.getP1PlaceOfBirth());
        map.put("p1BirthCertificateIssuedPlace", record.getP1BirthCertificateIssuedPlace());
        map.put("p1Nationality", record.getP1Nationality());
        map.put("p1PassportNumber", record.getP1PassportNumber());
        map.put("p1PassportIssuedPlace", record.getP1PassportIssuedPlace());
        map.put("p1PassportIssuedDate", record.getP1PassportIssuedDate());
        map.put("p1CurrentPlaceOfResidence", record.getP1CurrentPlaceOfResidence());
        map.put("p1PlaceOfResidenceInVietnamBeforeDeparture",
                record.getP1PlaceOfResidenceInVietnamBeforeDeparture());

        map.put("p2FullName", record.getP2FullName());
        map.put("p2Gender", record.getP2Gender());
        map.put("p2DateOfBirth", record.getP2DateOfBirth());
        map.put("p2Nationality", record.getP2Nationality());
        map.put("p2CurrentPlaceOfResidence", record.getP2CurrentPlaceOfResidence());

        map.put("p3FullName", record.getP3FullName());
        map.put("p3Gender", record.getP3Gender());
        map.put("p3DateOfBirth", record.getP3DateOfBirth());
        map.put("p3Nationality", record.getP3Nationality());
        map.put("p3CurrentPlaceOfResidence", record.getP3CurrentPlaceOfResidence());

        updateDocument(doc, map);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        doc.write(outputStream);
        outputStream.close();
        doc.close();

        return outputStream;
    }

    public ByteArrayOutputStream createReportForBirthRegistration(RegistrationOfBirthCertificate record)
            throws ParseException, IOException {
        String template = "./conf/ks_bantuongtrinh.docx";

        // Mở tài liệu mẫu
        FileInputStream inputStream = new FileInputStream(new File(template));
        XWPFDocument doc = new XWPFDocument(inputStream);

        // Thay thế các giá trị đại diện bằng dữ liệu thực tế
        Map<String, String> map = new HashMap<>();

        map.put("{childFullName}", AppUltil.toTitleCase(record.getChildVietnameseFullName()));
        map.put("{childDateOfBirth}", record.getChildDateOfBirth());
        map.put("{childPlaceOfBirth}", record.getChildPlaceOfBirth());

        if ("father".equals(record.getDeclarer())) {
            map.put("{fatherFullName}", AppUltil.toTitleCase(record.getFatherFullName()));
            map.put("{fatherDateOfBirth}", record.getFatherDateOfBirth());
            map.put("{fatherPassportNumber}", record.getFatherPassportNumber());
            map.put("{fatherPassportCreatedBy}", record.getFatherPassportCreatedBy());
            map.put("{fatherPassportCreatedAt}", record.getFatherPassportCreatedAt());
            map.put("{fatherResidence}", record.getFatherResidence() + ", Hàn Quốc");

            map.put("{gender}", "gái");
            map.put("{motherFullName}", AppUltil.toTitleCase(record.getMotherFullName()));
            map.put("{motherDateOfBirth}", record.getMotherDateOfBirth());
            map.put("{motherPassportNumber}", record.getMotherPassportNumber());
            map.put("{motherPassportCreatedBy}", record.getMotherPassportCreatedBy());
            map.put("{motherPassportCreatedAt}", record.getMotherPassportCreatedAt());
            map.put("{motherResidence}", record.getMotherResidence());

        } else {
            map.put("{fatherFullName}", AppUltil.toTitleCase(record.getMotherFullName()));
            map.put("{fatherDateOfBirth}", record.getMotherDateOfBirth());
            map.put("{fatherPassportNumber}", record.getMotherPassportNumber());
            map.put("{fatherPassportCreatedBy}", record.getMotherPassportCreatedBy());
            map.put("{fatherPassportCreatedAt}", record.getMotherPassportCreatedAt());
            map.put("{fatherResidence}", record.getMotherResidence() + ", Hàn Quốc");

            map.put("{gender}", "trai");
            map.put("{motherFullName}", AppUltil.toTitleCase(record.getFatherFullName()));
            map.put("{motherDateOfBirth}", record.getFatherDateOfBirth());
            map.put("{motherPassportNumber}", record.getFatherPassportNumber());
            map.put("{motherPassportCreatedBy}", record.getFatherPassportCreatedBy());
            map.put("{motherPassportCreatedAt}", record.getFatherPassportCreatedAt());
            map.put("{motherResidence}", record.getFatherResidence());

        }

        LocalDate lDate = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        map.put("{dateTime}", String.format("ngày %02d tháng %02d năm %d", lDate.getDayOfMonth(),
                lDate.getMonthValue(),
                lDate.getYear()));

        updateDocument(doc, map);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        doc.write(outputStream);
        outputStream.close();
        doc.close();

        return outputStream;
    }

    private void updateXWPFParagraph(XWPFParagraph p, Map<String, String> placeholders) {
        List<XWPFRun> runs = p.getRuns();
        for (XWPFRun r : runs) {
            String text = r.getText(0);
            // System.out.println("text: ["+ text+"]");
            for (String key : placeholders.keySet()) {
                if (text != null && text.contains(key)) {
                    text = text.replace(key, placeholders.get(key) == null ? "" : placeholders.get(key));
                    r.setText(text, 0);
                }
            }
        }
    }

    private void updateDocument(XWPFDocument doc, Map<String, String> placeholders) {

        for (XWPFTable table : doc.getTables()) {
            for (XWPFTableRow row : table.getRows()) {
                for (XWPFTableCell cell : row.getTableCells()) {
                    for (XWPFParagraph p : cell.getParagraphs()) {
                        updateXWPFParagraph(p, placeholders);
                    }
                }
            }
        }

        for (XWPFParagraph p : doc.getParagraphs()) {
            updateXWPFParagraph(p, placeholders);

            /*
             * List<XWPFRun> runs = p.getRuns();
             * for (XWPFRun r : runs) {
             * String text = r.getText(0);
             * // System.out.println("text: ["+ text+"]");
             * for (String key : placeholders.keySet()) {
             * if (text != null && text.contains(key)) {
             * text = text.replace(key, placeholders.get(key) == null ? "" :
             * placeholders.get(key));
             * r.setText(text, 0);
             * }
             * }
             * }
             */

        }
    }

}
