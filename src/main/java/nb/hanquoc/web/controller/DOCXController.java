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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nb.hanquoc.core.entity.RegistrationOfBirthCertificate;
import nb.hanquoc.core.entity.RenunciationOfCitizenship;
import nb.hanquoc.web.service.RenunciationOfCitizenshipService;
import nb.hanquoc.web.service.DOCXService;
import nb.hanquoc.web.service.RegistrationOfBirthCertificateService;

import java.io.ByteArrayOutputStream;

@Controller
@RequestMapping("/doc")
public class DOCXController {

    @Autowired
    private DOCXService docxService;

    @Autowired
    private RenunciationOfCitizenshipService renunciationService;

    @Autowired
    private RegistrationOfBirthCertificateService registrationOfBirthCertificateService;

    private Logger logger = LoggerFactory.getLogger(DOCXController.class);
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");

    @GetMapping("/renunciation-of-citizenship-2/{id}/{downloadCode}")
    public ResponseEntity<InputStreamResource> downloadRenunciationCurriculumVitae2(@PathVariable Long id,
            @PathVariable int downloadCode) {
        logger.info("downloadCurriculumVitae-2 id: " + id + ", downloadCode: " + downloadCode);
        try {
            InputStreamResource inputStreamResource = null;
            String fileName = null;
            RenunciationOfCitizenship record = renunciationService.findById(id).orElseThrow();

            // System.out.println("found: " + p.toString());
            if (record.getDownloadCode() == downloadCode) {
                ByteArrayOutputStream outputStream = docxService.createCurriculumVitae2(record);
                InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
                inputStreamResource = new InputStreamResource(inputStream);

                fileName = Normalizer.normalize(record.getP2FullName(), Normalizer.Form.NFD)
                        .replaceAll("[^\\p{ASCII}]", "");
                fileName = fileName.replace(" ", "");
                fileName = simpleDateFormat.format(new Date()) + "_CV_" + fileName + "_" + record.getId() + ".docx";

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
    public ResponseEntity<InputStreamResource> downloadRenunciationCurriculumVitae1(@PathVariable Long id,
            @PathVariable int downloadCode) {
        logger.info("downloadCurriculumVitae-1 id: " + id + ", downloadCode: " + downloadCode);
        try {
            InputStreamResource inputStreamResource = null;
            String fileName = null;
            RenunciationOfCitizenship record = renunciationService.findById(id).orElseThrow();

            // System.out.println("found: " + p.toString());
            if (record.getDownloadCode() == downloadCode) {
                ByteArrayOutputStream outputStream = docxService.createCurriculumVitae1(record);
                InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
                inputStreamResource = new InputStreamResource(inputStream);

                fileName = Normalizer.normalize(record.getP1FullName(), Normalizer.Form.NFD)
                        .replaceAll("[^\\p{ASCII}]", "");
                fileName = fileName.replace(" ", "");
                fileName = simpleDateFormat.format(new Date()) + "_CV_" + fileName + "_" + record.getId() + ".docx";

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

    @GetMapping("/registration-of-birth/doc-1/{id}/{downloadCode}")
    public ResponseEntity<InputStreamResource> downloadReportForBirthCertificate(@PathVariable Long id,
            @PathVariable int downloadCode) {
        logger.info("downloadReportForBirthCertificate id: " + id + ", downloadCode: " + downloadCode);
        try {
            InputStreamResource inputStreamResource = null;
            String fileName = null;
            RegistrationOfBirthCertificate record = registrationOfBirthCertificateService.findById(id).orElseThrow();

            // System.out.println("found: " + p.toString());
            if (record.getDownloadCode() == downloadCode) {
                ByteArrayOutputStream outputStream = docxService.createReportForBirthRegistration(record);
                InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
                inputStreamResource = new InputStreamResource(inputStream);

                fileName = Normalizer.normalize(record.getChildVietnameseFullName(), Normalizer.Form.NFD)
                        .replaceAll("[^\\p{ASCII}]", "");
                fileName = fileName.replace(" ", "");
                fileName = simpleDateFormat.format(new Date()) + "_KS-BTT_" + fileName + "_" + record.getId() + ".docx";

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
