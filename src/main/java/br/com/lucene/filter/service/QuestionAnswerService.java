package br.com.lucene.filter.service;

import br.com.lucene.filter.repository.model.QuestionAnswer;
import br.com.lucene.filter.repository.QuestionAnswerRepository;
import br.com.lucene.filter.service.model.QuestionAnswerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionAnswerService {

    private QuestionAnswerRepository repository;

    @Autowired
    public void setRepository(QuestionAnswerRepository repository) {
        this.repository = repository;
    }

    public void create(List<QuestionAnswerDTO> list) {
        this.repository.saveAll(list.stream().map(QuestionAnswer::new).collect(Collectors.toList()));
    }

    public List<QuestionAnswerDTO> findAll() {
       return this.repository.findAll()
                .stream()
                .map(QuestionAnswerDTO::new)
                .collect(Collectors.toList());
    }
}
