package Hardeng.Rest.config;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import Hardeng.Rest.Utilities.CsvObject;
import Hardeng.Rest.exceptions.InternalServerErrorException;
import Hardeng.Rest.exceptions.NoDataException;

public class CsvHttpMessageConverter extends AbstractHttpMessageConverter<Object>{
    private static final Logger log = LoggerFactory.getLogger(CsvHttpMessageConverter.class);
    public CsvHttpMessageConverter() {
        super(new MediaType("text", "csv"));
    }

    @Override
    protected boolean supports(Class<?> clazz) {
        return true;
    }
    
    private void writeList(List<Object> t, OutputStreamWriter stream) {
        
        HeaderColumnNameMappingStrategy<Object> strategy = new HeaderColumnNameMappingStrategy<>();
        if (t.isEmpty()) throw new NoDataException();
        strategy.setType(t.get(0).getClass());
        StatefulBeanToCsv<Object> beanToCsv = new StatefulBeanToCsvBuilder<>(stream)
         .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
         .withMappingStrategy(strategy)
         .build();
        try {
            beanToCsv.write(t);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void writeInternal(Object t, HttpOutputMessage outputMessage) 
     throws IOException, HttpMessageNotWritableException {

        OutputStreamWriter outputStream = new OutputStreamWriter(outputMessage.getBody());
        if (t instanceof List) writeList((List<Object>)t, outputStream);
        else if(t instanceof CsvObject) writeList(((CsvObject)t).getList(), outputStream);
        else throw new InternalServerErrorException();
        outputStream.close();
    }

    @Override
    protected Object readInternal(Class<? extends Object> clazz, HttpInputMessage inputMessage)
     throws IOException, HttpMessageNotReadableException {

        try {
            return new CsvToBeanBuilder<>(new InputStreamReader(inputMessage.getBody()))
                .withType(clazz.getClass()).build().parse();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
