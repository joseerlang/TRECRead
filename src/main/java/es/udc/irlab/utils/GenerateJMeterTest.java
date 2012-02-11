package es.udc.irlab.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URLEncoder;

public class GenerateJMeterTest {

	public static void main(String[] args) {
		try {
			System.out.println(args.length);
			if (args.length != 6)
				System.exit(0);

			String host = args[0];
			String port = args[1];
			String rows= args[2];
			String dirName = args[3];
			File file = new File(dirName);
			File fileout = new File(args[4]);
			FileInputStream fis = null;
			fis = new FileInputStream(file);
			FileOutputStream fout = new FileOutputStream(fileout);
			Integer total= new Integer(args[5]);
			// Here BufferedInputStream is added for fast reading.
			 BufferedReader d
	          = new BufferedReader(new InputStreamReader(fis));

			BufferedWriter dout= new BufferedWriter(new OutputStreamWriter(fout));
			
			dout.write("<hashTree><ConfigTestElement guiclass=\"HttpDefaultsGui\" testclass=\"ConfigTestElement\" testname=\"Valores por Defecto para Petición HTTP\" enabled=\"true\">\n"+
            "<elementProp name=\"HTTPsampler.Arguments\" elementType=\"Arguments\" guiclass=\"HTTPArgumentsPanel\" testclass=\"Arguments\" testname=\"Variables definidas por el Usuario\" enabled=\"true\">\n"+
             " <collectionProp name=\"Arguments.arguments\"/>\n"+
            "</elementProp>\n"+
            "<stringProp name=\"HTTPSampler.domain\">"+host+"</stringProp>\n"+
            "<stringProp name=\"HTTPSampler.port\">"+port+"</stringProp>\n"+
            "<stringProp name=\"HTTPSampler.connect_timeout\"></stringProp>\n"+
            "<stringProp name=\"HTTPSampler.response_timeout\">10000</stringProp>\n"+
            "<stringProp name=\"HTTPSampler.protocol\"></stringProp>\n"+
            "<stringProp name=\"HTTPSampler.contentEncoding\"></stringProp>\n"+
            "<stringProp name=\"HTTPSampler.path\"></stringProp>\n"+
          "</ConfigTestElement>\n"+
          "<hashTree/>");
			String structure="";
			// dis.available() returns 0 if the file does not have more lines.
			int i=0;
			while (d.ready() && i<total) {
				i++;
				structure="<HTTPSampler guiclass=\"HttpTestSampleGui\" testclass=\"HTTPSampler\" testname=\"Query\" enabled=\"true\">\n"+
	            "<elementProp name=\"HTTPsampler.Arguments\" elementType=\"Arguments\" guiclass=\"HTTPArgumentsPanel\" testclass=\"Arguments\" testname=\"Variables definidas por el Usuario\" enabled=\"true\">\n"+
	            "  <collectionProp name=\"Arguments.arguments\"/>\n"+
	            "</elementProp>\n"+
	            "<stringProp name=\"HTTPSampler.domain\">"+host+"</stringProp>\n"+
	            "<stringProp name=\"HTTPSampler.port\">"+port+"</stringProp>\n"+
	            "<stringProp name=\"HTTPSampler.connect_timeout\"></stringProp>\n"+
	            "<stringProp name=\"HTTPSampler.response_timeout\"></stringProp>\n"+
	            "<stringProp name=\"HTTPSampler.protocol\"></stringProp>\n"+
	            "<stringProp name=\"HTTPSampler.contentEncoding\"></stringProp>\n"+
	            "<stringProp name=\"HTTPSampler.path\">/solr/select?rows="+rows+"&amp;start=0&amp;q=html:"+URLEncoder.encode(d.readLine().split(":")[1],"UTF-8")+"&amp;solrjs.widgetid=result&amp;wt=velocity&amp;v.template=result</stringProp>\n"+
	            "<stringProp name=\"HTTPSampler.method\">GET</stringProp>\n"+
	            "<boolProp name=\"HTTPSampler.follow_redirects\">false</boolProp>\n"+
	            "<boolProp name=\"HTTPSampler.auto_redirects\">true</boolProp>\n"+
	            "<boolProp name=\"HTTPSampler.use_keepalive\">false</boolProp>\n"+
	            "<boolProp name=\"HTTPSampler.DO_MULTIPART_POST\">false</boolProp>\n"+
	            "<stringProp name=\"HTTPSampler.FILE_NAME\"></stringProp>\n"+
	            "<stringProp name=\"HTTPSampler.FILE_FIELD\"></stringProp>\n"+
	            "<stringProp name=\"HTTPSampler.mimetype\"></stringProp>\n"+
	            "<boolProp name=\"HTTPSampler.monitor\">false</boolProp>\n"+
	            "<stringProp name=\"HTTPSampler.embedded_url_re\"></stringProp>\n"+
	          "</HTTPSampler>\n"+
	          "<hashTree/>";
				dout.write(structure);				
			}
			dout.write("</hashTree>");
			// dispose all the resources after using them.
			d.close();
			dout.close();
			fis.close();
			fout.close();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}