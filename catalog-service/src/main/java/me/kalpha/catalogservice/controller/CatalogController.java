package me.kalpha.catalogservice.controller;

import me.kalpha.catalogservice.jpa.CatalogEntity;
import me.kalpha.catalogservice.service.CatalogService;
import me.kalpha.catalogservice.vo.CatalogResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/catalog-service")
public class CatalogController {
    private CatalogService catalogService;
    private Environment env;
    private ModelMapper mapper;

    @Autowired
    public CatalogController(CatalogService catalogService, Environment env, ModelMapper mapper) {
        this.catalogService = catalogService;
        this.env = env;
        this.mapper = mapper;
    }

    @GetMapping("/catalogs")
    public ResponseEntity<List<CatalogResponse>> getCatalogs() {
        Iterable<CatalogEntity> catalogEntities = catalogService.getAllCatalogs();
        List<CatalogResponse> result = new ArrayList<>();
        catalogEntities.forEach(v -> {
            result.add(mapper.map(v, CatalogResponse.class));
        });
        return ResponseEntity.ok(result);
    }

    @GetMapping("/health_check")
    public String status(HttpServletRequest request) {
        return String.format("It's working in Catalog Service on PORT : %s!", request.getServerPort());
    }
}
