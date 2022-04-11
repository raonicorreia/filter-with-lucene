package br.com.lucene.filter.service.model;

import br.com.lucene.filter.repository.model.QuestionAnswer;
import br.com.lucene.filter.repository.model.QuestionAnswerType;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

public class QuestionAnswerDTO implements Serializable {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long id;

    private String question;

    private String answer;

    private QuestionAnswerType type;

    private boolean exclude;

    public QuestionAnswerDTO() {
    }

    public QuestionAnswerDTO(QuestionAnswer questionAnswer) {
        this.id = questionAnswer.getId();
        this.question = questionAnswer.getQuestion();
        this.answer = questionAnswer.getAnswer();
        this.type = questionAnswer.getType();
        this.exclude = questionAnswer.isExclude();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public QuestionAnswerType getType() {
        return type;
    }

    public void setType(QuestionAnswerType type) {
        this.type = type;
    }

    public boolean isExclude() {
        return exclude;
    }

    public void setExclude(boolean exclude) {
        this.exclude = exclude;
    }
}
