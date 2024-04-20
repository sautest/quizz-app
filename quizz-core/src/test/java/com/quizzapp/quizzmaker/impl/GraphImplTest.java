package com.quizzapp.quizzmaker.impl;

import com.quizzapp.quizzmaker.dto.GraphDTO;
import com.quizzapp.quizzmaker.persistence.entities.*;
import com.quizzapp.quizzmaker.persistence.models.QuestionType;
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
                    writer.write("digraph{}");
                    return true;
                });
    }

    @Test
    public void testGetPositionWithId() {
        Question q1 = new Question(
                1, "test?", QuestionType.SINGLE_CHOICE, 10, false, 123,
                Arrays.asList(new QuestionOption(), new QuestionOption()),
                Arrays.asList(new QuestionLogic()),
                Arrays.asList(new Answer()),
                Arrays.asList(new Quiz()),
                Arrays.asList(new Survey())
        );
        Question q2 = new Question(
                2, "test?", QuestionType.SINGLE_CHOICE, 10, false, 123,
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
    public void testGetPositionWithNonExistantId() {
        Question q1 = new Question(
                1, "test?", QuestionType.SINGLE_CHOICE, 10, false, 123,
                Arrays.asList(new QuestionOption(), new QuestionOption()),
                Arrays.asList(new QuestionLogic()),
                Arrays.asList(new Answer()),
                Arrays.asList(new Quiz()),
                Arrays.asList(new Survey())
        );
        List<Question> list = List.of(q1);

        int position = graphService.getPositionById(list, 999);

        assertEquals(-1, position);
    }


    @Test
    public void testGetPositionWithIdWhenListIsEmpty() {
        List<Question> list = List.of();

        int position = graphService.getPositionById(list, 1);

        assertEquals(-1, position);
    }

    @Test
    public void testFindObjectWithId() {
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
    public void testFindObjectWithNonExistantId() {
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

    private String getPrivateFieldValue(Object obj, String fieldName) throws Exception {
        Field field = obj.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return (String) field.get(obj);
    }

    private String readResourceFile(String path) throws Exception {
        return new String(new ClassPathResource(path).getInputStream().readAllBytes(), StandardCharsets.UTF_8);
    }

    @Test
    void testAfterPropertiesSet() throws Exception {

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

    @Test
    void testCreateEdgesDefForQuiz() {

        Quiz existingQuiz = new Quiz();

        Question q1 = new Question(
                1, "test?", QuestionType.SINGLE_CHOICE, 10, false, 123,
                Arrays.asList(new QuestionOption(1, "test", false, new Question(), new ArrayList<Answer>()), new QuestionOption(2, "test2", true, new Question(), new ArrayList<Answer>())),
                Arrays.asList(new QuestionLogic()),
                Arrays.asList(new Answer()),
                Arrays.asList(new Quiz()),
                Arrays.asList(new Survey())
        );
        Question q2 = new Question(
                2, "test?", QuestionType.SINGLE_CHOICE, 10, false, 123,
                Arrays.asList(new QuestionOption(1, "test", false, new Question(), new ArrayList<Answer>()), new QuestionOption(2, "test2", true, new Question(), new ArrayList<Answer>())),
                Arrays.asList(new QuestionLogic()),
                Arrays.asList(new Answer()),
                Arrays.asList(new Quiz()),
                Arrays.asList(new Survey()
                ));

        existingQuiz.setQuestions(Arrays.asList(q1,q2));

        graphDTO = new GraphDTO();
        graphDTO.setQuiz(existingQuiz);
        graphDTO.setIsVerticalAlignment(true);

        when(velocityEngine.evaluate(any(VelocityContext.class), any(StringWriter.class), eq("TEMPLATE"), anyString())).thenReturn(true);

        String result = graphService.createEdgesDef(graphDTO);

        assertNotNull(result);

    }

    @Test
    void createQuestionsDefForQuiz() {
        Quiz existingQuiz = new Quiz();
        existingQuiz.setQuestions(Arrays.asList(
                new Question(
                        1, "test?", QuestionType.SINGLE_CHOICE, 10, false, 123,
                        Arrays.asList(new QuestionOption(), new QuestionOption()),
                        Arrays.asList(new QuestionLogic()),
                        Arrays.asList(new Answer()),
                        Arrays.asList(new Quiz()),
                        Arrays.asList(new Survey())
                ),
                new Question(
                        2, "test?", QuestionType.SINGLE_CHOICE, 10, false, 123,
                        Arrays.asList(new QuestionOption(), new QuestionOption()),
                        Arrays.asList(new QuestionLogic()),
                        Arrays.asList(new Answer()),
                        Arrays.asList(new Quiz()),
                        Arrays.asList(new Survey()
                        ))
        ));


        graphDTO.setQuiz(existingQuiz);


        String result = graphService.createQuestionsDef(graphDTO);

        String expected = "digraph{}";

    }


    @Test
    void testGenerate() {

        graphService = spy(new GraphImpl(velocityEngine));

        doReturn("mockedQuestions").when(graphService).createQuestionsDef(any(GraphDTO.class));
        doReturn("mockedEdges").when(graphService).createEdgesDef(any(GraphDTO.class));

        String result = graphService.generate(graphDTO);

        assertEquals("", "");

        verify(graphService).createQuestionsDef(graphDTO);
        verify(graphService).createEdgesDef(graphDTO);
    }
}
