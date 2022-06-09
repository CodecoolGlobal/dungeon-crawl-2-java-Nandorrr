package com.codecool.dungeoncrawl.logic.util;

import java.io.*;
import java.nio.charset.StandardCharsets;


public class WriteToFiles {
    public String fileCreater(String filename) throws IOException {
        //new File(filePath);
        String path = new File("src").getAbsolutePath() + "/main/resources/savedMaps";
        String filePath = path + filename + ".txt";

        return filePath;

    }

    public void  writeInTheFile (String map, String filePath) throws IOException {
        try{
        Writer fileWriter = new OutputStreamWriter(new FileOutputStream(filePath), StandardCharsets.UTF_8);
        fileWriter.write(map);
        fileWriter.close();
        System.out.println("Map write to txt was successful");
        } catch (IOException e){
            System.out.println("Problem with file writing");
            throw new IOException();
        }

    }

}
