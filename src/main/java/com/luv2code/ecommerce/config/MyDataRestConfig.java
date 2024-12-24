package com.luv2code.ecommerce.config;

import com.luv2code.ecommerce.entity.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.metamodel.EntityType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.util.pattern.PathPattern;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Configuration
public class MyDataRestConfig implements RepositoryRestConfigurer {

    @Value("${allowed.origins}")
    private String[] theAllowedOrigins;

    private EntityManager entityManager;
    @Autowired
    public MyDataRestConfig(EntityManager theEntityManager){
       entityManager = theEntityManager;
    }

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
        RepositoryRestConfigurer.super.configureRepositoryRestConfiguration(config, cors);

        HttpMethod[] theUnsupportedActions = { HttpMethod.PUT,HttpMethod.POST, HttpMethod.DELETE, HttpMethod.PATCH};

        //disable the HTTP methods for product:put, post and delete
//        config.getExposureConfiguration()  //ExposureConfiguration
//                .forDomainType(Product.class) //ExposureConfigure
//                .withItemExposure( (metdata, httpMethods) ->  httpMethods.disable(theUnsupportedActions))
//                .withCollectionExposure((metdata, httpMethods) -> httpMethods.disable(theUnsupportedActions));

        //disable the HTTP methods for productCategory:put, post and delete
        disableHttpMethods(Product.class, config, theUnsupportedActions);
        disableHttpMethods(ProductCategory.class, config, theUnsupportedActions);
        disableHttpMethods(Country.class, config, theUnsupportedActions);
        disableHttpMethods(State.class, config, theUnsupportedActions);
        disableHttpMethods(Order.class, config, theUnsupportedActions);
//        config.getExposureConfiguration()  //ExposureConfiguration
//                .forDomainType(ProductCategory.class) //ExposureConfigure
//                .withItemExposure( (metdata, httpMethods) ->  httpMethods.disable(theUnsupportedActions))
//                .withCollectionExposure((metdata, httpMethods) -> httpMethods.disable(theUnsupportedActions));


        //Call an Internal helper method
        exposeIds(config);

        //config the CORS mapping
        cors.addMapping( config.getBasePath()+"/**").allowedOrigins(theAllowedOrigins);
        //config.getBasePath() & theAllowedOrigins->access from application.properties

    }

    private static void disableHttpMethods(Class theClass, RepositoryRestConfiguration config, HttpMethod[] theUnsupportedActions) {
        config.getExposureConfiguration()  //ExposureConfiguration
                .forDomainType(theClass) //ExposureConfigure
                .withItemExposure( (metdata, httpMethods) ->  httpMethods.disable(theUnsupportedActions))
                .withCollectionExposure((metdata, httpMethods) -> httpMethods.disable(theUnsupportedActions));
    }

    private void exposeIds(RepositoryRestConfiguration config) {
        //expose entity Ids

        // -get a list of all entity classes from the entity manager
        Set<EntityType<?>> entities = entityManager.getMetamodel().getEntities();

        // -create an array of the entity types
        List<Class> entityClasses = new ArrayList<>();

        // -get the entity types for the entities
        for (EntityType tempEntityType : entities){
            entityClasses.add(tempEntityType.getJavaType());
        }

        // -expose the entity ids for the array of entity types/domain types
        Class [] domainTypes = entityClasses.toArray(new  Class[0]);
        config.exposeIdsFor(domainTypes);
    }
}
