package me.kalpha.catalogservice.service;

import lombok.extern.slf4j.Slf4j;
import me.kalpha.catalogservice.jpa.CatalogEntity;
import me.kalpha.catalogservice.jpa.CatalogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CatalogServiceImpl implements CatalogService {
    private Environment env;
    private CatalogRepository catalogRepository;

    @Autowired
    public CatalogServiceImpl(Environment env, CatalogRepository catalogRepository) {
        this.env = env;
        this.catalogRepository = catalogRepository;
    }

    @Override
    public Iterable<CatalogEntity> getAllCatalogs() {
        return catalogRepository.findAll();
    }
}
