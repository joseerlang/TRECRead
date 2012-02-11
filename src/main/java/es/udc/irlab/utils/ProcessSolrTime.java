package es.udc.irlab.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class ProcessSolrTime {

	public static void main(String[] args) {
		try {

			String dirName = args[0];
			File fileDir = new File(dirName);
			File[] files = fileDir.listFiles();
			String readline = "";
			Integer counter = 0;
			Integer numReq = 0;
			Double median=0.0;
			HashMap<String, Double> mapValues = new HashMap<String, Double>();
			for (int i = 0; i < files.length; i++) {
				
				String filename = files[i].getAbsolutePath();
				FileInputStream file = new FileInputStream(filename);
				InputStreamReader input = new InputStreamReader(file);
				BufferedReader br = new BufferedReader(input);
				if (!files[i].getName().equals("results.log"))
				while (br.ready()) { // while I can read .
					readline = br.readLine();
					if (readline.contains("SpellCheckComponent")){
						br.readLine();
						br.readLine();
						if (counter>0)
						{
						median= Double.valueOf(counter/numReq);
						if (numReq<100)
						mapValues.put(files[i].getName().concat("0"+numReq.toString()), median);
						else
						mapValues.put(files[i].getName().concat(numReq.toString()), median);
						}
						counter = 0;
						numReq = 0;
						
					}else{
						Integer valueRequest = Integer.valueOf((readline
								.split("=")[1]).trim());
						counter += valueRequest;
						numReq++;
					}
				}
				input.close();
				file.close();
				br.close();
			}
			//WriteResults
			FileOutputStream outputFile= new FileOutputStream( dirName+"/results.log");
			OutputStreamWriter output = new OutputStreamWriter(outputFile);
			BufferedWriter out= new BufferedWriter(output);
			
			List<String> keys = new ArrayList<String>(mapValues.keySet());
			Collections.sort(keys);
			for (String key:keys){				
				Double value = mapValues.get(key);
				out.write(key+","+value+"\n");
			}
			out.close();
			output.close();
			outputFile.close();
			System.out.println("finish");

		} catch (FileNotFoundException e) {
			e.printStackTrace(System.out);
		} catch (IOException e) {
			e.printStackTrace(System.out);
		}

	}
}
