package es.udc.irlab.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URLEncoder;

public class GenerateUrlTest {

	public static void main(String[] args) {
		try {

			if (args.length != 3)
				System.exit(0);

			String url = args[0];
			String dirName = args[1];
			File file = new File(dirName);
			File fileout = new File(args[2]);
			FileInputStream fis = null;
			fis = new FileInputStream(file);
			FileOutputStream fout = new FileOutputStream(fileout);
			
			// Here BufferedInputStream is added for fast reading.
			 BufferedReader d
	          = new BufferedReader(new InputStreamReader(fis));

			BufferedWriter dout= new BufferedWriter(new OutputStreamWriter(fout));

			// dis.available() returns 0 if the file does not have more lines.
			while (d.ready()) {

				// this statement reads the line from the file and print it to
				// the console.
				dout.write(url+URLEncoder.encode(d.readLine().split(":")[1],"UTF-8"));
				dout.write("\n");
			}

			// dispose all the resources after using them.
			d.close();
			dout.close();
			fis.close();
			fout.close();
			
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
