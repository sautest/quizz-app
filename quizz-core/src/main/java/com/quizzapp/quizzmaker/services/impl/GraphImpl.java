package com.quizzapp.quizzmaker.services.impl;

import com.quizzapp.quizzmaker.dto.GraphDTO;
import com.quizzapp.quizzmaker.persistence.entities.Question;
import com.quizzapp.quizzmaker.persistence.entities.QuestionLogic;
import com.quizzapp.quizzmaker.persistence.entities.QuestionOption;
import com.quizzapp.quizzmaker.persistence.models.LogicActionType;
import com.quizzapp.quizzmaker.persistence.models.LogicConditionType;
import com.quizzapp.quizzmaker.services.GraphService;
import com.quizzapp.quizzmaker.utils.GraphUtils;
import lombok.RequiredArgsConstructor;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GraphImpl implements GraphService, InitializingBean {
    private static final String VELOCITY_LOG_TAG = "TEMPLATE";
    private final VelocityEngine velocityEngine;


    private String graphTemplate;
    private String questionTemplate;
    private String edgeTemplate;
    private String startNodeTemplate;
    private String endNodeTemplate;


    @Override
    public String generate(GraphDTO graphDTO) {

        String questionsDef = createQuestionsDef(graphDTO);
        String edgesDef = createEdgesDef(graphDTO);

        VelocityContext context = new VelocityContext();

        context.put(GraphUtils.Keys.HORIZONTAL_ALIGNMENT, !graphDTO.getIsVerticalAlignment());
        context.put(GraphUtils.Keys.QUESTION_DEF, questionsDef);
        context.put(GraphUtils.Keys.EDGE_DEF, edgesDef);

        //System.out.println(processTemplate(context,graphTemplate));
        return processTemplate(context, graphTemplate);
    }


    private String createQuestionsDef(GraphDTO graphDTO) {
        List<Question> questions = new ArrayList<>();


        if (graphDTO.getQuiz() != null) {
            questions = graphDTO.getQuiz().getQuestions();
        } else {
            questions = graphDTO.getSurvey().getQuestions();
        }

        String[] questionNodes = new String[questions.size() + 2];

        for (int i = 0; i < questions.size(); i++) {
            Question question = questions.get(i);

            VelocityContext context = new VelocityContext();

            if (graphDTO.getQuiz() == null) {
                context.put(GraphUtils.Keys.IS_SURVEY_EDGE, true);
            }

            context.put(GraphUtils.Keys.NODE_ID, i);
            context.put(GraphUtils.Keys.QUESTION_ID, question.getId());
            context.put(GraphUtils.Keys.QUESTION_TITLE, question.getText());
            context.put(GraphUtils.Keys.QUESTION_OPTIONS, question.getOptions());

            questionNodes[i] = processTemplate(context, questionTemplate);

        }

        VelocityContext context = new VelocityContext();
        context.put(GraphUtils.Keys.NODE_ID, questions.size());
        questionNodes[questions.size()] = processTemplate(context, startNodeTemplate);

        context.put(GraphUtils.Keys.NODE_ID, questions.size() + 1);
        questionNodes[questions.size() + 1] = processTemplate(context, endNodeTemplate);

        return String.join("\n", questionNodes);

    }

    private String createEdgesDef(GraphDTO graphDTO) {

        List<Question> questions = new ArrayList<>();
        boolean isQuiz = true;

        if (graphDTO.getQuiz() != null) {
            questions = graphDTO.getQuiz().getQuestions();
        } else {
            isQuiz = false;
            questions = graphDTO.getSurvey().getQuestions();
        }


        int totalLogicEdges = 0;
        List<QuestionLogic> allQuestionsLogic = new ArrayList<>();
        for (Question question : questions) {
            allQuestionsLogic.addAll(question.getLogic());
            totalLogicEdges += question.getLogic().size();


        }

        String[] edgesNodes = new String[questions.size() + totalLogicEdges + 1];

        for (int i = 0; i < questions.size() - 1; i++) {

            VelocityContext context = new VelocityContext();

            context.put(GraphUtils.Keys.SOURCE_NODE, i);

            context.put(GraphUtils.Keys.TARGET_NODE, i + 1);

            context.put(GraphUtils.Keys.IS_DEFAULT_EDGE, true);

            edgesNodes[i] = processTemplate(context, edgeTemplate);

        }

        int step = 0;
        for (int i = 0; i < questions.size() - 1; i++) {


            for (int ii = 0; ii < questions.get(i).getLogic().size(); ii++) {
                QuestionOption option = findObjectById(questions.get(i).getOptions(), questions.get(i).getLogic().get(ii).getConditionOptionId());

                VelocityContext context = new VelocityContext();


                context.put(GraphUtils.Keys.SOURCE_NODE, i);
                if (questions.get(i).getLogic().get(ii).getActionType() == LogicActionType.END_QUESTIONING) {
                    context.put(GraphUtils.Keys.TARGET_NODE, questions.size() + 1);
                } else {
                    context.put(GraphUtils.Keys.TARGET_NODE, getPositionById(questions, questions.get(i).getLogic().get(ii).getActionOptionId()));

                }


                if (questions.get(i).getLogic().get(ii).getConditionType() == LogicConditionType.ALWAYS) {
                    context.put(GraphUtils.Keys.IS_ALWAYS_EDGE, true);
                } else if (isQuiz) {
                    if (option.getCorrect()) {
                        context.put(GraphUtils.Keys.IS_CORRECT_EDGE, true);
                    } else {
                        context.put(GraphUtils.Keys.IS_INCORRECT_EDGE, true);
                    }
                    context.put(GraphUtils.Keys.EDGE_LABEL, option.getText());

                } else {

                    context.put(GraphUtils.Keys.IS_SURVEY_EDGE, true);
                    context.put(GraphUtils.Keys.EDGE_LABEL, option.getText());
                }


                edgesNodes[questions.size() - 1 + step] = processTemplate(context, edgeTemplate);
                step++;
            }


        }

        VelocityContext context = new VelocityContext();
        context.put(GraphUtils.Keys.SOURCE_NODE, questions.size());
        context.put(GraphUtils.Keys.TARGET_NODE, 0);
        context.put(GraphUtils.Keys.IS_DEFAULT_EDGE, true);
        edgesNodes[questions.size() + step - 1] = processTemplate(context, edgeTemplate);

        context.put(GraphUtils.Keys.SOURCE_NODE, questions.size() - 1);
        context.put(GraphUtils.Keys.TARGET_NODE, questions.size() + 1);
        context.put(GraphUtils.Keys.IS_DEFAULT_EDGE, true);
        edgesNodes[questions.size() + step] = processTemplate(context, edgeTemplate);


        return String.join("\n", edgesNodes);

    }

    private static int getPositionById(List<Question> list, int id) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId() == id) {
                return i;
            }
        }
        return -1;
    }

    private static QuestionOption findObjectById(List<QuestionOption> list, int id) {
        for (QuestionOption obj : list) {
            if (obj.getId() == id) {
                return obj;
            }
        }
        return null;
    }


    private String processTemplate(VelocityContext context, String template) {
        StringWriter writer = new StringWriter();
        velocityEngine.evaluate(context, writer, VELOCITY_LOG_TAG, template);
        return writer.toString();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        graphTemplate = new String(new ClassPathResource("templates/graph/graph.vm").getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        questionTemplate = new String(new ClassPathResource("templates/graph/question.vm").getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        edgeTemplate = new String(new ClassPathResource("templates/graph/edge.vm").getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        startNodeTemplate = new String(new ClassPathResource("templates/graph/startNode.vm").getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        endNodeTemplate = new String(new ClassPathResource("templates/graph/endNode.vm").getInputStream().readAllBytes(), StandardCharsets.UTF_8);
    }


}
