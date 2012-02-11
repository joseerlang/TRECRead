package es.udc.irlab.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class FormatterData {

	public static void main(String[] args) {
		try {

			System.out.println("dale");
			if (args.length != 1)
				System.exit(0);

			String dirName = args[0];
			File fileDir = new File(dirName);
			File[] files = fileDir.listFiles();
			String filename = null;

			for (int i = 0; i < files.length; i++) {
				if (files[i].isFile()) {
					filename = files[i].getAbsolutePath();
					File fileout = new File(files[i].getParent() + "/out/"
							+ files[i].getName() + "_out.dat");
					FileInputStream fis = null;
					fis = new FileInputStream(filename);
					FileOutputStream fout = new FileOutputStream(fileout);

					// Here BufferedInputStream is added for fast reading.
					BufferedReader d = new BufferedReader(
							new InputStreamReader(fis));

					BufferedWriter dout = new BufferedWriter(
							new OutputStreamWriter(fout));

					// dis.available() returns 0 if the file does not have more
					// lines.
					while (d.ready()) {
						String dataHeader = d.readLine();
						if (!dataHeader.equals("END TEST")) {
							String dataResult = d.readLine();
							String[] parts = dataHeader.split(" ");
							String[] partsResult = dataResult.split(" ");
							if (parts[1].equals("success"))
								dout.write(partsResult[0] + "\t" + partsResult[1] + "\n");
							else
								dout.write(0 + "\t" + partsResult[1] + "\n");
						}
					}

					// dispose all the resources after using them.
					d.close();
					dout.close();
					fis.close();
					fout.close();
				}
			}

		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
	}
}
