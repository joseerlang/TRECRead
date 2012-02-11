package es.udc.irlab.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.zip.GZIPInputStream;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;

import es.udc.irlab.facade.WrapperFacadeSolj;

public class TrecReadZip {

	/**
	 * @param args
	 *            Without indexing strategy
	 */
	public static void main(String[] args) {
		try {

			WrapperFacadeSolj facade = new WrapperFacadeSolj(); // CommonsHttpSolrServer
			// server
			
			String dirName = args[0];
			
			if (args.length>0 && args[0] != null && args[0].equals("clean")){
				facade.reset(); // delete all documents in SOLR server.
				System.out.println("reseteado");
				return ;
			}
			
			if (args.length>0 && args[0] != null && args[0].equals("commit")){
				facade.commit(); //
				System.out.println("commit");
				return ;
			}
			File fileDir = new File(dirName);
			File[] files = fileDir.listFiles();
			Arrays.sort(files);
			int total =  files.length;
			System.out.println("total "+total);
			int start=0;
			
			if (args.length>1 && args[1] != null && args[1].startsWith("total=")){
				total = Integer.parseInt(args[1].split("=")[1]);
				System.out.println("total directory to index "+total);
			}
			
			if (args.length>2 && args[2] != null && args[2].startsWith("start=")){
				start = Integer.parseInt(args[2].split("=")[1]);
				System.out.println("start index in "+start);
			}
			
			
			for (int i = start; i < total; i++) {
				if (files[i].isDirectory()){
					processDir(facade,files[i]);
					System.out.println("processed directory "+files[i]);
				}
				facade.commit();
			}
				
			//facade.commit(); // commit and optimizate in solr.

		} catch (Exception e) {
			e.printStackTrace(System.out);
		}

	}

	public static void processDir(WrapperFacadeSolj facade, File directory) {

		int numDocuments = 0;
		File[] filess = directory.listFiles();
		String filenames = null;
		Collection<SolrInputDocument> list = new ArrayList<SolrInputDocument>();
		String docName = null;
		String docNumber = null;
		StringBuilder docHTML = null;
		boolean inside = true;
		try {
			for (int j = 0; j < filess.length; j++) {
				numDocuments = 0;
				filenames = filess[j].getAbsolutePath();
				FileInputStream file = new FileInputStream(filenames);
				GZIPInputStream fileZip = new GZIPInputStream(file);

				InputStreamReader input = new InputStreamReader(fileZip);
				BufferedReader br = new BufferedReader(input);
				list.clear();

				while (br.ready()) { // while I can read .
					inside = true;
					if (br.readLine().startsWith("<DOC>")) {
						docName = br.readLine();
						// delete <docno> tag
						if (docName.startsWith("<DOCNO>"))
							docName = docName
									.substring(7, docName.length() - 8);
						docNumber = br.readLine();
						if (docNumber.startsWith("<DOCOLDNO>"))
							docNumber = docNumber.substring(10, docNumber
									.length() - 11); // item for docoldno
						while (!br.readLine().startsWith("</DOCHDR>")) {
						} // header lines

						docHTML = new StringBuilder(br.readLine());
						while (inside) {
							if (docHTML.length() > 10) {
								// while don't find doc tag
								if (docHTML.substring(docHTML.length() - 10,
										docHTML.length()).contains("</DOC>")) {
									inside = false;
								} else {
									docHTML.append(" ").append(br.readLine());
								}
							} else {
								docHTML.append(" ").append(br.readLine());
							}

						}
						SolrInputDocument doc1 = new SolrInputDocument();
						doc1.addField("id", docName, 1.0f);
						doc1.addField("header", "", 1.0f);
						doc1.addField("number", "", 1.0f);
						doc1.addField("html", docHTML, 1.0f);
						list.add(doc1);
						numDocuments++;
					}					
				}
				// add list of documents to solr.
				facade.addDocument(list);
				input.close();
				fileZip.close();
				file.close();
				br.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace(System.out);
		} catch (IOException e) {
			e.printStackTrace(System.out);
		} catch (SolrServerException e) {
			e.printStackTrace(System.out);
		}		
	}
}
