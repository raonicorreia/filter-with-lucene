package br.com.lucene.filter.controller;

import br.com.lucene.filter.service.LuceneService;
import br.com.lucene.filter.service.QuestionAnswerService;
import br.com.lucene.filter.service.model.QuestionAnswerDTO;
import org.apache.lucene.queryparser.classic.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/v1/questions-answers")
public class QuestionAnswerController {

    private QuestionAnswerService service;

    @Autowired
    public void setService(QuestionAnswerService service) {
        this.service = service;
    }

    @PostMapping
    public void create(@RequestBody List<QuestionAnswerDTO> list) {
        this.service.create(list);
    }

    @GetMapping
    public List<QuestionAnswerDTO> findAll() {
        return this.service.findAll();
    }

    @PostMapping("/fileIndex")
    public void fileIndex() throws IOException {
        LuceneService.clearDirectory();
        LuceneService.fileIndex(this.findAll());
    }

    @GetMapping("/search")
    public List<QuestionAnswerDTO> searchText(@RequestBody String text) throws IOException, ParseException {
        return LuceneService.searchText(text);
    }
}