/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package call_java_rpc;

import java.util.*;
import helma.xmlrpc.*;
import java.io.IOException;
import java.lang.Object.*;
import org.apache.xerces.parsers.SAXParser;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.lang.*;
import java.sql.*;



/**
 *
 * @author parai
 */
public class Call_java_rpc {

    /**
     * @param args the command line arguments
     */

    public static void main(String[] args) throws XmlRpcException {
        // TODO code application logic here
        try
        {
            System.out.println("-----\nWelcome to the NLP system | Help -> parai.a@gmail.com\n------\n Before going ahead make sure you have started the Stanford RPC server\n and Python - Selenium RPC server.\n");
            System.out.println("Enter the port number on which Stanford RPC server is running:\t");            
            Scanner terminalInput = new Scanner(System.in);
            String the_port = terminalInput.nextLine();
            try{
                Integer.parseInt(the_port);
            }
            catch(Exception e){
                System.out.println("You have entered invalid number.\n");
                System.exit(0);
            }
            XmlRpc.setDriver("org.apache.xerces.parsers.SAXParser");
            XmlRpcClient server=new XmlRpcClient("http://localhost:" +the_port + "/RPC2");
            Vector params = new Vector();
            System.out.println("Enter the sentence(s)\n Like this: First sentence. Second sentence. ...\nEnter now:\n");
            the_port = terminalInput.nextLine();
            params.addElement(new String(the_port));

            Object result = server.execute("stan_nlp.parser_func", params);
            String parsed_res = ((String) result);
            String[] parsed = parsed_res.split("##");
                      
            String pattern = "([(+](\\w+|\\.+)\\s(\\w+|\\.+))";
            Pattern r = Pattern.compile(pattern);
            Matcher m;
            int start = 0;
            String simplified_parse = "";
            for(int i=0;i<parsed.length;++i){
                m = r.matcher(parsed[i]);
                start = 0;
                while(m.find(start)){
                    simplified_parse += m.group(0).substring(1) + ",";
                    start = m.start() + 1;
                }
                simplified_parse += "#";
                
            }
            
            // Now start the phrase fetching
            // done in JJ, NN/NNS - RB/RBR/RBS, JJ - JJ, JJ- NN/NNS, JJ - RB../, VB../ way
            String[] parse_array = simplified_parse.split("#");            
            ArrayList tags = new ArrayList();
            ArrayList words = new ArrayList();
            // Initialize
            tags.add("a");
            words.add("a");
            // Done initializing the arraylist
            String[] med;
            String final_list="";
            int j =0;
            for(int ind=0;ind <parse_array.length;++ind){
                med = parse_array[ind].split(",");                
                tags.clear();
                words.clear(); 
                for(j=0;j<med.length;++j){
                    tags.add(med[j].split(" ")[0].trim());
                    words.add(med[j].split(" ")[1].trim());
                }
                // Now pick up all the concerned phrases
                // First JJ, NN/NNS, anything
                for(j=0;j<tags.size()-1;++j){
                    if("JJ".equals(tags.get(j)) && "NN".equals(tags.get(j+1))){
                        final_list += words.get(j) + " " + words.get(j+1)+"#";
                    }
                    else if("JJ".equals(tags.get(j)) && "NNP".equals(tags.get(j+1))){
                        final_list += words.get(j) + " " + words.get(j+1);
                    }
                }
                // First RB/RBR.RBS, JJ, NOT NN/NNS
                for(j=0;j<tags.size()-2;++j){
                    if(("RB".equals(tags.get(j)) || "RBR".equals(tags.get(j)) || "RBS".equals(tags.get(j)))  && "JJ".equals(tags.get(j+1)) && (!("NN".equals(tags.get(j+2))) && !("NNS".equals(tags.get(j+2))))){
                        final_list += words.get(j) + " " + words.get(j+1)+"#";
                    }             
                }
                
                // First JJ, JJ, NOT NN/NNS
                for(j=0;j<tags.size()-2;++j){
                    if("JJ".equals(tags.get(j))  && "JJ".equals(tags.get(j+1)) && (!("NN".equals(tags.get(j+2))) && !("NNS".equals(tags.get(j+2))))){
                        final_list += words.get(j) + " " + words.get(j+1)+"#";
                    }             
                }
                
                // First NN/NNS, JJ, NOT NN/NNS
                for(j=0;j<tags.size()-2;++j){
                    if(("NN".equals(tags.get(j)) || "NNS".equals(tags.get(j)))  && "JJ".equals(tags.get(j+1)) && (!("NN".equals(tags.get(j+2))) && !("NNS".equals(tags.get(j+2))))){
                        final_list += words.get(j) + " " + words.get(j+1)+"#";
                    }             
                }
                
                // First RB/RBR/RBS, VB/VBD/VBG/VBN
                for(j=0;j<tags.size()-1;++j){
                    if(("RB".equals(tags.get(j)) || "RBR".equals(tags.get(j)) || "RBS".equals(tags.get(j)))  && ("VB".equals(tags.get(j+1)) || "VBN".equals(tags.get(j+1)) || "VBG".equals(tags.get(j+1)) || "VBD".equals(tags.get(j+1)))){
                        final_list += words.get(j) + " " + words.get(j+1)+"#";
                    }             
                }
                               
            }
            
            
            // Yoooo!! Time to calculate the sentiments
            // The probabilities of phrases are done via Google
            // Google access is done via Selenium via Python RPC server!
            // Lets hit the road!
            System.out.println("\nEnter the port number on which Python-Selenium server is running:\t");
            the_port = terminalInput.nextLine();
            try{
                Integer.parseInt(the_port);
            }
            catch(Exception e){
                System.out.println("\nInvalid port number. Exiting now!\n");
                System.exit(0);
            }
            System.out.println("\nInteracting with Google...This may take several minutes...\n");
            XmlRpcClient google_rpc=new XmlRpcClient("http://localhost:"+the_port+"/");
            params.clear();
            params.addElement(final_list);            
            // RPC initialization
            Vector params2 = new Vector();
            params2.addElement("test#");
            result = google_rpc.execute("get_count", params2);            
            // Initialization ended
            result = google_rpc.execute("get_count", params);
            String hits = ((String) result);
            //System.out.println(hits);
            //hits for "Excellent" = 352000000, "poor" = 197000000
            String[] nums = hits.split(",");
            ArrayList PMI = new ArrayList();
            int ind=0;
            Double d = 0.0;
            for(ind=0;ind<nums.length;ind=ind+2){                
                d = ((Double.parseDouble(hits.split(",")[ind])*197000000/(Double.parseDouble(hits.split(",")[ind+1])*352000000)));
                PMI.add(d.toString());
             }
            Double res = 0.0;
            for (ind=0;ind<PMI.size();++ind){
                res +=Math.log(Double.parseDouble(((String) PMI.get(ind))))/Math.log(2);
            }
            res = res/PMI.size();
            System.out.printf("Sentiment Value - Turney Algorithm -  is: %.2f\n\n", res);

            
            
            // Now run sentiment analysis from database - on Sentinet
            // DATABASE SENTINET ANALYSIS
            
            final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
            System.out.println("Enter database name:\t");
            the_port = terminalInput.nextLine();
            final String DB_URL = "jdbc:mysql://localhost:3306/"+ the_port;
            System.out.println("\nEnter database username:\t");
            the_port = terminalInput.nextLine();
            final String USER = the_port.trim();
            System.out.println("\nEnter database password:\t");
            the_port = terminalInput.nextLine();
            final String PASS = the_port.trim();
            Connection conn = null;
            
            try{
                conn =DriverManager.getConnection(DB_URL, USER, PASS);                
            }
            catch(Exception e){
                System.out.println("Sorry! Database connection could not be done!!");
                System.exit(0);
            }
           Statement stmt = conn.createStatement();
           String[] db_sent = final_list.split("#");
           String[] word_list;
           String my_word, ex_handler;
           Integer tot_res=0;
           Integer ii, jj, ii_stat, jj_stat, ii_pol, jj_pol;

           for(ind=0;ind<db_sent.length;++ind){
               word_list = db_sent[ind].split(" ");
               ii=0;jj=0;ii_stat=0;jj_stat=0;ii_pol=0;jj_pol=0;
               for(int inter=0;inter<2;++inter){
                   my_word = word_list[inter].trim();                   
                   // Check first database
                   try{
                       ResultSet rs = stmt.executeQuery("select * from sent_lexicon_1 where word='"+my_word+"'");
                       while(rs.next()){                           
                           if(inter==0 && "positive".equals(rs.getString("polarity"))){
                               ii_pol=1;
                               if("strongsubj".equals(rs.getString("type"))){
                                   ii_pol=2;
                               }
                           }
                           if(inter==0 && "negative".equals(rs.getString("polarity"))){
                               ii_pol=-1;
                               if("strongsubj".equals(rs.getString("type"))){
                                   ii_pol=-2;
                               }
                           }
                           
                           if(inter==1 && "positive".equals(rs.getString("polarity"))){
                               jj_pol=1;
                               if("strongsubj".equals(rs.getString("type"))){
                                   jj_pol=2;
                               }
                           }
                           if(inter==1 && "negative".equals(rs.getString("polarity"))){
                               jj_pol=-1;
                               if("strongsubj".equals(rs.getString("type"))){
                                   jj_pol=-2;
                               }
                           }

                       }
                       
                   }
                   catch(Exception e){
                       if(inter==0){
                           ii_stat=1;
                       }
                       else{
                           jj_stat=1;
                       }
                       ex_handler="";
                   }
                   // check second database
               }
               if(jj_pol!=0){
                   if(ii_pol*jj_pol>0){
                       tot_res+= ii_pol+ jj_pol;
                   }
               }
               
           }
           System.out.println("\nSentiment analysis from database! High positive means higher positive sentiment.");
           System.out.println("And lower negative number means larger negative sentiment.\n");
           System.out.printf("Seniment score from sentinet: %d\n\n", tot_res);
            
        }
        catch (Exception e) {
        System.err.println("JavaClient: " + e);
    }
    
}

}
