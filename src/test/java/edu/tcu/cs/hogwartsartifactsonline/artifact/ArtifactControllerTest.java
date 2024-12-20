package edu.tcu.cs.hogwartsartifactsonline.artifact;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.tcu.cs.hogwartsartifactsonline.artifact.dto.ArtifactDto;
import edu.tcu.cs.hogwartsartifactsonline.system.StatusCode;
import edu.tcu.cs.hogwartsartifactsonline.system.exception.ObjectNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * With the annotations, once we start the below test then spring boot autoconfigures
 * the mockMVC object, so we can use it inside of this test class.
 * We add the "addFilters" option to the @AutoConfigureMockMvc annotation because Spring Security's Servlet support
 * is based on Servlet Filters. This will essentially turn off Spring Security.
 */
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class ArtifactControllerTest {

    @Autowired
    MockMvc mockMvc;

    /**
     * Mockbean: Since the controller is the entry point of the back end.
     * We need to mock http requests using MockMVC
     */
    @MockBean
    ArtifactService artifactService;

    /**
     * Spring boot will inject the ObjectMapper to assist with serializing objects for testing.
     */
    @Autowired
    ObjectMapper objectMapper;

    List<Artifact> artifacts;

    @Value("${api.endpoint.base-url}")
    String baseUrl;

    @BeforeEach
    void setUp() {
        this.artifacts = new ArrayList<>();
        Artifact a1 = new Artifact();
        a1.setId("1250808601744904191");
        a1.setName("Deluminator");
        a1.setDescription("A Deluminator is a device invented by Albus Dumbledore that resembles a cigarette lighter. It is used to remove or absorb (as well as return) the light from any light source to provide cover to the user.");
        a1.setImageUrl("ImageUrl");
        this.artifacts.add(a1);

        Artifact a2 = new Artifact();
        a2.setId("1250808601744904192");
        a2.setName("Invisibility Cloak");
        a2.setDescription("An invisibility cloak is used to make the wearer invisible.");
        a2.setImageUrl("ImageUrl");
        this.artifacts.add(a2);

        Artifact a3 = new Artifact();
        a3.setId("1250808601744904193");
        a3.setName("Elder Wand");
        a3.setDescription("The Elder Wand, known throughout history as the Deathstick or the Wand of Destiny, is an extremely powerful wand made of elder wood with a core of Thestral tail hair.");
        a3.setImageUrl("ImageUrl");
        this.artifacts.add(a3);

        Artifact a4 = new Artifact();
        a4.setId("1250808601744904194");
        a4.setName("The Marauder's Map");
        a4.setDescription("A magical map of Hogwarts created by Remus Lupin, Peter Pettigrew, Sirius Black, and James Potter while they were students at Hogwarts.");
        a4.setImageUrl("ImageUrl");
        this.artifacts.add(a4);

        Artifact a5 = new Artifact();
        a5.setId("1250808601744904195");
        a5.setName("The Sword Of Gryffindor");
        a5.setDescription("A goblin-made sword adorned with large rubies on the pommel. It was once owned by Godric Gryffindor, one of the medieval founders of Hogwarts.");
        a5.setImageUrl("ImageUrl");
        this.artifacts.add(a5);

        Artifact a6 = new Artifact();
        a6.setId("1250808601744904196");
        a6.setName("Resurrection Stone");
        a6.setDescription("The Resurrection Stone allows the holder to bring back deceased loved ones, in a semi-physical form, and communicate with them.");
        a6.setImageUrl("ImageUrl");
        this.artifacts.add(a6);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testFindArtifactByIdSuccess() throws Exception{
        // Given
        given(this.artifactService.findById("1250808601744904191")).willReturn(this.artifacts.get(0));

        // When and Then
        /*
         * unlike testing a service object in a controller test we don't have to explicitly invoke the handler methods
         * because we have mockMVC which performs a fake HTTP request because we have the spring context.
         */
        this.mockMvc.perform(get(String.format("%s/artifacts/1250808601744904191", this.baseUrl)).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS.getHttpStatusCodeValue()))
                .andExpect(jsonPath("$.message").value("Find One Success"))
                .andExpect(jsonPath("$.data.id").value("1250808601744904191"))
                .andExpect(jsonPath("$.data.name").value("Deluminator"));

    }

    @Test
    void testFindArtifactByIdNotFound() throws Exception{
        // Given
        given(this.artifactService.findById("1250808601744904191")).willThrow(new ObjectNotFoundException("artifact", "1250808601744904191"));

        // When and Then
        /*
         * unlike testing a service object in a controller test we don't have to explicitly invoke the handler methods
         * because we have mockMVC which performs a fake HTTP request because we have the spring context.
         */
        this.mockMvc.perform(get(String.format("%s/artifacts/1250808601744904191", this.baseUrl)).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND.getHttpStatusCodeValue()))
                .andExpect(jsonPath("$.message").value("Could not find artifact with Id 1250808601744904191 :("))
                .andExpect(jsonPath("$.data").isEmpty());

    }

    @Test
    void testFindAllArtifactsSuccess() throws Exception {
        // note below how we combine "when" and "then" steps in a controller.

        // Given
        given(this.artifactService.findAll()).willReturn(this.artifacts);

        // When and Then
        this.mockMvc.perform(get(String.format("%s/artifacts", this.baseUrl)).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS.getHttpStatusCodeValue()))
                .andExpect(jsonPath("$.message").value("Find All Success"))
                .andExpect(jsonPath("$.data", hasSize(this.artifacts.size())))
                .andExpect(jsonPath("$.data[0].id").value("1250808601744904191"))
                .andExpect(jsonPath("$.data[0].name"). value("Deluminator"))
                .andExpect(jsonPath("$.data[1].id"). value("1250808601744904192"))
                .andExpect(jsonPath("$.data[1].name"). value("Invisibility Cloak"));
    }

    @Test
    void testAddArtifactSuccess() throws Exception {
        // Given
        /*
         * 1. prepared a fake dto
         * 2. used jackson to serialize this fake dto.
         * steps 1/2 simulate what a front-end would do.
         * The controller delegates to the service so we also need to invoke the given call that uses the save method from the service layer (mock the service call).
         * We don't actually save to the database but instead we return the fake data (savedArifact).
         */
        ArtifactDto artifactDto = new ArtifactDto(null,
                                    "Remembrall",
                                    "A Remembrall was a magical large marble-sized glass ball that contained smoke which turned red when its owner or user had forgotten something. It turned clear once whatever was forgotten was remembered.",
                                    "ImageUrl", null);

        String json = this.objectMapper.writeValueAsString(artifactDto); // serialized json.sssssssssssssssssssssssssssssssss

        Artifact savedArtifact = new Artifact();
        savedArtifact.setId("1250808601744904197");
        savedArtifact.setName("Remembrall");
        savedArtifact.setDescription("A Remembrall was a magical large marble-sized glass ball that contained smoke which turned red when its owner or user had forgotten something. It turned clear once whatever was forgotten was remembered.");
        savedArtifact.setImageUrl("ImageUrl");

        given(this.artifactService.save(Mockito.any(Artifact.class))).willReturn(savedArtifact);

        // When and Then
        this.mockMvc.perform(post(String.format("%s/artifacts", this.baseUrl)).
                        contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS.getHttpStatusCodeValue()))
                .andExpect(jsonPath("$.message").value("Add Success"))
                .andExpect(jsonPath("$.data.id").isNotEmpty())
                .andExpect(jsonPath("$.data.name").value(savedArtifact.getName()))
                .andExpect(jsonPath("$.data.description").value(savedArtifact.getDescription()))
                .andExpect(jsonPath("$.data.imageUrl").value(savedArtifact.getImageUrl()));
    }

    @Test
    void testUpdateArtifactSuccess() throws Exception {
        // Given
        // artifactDto should be provided from the client side.
        ArtifactDto artifactDto = new ArtifactDto("12508086017449041972",
                "Invisibility Cloak",
                "A new description.",
                "ImageUrl", null);

        String json = this.objectMapper.writeValueAsString(artifactDto); // serialized json.sssssssssssssssssssssssssssssssss

        // updatedArtifact is returned from the service.
        Artifact updatedArtifact = new Artifact();
        updatedArtifact.setId("12508086017449041972");
        updatedArtifact.setName("Invisibility Cloak");
        updatedArtifact.setDescription("A new description.");
        updatedArtifact.setImageUrl("ImageUrl");

        given(this.artifactService.update(eq("12508086017449041972"), Mockito.any(Artifact.class))).willReturn(updatedArtifact);

        // When and Then
        this.mockMvc.perform(put(String.format("%s/artifacts/12508086017449041972", this.baseUrl)).
                        contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS.getHttpStatusCodeValue()))
                .andExpect(jsonPath("$.message").value("Update Success"))
                .andExpect(jsonPath("$.data.id").value("12508086017449041972"))
                .andExpect(jsonPath("$.data.name").value(updatedArtifact.getName()))
                .andExpect(jsonPath("$.data.description").value(updatedArtifact.getDescription()))
                .andExpect(jsonPath("$.data.imageUrl").value(updatedArtifact.getImageUrl()));
    }

    @Test
    void testUpdateArtifactErrorWithNonExistentId() throws Exception {
        // Given
        // assume the invisibility cloak does not exist.
        ArtifactDto artifactDto = new ArtifactDto("12508086017449041972",
                "Invisibility Cloak",
                "A new description.",
                "ImageUrl", null);

        String json = this.objectMapper.writeValueAsString(artifactDto); // serialized json.

        given(this.artifactService.update(eq("12508086017449041972"), Mockito.any(Artifact.class)))
                .willThrow(new ObjectNotFoundException("artifact", "12508086017449041972"));

        // When and Then
        this.mockMvc.perform(put(String.format("%s/artifacts/12508086017449041972", this.baseUrl)).
                        contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND.getHttpStatusCodeValue()))
                .andExpect(jsonPath("$.message").value("Could not find artifact with Id 12508086017449041972 :("))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testDeleteArtifactSuccess() throws Exception {
        // Given
        doNothing().when(this.artifactService).delete("12508086017449041971");

        // When and Then
        this.mockMvc.perform(delete(String.format("%s/artifacts/12508086017449041971", this.baseUrl))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS.getHttpStatusCodeValue()))
                .andExpect(jsonPath("$.message").value("Delete Success"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testDeleteArtifactErrorWithNonExistentId() throws Exception {
        // Given
        // when service.delete gets called throw the below exception.
        doThrow(new ObjectNotFoundException("artifact", "12508086017449041971"))
                .when(this.artifactService).delete("12508086017449041971");

        // When and Then
        this.mockMvc.perform(delete(String.format("%s/artifacts/12508086017449041971", this.baseUrl))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND.getHttpStatusCodeValue()))
                .andExpect(jsonPath("$.message").value("Could not find artifact with Id 12508086017449041971 :("))
                .andExpect(jsonPath("$.data").isEmpty());
    }


}