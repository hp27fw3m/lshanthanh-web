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

import nb.hanquoc.web.AppUltil;
import nb.hanquoc.core.entity.Marriage;
import nb.hanquoc.web.service.MarriageService;
import nb.hanquoc.web.service.UserService;

@Controller
@RequestMapping("/marriage")
public class MarriageController {

    Logger logger = LoggerFactory.getLogger(MarriageController.class);

    private static String FORM_NAME_CN = "Giấy chứng nhận đủ kiều kiện kết hôn";
    private static String FORM_NAME_GC = "Tờ khai Ghi chú kết hôn";
    private static String FORM_NAME_KH = "Tờ khai Đăng ký kết hôn";
    private static String FORM_NAME_LH = "Tờ khai Ghi chú ly hôn";
    @Autowired
    private MarriageService marriageService;

    @Autowired
    private UserService userService;

    @GetMapping("/certificate-of-marriage-eligibility/create")
    public String form1Get(Model model) {
        Marriage record = new Marriage();
        record.setP1Ethnic("Kinh");
        record.setP2Nationality("Hàn Quốc");
        simulateData(record);
        record.setP2PassportIssuedPlace("Bộ Ngoại giao Hàn Quốc");
        model.addAttribute("record", record);
        model.addAttribute("message", FORM_NAME_CN);
        return "mr-certificate-of-marriage-eligibility-create";
    }

    @PostMapping("/certificate-of-marriage-eligibility/create")
    public String form1Post(@ModelAttribute Marriage record, Model model) {
        try {
            record.setId(null);
            record.setFormType("CN");
            record.setDownloadCode(AppUltil.downloadCode());
            record.setCreatedBy(userService.getCurrentUsername());
            record.setCreatedAt(AppUltil.dateTimeToString(new Date()));

            record.setP1FullName(record.getP1FullName().toUpperCase());
            record.setP1PassportNumber(record.getP1PassportNumber().toUpperCase());
            record.setP1Nationality("Việt Nam");

            record.setP2FullName(record.getP2FullName().toUpperCase());
            record.setP2PassportNumber(record.getP2PassportNumber().toUpperCase());

            Marriage savedRecord = marriageService.save(record);
            logger.info(record.toString());
            model.addAttribute("record", savedRecord);

            return "mr-marriage-download";
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            model.addAttribute("record", record);
            model.addAttribute("message", ex.getMessage());
            return "mr-certificate-of-marriage-eligibility-create";
        }

    }

    @GetMapping("/certificate-of-marriage-eligibility/edit/{id}/{downloadCode}")
    public String form1Edit(@PathVariable Long id, @PathVariable int downloadCode, Model model) {
        Marriage record = marriageService.findById(id).orElseThrow();
        if (record.getDownloadCode() != downloadCode) {
            record = new Marriage();
        }

        model.addAttribute("record", record);
        model.addAttribute("message", "[" + id + "-" + downloadCode + "] " + FORM_NAME_CN);
        return "mr-certificate-of-marriage-eligibility-create";
    }

    @GetMapping("/registration-of-marriage/create")
    public String form2Get(Model model) {
        Marriage record = new Marriage();
        model.addAttribute("record", record);
        model.addAttribute("message", FORM_NAME_GC);
        simulateData(record);
        return "mr-registration-of-marriage-create";
    }

    private void simulateData(Marriage record) {
        record.setP1FullName("Nguyễn Thị Huyền");
        record.setP1DateOfBirth("18/02/1996");
        record.setP1Ethnic("Kinh");
        record.setP1Gender("Nữ");
        record.setP1Nationality("Việt Nam");
        record.setP1PlaceOfBirth("Long An");
        record.setP1PlaceOfResidenceInVietnamBeforeDeparture("Phường Trại Cau, quận Lê Chân, thành phố Hải Phòng");
        record.setP1PassportNumber("C4549172");
        record.setP1PassportIssuedDate("01/01/2021");
        record.setP1PassportIssuedPlace("Cục Quản lý xuất nhập cảnh, Bộ Công an");
        record.setP1MaritalStatus("Chưa kết hôn");

        record.setP2FullName("HAM JUNGHOON");
        record.setP2DateOfBirth("07/01/1981");
        record.setP2Nationality("Hàn Quốc");
        record.setP2PassportIssuedDate("02/02/2002");
        record.setP2PassportNumber("M66266390");
        record.setP2PassportIssuedPlace("Bộ Ngoại giao Hàn Quốc");
        record.setP2MaritalStatus("Đã ly hôn");
        record.setP2CurrentPlaceOfResidence("123 Bukchon-ro, Jongno-gu, Seoul");

        record.setCertificateIssuedDate("08/03/2022");
    }

    @PostMapping("/registration-of-marriage/create")
    public String form2Post(@ModelAttribute Marriage record, Model model) {
        try {
            record.setId(null);
            record.setFormType("GC");
            record.setDownloadCode(AppUltil.downloadCode());
            record.setCreatedBy(userService.getCurrentUsername());
            record.setCreatedAt(AppUltil.dateTimeToString(new Date()));

            record.setP1Gender("Nữ");
            record.setP2Gender("Nam");

            Marriage savedRecord = marriageService.save(record);
            logger.info(record.toString());
            model.addAttribute("record", savedRecord);

            return "mr-marriage-download";
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            model.addAttribute("record", record);
            model.addAttribute("message", ex.getMessage());
            return "mr-registration-of-marriage-create";
        }
    }

    @GetMapping("/registration-of-marriage/edit/{id}/{downloadCode}")
    public String form2Edit(@PathVariable Long id, @PathVariable int downloadCode, Model model) {
        Marriage record = marriageService.findById(id).orElseThrow();
        if (record.getDownloadCode() != downloadCode) {
            record = new Marriage();
        }

        model.addAttribute("record", record);
        model.addAttribute("message", "[" + id + "-" + downloadCode + "] " + FORM_NAME_GC);
        return "mr-registration-of-marriage-create";
    }

    @GetMapping("/certificate-of-marriage/create")
    public String form3Get(Model model) {
        Marriage record = new Marriage();
        model.addAttribute("record", record);
        model.addAttribute("message", FORM_NAME_KH);
        simulateData(record);
        record.setP1CurrentPlaceOfResidence("Thành phố Busan");
        record.setP2CurrentPlaceOfResidence("Tỉnh Gyeongsangnam");

        return "mr-certificate-of-marriage-create";
    }

    @PostMapping("/certificate-of-marriage/create")
    public String form3Post(@ModelAttribute Marriage record, Model model) {
        try {
            record.setId(null);
            record.setFormType("KH");
            record.setDownloadCode(AppUltil.downloadCode());
            record.setCreatedBy(userService.getCurrentUsername());
            record.setCreatedAt(AppUltil.dateTimeToString(new Date()));

            record.setP1Gender("Nữ");
            record.setP2Gender("Nam");

            Marriage savedRecord = marriageService.save(record);
            logger.info(record.toString());
            model.addAttribute("record", savedRecord);

            return "mr-marriage-download";
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            model.addAttribute("record", record);
            model.addAttribute("message", ex.getMessage());
            return "mr-certificate-of-marriage-create";
        }
    }

    @GetMapping("/certificate-of-marriage/edit/{id}/{downloadCode}")
    public String form3Edit(@PathVariable Long id, @PathVariable int downloadCode, Model model) {
        Marriage record = marriageService.findById(id).orElseThrow();
        if (record.getDownloadCode() != downloadCode) {
            record = new Marriage();
        }

        model.addAttribute("record", record);
        model.addAttribute("message", "[" + id + "-" + downloadCode + "] " + FORM_NAME_KH);
        return "mr-certificate-of-marriage-create";
    }

    @GetMapping("/registration-of-divorce/create")
    public String form4Get(Model model) {
        Marriage record = new Marriage();
        model.addAttribute("record", record);
        model.addAttribute("message", FORM_NAME_LH);
        simulateData(record);

        record.setP1CurrentPlaceOfResidence("Thành phố Busan");
        record.setP2CurrentPlaceOfResidence("Tỉnh Gyeongsangnam");

        return "mr-registration-of-divorce-create";
    }

}
