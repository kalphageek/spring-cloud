package me.kalpha.catalogservice.service;

import me.kalpha.catalogservice.jpa.CatalogEntity;

public interface CatalogService {
    public Iterable<CatalogEntity> getAllCatalogs();
}
