package es.udc.irlab.utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class ProcessABLog {

	public static void main(String[] args) {
		try {
			FileInputStream file = new FileInputStream(args[0]);
			InputStreamReader input = new InputStreamReader(file);
			BufferedReader br = new BufferedReader(input);
			String line="";
			while (br.ready()) { // while I can read .
				line= br.readLine();
				if (line.contains("Document Length:        "))
				{
					
					String[]parts = line.split("Document Length:        ");
					String[]size = parts[1].split("bytes");
					System.out.println(Integer.parseInt(size[0].trim()));
				}
			}
			br.close();
			input.close();
			file.close();
		}catch (Exception e) {
			e.printStackTrace(System.out);
		}
	}
}
