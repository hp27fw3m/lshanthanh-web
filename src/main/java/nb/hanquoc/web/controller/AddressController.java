package nb.hanquoc.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import nb.hanquoc.web.service.PrefetchService;

@Controller
public class AddressController {
    @Autowired
    private PrefetchService appConfigService;

    @GetMapping("/address")
    String index(Model model) {
        List<String> items = appConfigService.getAddressList("address");
        model.addAttribute("items", items);
        return "address";
    }
}
