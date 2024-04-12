package com.quizzapp.quizzmaker.impl;

import com.quizzapp.quizzmaker.dto.GraphDTO;
import com.quizzapp.quizzmaker.dto.QuizDTO;
import com.quizzapp.quizzmaker.persistence.entities.*;
import com.quizzapp.quizzmaker.persistence.models.QuestionType;
import com.quizzapp.quizzmaker.services.GraphService;
import com.quizzapp.quizzmaker.services.impl.GraphImpl;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.ClassPathResource;

import java.io.StringWriter;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GraphImplTest {

    @Mock
    private VelocityEngine velocityEngine;

    @InjectMocks
    private GraphImpl graphService;

    private GraphDTO graphDTO;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Quiz quiz = mock(Quiz.class);
        Survey survey = mock(Survey.class);

        graphDTO = new GraphDTO();
        graphDTO.setQuiz(quiz);
        graphDTO.setSurvey(survey);
        graphDTO.setIsVerticalAlignment(true);

        when(velocityEngine.evaluate(any(VelocityContext.class), any(StringWriter.class), anyString(), anyString()))
                .then(invocation -> {
                    StringWriter writer = invocation.getArgument(1);
                    writer.write("Generated Graph String");
                    return true;
                });


    }

    @Test
    public void testGetPositionById_WithExistingId() {


        Question q1 = new Question(
                1, "What?", QuestionType.SINGLE_CHOICE, 10, false, 123,
                Arrays.asList(new QuestionOption(), new QuestionOption()),
                Arrays.asList(new QuestionLogic()),
                Arrays.asList(new Answer()),
                Arrays.asList(new Quiz()),
                Arrays.asList(new Survey())
        );
        Question q2 = new Question(
                2, "What?", QuestionType.SINGLE_CHOICE, 10, false, 123,
                Arrays.asList(new QuestionOption(), new QuestionOption()),
                Arrays.asList(new QuestionLogic()),
                Arrays.asList(new Answer()),
                Arrays.asList(new Quiz()),
                Arrays.asList(new Survey()
                ));
        List<Question> list = Arrays.asList(q1, q2);

        int position = graphService.getPositionById(list, 2);

        assertEquals(1, position);
    }

    @Test
    public void testGetPositionById_WithNonExistingId() {
        Question q1 = new Question(
                1, "What?", QuestionType.SINGLE_CHOICE, 10, false, 123,
                Arrays.asList(new QuestionOption(), new QuestionOption()),
                Arrays.asList(new QuestionLogic()),
                Arrays.asList(new Answer()),
                Arrays.asList(new Quiz()),
                Arrays.asList(new Survey())
        );
        List<Question> list = Collections.singletonList(q1);

        int position = graphService.getPositionById(list, 999);

        assertEquals(-1, position);
    }


    @Test
    public void testGetPositionById_WithEmptyList() {
        List<Question> list = Collections.emptyList();

        int position = graphService.getPositionById(list, 1);

        assertEquals(-1, position);
    }

    @Test
    public void testGetPositionById_WithNullList() {
        List<Question> list = null;

        assertThrows(NullPointerException.class, () -> {
            graphService.getPositionById(list, 1);
        });
    }

    @Test
    public void testGetPositionById_WithDuplicateIds() {
        Question q1 = new Question(
                1, "What?", QuestionType.SINGLE_CHOICE, 10, false, 123,
                Arrays.asList(new QuestionOption(), new QuestionOption()),
                Arrays.asList(new QuestionLogic()),
                Arrays.asList(new Answer()),
                Arrays.asList(new Quiz()),
                Arrays.asList(new Survey())
        );
        Question q2 = new Question(
                2, "What?", QuestionType.SINGLE_CHOICE, 10, false, 123,
                Arrays.asList(new QuestionOption(), new QuestionOption()),
                Arrays.asList(new QuestionLogic()),
                Arrays.asList(new Answer()),
                Arrays.asList(new Quiz()),
                Arrays.asList(new Survey()
                ));
        List<Question> list = Arrays.asList(q1, q2);

        int position = graphService.getPositionById(list, 1);

        assertEquals(0, position);
    }

    @Test
    public void testFindObjectById_WithExistingId() {
        QuestionOption option1 = new QuestionOption();
        option1.setId(1);
        option1.setText("Option 1");
        option1.setCorrect(true);

        QuestionOption option2 = new QuestionOption();
        option2.setId(2);
        option2.setText("Option 2");
        option2.setCorrect(false);

        QuestionOption option3 = new QuestionOption();
        option3.setId(3);
        option3.setText("Option 3");
        option3.setCorrect(true);


        List<QuestionOption> list = Arrays.asList(option1, option2, option3);

        QuestionOption result = graphService.findObjectById(list, 2);

        assertEquals(option2, result);
    }

    @Test
    public void testFindObjectById_WithNonExistingId() {
        QuestionOption option1 = new QuestionOption();
        option1.setId(1);
        option1.setText("Option 1");
        option1.setCorrect(true);

        QuestionOption option2 = new QuestionOption();
        option2.setId(2);
        option2.setText("Option 2");
        option2.setCorrect(false);

        QuestionOption option3 = new QuestionOption();
        option3.setId(3);
        option3.setText("Option 3");
        option3.setCorrect(true);


        List<QuestionOption> list = Arrays.asList(option1, option2, option3);

        QuestionOption result = graphService.findObjectById(list, 4);

        assertNull(result);
    }

    @Test
    public void testFindObjectById_WithEmptyList() {
        List<QuestionOption> list = Collections.emptyList();

        QuestionOption result = graphService.findObjectById(list, 1);

        assertNull(result);
    }


    @Test
    void afterPropertiesSet_loadsTemplatesCorrectly() throws Exception {

        graphService.afterPropertiesSet();

        String graphTemplate = getPrivateFieldValue(graphService, "graphTemplate");
        String questionTemplate = getPrivateFieldValue(graphService, "questionTemplate");
        String edgeTemplate = getPrivateFieldValue(graphService, "edgeTemplate");
        String startNodeTemplate = getPrivateFieldValue(graphService, "startNodeTemplate");
        String endNodeTemplate = getPrivateFieldValue(graphService, "endNodeTemplate");

        String expectedGraphTemplate = readResourceFile("templates/graph/graph.vm");
        String expectedQuestionTemplate = readResourceFile("templates/graph/question.vm");
        String expectedEdgeTemplate = readResourceFile("templates/graph/edge.vm");
        String expectedStartNodeTemplate = readResourceFile("templates/graph/startNode.vm");
        String expectedEndNodeTemplate = readResourceFile("templates/graph/endNode.vm");

        assertEquals(expectedGraphTemplate, graphTemplate);
        assertEquals(expectedQuestionTemplate, questionTemplate);
        assertEquals(expectedEdgeTemplate, edgeTemplate);
        assertEquals(expectedStartNodeTemplate, startNodeTemplate);
        assertEquals(expectedEndNodeTemplate, endNodeTemplate);
    }

    private String getPrivateFieldValue(Object obj, String fieldName) throws Exception {
        Field field = obj.getClass().getDeclaredField(fieldName);
        field.setAccessible(true); // Suppress Java language access checking
        return (String) field.get(obj);
    }

    private String readResourceFile(String path) throws Exception {
        return new String(new ClassPathResource(path).getInputStream().readAllBytes(), StandardCharsets.UTF_8);
    }


    @Test
    void createEdgesDef_QuizBased() {

        Quiz existingQuiz = new Quiz();
        Survey existingSurvey = new Survey();

        Question q1 = new Question(
                1, "What?", QuestionType.SINGLE_CHOICE, 10, false, 123,
                Arrays.asList(new QuestionOption(1, "test", false, new Question(), new ArrayList<Answer>()), new QuestionOption(2, "test2", true, new Question(), new ArrayList<Answer>())),
                Arrays.asList(new QuestionLogic()),
                Arrays.asList(new Answer()),
                Arrays.asList(new Quiz()),
                Arrays.asList(new Survey())
        );
        Question q2 = new Question(
                2, "What?", QuestionType.SINGLE_CHOICE, 10, false, 123,
                Arrays.asList(new QuestionOption(1, "test", false, new Question(), new ArrayList<Answer>()), new QuestionOption(2, "test2", true, new Question(), new ArrayList<Answer>())),
                Arrays.asList(new QuestionLogic()),
                Arrays.asList(new Answer()),
                Arrays.asList(new Quiz()),
                Arrays.asList(new Survey()
                ));

        existingQuiz.setQuestions(Arrays.asList(q1,q2));
        //existingSurvey.setQuestions(Arrays.asList(q1,q2));


        graphDTO = new GraphDTO();
        graphDTO.setQuiz(existingQuiz);
        //graphDTO.setSurvey(existingSurvey);
        graphDTO.setIsVerticalAlignment(true);


        // Stubbing the processTemplate method call. Adjust the matcher to match your method signature.
        when(velocityEngine.evaluate(any(VelocityContext.class), any(StringWriter.class), eq("TEMPLATE"), anyString())).thenReturn(true);

        // Call the method under test
        String result = graphService.createEdgesDef(graphDTO);

        // Assertions and verifications
        assertNotNull(result);
        // More assertions to verify the content of the result as needed

    }


    @Mock
    private Quiz quiz;

    @Mock
    private Survey survey;

    @Test
    void createQuestionsDef_WithQuiz() {
        Quiz existingQuiz = new Quiz();
        existingQuiz.setQuestions(Arrays.asList(
                new Question(
                        1, "What?", QuestionType.SINGLE_CHOICE, 10, false, 123,
                        Arrays.asList(new QuestionOption(), new QuestionOption()),
                        Arrays.asList(new QuestionLogic()),
                        Arrays.asList(new Answer()),
                        Arrays.asList(new Quiz()),
                        Arrays.asList(new Survey())
                ),
                new Question(
                        2, "What?", QuestionType.SINGLE_CHOICE, 10, false, 123,
                        Arrays.asList(new QuestionOption(), new QuestionOption()),
                        Arrays.asList(new QuestionLogic()),
                        Arrays.asList(new Answer()),
                        Arrays.asList(new Quiz()),
                        Arrays.asList(new Survey()
                        ))
        ));


        graphDTO.setQuiz(existingQuiz);


        String result = graphService.createQuestionsDef(graphDTO);

        String expected = "Processed questionTemplate with ID 0\n" +
                "Processed questionTemplate with ID 1\n" +
                "Processed startNodeTemplate with ID 2\n" +
                "Processed endNodeTemplate with ID 3";

    }


    @Test
    void generate_ShouldProcessGraphDTOAndReturnGraphString() {

        graphService = spy(new GraphImpl(velocityEngine));

        doReturn("mockedQuestionsDefinition").when(graphService).createQuestionsDef(any(GraphDTO.class));
        doReturn("mockedEdgesDefinition").when(graphService).createEdgesDef(any(GraphDTO.class));

        String result = graphService.generate(graphDTO);

        assertEquals("", "");

        verify(graphService).createQuestionsDef(graphDTO);
        verify(graphService).createEdgesDef(graphDTO);
    }
}
