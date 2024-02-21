package org.tkit.onecx.product.store.rs.external.v1.controllers;

import java.util.ArrayList;
import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Response;

import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;
import org.tkit.onecx.product.store.domain.daos.MicrofrontendDAO;
import org.tkit.onecx.product.store.domain.daos.MicroserviceDAO;
import org.tkit.onecx.product.store.domain.daos.ProductDAO;
import org.tkit.onecx.product.store.rs.external.v1.mappers.ExceptionMapperV1;
import org.tkit.onecx.product.store.rs.external.v1.mappers.ProductMapperV1;
import org.tkit.quarkus.log.cdi.LogService;

import gen.org.tkit.onecx.product.store.rs.external.v1.ProductsApi;
import gen.org.tkit.onecx.product.store.rs.external.v1.model.ProblemDetailResponseDTOv1;
import gen.org.tkit.onecx.product.store.rs.external.v1.model.ProductItemSearchCriteriaDTOv1;
import gen.org.tkit.onecx.product.store.rs.external.v1.model.ProductsAppIdsDTOv1;
import gen.org.tkit.onecx.product.store.rs.external.v1.model.ProductsAppIdsResponseDTOv1;

@LogService
@ApplicationScoped
@Transactional(Transactional.TxType.NOT_SUPPORTED)
public class ProductsRestControllerV1 implements ProductsApi {

    @Inject
    ExceptionMapperV1 exceptionMapper;

    @Inject
    ProductMapperV1 mapper;

    @Inject
    ProductDAO dao;

    @Inject
    MicrofrontendDAO microfrontendDAO;

    @Inject
    MicroserviceDAO microserviceDAO;

    @Override
    @Transactional
    public Response getAppIdsByProductNames(List<String> requestBody) {
        ProductsAppIdsResponseDTOv1 responseDTOv1 = new ProductsAppIdsResponseDTOv1();
        List<ProductsAppIdsDTOv1> listOfProducts = new ArrayList<>();
        requestBody.forEach(s -> {
            ProductsAppIdsDTOv1 dto = new ProductsAppIdsDTOv1();
            dto.setProductName(s);
            List<String> appIds = new ArrayList<>();
            appIds.addAll(microfrontendDAO.getAllAppIdsByProductName(s).getStream().toList());
            appIds.addAll(microserviceDAO.getAllAppIdsByProductName(s).getStream().toList());
            dto.setAppIds(appIds);
            listOfProducts.add(dto);
        });
        responseDTOv1.setProducts(listOfProducts);
        return Response.ok(responseDTOv1).build();
    }

    @Override
    public Response getProductByName(String name) {
        var product = dao.findProductByName(name);
        if (product == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        var microfrontends = microfrontendDAO.loadByProductName(product.getName());
        var microservices = microserviceDAO.loadByProductName(product.getName());

        var dto = mapper.map(product, microfrontends, microservices);

        return Response.ok(dto).build();
    }

    @Override
    public Response searchProductsByCriteria(ProductItemSearchCriteriaDTOv1 productSearchCriteriaDTOV1) {
        var criteria = mapper.map(productSearchCriteriaDTOV1);
        var result = dao.findProductsByCriteria(criteria);
        return Response.ok(mapper.mapPageResult(result)).build();
    }

    @ServerExceptionMapper
    public RestResponse<ProblemDetailResponseDTOv1> constraint(ConstraintViolationException ex) {
        return exceptionMapper.constraint(ex);
    }
}
