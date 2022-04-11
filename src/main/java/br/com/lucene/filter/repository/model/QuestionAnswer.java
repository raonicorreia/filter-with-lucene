package br.com.lucene.filter.repository.model;

import br.com.lucene.filter.service.model.QuestionAnswerDTO;

import javax.persistence.*;

@Entity
public class QuestionAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(nullable = false, unique = true)
    private String question;

    @Column(nullable = false)
    private String answer;

    @Column(nullable = false)
    private QuestionAnswerType type;

    @Column(nullable = false)
    private boolean exclude;

    public QuestionAnswer() {
    }

    public QuestionAnswer(QuestionAnswerDTO dto) {
        this.id = dto.getId();
        this.question = dto.getQuestion();
        this.answer = dto.getAnswer();
        this.type = dto.getType();
        if (dto.getType() == null) {
            this.type = QuestionAnswerType.TEXT;
        } else {
            this.type = dto.getType();
        }
        this.exclude = dto.isExclude();
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
