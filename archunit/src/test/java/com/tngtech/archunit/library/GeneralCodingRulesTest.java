package com.tngtech.archunit.library;

import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.library.testclasses.packages.correct.customsuffix.ImplementationClassWithCorrectPackageCustomSuffix;
import com.tngtech.archunit.library.testclasses.packages.correct.defaultsuffix.ImplementationClassWithCorrectPackage;
import com.tngtech.archunit.library.testclasses.packages.correct.notest.ImplementationClassWithoutTestClass;
import com.tngtech.archunit.library.testclasses.packages.correct.onedirmatching.ImplementationClassWithOneTestPackageMatchingOutOfTwo;
import com.tngtech.archunit.library.testclasses.packages.incorrect.nodirmatching.ImplementationClassWithMultipleTestsNotMatchingImplementationClassPackage;
import com.tngtech.archunit.library.testclasses.packages.incorrect.wrongsubdir.customsuffix.ImplementationClassWithWrongTestClassPackageCustomSuffix;
import com.tngtech.archunit.library.testclasses.packages.incorrect.wrongsubdir.customsuffix.subdir.ImplementationClassWithWrongTestClassPackageCustomSuffixTestingScenario;
import com.tngtech.archunit.library.testclasses.packages.incorrect.wrongsubdir.defaultsuffix.ImplementationClassWithWrongTestClassPackage;
import com.tngtech.archunit.library.testclasses.packages.incorrect.wrongsubdir.defaultsuffix.subdir.ImplementationClassWithWrongTestClassPackageTest;
import org.junit.Test;

import static com.tngtech.archunit.library.GeneralCodingRules.testClassesShouldResideInTheSamePackageAsImplementation;
import static com.tngtech.archunit.testutil.Assertions.assertThatRule;

public class GeneralCodingRulesTest {

    @Test
    public void test_class_in_same_package_should_fail_when_test_class_reside_in_different_package_as_implementation() {
        assertThatRule(testClassesShouldResideInTheSamePackageAsImplementation())
                .checking(new ClassFileImporter().importPackagesOf(ImplementationClassWithWrongTestClassPackage.class))
                .hasOnlyOneViolationWithStandardPattern(ImplementationClassWithWrongTestClassPackageTest.class,
                        "does not reside in same package as implementation class <" + ImplementationClassWithWrongTestClassPackage.class.getName()
                                + ">");
    }

    @Test
    public void test_class_in_same_package_should_fail_when_test_class_reside_in_different_package_as_implementation_with_custom_suffix() {
        assertThatRule(testClassesShouldResideInTheSamePackageAsImplementation("TestingScenario"))
                .checking(new ClassFileImporter().importPackagesOf(ImplementationClassWithWrongTestClassPackageCustomSuffix.class))
                .hasOnlyOneViolationWithStandardPattern(ImplementationClassWithWrongTestClassPackageCustomSuffixTestingScenario.class,
                        "does not reside in same package as implementation class <"
                                + ImplementationClassWithWrongTestClassPackageCustomSuffix.class.getName() + ">");
    }

    @Test
    public void test_class_in_same_package_should_pass_when_test_class_and_implementation_class_reside_in_the_same_package() {
        assertThatRule(testClassesShouldResideInTheSamePackageAsImplementation())
                .checking(new ClassFileImporter().importPackagesOf(ImplementationClassWithCorrectPackage.class))
                .hasNoViolation();
    }

    @Test
    public void test_class_in_same_package_should_pass_when_test_class_and_implementation_class_reside_in_the_same_package_with_custom_suffix() {
        assertThatRule(testClassesShouldResideInTheSamePackageAsImplementation("TestingScenario"))
                .checking(new ClassFileImporter().importPackagesOf(ImplementationClassWithCorrectPackageCustomSuffix.class))
                .hasNoViolation();
    }

    @Test
    public void should_pass_when_test_class_is_missing_and_only_implementation_provided() {
        assertThatRule(testClassesShouldResideInTheSamePackageAsImplementation())
                .checking(new ClassFileImporter().importPackagesOf(ImplementationClassWithoutTestClass.class))
                .hasNoViolation();
    }

    @Test
    public void should_pass_when_one_of_multiple_matching_test_classes_resides_in_the_implementation_package() {
        assertThatRule(testClassesShouldResideInTheSamePackageAsImplementation())
                .checking(new ClassFileImporter().importPackagesOf(ImplementationClassWithOneTestPackageMatchingOutOfTwo.class))
                .hasNoViolation();
    }

    @Test
    public void should_not_pass_when_none_of_multiple_matching_test_classes_resides_in_implementation_package() {
        assertThatRule(testClassesShouldResideInTheSamePackageAsImplementation())
                .checking(new ClassFileImporter().importPackagesOf(ImplementationClassWithMultipleTestsNotMatchingImplementationClassPackage.class))
                .hasViolations(2)
                .hasViolationWithStandardPattern(
                        com.tngtech.archunit.library.testclasses.packages.incorrect.nodirmatching.wrongdir1.ImplementationClassWithMultipleTestsNotMatchingImplementationClassPackageTest.class,
                        "does not reside in same package as implementation class <"
                                + ImplementationClassWithMultipleTestsNotMatchingImplementationClassPackage.class.getName() + ">"
                )
                .hasViolationWithStandardPattern(
                        com.tngtech.archunit.library.testclasses.packages.incorrect.nodirmatching.wrongdir2.ImplementationClassWithMultipleTestsNotMatchingImplementationClassPackageTest.class,
                        "does not reside in same package as implementation class <"
                                + ImplementationClassWithMultipleTestsNotMatchingImplementationClassPackage.class.getName() + ">"
                );
    }

}
