package com.example.ResumeAnalyzerPro_Final.util;

import org.apache.tika.Tika;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
public class TikaParser {

    public String parse(InputStream stream){

        try{

            Tika tika=new Tika();

            return tika.parseToString(stream);

        }catch(Exception e){

            return "";

        }

    }

}
