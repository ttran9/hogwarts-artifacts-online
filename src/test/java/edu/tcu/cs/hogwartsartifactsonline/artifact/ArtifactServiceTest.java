package edu.tcu.cs.hogwartsartifactsonline.artifact;

import edu.tcu.cs.hogwartsartifactsonline.artifact.utils.IdWorker;
import edu.tcu.cs.hogwartsartifactsonline.wizard.Wizard;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

/**
 *
 */
@ExtendWith(MockitoExtension.class)
class ArtifactServiceTest {

    /**
     * The Mock annotation tells mockito we want to simulate the artifactrepository and to not call the real object.
     */
    @Mock
    ArtifactRepository artifactRepository;

    @Mock
    IdWorker idWorker;

    /**
     * We inject the artifactRepository mock into the artifactService object.
     * Note this is not a real artifactRepository object/bean.
     */
    @InjectMocks
    ArtifactService artifactService;

    List<Artifact> artifacts;

    /**
     * Before each test method is invoked the setUp method will run.
     */
    @BeforeEach
    void setUp() {
        Artifact a1 = new Artifact();
        a1.setId("1250808601744904191");
        a1.setName("Deluminator");
        a1.setDescription("A Deluminator is a device invented by Albus Dumbledore that resembles a cigarette lighter. It is used to remove or absorb (as well as return) the light from any light source to provide cover to the user.");
        a1.setImageUrl("imageUrl");

        Artifact a2 = new Artifact();
        a2.setId("1250808601744904192");
        a2.setName("Invisibility Cloak");
        a2.setDescription("An invisibility cloak is used to make the wearer invisible.");
        a2.setImageUrl("imageUrl");

        this.artifacts = new ArrayList<>();
        this.artifacts.add(a1);
        this.artifacts.add(a2);
    }

    /**
     * After each test method is invoked the tearDown method will run.
     */
    @AfterEach
    void tearDown() {
    }


    /**
     * test the findById inside the ArtifactService test.
     */
    @Test
    void testFindByIdSuccess() {
        // Given. Arrange imputs and targets. Define the behavior of the Mock object artifactRepository.
        /*
         * prepare fake data.
         * define behavior of the mock.
         */
        /*
         * "id": "1250808601744904192",
         * "name": "Invisibility Cloak",
         * "description": "An invisibility cloak is used to make the wearer invisible.",
         * "imageUrl": "ImageUrl",
         */
        Artifact artifact = new Artifact();
        artifact.setId("1250808601744904192");
        artifact.setName("Invisibility Cloak");
        artifact.setDescription("An invisibility cloak is used to make the wearer invisible.");
        artifact.setImageUrl("ImageUrl");

        Wizard wizard = new Wizard();
        wizard.setId(2);
        wizard.setName("Harry Potter");

        artifact.setOwner(wizard);

        /*
          * define the behavior of the artifactRepository mock.
          * we want the mock to return the above artifact instead of hitting our database.
         */
        given(artifactRepository.findById("1250808601744904192")).willReturn(Optional.of(artifact)); // Defines the behavior of the mock object.

        // When. Act on the target behavior. When steps should cover the method to be tested.
        /*
         * Where we call the method (findById) to be tested.
         */
        Artifact returnedArtifact = artifactService.findById("1250808601744904192");

        // Then. Assert expected outcomes.
        /*
         * The assert step. Where we compare the result of the call from the "when" step to the expected result.
         */
        assertThat(returnedArtifact.getId()).isEqualTo(artifact.getId());
        assertThat(returnedArtifact.getName()).isEqualTo(artifact.getName());
        assertThat(returnedArtifact.getDescription()).isEqualTo(artifact.getDescription());
        assertThat(returnedArtifact.getImageUrl()).isEqualTo(artifact.getImageUrl());

        // verify that the findById method of the mock gets called exactly once.
        verify(artifactRepository, times(1)).findById("1250808601744904192");
    }

    @Test
    void testFindByIdNotFound() {
        // Given
        /*
         * No need to prepare fake data because we don't return anything because it is not found.
         * We still need to define the behavior of the artifactRepository mock.
         */
        given(artifactRepository.findById(Mockito.any(String.class))).willReturn(Optional.empty());

        // When
//        Artifact returnedArtifact = artifactService.findById("1250808601744904192");
        /*
         * The below means that if artifactService.findById throws an exception then catchThrowable will catch the
         * exception and assign that exception to the thrown variable inside of the "then" portion of our code.
         */
        Throwable thrown = catchThrowable(() -> {
                Artifact returnedArtifact = artifactService.findById("1250808601744904192");
        });

        // Then
        /*
         * Verify if the exception thrown by artifactService.findById is an
         * instance of the ArtifactNotFoundException class.
         */
        assertThat(thrown)
                .isInstanceOf(ArtifactNotFoundException.class)
                .hasMessage("Could not find artifact with Id 1250808601744904192 :(");

        verify(artifactRepository, times(1)).findById("1250808601744904192");
    }

    @Test
    void testFindAllSuccess() {
        // Given
        given(artifactRepository.findAll()).willReturn(this.artifacts);

        // When
        List<Artifact> actualArtifacts = artifactService.findAll();

        // Then
        assertThat(actualArtifacts.size()).isEqualTo(this.artifacts.size());
        verify(artifactRepository, times(1)).findAll();
    }

    @Test
    void testSaveSuccess() {
        // Given
        Artifact newArtifact = new Artifact();
        newArtifact.setName("Artifact 3");
        newArtifact.setDescription("Description");
        newArtifact.setImageUrl("ImageUrl");

        // Describe behavior of mock object.
        // below generates fake data instead of generating a real value using the idWorker's algorithm.
        given(idWorker.nextId()).willReturn(123456L);
        // the same object that is being persisted is returned.
        given(artifactRepository.save(newArtifact)).willReturn(newArtifact);

        // When
        Artifact savedArtifact = artifactService.save(newArtifact);

        // Then
        assertThat(savedArtifact.getId()).isEqualTo("123456");
        assertThat(savedArtifact.getName()).isEqualTo(newArtifact.getName());
        assertThat(savedArtifact.getDescription()).isEqualTo(newArtifact.getDescription());
        assertThat(savedArtifact.getImageUrl()).isEqualTo(newArtifact.getImageUrl());
        verify(artifactRepository, times(1)).save(newArtifact);
    }

    @Test
    void testUpdateSuccess() {
        // Given
        /*
         * Create a fake artifact that mimics one that exists in the database
         */
        Artifact oldArtifact = new Artifact();
        oldArtifact.setId("1250808601744904192");
        oldArtifact.setName("Invisibility Cloak");
        oldArtifact.setDescription("An invisibility cloak is used to make the wearer invisible.");
        oldArtifact.setImageUrl("ImageUrl");

        Artifact update = new Artifact();
        update.setId("1250808601744904192"); // can leave this but we won't be updating it.
        update.setName("Invisibility Cloak");
        update.setDescription("A new description.");
        update.setImageUrl("ImageUrl");

        /**
         * 1. First find the artifact
         * 2. Update the old artifact with the new/updated values and save with new values.
         */
        given(this.artifactRepository.findById("1250808601744904192")).willReturn(Optional.of(oldArtifact));
        /*
         * note: when we save the old artifact and return the old one it already has updated values.
         */
        given(this.artifactRepository.save(oldArtifact)).willReturn(oldArtifact);

        // When
        Artifact updatedArtifact = this.artifactService.update("1250808601744904192", update);

        // Then
//        assertThat(updatedArtifact.getId()).isEqualTo("1250808601744904192");
        assertThat(updatedArtifact.getId()).isEqualTo(update.getId());
        assertThat(updatedArtifact.getDescription()).isEqualTo(update.getDescription());
        verify(this.artifactRepository, times(1)).findById("1250808601744904192");
        verify(this.artifactRepository, times(1)).save(oldArtifact);
    }

    @Test
    void testUpdateNotFound() {
        // Given
        /**
         * Prepare an update object
         */
        Artifact update = new Artifact();
//        update.setId("1250808601744904192");
        update.setName("Invisibility Cloak");
        update.setDescription("A new description.");
        update.setImageUrl("ImageUrl");

        given(artifactRepository.findById("1250808601744904192")).willReturn((Optional.empty()));
        // When
        assertThrows(ArtifactNotFoundException.class, () -> {
            artifactService.update("1250808601744904192", update);
        });

        // Then
        verify(artifactRepository, times(1)).findById("1250808601744904192");
    }

    @Test
    void testDeleteSuccess() {
        // Given
        Artifact artifact = new Artifact();
        artifact.setId("1250808601744904192");
        artifact.setName("Invisibility Cloak");
        artifact.setDescription("An invisibility cloak is used to make the wearer invisible.");
        artifact.setImageUrl("ImageUrl");

        given(artifactRepository.findById("1250808601744904192")).willReturn(Optional.of(artifact));

        // when the deleteById method is called do nothing.
        doNothing().when(artifactRepository).deleteById("1250808601744904192");

        // When
        artifactService.delete("1250808601744904192");

        // Then
        verify(artifactRepository, times(1)).deleteById("1250808601744904192");
    }

    @Test
    void testDeleteNotFound() {
        // Given

        // note the willReturn returning an empty object.
        given(artifactRepository.findById("1250808601744904192")).willReturn(Optional.empty());

        // When
        assertThrows(ArtifactNotFoundException.class, () -> {
            artifactService.delete("1250808601744904192");
        });

        // Then
        verify(artifactRepository, times(1)).findById("1250808601744904192");
    }
}