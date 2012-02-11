package es.udc.irlab.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;

import es.udc.irlab.facade.WrapperFacadeSolj;

public class TrecRead {

	/**
	 * @param args
	 *            Without indexing strategy
	 */
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		try {

			String dirName = args[0];
			File fileDir = new File(dirName);
			File[] files = fileDir.listFiles();

			Collection list = new ArrayList();
			String docName = null;
			String docNumber = null;
			StringBuilder docHTML = null;
			String filename = null;
			boolean inside = true;
//			Long tinitial = null;
//			Long tfinish = null;
			int numDocuments = 0;
			WrapperFacadeSolj facade = new WrapperFacadeSolj(); // CommonsHttpSolrServer
																// server
			for (int i = 0; i < files.length; i++) {
//				tinitial = System.currentTimeMillis();
				numDocuments = 0;
				if (i == 0 && args[0].endsWith("wt01")){
					facade.reset(); // delete all documents in SOLR server.
					System.out.println("reseteado");					
				}
				filename = files[i].getAbsolutePath();
				FileInputStream file = new FileInputStream(filename);
				InputStreamReader input = new InputStreamReader(file);
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
								// while don't  find doc tag
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
						doc1.addField("number", docNumber, 1.0f);
						doc1.addField("html", docHTML, 1.0f);
						list.add(doc1);
						numDocuments++;
					}

				}
				// add list of documents to solr.
				facade.addDocument(list); 
				input.close();
				file.close();
				br.close();
//				tfinish = System.currentTimeMillis();
//				System.out.println("Time readTrec v2  of file "
//						+ (tfinish - tinitial) + " ms  for " + numDocuments
//						+ " documents in " + filename);
			}
			facade.commit(); // commit and optimizate in solr.

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();

		} catch (SolrServerException e) {
			e.printStackTrace();
		}

	}
}
