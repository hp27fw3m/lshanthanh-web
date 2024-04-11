package nb.hanquoc.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import nb.hanquoc.web.service.PrefetchService;

@RestController
public class PrefetchController {

    @Autowired
    private PrefetchService appConfigService;

    @GetMapping("/ajax/{key}")
    public List<String> findPassportByID(@PathVariable String key) {
        return appConfigService.getAddressList(key);
    }
}
