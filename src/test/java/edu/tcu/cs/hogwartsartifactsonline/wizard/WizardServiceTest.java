package edu.tcu.cs.hogwartsartifactsonline.wizard;

import edu.tcu.cs.hogwartsartifactsonline.artifact.Artifact;
import edu.tcu.cs.hogwartsartifactsonline.artifact.ArtifactRepository;
import edu.tcu.cs.hogwartsartifactsonline.artifact.utils.IdWorker;
import edu.tcu.cs.hogwartsartifactsonline.system.exception.ObjectNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

/**
 * @ExtendWith: Allows us to use JUnit extensions.
 */
@ExtendWith(MockitoExtension.class)
class WizardServiceTest {

    @Mock
    WizardRepository wizardRepository;

    List<Wizard> wizards;

    List<Artifact> artifacts;

    @Mock
    IdWorker idWorker;

    // Since WizardService depends on ArtifactRepository we need to mock this behavior
    @Mock
    ArtifactRepository artifactRepository;

    /**
     * Inject the wizardRepository mock and artifactRepository mock into the wizardService object.
     */
    @InjectMocks
    WizardService wizardService;

    @BeforeEach
    void setUp() {

        // fake/mock wizards
        Wizard wizard = new Wizard();
        wizard.setId(1);
        wizard.setName("Albus Dumbledore");

        Wizard wizard1 = new Wizard();
        wizard1.setId(2);
        wizard1.setName("Harry Potter");

        Wizard wizard2 = new Wizard();
        wizard2.setId(3);
        wizard2.setName("Neville Longbottom");

        this.wizards = new ArrayList<>(Arrays.asList(wizard, wizard1, wizard2));

        // fake/mock artifacts
        Artifact a1 = new Artifact();
        a1.setId("1250808601744904191");
        a1.setName("Deluminator");
        a1.setDescription("A Deluminator is a device invented by Albus Dumbledore that resembles a cigarette lighter. It is used to remove or absorb (as well as return) the light from any light source to provide cover to the user.");
        a1.setImageUrl("ImageUrl");

        Artifact a2 = new Artifact();
        a2.setId("1250808601744904192");
        a2.setName("Invisibility Cloak");
        a2.setDescription("An invisibility cloak is used to make the wearer invisible.");
        a2.setImageUrl("ImageUrl");

        Artifact a3 = new Artifact();
        a3.setId("1250808601744904193");
        a3.setName("Elder Wand");
        a3.setDescription("The Elder Wand, known throughout history as the Deathstick or the Wand of Destiny, is an extremely powerful wand made of elder wood with a core of Thestral tail hair.");
        a3.setImageUrl("ImageUrl");

        Artifact a4 = new Artifact();
        a4.setId("1250808601744904194");
        a4.setName("The Marauder's Map");
        a4.setDescription("A magical map of Hogwarts created by Remus Lupin, Peter Pettigrew, Sirius Black, and James Potter while they were students at Hogwarts.");
        a4.setImageUrl("ImageUrl");

        Artifact a5 = new Artifact();
        a5.setId("1250808601744904195");
        a5.setName("The Sword Of Gryffindor");
        a5.setDescription("A goblin-made sword adorned with large rubies on the pommel. It was once owned by Godric Gryffindor, one of the medieval founders of Hogwarts.");
        a5.setImageUrl("ImageUrl");

        Artifact a6 = new Artifact();
        a6.setId("1250808601744904196");
        a6.setName("Resurrection Stone");
        a6.setDescription("The Resurrection Stone allows the holder to bring back deceased loved ones, in a semi-physical form, and communicate with them.");
        a6.setImageUrl("ImageUrl");

        artifacts = new ArrayList<>(Arrays.asList(a1, a2, a3, a4, a5, a6));

        wizard1.addArtifact(a1);
        wizard1.addArtifact(a2);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testFindWizardByIdSuccess() {
        // Given
        // reference a fake wizard.
        Wizard wizard = this.wizards.get(0);
        int wizardId = wizard.getId();
        // we must mock the wizardRepository's findWizardById call
        given(wizardRepository.findById(wizardId)).willReturn(Optional.of(wizard));

        // When
        Wizard returnedWizard = wizardService.findWizardById(wizardId);

        // Then
        // compare the results from our obtained wizard to the mocked wizard.
        assertThat(returnedWizard.getId()).isEqualTo(wizard.getId());
        assertThat(returnedWizard.getName()).isEqualTo(wizard.getName());

        // verify the number of times findWizardById was called.
        verify(wizardRepository, times(1)).findById(wizard.getId());
    }

    @Test
    void TestFindWizardByIdNotFound() {
        // Given
        int nonExistingWizardId = 9;
        // mock the wizardRepository's findWizardById call. note: we don't need to prepare fake data as we're going to return nothing.
//        given(wizardRepository.findById(anyInt())).willReturn(Optional.empty());
        given(wizardRepository.findById(nonExistingWizardId)).willReturn(Optional.empty());

        // When
        /*
         * If wizardService.findWizardById throws an exception then catchThrowable will catch
         * the exception and assign that exception to the thrown variable.
         * The thrown variable can be used in the "then" portion of our test.
         */
        Throwable thrown = catchThrowable(() -> {
            Wizard returnedWizard = wizardService.findWizardById(nonExistingWizardId);
        });

        // Then
        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find wizard with Id 9 :(");

        // verify that our repository invoked the findById method once.
        verify(wizardRepository, times(1)).findById(nonExistingWizardId);
    }

    @Test
    void testFindAllSuccess() {
        // Given
        given(wizardRepository.findAll()).willReturn(this.wizards);

        // When
        List<Wizard> returnedWizards = wizardService.findAll();

        // Then
        assertThat(returnedWizards.size()).isEqualTo(this.wizards.size());
    }

    @Test
    void testSaveSuccess() {
        // Given
        Wizard newWizard = new Wizard();
        newWizard.setName("Hermione Granger");

        // Describe behavior of the mock object (newWizard)
        given(idWorker.nextId()).willReturn(4L);
        // the same that is being saved will be returned.
        given(wizardRepository.save(newWizard)).willReturn(newWizard);

        // When
        Wizard savedWizard = wizardService.save(newWizard);

        // Then
        assertThat(savedWizard.getId()).isEqualTo(4L);
        assertThat(savedWizard.getName()).isEqualTo(newWizard.getName());
        assertThat(savedWizard.getNumberOfArtifacts()).isEqualTo(newWizard.getNumberOfArtifacts());
    }

    @Test
    void testUpdateSuccess() {
        // Given
        Wizard oldWizard = this.wizards.get(1);
        Wizard newWizard = new Wizard();
        newWizard.setId(oldWizard.getId());
        newWizard.setName(oldWizard.getName() + "-update");
        newWizard.setArtifacts(oldWizard.getArtifacts());

        /*
         * 1. Find the artifact
         * 2. Update the old artifact with new values.
         */
        given(this.wizardRepository.findById(oldWizard.getId())).willReturn(Optional.of(oldWizard));
        given(this.wizardRepository.save(oldWizard)).willReturn(oldWizard);

        // When
        Wizard updatedWizard = this.wizardService.update(newWizard.getId(), newWizard);

        assertThat(updatedWizard.getId()).isEqualTo(newWizard.getId());
        assertThat(updatedWizard.getName()).isEqualTo(newWizard.getName());
        assertThat(updatedWizard.getNumberOfArtifacts()).isEqualTo(newWizard.getNumberOfArtifacts());

        verify(this.wizardRepository, times(1)).findById(oldWizard.getId());
        verify(this.wizardRepository, times(1)).save(oldWizard);

    }

    @Test
    void testUpdateNotFound() {
        // Given
        /*
         * Prepare an update object.
         */
        Wizard oldWizard = this.wizards.get(1);
        Wizard newWizard = new Wizard();
        newWizard.setName(oldWizard.getName() + "-update");
        newWizard.setArtifacts(oldWizard.getArtifacts());
        given(wizardRepository.findById(9)).willReturn(Optional.empty());

        // When
        assertThrows(ObjectNotFoundException.class, () -> {
           wizardService.update(9, newWizard);
        });

        // Then
        verify(wizardRepository, times(1)).findById(9);

    }

    @Test
    void testDeleteSuccess() {
        // Given
        Wizard wizard = this.wizards.get(0);
        given(wizardRepository.findById(wizard.getId())).willReturn(Optional.of(wizard));
        // nothing needs to be done when the delete method is invoked (we return null in the service layer).
        doNothing().when(wizardRepository).deleteById(wizard.getId());

        // When
        wizardService.delete(wizard.getId());

        // Then
        verify(wizardRepository, times(1)).deleteById(wizard.getId());

    }

    @Test
    void testDeleteNotFound() {
        // Given
        given(wizardRepository.findById(9)).willReturn(Optional.empty());

        // When
        assertThrows(ObjectNotFoundException.class, () -> {
            wizardService.delete(9);
        });

        // Then
        verify(wizardRepository, times(1)).findById(9);
    }

    @Test
    void testAssignArtifactSuccess() {
        // Given
        // prepare fake data. fake artifact and wizards.

        Artifact artifact = new Artifact();
        Artifact oldArifact = this.artifacts.get(1);
        artifact.setId(oldArifact.getId());
        artifact.setName(oldArifact.getName());
        artifact.setDescription(oldArifact.getDescription());
        artifact.setImageUrl(oldArifact.getImageUrl());


        Wizard w2 = new Wizard();
        Wizard oldw2Wizard = this.wizards.get(1);
        w2.setId(oldw2Wizard.getId());
        w2.setName(oldw2Wizard.getName());
        w2.addArtifact(artifact);

        Wizard w3 = new Wizard();
        Wizard oldw3Wizard = this.wizards.get(2);
        w3.setId(oldw3Wizard.getId());
        w3.setName(oldw3Wizard.getName());

        // define behavior of the mock objects.
        // mock the behavior of artifactRepository's findById method.
        given(this.artifactRepository.findById(artifact.getId())).willReturn(Optional.of(artifact));
        // mock the behavior of wizardRepository's findById
        given(this.wizardRepository.findById(w3.getId())).willReturn(Optional.of(w3));

        // When
        // the find methods below should work as both exist.
        this.wizardService.assignArtifact(w3.getId(), artifact.getId());

        // Then
        /*
         * before the assignArtifact the Invisiblity cloak belongs to Harry Potter and Neville doesn't have anything in his artifactList
         * after assignArtifact the invisiblity cloak belongs to Neville and he should have one item in his artifactList.
         */
        assertThat(artifact.getOwner().getId()).isEqualTo(w3.getId());
        assertThat(w3.getArtifacts()).contains(artifact);
    }

    @Test
    void testAssignArtifactErrorWithNonExistentWizardId() {
        // Given
        // prepare fake data. fake artifact and wizards.

        Artifact artifact = new Artifact();
        Artifact oldArifact = this.artifacts.get(1);
        artifact.setId(oldArifact.getId());
        artifact.setName(oldArifact.getName());
        artifact.setDescription(oldArifact.getDescription());
        artifact.setImageUrl(oldArifact.getImageUrl());


        Wizard w2 = new Wizard();
        Wizard oldw2Wizard = this.wizards.get(1);
        w2.setId(oldw2Wizard.getId());
        w2.setName(oldw2Wizard.getName());
        w2.addArtifact(artifact);

        // define behavior of the mock objects.
        // mock the behavior of artifactRepository's findById method.
        given(this.artifactRepository.findById(artifact.getId())).willReturn(Optional.of(artifact));
        // mock the behavior of wizardRepository's findById
        given(this.wizardRepository.findById(3)).willReturn(Optional.empty());

        // When
        // the find methods below should work as both exist.
        Throwable thrown = assertThrows(ObjectNotFoundException.class, () -> {
            this.wizardService.assignArtifact(3, artifact.getId());
        });

        // Then
        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find wizard with Id 3 :(");
        assertThat(artifact.getOwner().getId()).isEqualTo(w2.getId());
    }

    @Test
    void testAssignArtifactErrorWithNonExistentArtifactId() {
        // Given
        // prepare fake data. fake artifact and wizards.

        Artifact artifact = new Artifact();
        Artifact oldArifact = this.artifacts.get(1);
        artifact.setId(oldArifact.getId());
        artifact.setName(oldArifact.getName());
        artifact.setDescription(oldArifact.getDescription());
        artifact.setImageUrl(oldArifact.getImageUrl());


        Wizard w2 = new Wizard();
        Wizard oldw2Wizard = this.wizards.get(1);
        w2.setId(oldw2Wizard.getId());
        w2.setName(oldw2Wizard.getName());
        w2.addArtifact(artifact);

        Wizard w3 = new Wizard();
        Wizard oldw3Wizard = this.wizards.get(2);
        w3.setId(oldw3Wizard.getId());
        w3.setName(oldw3Wizard.getName());

        given(this.artifactRepository.findById(artifact.getId())).willReturn(Optional.empty());
        // mock the behavior of wizardRepository's findById
        // we do not mock the wizardRepository's findById call as we will throw an exception when looking for the artifact above.

        // define behavior of the mock objects.
        // mock the behavior of artifactRepository's findById method.
        Throwable thrown = assertThrows(ObjectNotFoundException.class, () -> {
            this.wizardService.assignArtifact(w3.getId(), artifact.getId());
        });

//
//        // When
//        this.wizardService.assignArtifact(w3.getId(), artifact.getId());

        // Then
        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage(String.format("Could not find artifact with Id %s :(", artifact.getId()));
    }

}