package es.udc.irlab.utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;

public class ProcessDocumentSizeTomcatLog {

	public static void main(String[] args) {
		try {
			FileInputStream file = new FileInputStream(args[0]);
			InputStreamReader input = new InputStreamReader(file);
			BufferedReader br = new BufferedReader(input);
			String line="";
			int limit=10;
			int count=0;
			double total=0;
			DecimalFormat twoDForm = new DecimalFormat("#.##");
			while (br.ready()) { // while I can read .
				line= br.readLine();
				if (line.contains("sizeResult="))
				{
					
					String[]parts = line.split("sizeResult=");
					count++;
					if (count==limit)
					{
						count=0;
						System.out.println(twoDForm.format(total));
						total=0;
					}
					else
					{
						total = Integer.parseInt(parts[1].trim());
					}
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
