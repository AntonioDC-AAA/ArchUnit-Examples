package com.tngtech.archunit.exampletest;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.example.layers.SomeMediator;
import com.tngtech.archunit.example.layers.service.ServiceViolatingLayerRules;
import org.junit.Test;

import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

public class LayeredArchitectureTest {
    private final JavaClasses classes = new ClassFileImporter().importPackages("com.tngtech.archunit.example.layers");

    @Test
    public void layer_dependencies_are_respected() {
        layeredArchitecture().consideringAllDependencies()

                .layer("Controllers").definedBy("com.tngtech.archunit.example.layers.controller..")
                .layer("Services").definedBy("com.tngtech.archunit.example.layers.service..")
                .layer("Persistence").definedBy("com.tngtech.archunit.example.layers.persistence..")

                .whereLayer("Controllers").mayNotBeAccessedByAnyLayer()
                .whereLayer("Services").mayOnlyBeAccessedByLayers("Controllers")
                .whereLayer("Persistence").mayOnlyBeAccessedByLayers("Services")

                .check(classes);
    }

    @Test
    public void layer_dependencies_are_respected_with_exception() {
        layeredArchitecture().consideringAllDependencies()

                .layer("Controllers").definedBy("com.tngtech.archunit.example.layers.controller..")
                .layer("Services").definedBy("com.tngtech.archunit.example.layers.service..")
                .layer("Persistence").definedBy("com.tngtech.archunit.example.layers.persistence..")

                .whereLayer("Controllers").mayNotBeAccessedByAnyLayer()
                .whereLayer("Services").mayOnlyBeAccessedByLayers("Controllers")
                .whereLayer("Persistence").mayOnlyBeAccessedByLayers("Services")

                .ignoreDependency(SomeMediator.class, ServiceViolatingLayerRules.class)

                .check(classes);
    }
}
