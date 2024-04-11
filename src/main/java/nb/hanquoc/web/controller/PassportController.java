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
import org.springframework.web.bind.annotation.RequestParam;

import nb.hanquoc.web.AppUltil;
import nb.hanquoc.core.entity.Passport2023;
import nb.hanquoc.web.service.Passport2023Service;
import nb.hanquoc.web.service.UserService;

@Controller
public class PassportController {
    private static final String TK02 = "Mẫu TK02 - Đề nghị cấp hộ chiếu phổ thông ở nước ngoài dành cho người từ 14 tuổi trở lên";
    private static final String TK02_A = "Mẫu TK02a - Đề nghị cấp hộ chiếu phổ thông ở nước ngoài dành cho người chưa đủ 14 tuổi";
    private Logger logger = LoggerFactory.getLogger(PassportController.class);

    @Autowired
    private Passport2023Service service;

    @Autowired
    private UserService userService;

    @GetMapping("/passport-1/create")
    public String handleGetPassportCreate1(Model model) {
        Passport2023 passport = new Passport2023();
        passport.setP1Ethnic("Kinh");
        model.addAttribute("passport", passport);
        model.addAttribute("message", TK02);
        return "passport-1-create";
    }

    @GetMapping("/passport-2/create")
    public String handleGetPassportCreate2(Model model) {
        Passport2023 passport = new Passport2023();
        passport.setP1PlaceOfBirth("Hàn Quốc");
        passport.setP1Ethnic("Kinh");
        model.addAttribute("passport", passport);
        model.addAttribute("message", TK02_A);
        return "passport-2-create";
    }

    @PostMapping("/passport-1/create")
    public String handlePostPassportCreate1(@ModelAttribute Passport2023 passport, Model model) {
        try {
            passport.setId(null);
            passport.setP1FullName(passport.getP1FullName().toUpperCase());
            passport.setP1PassportNumber(passport.getP1PassportNumber().toUpperCase());
            passport.setP1PassportSigner(AppUltil.toTitleCase(passport.getP1PassportSigner()));
            //passport.setP1VietnameseAddress(AppUltil.toTitleCase(passport.getP1VietnameseAddress()));
            passport.setP1FatherFullName(AppUltil.toTitleCase(passport.getP1FatherFullName()));
            passport.setP1MotherFullName(AppUltil.toTitleCase(passport.getP1MotherFullName()));
            passport.setDownloadCode(AppUltil.downloadCode());
            passport.setCreatedBy(userService.getCurrentUsername());
            passport.setCreatedAt(AppUltil.dateTimeToString(new Date()));

            Passport2023 savedPassport = service.savePassport(passport);
            model.addAttribute("passport", savedPassport);
            logger.info(passport.toString());
            return "passport-1-download";
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            model.addAttribute("message", ex.getMessage());
            return "passport-1-create";
        }

    }

    @PostMapping("/passport-2/create")
    public String handlePostPassportCreate2(@ModelAttribute Passport2023 passport, Model model) {
        try {
            passport.setId(null);
            passport.setP1FullName(passport.getP1FullName().toUpperCase());
            passport.setP1PassportNumber(passport.getP1PassportNumber().toUpperCase());
            passport.setP1PassportSigner(AppUltil.toTitleCase(passport.getP1PassportSigner()));
            //passport.setP1VietnameseAddress(AppUltil.toTitleCase(passport.getP1VietnameseAddress()));
            passport.setP1FatherFullName(AppUltil.toTitleCase(passport.getP1FatherFullName()));
            passport.setP1MotherFullName(AppUltil.toTitleCase(passport.getP1MotherFullName()));
            passport.setP1ForeignAddress(passport.getP2CurrentPlaceOfResidence());

            passport.setDownloadCode(AppUltil.downloadCode());
            passport.setCreatedBy(userService.getCurrentUsername());
            passport.setCreatedAt(AppUltil.dateTimeToString(new Date()));

            passport.setP2FullName(passport.getP2FullName().toUpperCase());
            passport.setP2PassportNumber(passport.getP2PassportNumber().toUpperCase());

            Passport2023 savedPassport = service.savePassport(passport);
            model.addAttribute("passport", savedPassport);
            logger.info(passport.toString());
            return "passport-2-download";
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            model.addAttribute("message", ex.getMessage());
            return "passport-2-create";
        }

    }

    @GetMapping({ "/passport-1/search", "/passport-2/search" })
    public String getSearchPassport(Model model) {
        //
        model.addAttribute("message", "Nhập đầy đủ các thông tin dưới đây để tìm kiếm tờ khai.");
        return "passport-1-search";
    }

    @PostMapping({ "/passport-1/search", "/passport-2/search" })
    public String postSearchPassport(@RequestParam("id") String id, @RequestParam("downloadCode") String downloadCode,
            Model model) {
        return "redirect:edit/" + id + "/" + downloadCode;
    }

    @GetMapping("/passport-1/edit/{id}/{downloadCode}")
    public String handleEdit1Get(@PathVariable Long id, @PathVariable int downloadCode, Model model) {

        Passport2023 record = service.findPassportById(id);
        if (record.getDownloadCode() != downloadCode) {
            record = new Passport2023();
        }

        model.addAttribute("passport", record);
        model.addAttribute("message", TK02);
        return "passport-1-create";
    }

    @GetMapping("/passport-2/edit/{id}/{downloadCode}")
    public String handleEdit2Get(@PathVariable Long id, @PathVariable int downloadCode, Model model) {

        Passport2023 record = service.findPassportById(id);
        if (record.getDownloadCode() != downloadCode) {
            record = new Passport2023();
        }

        model.addAttribute("passport", record);
        model.addAttribute("message", TK02);
        return "passport-2-create";
    }

}
