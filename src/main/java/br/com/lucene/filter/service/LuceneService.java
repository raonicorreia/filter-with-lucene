package br.com.lucene.filter.service;

import br.com.lucene.filter.repository.model.QuestionAnswerType;
import br.com.lucene.filter.service.model.QuestionAnswerDTO;
import br.com.lucene.filter.util.Util;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.br.BrazilianAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class LuceneService {
    private static final String DIRECTORY_INDEX = "C:/temp/directory";

    private static final String DIRECTORY_FILES = "C:/temp/files/";

    private static final String CONST_QUESTION = "QUESTION";

    private static final String CONST_ANSWER = "ANSWER";

    private static final String CONST_TYPE= "TYPE";

    public static void clearDirectory() {
        File file = new File(DIRECTORY_INDEX);
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File fs : files) {
                fs.delete();
            }
        }
        System.out.println("=======================================");
        System.out.println("Indices removidos");
        System.out.println("=======================================");
    }

    public static void fileIndex(List<QuestionAnswerDTO> questionAnswers) throws IOException {
        Analyzer analyzer = new BrazilianAnalyzer();
        Directory directory = FSDirectory.open(Paths.get(DIRECTORY_INDEX));
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        IndexWriter indexWriter = new IndexWriter(directory, config);
        for (QuestionAnswerDTO dto : questionAnswers) {
            Document doc = new Document();
            if (dto.getType() == QuestionAnswerType.PDF) {
                doc.add(new TextField(CONST_QUESTION, pdfToText(dto.getAnswer()), Field.Store.YES));
            } else {
                doc.add(new TextField(CONST_QUESTION, dto.getQuestion(), Field.Store.YES));
            }
            doc.add(new TextField(CONST_TYPE, dto.getType().toString(), Field.Store.YES));
            doc.add(new TextField(CONST_ANSWER, dto.getAnswer(), Field.Store.YES));
            indexWriter.addDocument(doc);
        }
        indexWriter.close();
    }

    public static List<QuestionAnswerDTO> searchText(String text) throws IOException, ParseException {
        List<QuestionAnswerDTO> questionAnswerDTOS = new ArrayList<>();
        Analyzer analyzer = new BrazilianAnalyzer();
        Directory directory = FSDirectory
                .open(Paths.get(DIRECTORY_INDEX));
        DirectoryReader directoryReader = DirectoryReader.open(directory);
        IndexSearcher indexSearcher = new IndexSearcher(directoryReader);
        QueryParser queryParser = new QueryParser(CONST_QUESTION, analyzer);
        Query query = queryParser.parse(text);
        TopFieldDocs topFieldDocs = indexSearcher.search(query, 5, Sort.RELEVANCE, true);
        ScoreDoc[] hits = topFieldDocs.scoreDocs;
        for (int i = 0; i < hits.length; i++) {
            Document hitDoc = indexSearcher.doc(hits[i].doc);
            QuestionAnswerDTO dto = new QuestionAnswerDTO();
            dto.setQuestion(hitDoc.get(CONST_QUESTION));
            dto.setAnswer(hitDoc.get(CONST_ANSWER));
            dto.setType(QuestionAnswerType.valueOf(hitDoc.get(CONST_TYPE)));
            questionAnswerDTOS.add(dto);
        }
        directoryReader.close();
        directory.close();
        return questionAnswerDTOS;
    }

    private static String pdfToText(String path) throws IOException {
        File f = Util.getFile(path);
        PDFParser parser = new PDFParser(new RandomAccessFile(f, "r"));
        parser.parse();
        COSDocument cosDoc = parser.getDocument();
        PDFTextStripper pdfStripper = new PDFTextStripper();
        PDDocument pdDoc = new PDDocument(cosDoc);
        return pdfStripper.getText(pdDoc);
    }

}
