package nb.hanquoc.web.controller;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nb.hanquoc.core.entity.RegistrationOfBirthCertificate;
import nb.hanquoc.core.entity.RenunciationOfCitizenship;
import nb.hanquoc.web.AppUltil;
import nb.hanquoc.core.entity.Marriage;
import nb.hanquoc.core.entity.Passport2023;
import nb.hanquoc.web.service.RegistrationOfBirthCertificateService;
import nb.hanquoc.web.service.RegistrationOfBirthService;
import nb.hanquoc.web.service.RenunciationOfCitizenshipService;
import nb.hanquoc.web.service.UserService;
import nb.hanquoc.web.service.MarriageService;
import nb.hanquoc.web.service.Passport2023Service;
import java.io.ByteArrayOutputStream;

@RestController
@RequestMapping("/pdf")
public class PDFController {
    @Autowired
    private Passport2023Service passportService;

    @Autowired
    private RegistrationOfBirthCertificateService registrationOfBirthCertificateService;

    @Autowired
    private RegistrationOfBirthService registrationOfBirthService;

    @Autowired
    private RenunciationOfCitizenshipService reunciationService;

    @Autowired
    private MarriageService marriageService;

    @Autowired
    private UserService userService;

    private Logger logger = LoggerFactory.getLogger(PDFController.class);

    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");

    @GetMapping("/passport/{id}/{downloadCode}")
    public ResponseEntity<InputStreamResource> downloadPassportForm(@PathVariable Long id,
            @PathVariable int downloadCode) {

        logger.info("downloadPassportForm id: " + id + ", downloadCode: " + downloadCode);
        try {
            InputStreamResource inputStreamResource = null;
            String fileName = null;
            Passport2023 p = passportService.findPassportById(id);

            // System.out.println("found: " + p.toString());
            if (p.getDownloadCode() == downloadCode) {
                ByteArrayOutputStream outputStream;
                if (AppUltil.isNullOrEmpty(p.getP2Role()))
                    outputStream = passportService.createPdfForm1(p);
                else
                    outputStream = passportService.createPdfForm2(p);
                InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
                inputStreamResource = new InputStreamResource(inputStream);

                fileName = Normalizer.normalize(p.getP1FullName(), Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
                fileName = fileName.replace(" ", "");
                fileName = simpleDateFormat.format(new Date()) + "_" + fileName + "_" + p.getId() + ".pdf";

            }
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(inputStreamResource);
        } catch (Exception ex) {
            logger.warn(ex.getMessage(), ex);
            return ResponseEntity.ok()
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(new InputStreamResource(new ByteArrayInputStream(ex.getMessage().getBytes())));

        }

    }

    @GetMapping("/registration-of-birth-certificate/{id}/{downloadCode}")
    public ResponseEntity<InputStreamResource> downloadBirthRecordForm(@PathVariable Long id,
            @PathVariable int downloadCode) {

        logger.info("downloadBirthRecordForm id: " + id + ", downloadCode: " + downloadCode);
        try {
            InputStreamResource inputStreamResource = null;
            String fileName = null;
            RegistrationOfBirthCertificate record = registrationOfBirthCertificateService.findById(id).orElseThrow();

            // System.out.println("found: " + p.toString());
            if (record.getDownloadCode() == downloadCode) {
                ByteArrayOutputStream outputStream = registrationOfBirthCertificateService.createPdfForm(record);
                InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
                inputStreamResource = new InputStreamResource(inputStream);

                fileName = Normalizer.normalize(record.getChildVietnameseFullName(), Normalizer.Form.NFD)
                        .replaceAll("[^\\p{ASCII}]", "");
                fileName = fileName.replace(" ", "");
                fileName = simpleDateFormat.format(new Date()) + "_" + fileName + "_" + record.getId() + ".pdf";

            }
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(inputStreamResource);
        } catch (Exception ex) {
            logger.warn(ex.getMessage(), ex);
            return ResponseEntity.ok()
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(new InputStreamResource(new ByteArrayInputStream(ex.getMessage().getBytes())));

        }
    }

    @GetMapping("/renunciation-of-citizenship-2/{id}/{downloadCode}")
    public ResponseEntity<InputStreamResource> downloadRenunciationOfCitizenship2(@PathVariable Long id,
            @PathVariable int downloadCode) {

        logger.info("downloadRenunciationOfCitizenship2 id: " + id + ", downloadCode: " + downloadCode);
        try {
            InputStreamResource inputStreamResource = null;
            String fileName = null;
            RenunciationOfCitizenship record = reunciationService.findById(id).orElseThrow();

            // System.out.println("found: " + p.toString());
            if (record.getDownloadCode() == downloadCode) {
                ByteArrayOutputStream outputStream = reunciationService.createRenunciationOfCitizenship2(record);
                InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
                inputStreamResource = new InputStreamResource(inputStream);

                fileName = Normalizer.normalize(record.getP2FullName(), Normalizer.Form.NFD)
                        .replaceAll("[^\\p{ASCII}]", "");
                fileName = fileName.replace(" ", "");
                fileName = simpleDateFormat.format(new Date()) + "_THQT_" + fileName + "_" + record.getId() + ".pdf";

            }
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(inputStreamResource);
        } catch (Exception ex) {
            logger.warn(ex.getMessage(), ex);
            return ResponseEntity.ok()
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(new InputStreamResource(new ByteArrayInputStream(ex.getMessage().getBytes())));

        }
    }

    @GetMapping("/renunciation-of-citizenship-1/{id}/{downloadCode}")
    public ResponseEntity<InputStreamResource> downloadRenunciationOfCitizenship1(@PathVariable Long id,
            @PathVariable int downloadCode) {

        logger.info("downloadRenunciationOfCitizenship1 id: " + id + ", downloadCode: " + downloadCode);
        try {
            InputStreamResource inputStreamResource = null;
            String fileName = null;
            RenunciationOfCitizenship record = reunciationService.findById(id).orElseThrow();

            // System.out.println("found: " + p.toString());
            if (record.getDownloadCode() == downloadCode) {
                ByteArrayOutputStream outputStream = reunciationService.createRenunciationOfCitizenship1(record);
                InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
                inputStreamResource = new InputStreamResource(inputStream);

                fileName = Normalizer.normalize(record.getP1FullName(), Normalizer.Form.NFD)
                        .replaceAll("[^\\p{ASCII}]", "");
                fileName = fileName.replace(" ", "");
                fileName = simpleDateFormat.format(new Date()) + "_THQT_" + fileName + "_" + record.getId() + ".pdf";

            }
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(inputStreamResource);
        } catch (Exception ex) {
            logger.warn(ex.getMessage(), ex);
            return ResponseEntity.ok()
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(new InputStreamResource(new ByteArrayInputStream(ex.getMessage().getBytes())));

        }
    }

    @GetMapping("/registration-of-birth/form-3/{id}/{downloadCode}")
    public ResponseEntity<InputStreamResource> downloadRegistrationOfBirthForm3(@PathVariable Long id,
            @PathVariable int downloadCode) {

        logger.info("downloadRegistrationOfBirthForm3 id: " + id + ", downloadCode: " + downloadCode);
        try {
            InputStreamResource inputStreamResource = null;
            String fileName = null;
            RegistrationOfBirthCertificate record = registrationOfBirthCertificateService.findById(id).orElseThrow();

            // System.out.println("found: " + p.toString());
            if (record.getDownloadCode() == downloadCode) {
                ByteArrayOutputStream outputStream = registrationOfBirthService.createPDFPage3(record);
                InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
                inputStreamResource = new InputStreamResource(inputStream);

                fileName = Normalizer.normalize(record.getChildVietnameseFullName(), Normalizer.Form.NFD)
                        .replaceAll("[^\\p{ASCII}]", "");
                fileName = fileName.replace(" ", "");
                fileName = simpleDateFormat.format(new Date()) + "_KS-CNC_" + fileName + "_" + record.getId() + ".pdf";

            }
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(inputStreamResource);
        } catch (Exception ex) {
            logger.warn(ex.getMessage(), ex);
            return ResponseEntity.ok()
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(new InputStreamResource(new ByteArrayInputStream(ex.getMessage().getBytes())));

        }
    }

    @GetMapping("/registration-of-birth/form-1/{id}/{downloadCode}")
    public ResponseEntity<InputStreamResource> downloadRegistrationOfBirthForm1(@PathVariable Long id,
            @PathVariable int downloadCode) {

        logger.info("downloadRegistrationOfBirthForm1 id: " + id + ", downloadCode: " + downloadCode);
        try {
            InputStreamResource inputStreamResource = null;
            String fileName = null;
            RegistrationOfBirthCertificate record = registrationOfBirthCertificateService.findById(id).orElseThrow();

            // System.out.println("found: " + p.toString());
            if (record.getDownloadCode() == downloadCode) {
                int pageNumber;
                String userName = userService.getCurrentUsername();
                switch (userName) {
                    case "busan":
                    case "gwangju":
                    case "nvseoul":
                        pageNumber = 1;
                        break;
                    default:
                        pageNumber = 0;
                        break;
                }
                ByteArrayOutputStream outputStream = registrationOfBirthService.createPDFPage1(record, pageNumber);
                InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
                inputStreamResource = new InputStreamResource(inputStream);

                fileName = Normalizer.normalize(record.getChildVietnameseFullName(), Normalizer.Form.NFD)
                        .replaceAll("[^\\p{ASCII}]", "");
                fileName = fileName.replace(" ", "");
                fileName = simpleDateFormat.format(new Date()) + "_KS_" + fileName + "_" + record.getId() + ".pdf";

            }
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(inputStreamResource);
        } catch (Exception ex) {
            logger.warn(ex.getMessage(), ex);
            return ResponseEntity.ok()
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(new InputStreamResource(new ByteArrayInputStream(ex.getMessage().getBytes())));

        }
    }

    private Passport2023 createPassportFromBirth(RegistrationOfBirthCertificate birth) {
        Passport2023 p = new Passport2023();

        // representative
        String rName = birth.getMotherFullName();
        String rGender = "Nữ";
        String rPassportNumber = birth.getMotherPassportNumber();
        String rPassportCreatedAt = birth.getMotherPassportCreatedAt();
        String rResidence = birth.getMotherResidence();
        String rDateOfBirth = birth.getMotherDateOfBirth();

        if ("father".equals(birth.getDeclarer().toLowerCase().trim())) {
            System.out.println("fa");
            rName = birth.getFatherFullName();
            rGender = "Nam";
            rPassportNumber = birth.getFatherPassportNumber();
            rPassportCreatedAt = birth.getFatherPassportCreatedAt();
            rResidence = birth.getFatherResidence();
            rDateOfBirth = birth.getFatherDateOfBirth();
        }

        p.setP2FullName(rName);
        p.setP2DateOfBirth(rDateOfBirth);
        p.setP2Gender(rGender);
        p.setP2PassportNumber(rPassportNumber);
        p.setP2PassportIssuedDate(rPassportCreatedAt);
        p.setP2CurrentPlaceOfResidence(rResidence);
        p.setP2PhoneNumber("");
        p.setP2Role(birth.getDeclarer());

        p.setId(birth.getId());

        p.setP1FullName(birth.getChildVietnameseFullName());
        p.setP1PlaceOfBirth("Hàn Quốc");
        p.setP1Gender(birth.getChildGender());
        p.setP1DateOfBirth(birth.getChildDateOfBirth());
        p.setP1Ethnic(birth.getChildEthnicGroup());
        p.setP1ForeignAddress(rResidence);
        p.setP1FatherFullName(birth.getFatherFullName());
        p.setP1FatherDateOfBirth(birth.getFatherDateOfBirth());
        p.setP1MotherFullName(birth.getMotherFullName());
        p.setP1MotherDateOfBirth(birth.getMotherDateOfBirth());
        p.setP1VietnameseAddress("");
        p.setP1PassportNumber("");
        p.setP1PassportIssuedDate("");

        return p;
    }

    @GetMapping("/registration-of-birth/form-2/{id}/{downloadCode}")
    public ResponseEntity<InputStreamResource> downloadRegistrationOfBirthForm2(@PathVariable Long id,
            @PathVariable int downloadCode) {

        logger.info("downloadRegistrationOfBirthForm2 id: " + id + ", downloadCode: " + downloadCode);
        try {
            InputStreamResource inputStreamResource = null;
            String fileName = null;
            RegistrationOfBirthCertificate record = registrationOfBirthCertificateService.findById(id).orElseThrow();

            // System.out.println("found: " + p.toString());
            if (record.getDownloadCode() == downloadCode) {
                // convert RegistrationOfBirthCertificate to Passport2023
                // ByteArrayOutputStream outputStream =
                // registrationOfBirthService.createPDFPage2(record);
                Passport2023 p = this.createPassportFromBirth(record);
                ByteArrayOutputStream outputStream = passportService.createPdfForm2(p);
                InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
                inputStreamResource = new InputStreamResource(inputStream);

                fileName = Normalizer.normalize(record.getChildVietnameseFullName(), Normalizer.Form.NFD)
                        .replaceAll("[^\\p{ASCII}]", "");
                fileName = fileName.replace(" ", "");
                fileName = simpleDateFormat.format(new Date()) + "_KS-HC_" + fileName + "_" + record.getId() + ".pdf";

            }
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(inputStreamResource);
        } catch (Exception ex) {
            logger.warn(ex.getMessage(), ex);
            return ResponseEntity.ok()
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(new InputStreamResource(new ByteArrayInputStream(ex.getMessage().getBytes())));

        }
    }

    @GetMapping("/marriage/{id}/{downloadCode}")
    public ResponseEntity<InputStreamResource> downloadMarriageForm(@PathVariable Long id,
            @PathVariable int downloadCode) {

        logger.info("downloadRegistrationOfBirthForm1 id: " + id + ", downloadCode: " + downloadCode);
        try {
            InputStreamResource inputStreamResource = null;
            String fileName = null;
            Marriage record = marriageService.findById(id).orElseThrow();

            // System.out.println("found: " + p.toString());
            if (record.getDownloadCode() == downloadCode) {

                ByteArrayOutputStream outputStream = marriageService.createPDFPage(record);
                InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
                inputStreamResource = new InputStreamResource(inputStream);

                fileName = Normalizer.normalize(record.getP1FullName(), Normalizer.Form.NFD)
                        .replaceAll("[^\\p{ASCII}]", "");
                fileName = fileName.replace(" ", "");
                fileName = simpleDateFormat.format(new Date()) + "_" + record.getFormType() + "_" + fileName + "_"
                        + record.getId() + ".pdf";

            }
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(inputStreamResource);
        } catch (Exception ex) {
            logger.warn(ex.getMessage(), ex);
            return ResponseEntity.ok()
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(new InputStreamResource(new ByteArrayInputStream(ex.getMessage().getBytes())));
        }
    }
}
