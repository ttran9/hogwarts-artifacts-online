package edu.tcu.cs.hogwartsartifactsonline.wizard;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.tcu.cs.hogwartsartifactsonline.artifact.Artifact;
import edu.tcu.cs.hogwartsartifactsonline.system.StatusCode;
import edu.tcu.cs.hogwartsartifactsonline.system.exception.ObjectNotFoundException;
import edu.tcu.cs.hogwartsartifactsonline.wizard.dto.WizardDto;
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
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * For @SpringBootTest: bring in the spring context into our tests.
 * For @AutoConfigureMockMvc: have spring autowire this so we can make mock http requests.
 */
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class WizardControllerTest {

    /**
     * Object to mock HTTP requests.
     */
    @Autowired
    MockMvc mockMvc;

    /**
     * Service to mock method calls to the database.
     */
    @MockBean
    WizardService wizardService;

    /**
     * Converter object to convert a POJO to a json object to be able to make
     * POST and PUT requests.
     */
    @Autowired
    ObjectMapper objectMapper;

    @Value("${api.endpoint.base-url}")
    String baseUrl;

    private List<Wizard> wizards;

    private List<Artifact> artifacts;
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

        wizards = new ArrayList<>(List.of(wizard, wizard1, wizard2));

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

        // mock adding artifacts.
        wizard.addArtifact(a1);
        wizard.addArtifact(a2);
        artifacts = new ArrayList<>(Arrays.asList(a1, a2, a3, a4, a5, a6));
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testFindWizardByIdSuccess() throws Exception {
        Wizard firstWizard = this.wizards.get(0);
        int wizardId = firstWizard.getId();
        // Given
        given(this.wizardService.findWizardById(wizardId)).willReturn(firstWizard);

        // When and Then
        // we only need to mock an http get request here and don't need to invoke service level method(s).
//        this.mockMvc.perform(get("/api/v1/wizards/1")
        this.mockMvc.perform(get(String.format("%s/wizards/%s", this.baseUrl, wizardId))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS.getHttpStatusCodeValue()))
                .andExpect(jsonPath("$.message").value("Find One Success"))
                .andExpect(jsonPath("$.data.id").value(wizardId))
                .andExpect(jsonPath("$.data.name").value(firstWizard.getName()))
                .andExpect(jsonPath("$.data.numberOfArtifacts").value(firstWizard.getNumberOfArtifacts()));
    }

    @Test
    void testFindWizardByIdNotFound() throws Exception {
        // Given
        given(this.wizardService.findWizardById(9)).willThrow(new ObjectNotFoundException("wizard", 9));

        // When and Then
        this.mockMvc.perform(get(String.format("%s/wizards/9", this.baseUrl))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND.getHttpStatusCodeValue()))
                .andExpect(jsonPath("$.message").value("Could not find wizard with Id 9 :("))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testFindAllWizardsSuccess() throws Exception {
        // Given
        Wizard firstWizard = this.wizards.get(0);
        Wizard secondWizard = this.wizards.get(1);
        Wizard thirdWizard = this.wizards.get(2);
        given(this.wizardService.findAll()).willReturn(this.wizards);

        // When and Then
        this.mockMvc.perform(get(String.format("%s/wizards", this.baseUrl))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS.getHttpStatusCodeValue()))
                .andExpect(jsonPath("$.message").value("Find All Success"))
                .andExpect(jsonPath("$.data", hasSize(this.wizards.size())))
                .andExpect(jsonPath("$.data[0].id").value(firstWizard.getId()))
                .andExpect(jsonPath("$.data[0].name").value(firstWizard.getName()))
                .andExpect(jsonPath("$.data[0].numberOfArtifacts").value(firstWizard.getNumberOfArtifacts()))
                .andExpect(jsonPath("$.data[1].id").value(secondWizard.getId()))
                .andExpect(jsonPath("$.data[1].name").value(secondWizard.getName()))
                .andExpect(jsonPath("$.data[1].numberOfArtifacts").value(secondWizard.getNumberOfArtifacts()))
                .andExpect(jsonPath("$.data[2].id").value(thirdWizard.getId()))
                .andExpect(jsonPath("$.data[2].name").value(thirdWizard.getName()))
                .andExpect(jsonPath("$.data[2].numberOfArtifacts").value(thirdWizard.getNumberOfArtifacts()));
    }

    @Test
    void testAddWizardSuccess() throws Exception {
        // Given
        WizardDto wizardDto = new WizardDto(null,
                "Hermione Granger", null);

        String json = this.objectMapper.writeValueAsString(wizardDto);

        Wizard savedWizard = new Wizard();
        savedWizard.setId(4);
        savedWizard.setName("Hermione Granger");

        given(this.wizardService.save(any(Wizard.class))).willReturn(savedWizard);

        // When and Then
        this.mockMvc.perform(post(String.format("%s/wizards", this.baseUrl))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS.getHttpStatusCodeValue()))
                .andExpect(jsonPath("$.message").value("Add Success"))
                .andExpect(jsonPath("$.data.id").value(savedWizard.getId()))
                .andExpect(jsonPath("$.data.name").value(savedWizard.getName()))
                .andExpect(jsonPath("$.data.numberOfArtifacts").value(savedWizard.getNumberOfArtifacts()));
    }

//    @Test
    // unsure how to implement.
    void testAddWizardBadRequest() throws Exception {
        // Given
        WizardDto wizardDto = new WizardDto(null, "", null);
        String json = this.objectMapper.writeValueAsString(wizardDto);
//        Map<String, String> errorMap = new HashMap();
//        errorMap.put("name", "name is required");
//
//        BindingResult bindingResult = new BeanPropertyBindingResult(wizardDto, "wizard");
//        bindingResult.addError("error");
//
//        Exception ex = new MethodArgumentNotValidException(null);
//
//        doThrow(new MethodArgumentNotValidException("12508086017449041971")).when(this.wizardService).save("12508086017449041971");

//        given(this.wizardService.save(Mockito.any(Wizard.class))).willReturn(savedWizard);
        given(this.wizardService.save(Mockito.any(Wizard.class))).willThrow(new ObjectNotFoundException("wizard", "f"));


        this.mockMvc.perform(post(String.format("%s/wizards", this.baseUrl))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.INVALID_ARGUMENT.getHttpStatusCodeValue()))
                .andExpect(jsonPath("$.message").value("Provided arguments are invalid, see data for details."))
                .andExpect(jsonPath("$.data.name").value("name is required."));
    }

    @Test
    void testUpdateWizardSuccess() throws Exception {
        // Given
        // create the dto.
        Wizard firstWizard = this.wizards.get(0);
        WizardDto wizardDto = new WizardDto(firstWizard.getId(),
                firstWizard.getName() + "-update", firstWizard.getNumberOfArtifacts());
        String json = this.objectMapper.writeValueAsString(wizardDto);

        // updated wizard is returned from the service.
        Wizard updatedWizard = new Wizard();
        updatedWizard.setId(wizardDto.id());
        updatedWizard.setName(wizardDto.name());

        given(this.wizardService.update(eq(updatedWizard.getId()), Mockito.any(Wizard.class))).willReturn(updatedWizard);

        // When and Then
        this.mockMvc.perform(put(String.format("%s/wizards/%s", this.baseUrl, wizardDto.id()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS.getHttpStatusCodeValue()))
                .andExpect(jsonPath("$.message").value("Update Success"));
    }

    @Test
    void testUpdateWizardErrorWithNonExistentId() throws Exception {
        // Given
        WizardDto wizardDto = new WizardDto(9,
                "Hermione Granger", 0);
        String json = this.objectMapper.writeValueAsString(wizardDto);
        given(this.wizardService.update(eq(9), Mockito.any(Wizard.class))).willThrow(new ObjectNotFoundException("wizard", 9));

        this.mockMvc.perform(put(String.format("%s/wizards/%s", this.baseUrl, wizardDto.id()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND.getHttpStatusCodeValue()))
                .andExpect(jsonPath("$.message").value("Could not find wizard with Id 9 :("))
                .andExpect(jsonPath("$.data").isEmpty());

    }

    @Test
    void testDeleteWizardSuccess() throws Exception {
        // Given
        doNothing().when(this.wizardService).delete(wizards.get(0).getId());

        // When and Then
        this.mockMvc.perform(delete(String.format("%s/wizards/%s", this.baseUrl, wizards.get(0).getId()))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS.getHttpStatusCodeValue()))
                .andExpect(jsonPath("$.message").value("Delete Success"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testDeleteWizardFailureWithNonExistentId() throws Exception {
        // Given
        doThrow(new ObjectNotFoundException("wizard", 9)).when(this.wizardService).delete(9);

        // When and Then
        this.mockMvc.perform(delete(String.format("%s/wizards/%s", this.baseUrl, 9))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND.getHttpStatusCodeValue()))
                .andExpect(jsonPath("$.message").value("Could not find wizard with Id 9 :("))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testAssignArtifactSuccess() throws Exception {
        Wizard wizard = this.wizards.get(0);
        Artifact artifact = this.artifacts.get(0);
        // Given
        // Mock tbe behavior of the service method's assignArtifact
        doNothing().when(this.wizardService).assignArtifact(wizard.getId(), artifact.getId());


        // Note: The client doesn't send any json in the request body.
        this.mockMvc.perform(put(String.format("%s/wizards/%s/artifacts/%s", this.baseUrl, wizard.getId(), artifact.getId()))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS.getHttpStatusCodeValue()))
                .andExpect(jsonPath("$.message").value("Artifact Assignment Success"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testAssignArtifactErrorWithNonExistentWizardId() throws Exception {
        Wizard wizard = this.wizards.get(0);
        Artifact artifact = this.artifacts.get(0);
        // Given
        // Mock tbe behavior of the service method's assignArtifact
        doThrow(new ObjectNotFoundException("wizard", 5)).when(this.wizardService).assignArtifact(5, artifact.getId());


        // Note: The client doesn't send any json in the request body.
        this.mockMvc.perform(put(String.format("%s/wizards/%s/artifacts/%s", this.baseUrl, 5, artifact.getId()))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND.getHttpStatusCodeValue()))
                .andExpect(jsonPath("$.message").value(String.format("Could not find wizard with Id %s :(", 5)))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testAssignArtifactErrorWithNonExistentArtifactId() throws Exception {
        Wizard wizard = this.wizards.get(0);
        Artifact artifact = this.artifacts.get(0);
        // Given
        // Mock tbe behavior of the service method's assignArtifact
        doThrow(new ObjectNotFoundException("artifact", "1250808601744904199")).when(this.wizardService).assignArtifact(wizard.getId(), "1250808601744904199");


        // Note: The client doesn't send any json in the request body.
        this.mockMvc.perform(put(String.format("%s/wizards/%s/artifacts/%s", this.baseUrl, wizard.getId(), "1250808601744904199"))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND.getHttpStatusCodeValue()))
                .andExpect(jsonPath("$.message").value(String.format("Could not find artifact with Id %s :(", "1250808601744904199")))
                .andExpect(jsonPath("$.data").isEmpty());
    }
}