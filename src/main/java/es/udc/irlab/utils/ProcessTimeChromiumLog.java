package es.udc.irlab.utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;

public class ProcessTimeChromiumLog {

	public static void main(String[] args) {
		try {
			FileInputStream file = new FileInputStream(args[0]);
			InputStreamReader input = new InputStreamReader(file);
			BufferedReader br = new BufferedReader(input);
			String line="";
			 DecimalFormat twoDForm = new DecimalFormat("#.##");
			while (br.ready()) { // while I can read .
				line= br.readLine();
				if (line.contains("10 "))
				{
					
					String[]parts = line.split("10 ");
					
					System.out.println(twoDForm.format(Double.parseDouble(parts[1].trim())*100));
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
