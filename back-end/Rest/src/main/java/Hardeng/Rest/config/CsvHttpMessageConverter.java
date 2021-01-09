package Hardeng.Rest.config;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

public class CsvHttpMessageConverter extends AbstractHttpMessageConverter<Object>{
    
    public CsvHttpMessageConverter() {
        super(new MediaType("text", "csv"));
    }

    @Override
    protected boolean supports(Class<?> clazz) {
        return true;
    }

    @Override
    protected void writeInternal(Object t, HttpOutputMessage outputMessage) 
     throws IOException, HttpMessageNotWritableException {
        
        // ColumnPositionMappingStrategy mappingStrategy = new ColumnPositionMappingStrategy();
        
        OutputStreamWriter outputStream = new OutputStreamWriter(outputMessage.getBody());
        StatefulBeanToCsv beanToCsv = new StatefulBeanToCsvBuilder(outputStream).build();
        try {
            beanToCsv.write(t);
            outputStream.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
