package es.udc.irlab.facade;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Collection;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrInputDocument;

import es.udc.irlab.utils.Constants;

public class WrapperFacadeSolj {

	private SolrServer solrServer = null;

	public WrapperFacadeSolj() throws MalformedURLException {
		this.solrServer = new CommonsHttpSolrServer(Constants.URL);
	}

	public void reset() throws SolrServerException, IOException {
		this.solrServer.deleteByQuery("*:*");
	}

	public void addDocument(Collection<SolrInputDocument> c)
			throws SolrServerException, IOException {
		this.solrServer.add(c);
	}

	public void commit() throws SolrServerException, IOException {
		this.solrServer.optimize();
		this.solrServer.commit();		
	}
	
	public long numDocumentsIndexed() throws SolrServerException{
		SolrQuery query = new SolrQuery("*:*");
		QueryResponse response=   this.solrServer.query(query);
		return response.getResults().getNumFound();
	}
	
	
}
