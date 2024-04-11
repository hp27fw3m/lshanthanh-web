package nb.hanquoc.web.controller;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import nb.hanquoc.web.AppUltil;
import nb.hanquoc.core.entity.RegistrationOfBirthCertificate;
import nb.hanquoc.web.service.RegistrationOfBirthCertificateService;
import nb.hanquoc.web.service.UserService;

@Controller
@RequestMapping("/registration-of-birth")
public class RegistrationOfBirthController {
    private Logger logger = LoggerFactory.getLogger(RegistrationOfBirthController.class);

    @Autowired
    private RegistrationOfBirthCertificateService service;

    @Autowired
    private UserService userService;

    private static final String FORM_NAME = "Khai sinh";

    @GetMapping({ "/", "/create" })
    public String handleGet(Model model) {
        RegistrationOfBirthCertificate record = new RegistrationOfBirthCertificate();
        record.setChildEthnicGroup("Kinh");
        record.setMotherEthnicGroup("Kinh");
        record.setFatherEthnicGroup("Kinh");
        record.setFatherNationality("Việt Nam");
        record.setMotherNationality("Việt Nam");
        model.addAttribute("record", record);
        model.addAttribute("message", FORM_NAME);
        return "registration-of-birth-create";
    }

    @PostMapping("/create")
    public String handlePost(@ModelAttribute RegistrationOfBirthCertificate record, Model model) {

        try {
            record.setId(null);
            record.setChildVietnameseFullName(record.getChildVietnameseFullName().toUpperCase());

            record.setChildEthnicGroup(AppUltil.toTitleCase(record.getChildEthnicGroup()));

            if (AppUltil.isNullOrEmpty(record.getFatherFullName())) {
                record.setFatherNationality("");
                record.setFatherEthnicGroup("");
            } else {
                record.setFatherFullName(record.getFatherFullName().toUpperCase());
                record.setFatherNationality(AppUltil.toTitleCase(record.getFatherNationality()));
                record.setFatherEthnicGroup(AppUltil.toTitleCase(record.getFatherEthnicGroup()));
                record.setFatherPassportNumber(record.getFatherPassportNumber().toUpperCase());
            }

            record.setMotherFullName(record.getMotherFullName().toUpperCase());
            record.setMotherNationality(AppUltil.toTitleCase(record.getMotherNationality()));
            record.setMotherEthnicGroup(AppUltil.toTitleCase(record.getMotherEthnicGroup()));
            record.setMotherPassportNumber(record.getMotherPassportNumber().toUpperCase());

            record.setDownloadCode(AppUltil.downloadCode());
            record.setCreatedBy(userService.getCurrentUsername());
            record.setCreatedAt(AppUltil.dateTimeToString(new Date()));

            RegistrationOfBirthCertificate savedRecord = service.save(record);
            logger.info(record.toString());
            model.addAttribute("record", savedRecord);

            return "registration-of-birth-download";
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            model.addAttribute("record", record);
            model.addAttribute("message", ex.getMessage());
            return "registration-of-birth-create";
        }

    }

    @GetMapping("/search")
    public String getSearchPassport(Model model) {
        //
        model.addAttribute("message", "Nhập đầy đủ các thông tin dưới đây để tìm kiếm tờ khai");
        return "registration-of-birth-certificate-search";
    }

    @PostMapping("/search")
    public String postSearchPassport(@RequestParam("id") Long id, @RequestParam("downloadCode") int downloadCode,
            Model model) {
        return "redirect:edit/" + id + "/" + downloadCode;
    }

    @GetMapping("/edit/{id}/{downloadCode}")
    public String handleEditGet(@PathVariable Long id, @PathVariable int downloadCode, Model model) {

        RegistrationOfBirthCertificate record = service.findById(id).orElseThrow();
        if (record.getDownloadCode() != downloadCode) {
            record = new RegistrationOfBirthCertificate();
        }

        model.addAttribute("record", record);
        model.addAttribute("message", FORM_NAME);
        return "registration-of-birth-create";
    }

}
