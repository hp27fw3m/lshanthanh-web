package nb.hanquoc.web.service;

import nb.hanquoc.web.repository.RenunciationOfCitizenshipRepository;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import nb.hanquoc.web.AppUltil;
import nb.hanquoc.core.entity.RenunciationOfCitizenship;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RenunciationOfCitizenshipService {

        @Autowired
        private RenunciationOfCitizenshipRepository RenunciationCitizenshipRepository;

        public List<RenunciationOfCitizenship> findAll() {
                return (List<RenunciationOfCitizenship>) RenunciationCitizenshipRepository.findAll();
        }

        @SuppressWarnings("null")
        public RenunciationOfCitizenship save(RenunciationOfCitizenship person) {
                return RenunciationCitizenshipRepository.save(person);
        }

        
        @SuppressWarnings("null")
        public Optional<RenunciationOfCitizenship> findById(Long id) {
                return RenunciationCitizenshipRepository.findById(id);
        }

        public ByteArrayOutputStream createRenunciationOfCitizenship2(RenunciationOfCitizenship record)
                        throws ParseException, IOException {
                String pdfTemplate = "./conf/tpqt-2020-dxtqt2.pdf";
                PDDocument document = PDDocument.load(new File(pdfTemplate));
                updateRenunciation2Page1(document, record);
                updateRenunciation2Page2(document, record);

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                document.save(outputStream);
                document.close();
                return outputStream;
        }

        private void updateRenunciation2Page1(PDDocument document, RenunciationOfCitizenship p)
                        throws IOException {
                PDPage page = document.getPage(0);

                PDPageContentStream contentStream = new PDPageContentStream(document, page,
                                PDPageContentStream.AppendMode.APPEND, true, true);

                contentStream.setFont(PDType1Font.COURIER, 10);
                float line = 810;
                line -= 36;
                contentStream.beginText();
                contentStream.newLineAtOffset(424, line);
                contentStream.showText("[" + p.getId() + " - " + p.getDownloadCode() + "]");
                contentStream.endText();

                line = 810 - 342;
                PDType0Font font = PDType0Font.load(document, new File("./conf/seguisb.ttf"));
                contentStream.setFont(font, 11);

                contentStream.beginText();
                contentStream.newLineAtOffset(210, line);
                contentStream.showText(p.getP1FullName());
                contentStream.endText();

                // Add the Sex text string
                contentStream.beginText();
                contentStream.newLineAtOffset(470, line);
                contentStream.showText(p.getP1Gender());
                contentStream.endText();

                // dateOfBirth
                line -= 22;
                contentStream.beginText();
                contentStream.newLineAtOffset(210, line);
                contentStream.showText(p.getP1DateOfBirth());
                contentStream.endText();

                // placeOfBirth
                line -= 22;
                contentStream.beginText();
                contentStream.newLineAtOffset(150, line);
                contentStream.showText(p.getP1PlaceOfBirth());
                contentStream.endText();

                // nationality
                line -= 22;
                contentStream.beginText();
                contentStream.newLineAtOffset(150, line);
                contentStream.showText(p.getP1Nationality());
                contentStream.endText();

                line -= 22;
                contentStream.beginText();
                contentStream.newLineAtOffset(300, line);
                contentStream.showText("Hộ chiếu");
                contentStream.endText();

                contentStream.beginText();
                contentStream.newLineAtOffset(450, line);
                contentStream.showText(p.getP1PassportNumber());
                contentStream.endText();

                line -= 22;
                contentStream.beginText();
                contentStream.newLineAtOffset(95, line);
                contentStream.showText(p.getP1PassportIssuedPlace());
                contentStream.endText();

                LocalDate lDate = AppUltil.stringToDate(p.getP1PassportIssuedDate());

                contentStream.beginText();
                contentStream.newLineAtOffset(385, line);
                contentStream.showText(
                                String.format("%02d              %02d             %d", lDate.getDayOfMonth(),
                                                lDate.getMonthValue(),
                                                lDate.getYear()));
                contentStream.endText();

                line -= 22 * 2;
                contentStream.beginText();
                contentStream.newLineAtOffset(95, line);
                contentStream.showText(p.getP1CurrentPlaceOfResidence());
                contentStream.endText();

                line -= 20 * 2;
                contentStream.beginText();
                contentStream.newLineAtOffset(210, line);
                contentStream.showText(p.getP1Relationship());
                contentStream.endText();

                line -= 20 * 6 + 3;
                contentStream.beginText();
                contentStream.newLineAtOffset(210, line);
                contentStream.showText(p.getP2FullName());
                contentStream.endText();

                contentStream.beginText();
                contentStream.newLineAtOffset(470, line);
                contentStream.showText(p.getP2Gender());
                contentStream.endText();

                // dateOfBirth
                line -= 22 + 1;
                contentStream.beginText();
                contentStream.newLineAtOffset(210, line);
                contentStream.showText(p.getP2DateOfBirth());
                contentStream.endText();

                // placeOfBirth
                line -= 22;
                contentStream.beginText();
                contentStream.newLineAtOffset(150, line);
                contentStream.showText(p.getP2PlaceOfBirth());
                contentStream.endText();

                // nationality
                line -= 22;
                contentStream.beginText();
                contentStream.newLineAtOffset(220, line);
                contentStream.showText(p.getP2BirthCertificateIssuedPlace());
                contentStream.endText();

                contentStream.close();
        }

        private void updateRenunciation2Page2(PDDocument document, RenunciationOfCitizenship p)
                        throws IOException {
                PDPage page = document.getPage(1);

                PDPageContentStream contentStream = new PDPageContentStream(document, page,
                                PDPageContentStream.AppendMode.APPEND, true, true);

                float line = 810;
                line -= 48;
                PDType0Font font = PDType0Font.load(document, new File("./conf/seguisb.ttf"));
                contentStream.setFont(font, 11);

                contentStream.beginText();
                contentStream.newLineAtOffset(200, line);
                contentStream.showText(p.getP2PassportNumber());
                contentStream.endText();

                // Add the Sex text string
                contentStream.beginText();
                contentStream.newLineAtOffset(310, line);
                contentStream.showText(p.getP2PassportIssuedPlace());
                contentStream.endText();

                line -= 22;

                LocalDate lDate = AppUltil.stringToDate(p.getP2PassportIssuedDate());
                contentStream.beginText();
                contentStream.newLineAtOffset(135, line);
                contentStream.showText(
                                String.format("%02d                  %02d                %02d", lDate.getDayOfMonth(),
                                                lDate.getMonthValue(),
                                                lDate.getYear()));
                contentStream.endText();

                line -= 22 * 4 - 2;
                contentStream.beginText();
                contentStream.newLineAtOffset(100, line);
                contentStream.showText(p.getP2CurrentPlaceOfResidence());
                contentStream.endText();

                line -= 22 * 3;
                contentStream.beginText();
                contentStream.newLineAtOffset(100, line);
                contentStream.showText(p.getP2PlaceOfResidenceInVietnamBeforeDeparture());
                contentStream.endText();

                line -= 22 * 2;
                contentStream.beginText();
                contentStream.newLineAtOffset(100, line);
                contentStream.showText(p.getP2ReasonForRenunciation());
                contentStream.endText();

                line -= 22 * 4 + 14;

                lDate = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                contentStream.beginText();
                contentStream.newLineAtOffset(300, line);
                contentStream.showText(String.format("Seoul                %02d               %02d            %d",
                                lDate.getDayOfMonth(), lDate.getMonthValue(), lDate.getYear()));
                contentStream.endText();

                line -= 115;
                String requestPerson = p.getP1FullName();

                contentStream.beginText();
                contentStream.newLineAtOffset(361, line);
                contentStream.showText(AppUltil.toTitleCase(requestPerson));
                contentStream.endText();

                // Close the content stream
                contentStream.close();
        }

        public ByteArrayOutputStream createRenunciationOfCitizenship1(RenunciationOfCitizenship record)
                        throws ParseException, IOException {
                String pdfTemplate = "./conf/tpqt-2020-dxtqt1.pdf";
                PDDocument document = PDDocument.load(new File(pdfTemplate));
                updateRenunciation1Page1(document, record);
                updateRenunciation1Page2(document, record);
                updateRenunciation1Page3(document, record);

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                document.save(outputStream);
                document.close();
                return outputStream;
        }

        private void updateRenunciation1Page1(PDDocument document, RenunciationOfCitizenship p)
                        throws IOException {
                PDPage page = document.getPage(0);

                PDPageContentStream contentStream = new PDPageContentStream(document, page,
                                PDPageContentStream.AppendMode.APPEND, true, true);

                contentStream.setFont(PDType1Font.COURIER, 10);
                float line = 810;
                line -= 36;
                contentStream.beginText();
                contentStream.newLineAtOffset(428, line);
                contentStream.showText("[" + p.getId() + " - " + p.getDownloadCode() + "]");
                contentStream.endText();

                line -= 236;
                PDType0Font font = PDType0Font.load(document, new File("./conf/seguisb.ttf"));
                contentStream.setFont(font, 11);

                // name, gender, dateofbirth
                contentStream.beginText();
                contentStream.newLineAtOffset(210, line);
                contentStream.showText(p.getP1FullName());
                contentStream.endText();

                contentStream.beginText();
                contentStream.newLineAtOffset(470, line);
                contentStream.showText(p.getP1Gender());
                contentStream.endText();

                line -= 22;
                contentStream.beginText();
                contentStream.newLineAtOffset(210, line);
                contentStream.showText(p.getP1DateOfBirth());
                contentStream.endText();

                line -= 22;
                contentStream.beginText();
                contentStream.newLineAtOffset(150, line);
                contentStream.showText(p.getP1PlaceOfBirth());
                contentStream.endText();

                line -= 23;
                contentStream.beginText();
                contentStream.newLineAtOffset(210, line);
                contentStream.showText(p.getP1BirthCertificateIssuedPlace());
                contentStream.endText();

                // pasport
                line -= 22 * 2;
                contentStream.beginText();
                contentStream.newLineAtOffset(210, line);
                contentStream.showText(p.getP1PassportNumber());
                contentStream.endText();

                contentStream.beginText();
                contentStream.newLineAtOffset(300, line);
                contentStream.showText(p.getP1PassportIssuedPlace());
                contentStream.endText();

                line -= 22;
                LocalDate lDate = AppUltil.stringToDate(p.getP1PassportIssuedDate());
                contentStream.beginText();
                contentStream.newLineAtOffset(135, line);
                contentStream.showText(
                                String.format("%02d                  %02d                %d", lDate.getDayOfMonth(),
                                                lDate.getMonthValue(),
                                                lDate.getYear()));
                contentStream.endText();

                // idendityCard
                String text = "CMND";
                if (p.getP1IdentityCardNumber().length() > 9) {
                        text = "CCCD";
                }

                line -= 22;
                contentStream.beginText();
                contentStream.newLineAtOffset(300, line);
                contentStream.showText(text + " số " + p.getP1IdentityCardNumber());
                contentStream.endText();
                
                line -= 22;
                contentStream.beginText();
                contentStream.newLineAtOffset(100, line);
                contentStream.showText(p.getP1IdentityCardIssuedPlace()
                                + String.format(" cấp ngày %02d tháng %02d năm %d", lDate.getDayOfMonth(),
                                                lDate.getMonthValue(),
                                                lDate.getYear()));
                contentStream.endText();

                // curent place of residence
                line -= 22 * 2;
                contentStream.beginText();
                contentStream.newLineAtOffset(100, line);
                contentStream.showText(p.getP1CurrentPlaceOfResidence());
                contentStream.endText();

                line -= 22 * 3;
                contentStream.beginText();
                contentStream.newLineAtOffset(100, line);
                contentStream.showText(p.getP1PlaceOfResidenceInVietnamBeforeDeparture());
                contentStream.endText();

                line -= 22 * 4 + 12;
                contentStream.beginText();
                contentStream.newLineAtOffset(100, line);
                contentStream.showText(p.getP1ReasonForRenunciation());
                contentStream.endText();

                contentStream.close();
        }

        private void updateRenunciation1Page2(PDDocument document, RenunciationOfCitizenship p) throws IOException {

                if (p.getP2FullName() == null || p.getP2FullName().length() == 0) {
                        return;
                }
                PDPage page = document.getPage(1);

                PDPageContentStream contentStream = new PDPageContentStream(document, page,
                                PDPageContentStream.AppendMode.APPEND, true, true);

                PDType0Font font = PDType0Font.load(document, new File("./conf/seguisb.ttf"));
                contentStream.setFont(font, 11);

                float line = 810;
                line -= 85;

                // name, gender, dateofbirth
                contentStream.beginText();
                contentStream.newLineAtOffset(210, line);
                contentStream.showText(p.getP2FullName());
                contentStream.endText();

                contentStream.beginText();
                contentStream.newLineAtOffset(470, line);
                contentStream.showText(p.getP2Gender());
                contentStream.endText();

                line -= 22;
                contentStream.beginText();
                contentStream.newLineAtOffset(210, line);
                contentStream.showText(p.getP2DateOfBirth());
                contentStream.endText();

                line -= 22;
                contentStream.beginText();
                contentStream.newLineAtOffset(210, line);
                contentStream.showText(p.getP2PlaceOfBirth());
                contentStream.endText();

                line -= 22;
                contentStream.beginText();
                contentStream.newLineAtOffset(210, line);
                contentStream.showText(p.getP2BirthCertificateIssuedPlace());
                contentStream.endText();

                line -= 22;
                line -= 22 + 2;
                contentStream.beginText();
                contentStream.newLineAtOffset(200, line);
                contentStream.showText(p.getP2PassportNumber());
                contentStream.endText();

                contentStream.beginText();
                contentStream.newLineAtOffset(300, line);
                contentStream.showText(p.getP2PassportIssuedPlace());
                contentStream.endText();

                line -= 22;
                LocalDate lDate = AppUltil.stringToDate(p.getP2PassportIssuedDate());
                contentStream.beginText();
                contentStream.newLineAtOffset(140, line);
                contentStream.showText(
                                String.format("%02d                  %02d                %d", lDate.getDayOfMonth(),
                                                lDate.getMonthValue(),
                                                lDate.getYear()));
                contentStream.endText();

                line -= 22 * 3 + 20;
                contentStream.beginText();
                contentStream.newLineAtOffset(100, line);
                contentStream.showText(p.getP2CurrentPlaceOfResidence());
                contentStream.endText();

                // the second child
                // name, gender, dateofbirth
                contentStream.beginText();
                line -= 22 * 2;
                contentStream.newLineAtOffset(210, line);
                contentStream.showText(p.getP3FullName());
                contentStream.endText();

                contentStream.beginText();
                contentStream.newLineAtOffset(470, line);
                contentStream.showText(p.getP3Gender());
                contentStream.endText();

                line -= 22;
                contentStream.beginText();
                contentStream.newLineAtOffset(210, line);
                contentStream.showText(p.getP3DateOfBirth());
                contentStream.endText();

                line -= 22;
                contentStream.beginText();
                contentStream.newLineAtOffset(210, line);
                contentStream.showText(p.getP3PlaceOfBirth());
                contentStream.endText();

                line -= 22;
                contentStream.beginText();
                contentStream.newLineAtOffset(210, line);
                contentStream.showText(p.getP3BirthCertificateIssuedPlace());
                contentStream.endText();

                line -= 22;
                line -= 22 + 2;
                contentStream.beginText();
                contentStream.newLineAtOffset(200, line);
                contentStream.showText(p.getP3PassportNumber());
                contentStream.endText();

                contentStream.beginText();
                contentStream.newLineAtOffset(300, line);
                contentStream.showText(p.getP3PassportIssuedPlace());
                contentStream.endText();

                line -= 22;
                lDate = AppUltil.stringToDate(p.getP3PassportIssuedDate());
                contentStream.beginText();
                contentStream.newLineAtOffset(140, line);
                contentStream.showText(
                                String.format("%02d                  %02d                %d", lDate.getDayOfMonth(),
                                                lDate.getMonthValue(),
                                                lDate.getYear()));
                contentStream.endText();

                line -= 22 * 3 + 20;
                contentStream.beginText();
                contentStream.newLineAtOffset(100, line);
                contentStream.showText(p.getP3CurrentPlaceOfResidence());
                contentStream.endText();
                contentStream.close();
        }

        private void updateRenunciation1Page3(PDDocument document, RenunciationOfCitizenship p)
                        throws IOException {
                PDPage page = document.getPage(2);

                PDPageContentStream contentStream = new PDPageContentStream(document, page,
                                PDPageContentStream.AppendMode.APPEND, true, true);

                PDType0Font font = PDType0Font.load(document, new File("./conf/seguisb.ttf"));
                contentStream.setFont(font, 11);

                float line = 810;
                line -= 50;

                LocalDate lDate = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                contentStream.beginText();
                contentStream.newLineAtOffset(300, line);
                contentStream.showText(String.format("Seoul                %02d               %02d            %d",
                                lDate.getDayOfMonth(), lDate.getMonthValue(), lDate.getYear()));
                contentStream.endText();

                line -= 100;
                String requestPerson = p.getP1FullName();

                contentStream.beginText();
                contentStream.newLineAtOffset(361, line);
                contentStream.showText(AppUltil.toTitleCase(requestPerson));
                contentStream.endText();

                contentStream.close();
        }

}