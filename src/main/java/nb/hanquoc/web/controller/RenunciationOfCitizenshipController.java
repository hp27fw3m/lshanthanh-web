package nb.hanquoc.web.controller;

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
import nb.hanquoc.core.entity.RenunciationOfCitizenship;
import nb.hanquoc.web.service.RenunciationOfCitizenshipService;
import nb.hanquoc.web.service.UserService;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping("/renunciation-of-citizenship")
public class RenunciationOfCitizenshipController {
    private Logger logger = LoggerFactory.getLogger(RenunciationOfCitizenshipController.class);

    @Autowired
    private RenunciationOfCitizenshipService service;
    @Autowired
    private UserService userService;

    @GetMapping("/create-1")
    public String handleCreate1Get(Model model) {

        RenunciationOfCitizenship renun = new RenunciationOfCitizenship();
        renun.setP1ReasonForRenunciation("Để nhập quốc tịch Hàn Quốc");

        model.addAttribute("renunciationOfCitizenship", renun);
        model.addAttribute("message", "Mẫu TP/QT-2020-ĐXTQT.1");
        return "renunciation-of-citizenship-create-1";
    }

    @PostMapping("/create-1")
    public String handleCreate1Post(@ModelAttribute RenunciationOfCitizenship record, Model model) {

        try {
            record.setId(null);
            record.setP1FullName(record.getP1FullName().toUpperCase());
            record.setP1PassportNumber(record.getP1PassportNumber().toUpperCase());

            // record.setP2FullName(record.getP2FullName().toUpperCase());
            // record.setP3FullName(record.getP3FullName().toUpperCase());

            // record.setP2PassportNumber(record.getP2PassportNumber().toUpperCase());
            // record.setP3PassportNumber(record.getP3PassportNumber().toUpperCase());

            if (record.getP2FullName() != null && record.getP2FullName().length() > 0) {
                record.setP2CurrentPlaceOfResidence(record.getP1CurrentPlaceOfResidence());
                record.setP2PlaceOfResidenceInVietnamBeforeDeparture(
                        record.getP1PlaceOfResidenceInVietnamBeforeDeparture());
            }

            if (record.getP3FullName() != null && record.getP3FullName().length() > 0) {
                record.setP3CurrentPlaceOfResidence(record.getP1CurrentPlaceOfResidence());
                record.setP3PlaceOfResidenceInVietnamBeforeDeparture(
                        record.getP1PlaceOfResidenceInVietnamBeforeDeparture());
            }

            record.setDownloadCode(AppUltil.downloadCode());
            record.setCreatedBy(userService.getCurrentUsername());
            record.setCreatedAt(AppUltil.dateTimeToString(new Date()));
            RenunciationOfCitizenship savedRecord = service.save(record);

            logger.info(savedRecord.toString());
            model.addAttribute("record", savedRecord);
            return "renunciation-of-citizenship-download-1";
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            model.addAttribute("record", record);
            model.addAttribute("message", "Mẫu TP/QT-2020-ĐXTQT.1 - " + ex.getMessage());
            return "renunciation-of-citizenship-create-1";
        }
    }

    @GetMapping("/search-1")
    public String handleSearch1Get(Model model) {
        model.addAttribute("message", "Nhập đầy đủ các thông tin dưới đây để tìm kiếm tờ khai");
        return "renunciation-of-citizenship-search-1";
    }

    @PostMapping("/search-1")
    public String handleSearch1Post(@RequestParam("id") Long id, @RequestParam("downloadCode") int downloadCode) {

        return "redirect:edit-1/" + id + "/" + downloadCode;
    }

    @GetMapping("/edit-1/{id}/{downloadCode}")
    public String handleEdit1Get(@PathVariable Long id, @PathVariable int downloadCode, Model model) {

        RenunciationOfCitizenship record = service.findById(id).orElseThrow();
        if (record.getDownloadCode() != downloadCode) {
            record = new RenunciationOfCitizenship();
        }

        model.addAttribute("renunciationOfCitizenship", record);
        model.addAttribute("message", "Mẫu TP/QT-2020-ĐXTQT.1");
        return "renunciation-of-citizenship-create-1";
    }

    @GetMapping("/create-2")
    public String handleCreate2Get(Model model) {

        RenunciationOfCitizenship renun = new RenunciationOfCitizenship();
        renun.setP2ReasonForRenunciation("Để nhập quốc tịch Hàn Quốc");
        model.addAttribute("renunciationOfCitizenship", renun);
        model.addAttribute("message", "Mẫu TP/QT-2020-ĐXTQT.2");
        return "renunciation-of-citizenship-create-2";
    }

    @PostMapping("/create-2")
    public String handleCreate2Post(@ModelAttribute RenunciationOfCitizenship record, Model model) {

        try {
            record.setP1FullName(record.getP1FullName().toUpperCase());
            record.setP2FullName(record.getP2FullName().toUpperCase());
            record.setP1PassportNumber(record.getP1PassportNumber().toUpperCase());
            record.setP2PassportNumber(record.getP2PassportNumber().toUpperCase());
            record.setP2CurrentPlaceOfResidence(record.getP1CurrentPlaceOfResidence());
            record.setDownloadCode(AppUltil.downloadCode());
            record.setCreatedBy(userService.getCurrentUsername());
            record.setCreatedAt(AppUltil.dateTimeToString(new Date()));

            RenunciationOfCitizenship savedRecord = service.save(record);
            logger.info(savedRecord.toString());
            model.addAttribute("record", savedRecord);
            return "renunciation-of-citizenship-download-2";
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            model.addAttribute("record", record);
            model.addAttribute("message", "Mẫu TP/QT-2020-ĐXTQT.2 - " + ex.getMessage());
            return "renunciation-of-citizenship-create-2";
        }
    }

    @GetMapping("/search-2")
    public String handleSearch2Get(Model model) {
        model.addAttribute("message", "Nhập đầy đủ các thông tin dưới đây để tìm kiếm tờ khai");
        return "renunciation-of-citizenship-search-2";
    }

    @PostMapping("/search-2")
    public String handleSearch2Post(@RequestParam("id") Long id, @RequestParam("downloadCode") int downloadCode) {

        return "redirect:edit-2/" + id + "/" + downloadCode;
    }

    @GetMapping("/edit-2/{id}/{downloadCode}")
    public String handleEdit2Get(@PathVariable Long id, @PathVariable int downloadCode, Model model) {

        RenunciationOfCitizenship record = service.findById(id).orElseThrow();
        if (record.getDownloadCode() != downloadCode) {
            record = new RenunciationOfCitizenship();
        }

        model.addAttribute("renunciationOfCitizenship", record);
        model.addAttribute("message", "Mẫu TP/QT-2020-ĐXTQT.2");
        return "renunciation-of-citizenship-create-2";
    }

}
