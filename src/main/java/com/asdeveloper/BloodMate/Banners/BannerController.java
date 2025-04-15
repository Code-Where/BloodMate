package com.asdeveloper.BloodMate.Banners;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/banners")
public class BannerController {

    @Autowired
    private BannerService bannerService;

    @GetMapping
    public List<Banner> getBanners() {
        return bannerService.getAllBanners();
    }

    @PostMapping
    public Banner addBanner(@RequestBody Banner banner) {
        return bannerService.addBanner(banner);
    }

    @DeleteMapping("/{id}")
    public String deleteBanner(@PathVariable Long id) {
        bannerService.deleteBanner(id);
        return "Banner deleted successfully!";
    }
}
