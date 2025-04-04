package org.tkit.onecx.product.store.rs.internal.mappers;

import java.util.List;

import org.mapstruct.*;
import org.tkit.onecx.product.store.domain.criteria.ProductSearchCriteria;
import org.tkit.onecx.product.store.domain.models.Microfrontend;
import org.tkit.onecx.product.store.domain.models.Microservice;
import org.tkit.onecx.product.store.domain.models.Product;
import org.tkit.quarkus.jpa.daos.PageResult;
import org.tkit.quarkus.rs.mappers.OffsetDateTimeMapper;

import gen.org.tkit.onecx.product.store.rs.internal.model.*;

@Mapper(uses = { OffsetDateTimeMapper.class })
public interface ProductMapper {

    @Mapping(target = "productNames", ignore = true)
    ProductSearchCriteria map(ProductSearchCriteriaDTO data);

    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "creationUser", ignore = true)
    @Mapping(target = "modificationDate", ignore = true)
    @Mapping(target = "modificationUser", ignore = true)
    @Mapping(target = "controlTraceabilityManual", ignore = true)
    @Mapping(target = "modificationCount", ignore = true)
    @Mapping(target = "persisted", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "operator", constant = "false")
    @Mapping(target = "undeployed", qualifiedByName = "undeployed")
    Product create(CreateProductRequestDTO dto);

    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "creationUser", ignore = true)
    @Mapping(target = "modificationDate", ignore = true)
    @Mapping(target = "modificationUser", ignore = true)
    @Mapping(target = "controlTraceabilityManual", ignore = true)
    @Mapping(target = "modificationCount", ignore = true)
    @Mapping(target = "persisted", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "operator", constant = "false")
    @Mapping(target = "undeployed", qualifiedByName = "undeployed")
    void update(UpdateProductRequestDTO dto, @MappingTarget Product product);

    @Mapping(target = "removeStreamItem", ignore = true)
    ProductPageResultDTO mapPageResult(PageResult<Product> page);

    @Mapping(target = "applications", ignore = true)
    @Mapping(target = "removeApplicationsItem", ignore = true)
    @Mapping(target = "undeployed", qualifiedByName = "get-undeployed")
    ProductAbstractDTO mapProductAbstract(Product data);

    @Mapping(target = "undeployed", qualifiedByName = "get-undeployed")
    ProductDTO map(Product data);

    @Named("get-undeployed")
    default Boolean getUndeployed(Boolean value) {
        if (value == null) {
            return false;
        }
        return value;
    }

    @Named("undeployed")
    @SuppressWarnings("java:S2447")
    default Boolean setUndeployed(Boolean value) {
        if (value == null || !value) {
            return null;
        }
        return true;
    }

    List<ApplicationAbstractDTO> mapMfeToAppAbstracts(List<Microfrontend> microfrontend);

    List<ApplicationAbstractDTO> mapMsToAppAbstracts(List<Microservice> microservice);

    @Mapping(target = "deprecated", ignore = true)
    @Mapping(target = "appName", source = "name")
    ApplicationAbstractDTO mapMsToAppAbstract(Microservice microservice);

}
