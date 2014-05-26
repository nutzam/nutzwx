package net.wendal.nutzwx.service;

import org.apache.lucene.index.IndexWriter;

public class ContentIndexService {

	protected String indexPath;
	
	protected IndexWriter indexWriter;

	public ContentIndexService(String indexPath) {
		this.indexPath = indexPath;
	}
	
	
}
