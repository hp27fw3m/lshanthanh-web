package nb.hanquoc.web.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.stereotype.Service;

import nb.hanquoc.web.AppUltil;
import nb.hanquoc.web.VietNumber;
import nb.hanquoc.core.entity.RegistrationOfBirthCertificate;

@Service
public class RegistrationOfBirthService {

        private static HashMap<Integer, byte[]> templateMap = new HashMap<>();

        private static byte[] getTemplateBinary(int page) throws IOException {

                byte[] templateBinary = templateMap.get(page);
                if (templateBinary == null) {
                        switch (page) {
                                case 0:
                                        templateBinary = Files.readAllBytes(Paths.get("./conf/blank.pdf"));
                                        break;
                                case 1:
                                        templateBinary = Files.readAllBytes(Paths.get("./conf/ks_bw.pdf"));
                                        break;
                                case 2:
                                        templateBinary = Files.readAllBytes(Paths.get("./conf/tk02a.pdf"));
                                        break;
                                case 3:
                                        templateBinary = Files.readAllBytes(Paths.get("./conf/cmc.pdf"));
                                        break;
                        }
                        templateMap.put(page, templateBinary);
                }
                return templateBinary;
        }

        public ByteArrayOutputStream createPDFPage1(RegistrationOfBirthCertificate p, int pageNumber)
                        throws ParseException, IOException {

                PDDocument document = PDDocument.load(getTemplateBinary(pageNumber));

                PDPage page = document.getPage(0);

                PDPageContentStream contentStream = new PDPageContentStream(document, page,
                                PDPageContentStream.AppendMode.APPEND, true, true);

                contentStream.setFont(PDType1Font.COURIER, 11);
                float lineHeight = 22;
                float line = 810;
                line -= 36;
                contentStream.beginText();
                contentStream.newLineAtOffset(28, line);
                contentStream.showText("[" + p.getId() + "-" + p.getDownloadCode() + "]");
                contentStream.endText();

                PDType0Font font = PDType0Font.load(document, new File("./conf/seguisb.ttf"));
                contentStream.setFont(font, 11);

                line -= 90;
                contentStream.beginText();
                contentStream.newLineAtOffset(222, line);
                contentStream.showText("Đại sứ quán Việt Nam tại Hàn Quốc");
                contentStream.endText();

                // declarer
                String declarer = p.getDeclarer();
                if ("mother".equals(declarer)) {

                        line -= lineHeight + 12;
                        contentStream.beginText();
                        contentStream.newLineAtOffset(272, line);
                        contentStream.showText(AppUltil.toTitleCase(p.getMotherFullName()));
                        contentStream.endText();

                        line -= lineHeight;
                        contentStream.beginText();
                        contentStream.newLineAtOffset(172, line);
                        contentStream.showText(String.format("Hộ chiếu số %s do %s cấp ngày %s",
                                        p.getMotherPassportNumber(),
                                        p.getMotherPassportCreatedBy(), p.getMotherPassportCreatedAt()));
                        contentStream.endText();

                        // line -= lineHeight;
                        // contentStream.beginText();
                        // contentStream.newLineAtOffset(182, line);
                        // contentStream.showText(String.format("cấp ngày %s",
                        // p.getMotherPassportCreatedAt()));
                        // contentStream.endText();

                        line -= lineHeight;
                        contentStream.beginText();
                        contentStream.newLineAtOffset(182, line);
                        contentStream.showText(p.getMotherResidence() + ", Hàn Quốc");
                        contentStream.endText();

                        line -= lineHeight * 2;
                        contentStream.beginText();
                        contentStream.newLineAtOffset(272, line);
                        contentStream.showText("Mẹ ruột");
                        contentStream.endText();
                } else {
                        line -= lineHeight + 12;
                        contentStream.beginText();
                        contentStream.newLineAtOffset(272, line);
                        contentStream.showText(AppUltil.toTitleCase(p.getFatherFullName()));
                        contentStream.endText();

                        line -= lineHeight;
                        contentStream.beginText();
                        contentStream.newLineAtOffset(172, line);
                        contentStream.showText(String.format("Hộ chiếu số %s do %s cấp ngày %s",
                                        p.getFatherPassportNumber(),
                                        p.getFatherPassportCreatedBy(), p.getFatherPassportCreatedAt()));
                        contentStream.endText();

                        // line -= lineHeight;
                        // contentStream.beginText();
                        // contentStream.newLineAtOffset(182, line);
                        // contentStream.showText(String.format("cấp ngày %s",
                        // p.getMotherPassportCreatedAt()));
                        // contentStream.endText();

                        line -= lineHeight;
                        contentStream.beginText();
                        contentStream.newLineAtOffset(182, line);
                        contentStream.showText(p.getFatherResidence() + ", Hàn Quốc");
                        contentStream.endText();

                        line -= lineHeight * 2;
                        contentStream.beginText();
                        contentStream.newLineAtOffset(272, line);
                        contentStream.showText("Cha ruột");
                        contentStream.endText();
                }

                // child
                line -= lineHeight * 2;
                contentStream.beginText();
                contentStream.newLineAtOffset(272, line);
                contentStream.showText(p.getChildVietnameseFullName());
                contentStream.endText();

                line -= lineHeight;
                contentStream.beginText();
                contentStream.newLineAtOffset(222, line);
                contentStream.showText(p.getChildDateOfBirth());
                contentStream.endText();

                line -= lineHeight;
                LocalDate lDate = AppUltil.stringToDate(p.getChildDateOfBirth());
                contentStream.beginText();
                contentStream.newLineAtOffset(100, line);
                contentStream.showText(String.format("Ngày%s, tháng%s, năm%s", VietNumber.docso(lDate.getDayOfMonth()),
                                VietNumber.docso(lDate.getMonthValue()), VietNumber.docso(lDate.getYear())));
                contentStream.endText();

                line -= lineHeight;
                contentStream.beginText();
                contentStream.newLineAtOffset(182, line);
                contentStream.showText(p.getChildPlaceOfBirth() + ", Hàn Quốc");
                contentStream.endText();

                line -= lineHeight;
                contentStream.beginText();
                contentStream.newLineAtOffset(131, line);
                contentStream.showText(p.getChildGender());
                contentStream.endText();

                contentStream.beginText();
                contentStream.newLineAtOffset(252, line);
                contentStream.showText(p.getChildEthnicGroup());
                contentStream.endText();

                contentStream.beginText();
                contentStream.newLineAtOffset(444, line);
                contentStream.showText("Việt Nam");
                contentStream.endText();

                line -= lineHeight;
                contentStream.beginText();
                contentStream.newLineAtOffset(151, line);
                contentStream.showText(p.getChildPlaceOfOrigin());
                contentStream.endText();

                // father
                line -= lineHeight;
                contentStream.beginText();
                contentStream.newLineAtOffset(272, line);
                contentStream.showText(AppUltil.toTitleCase(p.getFatherFullName()));
                contentStream.endText();

                line -= lineHeight;
                lDate = AppUltil.stringToDate(p.getFatherDateOfBirth());
                contentStream.beginText();
                contentStream.newLineAtOffset(131, line);
                contentStream.showText(String.valueOf(lDate == null ? "" : lDate.getYear()));
                contentStream.endText();

                contentStream.beginText();
                contentStream.newLineAtOffset(252, line);
                contentStream.showText(AppUltil.isNullOrEmpty(p.getFatherFullName()) ? "" : p.getFatherEthnicGroup());
                contentStream.endText();

                contentStream.beginText();
                contentStream.newLineAtOffset(444, line);
                contentStream.showText(AppUltil.isNullOrEmpty(p.getFatherFullName()) ? "" : p.getFatherNationality());
                contentStream.endText();

                line -= lineHeight;
                contentStream.beginText();
                contentStream.newLineAtOffset(182, line);
                contentStream.showText(AppUltil.isNullOrEmpty(p.getFatherFullName()) ? ""
                                : p.getFatherResidence() + ", Hàn Quốc");
                contentStream.endText();

                // mother
                line -= lineHeight * 2;
                contentStream.beginText();
                contentStream.newLineAtOffset(272, line);
                contentStream.showText(AppUltil.toTitleCase(p.getMotherFullName()));
                contentStream.endText();

                line -= lineHeight;
                lDate = AppUltil.stringToDate(p.getMotherDateOfBirth());
                contentStream.beginText();
                contentStream.newLineAtOffset(131, line);
                contentStream.showText(String.valueOf(lDate.getYear()));
                contentStream.endText();

                contentStream.beginText();
                contentStream.newLineAtOffset(252, line);
                contentStream.showText(p.getMotherEthnicGroup());
                contentStream.endText();

                contentStream.beginText();
                contentStream.newLineAtOffset(444, line);
                contentStream.showText(p.getMotherNationality());
                contentStream.endText();

                line -= lineHeight;
                contentStream.beginText();
                contentStream.newLineAtOffset(182, line);
                contentStream.showText(p.getMotherResidence() + ", Hàn Quốc");
                contentStream.endText();

                lDate = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                line -= lineHeight * 4 + 15;
                contentStream.beginText();
                contentStream.newLineAtOffset(242, line);
                contentStream.showText(
                                String.format("Seoul                          %02d                  %02d               %02d",
                                                lDate.getDayOfMonth(),
                                                lDate.getMonthValue(),
                                                lDate.getYear()));
                contentStream.endText();

                line -= lineHeight * 4 + 2;
                contentStream.beginText();
                contentStream.newLineAtOffset(370, line);
                contentStream.showText(AppUltil.toTitleCase(
                                "mother".equals(p.getDeclarer()) ? p.getMotherFullName() : p.getFatherFullName()));
                contentStream.endText();

                contentStream.close();

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                document.save(outputStream);
                document.close();
                return outputStream;
        }


        public ByteArrayOutputStream createPDFPage3(RegistrationOfBirthCertificate p)
                        throws ParseException, IOException {
                // String pdfTemplate = "./conf/cmc.pdf";
                PDDocument document = PDDocument.load(getTemplateBinary(3));
                // PDDocument document = PDDocument.load(new File("./conf/cmc.pdf"));

                PDPage page = document.getPage(0);

                PDPageContentStream contentStream = new PDPageContentStream(document, page,
                                PDPageContentStream.AppendMode.APPEND, true, true);

                contentStream.setFont(PDType1Font.COURIER, 10);
                float lineHeight = 21;
                float line = 810;
                line -= 36;
                contentStream.beginText();
                contentStream.newLineAtOffset(28, line);
                contentStream.showText("[" + p.getId() + "]");
                contentStream.endText();

                line -= 90;
                PDType0Font font = PDType0Font.load(document, new File("./conf/seguisb.ttf"));
                contentStream.setFont(font, 11);

                contentStream.beginText();
                contentStream.newLineAtOffset(211, line);
                contentStream.showText("Đại sứ quán Việt Nam tại Hàn Quốc");
                contentStream.endText();

                line -= 27;
                // name, gender, dateofbirth
                contentStream.beginText();
                contentStream.newLineAtOffset(272, line);
                contentStream.showText(p.getMotherFullName());
                contentStream.endText();

                line -= lineHeight;
                contentStream.beginText();
                contentStream.newLineAtOffset(182, line);
                contentStream.showText(p.getMotherResidence() + ", Hàn Quốc");
                contentStream.endText();
                line -= lineHeight - 1;
                contentStream.beginText();
                contentStream.newLineAtOffset(182, line);
                contentStream.showText(String.format("Hộ chiếu số %s do %s", p.getMotherPassportNumber(),
                                p.getMotherPassportCreatedBy()));
                contentStream.endText();

                line -= lineHeight - 2;
                contentStream.beginText();
                contentStream.newLineAtOffset(182, line);
                contentStream.showText(String.format("cấp ngày %s", p.getMotherPassportCreatedAt()));
                contentStream.endText();

                line -= lineHeight - 2;
                contentStream.beginText();
                contentStream.newLineAtOffset(272, line);
                contentStream.showText("Mẹ ruột");
                contentStream.endText();

                line -= lineHeight * 2 + 6;
                contentStream.beginText();
                contentStream.newLineAtOffset(182, line);
                contentStream.showText(p.getChildVietnameseFullName());
                contentStream.endText();

                line -= lineHeight;
                contentStream.beginText();
                contentStream.newLineAtOffset(182, line);
                contentStream.showText(p.getChildDateOfBirth());
                contentStream.endText();

                line -= lineHeight;
                contentStream.beginText();
                contentStream.newLineAtOffset(151, line);
                contentStream.showText(p.getChildGender());
                contentStream.endText();

                contentStream.beginText();
                contentStream.newLineAtOffset(272, line);
                contentStream.showText(p.getChildEthnicGroup());
                contentStream.endText();

                contentStream.beginText();
                contentStream.newLineAtOffset(444, line);
                contentStream.showText("Việt Nam");
                contentStream.endText();

                line -= lineHeight;
                contentStream.beginText();
                contentStream.newLineAtOffset(151, line);
                contentStream.showText(p.getMotherResidence() + ", Hàn Quốc");
                contentStream.endText();

                // father
                line -= lineHeight * 3 + 4;

                contentStream.beginText();
                contentStream.newLineAtOffset(182, line);
                contentStream.showText(p.getFatherFullName());
                contentStream.endText();

                line -= lineHeight;
                contentStream.beginText();
                contentStream.newLineAtOffset(182, line);
                contentStream.showText(p.getFatherDateOfBirth());
                contentStream.endText();

                line -= lineHeight;
                contentStream.beginText();
                contentStream.newLineAtOffset(151, line);
                contentStream.showText("Nam");
                contentStream.endText();

                contentStream.beginText();
                contentStream.newLineAtOffset(272, line);
                contentStream.showText(p.getFatherEthnicGroup());
                contentStream.endText();

                contentStream.beginText();
                contentStream.newLineAtOffset(444, line);
                contentStream.showText(p.getFatherNationality());
                contentStream.endText();

                line -= lineHeight;
                contentStream.beginText();
                contentStream.newLineAtOffset(151, line);
                contentStream.showText(p.getFatherResidence() + ", Hàn Quốc");
                contentStream.endText();

                line -= lineHeight - 1;
                contentStream.beginText();
                contentStream.newLineAtOffset(182, line);
                contentStream.showText(String.format("Hộ chiếu số %s do %s", p.getFatherPassportNumber(),
                                p.getFatherPassportCreatedBy()));
                contentStream.endText();

                line -= lineHeight - 4;
                contentStream.beginText();
                contentStream.newLineAtOffset(182, line);
                contentStream.showText(String.format("cấp ngày %s", p.getFatherPassportCreatedAt()));
                contentStream.endText();

                LocalDate lDate = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                line -= lineHeight * 3 + 4;
                contentStream.beginText();
                contentStream.newLineAtOffset(410, line);
                contentStream.showText(
                                String.format("%02d                %02d            %02d", lDate.getDayOfMonth(),
                                                lDate.getMonthValue(),
                                                lDate.getYear()));
                contentStream.endText();

                line -= lineHeight * 4;
                contentStream.beginText();
                contentStream.newLineAtOffset(400, line);
                contentStream.showText(AppUltil.toTitleCase(p.getMotherFullName()));
                contentStream.endText();

                line -= lineHeight * 5;

                contentStream.beginText();
                contentStream.newLineAtOffset(141, line);
                contentStream.showText(AppUltil.toTitleCase(p.getFatherFullName()));
                contentStream.endText();

                contentStream.close();

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                document.save(outputStream);
                document.close();
                return outputStream;
        }

}
