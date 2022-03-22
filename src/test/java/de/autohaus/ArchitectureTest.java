package de.autohaus;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

public class ArchitectureTest {

    private final JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_JARS)
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("de.autohaus");

    @Test
    public void CheckLayeredArchitecture() {
        layeredArchitecture()
                .layer("ui").definedBy("de.autohaus.ui..")
                .layer("logic").definedBy("de.autohaus.logic..")
                .layer("data").definedBy("de.autohaus.data..")
                .layer("model").definedBy("de.autohaus.model..")
                .whereLayer("ui").mayNotBeAccessedByAnyLayer()
                .whereLayer("logic").mayOnlyBeAccessedByLayers("ui")
                .whereLayer("data").mayOnlyBeAccessedByLayers("logic")
                .whereLayer("model").mayOnlyBeAccessedByLayers("ui", "logic", "data")
                .check(importedClasses);
    }

    @Test
    public void CheckUilDependencies(){
        noClasses().that().resideInAPackage("..de.autohaus.ui..").should().dependOnClassesThat()
                .resideOutsideOfPackages("..de.autohaus.ui..", "..de.autohaus.logic..", "..de.autohaus.model..","..java.." ,"..javax..", "..intellij..").check(importedClasses);
    }

    @Test
    public void CheckLogicDependencies(){
        noClasses().that().resideInAPackage("..de.autohaus.logic..").should().dependOnClassesThat()
                .resideOutsideOfPackages("..de.autohaus.logic..", "..de.autohaus.data..", "..de.autohaus.model..","..java..", "..javax..").check(importedClasses);
    }

    @Test
    public void CheckDataDependencies(){
        noClasses().that().resideInAPackage("..de.autohaus.data..").should().dependOnClassesThat()
                .resideOutsideOfPackages("..de.autohaus.data..", "..de.autohaus.model..","..java..").check(importedClasses);
    }
}
