package com.sergiysuslo.onlineshop.config;

import com.sergiysuslo.onlineshop.entity.Product;
import com.sergiysuslo.onlineshop.entity.ProductCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.EntityType;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Configuration
public class MyDataRestConfig implements RepositoryRestConfigurer {

    private EntityManager entityManager;

    @Autowired
    public MyDataRestConfig(EntityManager theEntityManager) {
        entityManager = theEntityManager;
    }

    /* Disable POST, PUT, DELETE HTTP Methods for Product and ProductCategory endpoints */
    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {

        HttpMethod[] unsupportedHttpMethods = {HttpMethod.PUT, HttpMethod.POST, HttpMethod.DELETE};

        config.getExposureConfiguration()
                .forDomainType(Product.class)
                .withItemExposure((metdata, httpMethods) -> httpMethods.disable(unsupportedHttpMethods))
                .withCollectionExposure((metdata, httpMethods) -> httpMethods.disable(unsupportedHttpMethods));

        config.getExposureConfiguration()
                .forDomainType(ProductCategory.class)
                .withItemExposure((metdata, httpMethods) -> httpMethods.disable(unsupportedHttpMethods))
                .withCollectionExposure((metdata, httpMethods) -> httpMethods.disable(unsupportedHttpMethods));

        exposeIds(config);
    }

    //helper method to expose product category id's
    private void exposeIds(RepositoryRestConfiguration config) {

        Set<EntityType<?>> entities = entityManager.getMetamodel().getEntities();
        List<Class> entityClasses =  new ArrayList<>();

        for( EntityType entity : entities) {
            entityClasses.add(entity.getJavaType());
        }

        Class[] domainTypes = entityClasses.toArray(new Class[0]);
        config.exposeIdsFor(domainTypes);
    }
}
