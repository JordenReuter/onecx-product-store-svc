package org.tkit.onecx.product.store.rs.external.v1.mappers;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.mapstruct.*;
import org.tkit.onecx.product.store.domain.criteria.ProductSearchCriteria;
import org.tkit.onecx.product.store.domain.models.*;
import org.tkit.onecx.product.store.domain.wrapper.ProductLoadResultWrapper;
import org.tkit.quarkus.jpa.daos.PageResult;
import org.tkit.quarkus.rs.mappers.OffsetDateTimeMapper;

import gen.org.tkit.onecx.product.store.rs.external.v1.model.*;

@Mapper(uses = { OffsetDateTimeMapper.class })
public interface ProductMapperV1 {

    @Mapping(target = "providers", ignore = true)
    @Mapping(target = "classifications", ignore = true)
    @Mapping(target = "displayName", ignore = true)
    @Mapping(target = "name", ignore = true)
    ProductSearchCriteria map(ProductItemLoadSearchCriteriaDTOv1 data);

    @Mapping(target = "providers", ignore = true)
    @Mapping(target = "classifications", ignore = true)
    @Mapping(target = "displayName", ignore = true)
    @Mapping(target = "name", ignore = true)
    ProductSearchCriteria map(ProductItemSearchCriteriaDTOv1 productSearchCriteriaDTOV1);

    @Mapping(target = "removeStreamItem", ignore = true)
    ProductItemPageResultDTOv1 mapPageResult(PageResult<Product> page);

    @Mapping(target = "classifications", qualifiedByName = "toString")
    ProductItemDTOv1 maPageItems(Product data);

    @Mapping(target = "removeMicrofrontendsItem", ignore = true)
    @Mapping(target = "microfrontends", ignore = true)
    @Mapping(target = "removeMicroservicesItem", ignore = true)
    @Mapping(target = "microservices", ignore = true)
    @Mapping(target = "classifications", qualifiedByName = "toString")
    ProductDTOv1 map(Product data);

    default ProductDTOv1 map(Product product, Stream<Microfrontend> microfrontends, Stream<Microservice> microservices) {
        var p = map(product);
        p.setMicrofrontends(items(microfrontends));
        p.setMicroservices(microservices(microservices));
        return p;
    }

    List<MicrofrontendDTOv1> items(Stream<Microfrontend> microfrontends);

    List<MicroserviceDTOv1> microservices(Stream<Microservice> microservices);

    @Mapping(target = "removeEndpointsItem", ignore = true)
    MicrofrontendDTOv1 map(Microfrontend data);

    MicroserviceDTOv1 map(Microservice data);

    @Mapping(target = "version", source = "appVersion")
    MicrofrontendAbstractDTOv1 mapAbstract(Microfrontend mfe);

    @Mapping(target = "appName", source = "name")
    MicroserviceAbstractDTOv1 mapAbstract(Microservice mfe);

    List<MicrofrontendAbstractDTOv1> mapMfeList(List<Microfrontend> mfes);

    List<MicroserviceAbstractDTOv1> mapMsList(List<Microservice> ms);

    List<SlotAbstractDTOv1> mapSlotList(List<Slot> slot);

    default ProductsAbstractDTOv1 map(ProductLoadResultWrapper wrapper) {
        ProductsAbstractDTOv1 result = mapProduct(wrapper.getProduct());
        result.setMicrofrontends(mapMfeList(wrapper.getMicrofrontends()));
        result.setMicroservices(mapMsList(wrapper.getMicroservices()));
        result.setSlots(mapSlotList(wrapper.getSlots()));
        return result;
    }

    @Mapping(target = "removeStreamItem", ignore = true)
    ProductsLoadResultDTOv1 mapPageResultWrapper(PageResult<ProductLoadResultWrapper> page);

    @Mapping(target = "slots", ignore = true)
    @Mapping(target = "removeSlotsItem", ignore = true)
    @Mapping(target = "microfrontends", ignore = true)
    @Mapping(target = "removeMicrofrontendsItem", ignore = true)
    @Mapping(target = "microservices", ignore = true)
    @Mapping(target = "removeMicroservicesItem", ignore = true)
    @Mapping(target = "classifications", qualifiedByName = "toString")
    ProductsAbstractDTOv1 mapProduct(Product product);

    default LoadProductResponseDTOv1 createLoadProductResponse(List<Product> products,
            Map<String, List<Microfrontend>> microfrontends) {

        if (products == null) {
            return new LoadProductResponseDTOv1();
        }
        var tmp = products.stream().map(p -> createLoadProduct(p, microfrontends)).toList();
        return new LoadProductResponseDTOv1().products(tmp);
    }

    default LoadProductItemDTOv1 createLoadProduct(Product product, Map<String, List<Microfrontend>> microfrontends) {
        var tmp = createLoadProduct(product);
        tmp.setMicrofrontends(createLoadProductMicrofrontends(microfrontends.get(product.getName())));
        return tmp;
    }

    @Mapping(target = "microfrontends", ignore = true)
    @Mapping(target = "removeMicrofrontendsItem", ignore = true)
    LoadProductItemDTOv1 createLoadProduct(Product product);

    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
    List<LoadProductMicrofrontendDTOv1> createLoadProductMicrofrontends(List<Microfrontend> microfrontends);

    @Mapping(target = "removeEndpointsItem", ignore = true)
    LoadProductMicrofrontendDTOv1 createLoadProductMicrofrontend(Microfrontend microfrontend);

    @Mapping(target = "value", source = "classification")
    @Mapping(target = "id", ignore = true)
    ProductClassification mapClassificationString(String classification);

    @Named("updateList")
    default Set<ProductClassification> updateList(List<String> listToUpdate) {
        var list = new HashSet<ProductClassification>();
        if (listToUpdate != null) {
            for (var cf : listToUpdate) {
                list.add(mapClassificationString(cf));
            }
        }
        return list;
    }

    @Named("toString")
    default String setToString(Set<ProductClassification> classifications) {
        if (classifications != null && !classifications.isEmpty()) {
            return classifications.stream()
                    .map(ProductClassification::getValue)
                    .collect(Collectors.joining(","));
        }
        return "";
    }
}
