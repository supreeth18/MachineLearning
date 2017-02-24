package hw2;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;
public class NBayesFilteringStopWords {
	

	

		static Integer totalSpamCount=0,totalHamCount=0,totalCount=0,category=0;
		static double probability[];
		static ArrayList<String> list=new ArrayList<String>();
		static String[] tHam,tspam;
		static HashMap<String,String> stopText=new HashMap<String,String>();
		static String hamDataForTest,spamDataForTest,hamDataForTrain,spamDataForTrain,stopWordsFile;
		

		public static double hamTest(Double[][] condprob)
				throws FileNotFoundException, IOException {
			File file = new File(hamDataForTest);
			File[] arrayOfFiles = file.listFiles();
			tHam=new String[arrayOfFiles.length];
			StringBuilder builder=new StringBuilder(); 
			int probabilityOfCount=0;
			for (int i = 0; i < arrayOfFiles.length; i++) {
				builder=new StringBuilder();
				FileInputStream inputStream=new FileInputStream(new File(arrayOfFiles[i].toString()));
				BufferedReader readerOutput=new BufferedReader(new InputStreamReader(new FileInputStream(new File(arrayOfFiles[i].toString()))));
				String totalRes=readerOutput.readLine();
				while (totalRes!=null) {
					builder.append(totalRes);
					totalRes=readerOutput.readLine();
				}
				totalRes=builder.toString();
				totalRes=totalRes.replaceAll(" ' ","'");
				totalRes=totalRes.replaceAll("[^a-zA-Z']"," ");
				totalRes=totalRes.replaceAll("''"," ");
				totalRes=totalRes.replaceAll("\\s+", " ");
				totalRes=totalRes.trim();

				double arrayOfResults[]=new double[category];
				for (int j = 0; j < category; j++) {
					arrayOfResults[j]=(double)(-1*Math.log10(probability[j])/Math.log(2));
					String[] temp=totalRes.split(" ");
					for (int k = 0; k < temp.length; k++) {
						if(stopText.containsKey(temp[k]))
							continue;
						String t=temp[k];
						int index=list.indexOf(t);
						if(index!=-1){
							arrayOfResults[j]+=(double)(-1*Math.log10(condprob[index][j])/Math.log(2));
						}
					}
				}
				if(arrayOfResults[0]<arrayOfResults[1]){
					tHam[i]="HAM";
					probabilityOfCount++;
				}
				else{
					tHam[i]="SPAM";
				}
			}
			double result=(double)probabilityOfCount/(double)arrayOfFiles.length;
			return result;
		}

		public  static double spamTest(Double[][] condprob)
				throws FileNotFoundException, IOException {
			File file = new File(spamDataForTest);
			File[] arrayOfFiles = file.listFiles();
			tspam=new String[arrayOfFiles.length];
			int probabilityOfCount=0;
			StringBuilder builder=new StringBuilder(); 
			for (int i = 0; i < arrayOfFiles.length; i++) {
				builder=new StringBuilder();
				FileInputStream inputStream=new FileInputStream(new File(arrayOfFiles[i].toString()));
				BufferedReader reader=new BufferedReader(new InputStreamReader(new FileInputStream(new File(arrayOfFiles[i].toString()))));
				String arrayOfDataResults=reader.readLine();
				while (arrayOfDataResults!=null) {
					builder.append(arrayOfDataResults);
					arrayOfDataResults=reader.readLine();
				}
				arrayOfDataResults=builder.toString();
				arrayOfDataResults=arrayOfDataResults.replaceAll(" ' ","'");
				arrayOfDataResults=arrayOfDataResults.replaceAll("[^a-zA-Z']"," ");
				arrayOfDataResults=arrayOfDataResults.replaceAll("''"," ");
				arrayOfDataResults=arrayOfDataResults.replaceAll("\\s+", " ");
				arrayOfDataResults=arrayOfDataResults.trim();
				double resultArray[]=new double[category];
				for (int j = 0; j < category; j++) {
					resultArray[j]=(double)(-1*Math.log(probability[j])/Math.log(2));
					String[] temp=arrayOfDataResults.split(" ");
					for (int k = 0; k < temp.length; k++) {
						if(stopText.containsKey(temp[k]))
							continue;
						String t=temp[k];
						int index=list.indexOf(t);
						if(index!=-1){
							resultArray[j]+=(double)(-1*Math.log(condprob[index][j])/Math.log(2));
						}	
					}
				}
				if(resultArray[0]<resultArray[1]){
					tspam[i]="HAM";
				}
				else{
					tspam[i]="SPAM";
					probabilityOfCount++;
				}
			}
			double result=(double)probabilityOfCount/(double)arrayOfFiles.length;
			return result;
		}
		public static Double[][] naiveBayesBasic() throws Exception{
			ArrayList<String[]> vocabulary=extractAllTheWords();
			category=vocabulary.size();
			String[] ham=vocabulary.get(0);
			String[] spam=vocabulary.get(1);
			TreeSet<String> set=new TreeSet<String>();
			for (int i = 0; i < ham.length; i++) {
				ham[i]=ham[i].replaceAll(" ' ","'");
				ham[i]=ham[i].replaceAll("[^a-zA-Z']"," ");
				ham[i]=ham[i].replaceAll("''"," ");
				ham[i]=ham[i].replaceAll("\\s+", " ");
				ham[i]=ham[i].trim();
				String temp[]=ham[i].split(" ");
				for (int j = 0; j < temp.length; j++) {
					if(stopText.containsKey(temp[j].toLowerCase())){
						continue;
					}
					else{
						set.add(temp[j].toLowerCase());
					}
				}
			}
			for (int i = 0; i < spam.length; i++) {
				spam[i]=spam[i].replaceAll(" ' ","'");
				spam[i]=spam[i].replaceAll("[^a-zA-Z']"," ");
				spam[i]=spam[i].replaceAll("''"," ");
				spam[i]=spam[i].replaceAll("\\s+", " ");
				spam[i]=spam[i].trim();

				String temp[]=spam[i].split(" ");
				for (int j = 0; j < temp.length; j++) {
					if(stopText.containsKey(temp[j].toLowerCase())){
						continue;
					}
					else{
						set.add(temp[j].toLowerCase());
					}
				}
			}
			totalSpamCount=spam.length;
			totalHamCount=ham.length;
			totalCount=totalSpamCount+totalHamCount;
			String[] str=new String[vocabulary.size()];
			probability=new double[vocabulary.size()];
			StringBuilder textc=new StringBuilder();

			Double[][] totalPossibleProb=new Double[set.size()][vocabulary.size()]; 
			for (int i = 0; i < vocabulary.size(); i++) {//for each class
				Integer Nc=vocabulary.get(i).length;
				textc=new StringBuilder();
				probability[i]=(double)Nc/(double)totalCount;//calculate prior value for this class
				if(i==0){//for ham,calculate textc
					File folder = new File(hamDataForTrain);
					TreeSet<String> hamSet=new TreeSet<String>();
					File[] hamlistOfFiles = folder.listFiles();
					StringBuilder builder=new StringBuilder(); 
					for (int j = 0; j < hamlistOfFiles.length; j++) {
						FileInputStream en=new FileInputStream(new File(hamlistOfFiles[j].toString()));
						BufferedReader read=new BufferedReader(new InputStreamReader(new FileInputStream(new File(hamlistOfFiles[j].toString()))));
						String str1=read.readLine();
						while (str1!=null) {
							builder.append(str1);
							str1=read.readLine();
						}
					}
					str[i]=builder.toString();
					str[i]=str[i].replaceAll(" ' ","'");
					str[i]=str[i].replaceAll("[^a-zA-Z']"," ");
					str[i]=str[i].replaceAll("''"," ");
					str[i]=str[i].replaceAll("\\s+", " ");
					str[i]=str[i].trim();
					String[] temp=str[i].split(" ");
					int k=0;
					for (int j = 0; j < temp.length; j++) {
						if(stopText.containsKey(temp[j].toLowerCase())){
							k++;
						}
						else{
							boolean b=hamSet.add(temp[j].toLowerCase());
							if(b==true||b==false){
								textc.append(temp[j]+" ");
							}
						}
					}
					System.out.println("Hamset count is:"+hamSet.size());
				}
				else if(i==1){
					File folder = new File(spamDataForTrain);
					File[] arrayOfSpamFiles = folder.listFiles();
					StringBuilder builder=new StringBuilder(); 
					for (int j = 0; j < arrayOfSpamFiles.length; j++) {
						FileInputStream en=new FileInputStream(new File(arrayOfSpamFiles[j].toString()));
						BufferedReader x=new BufferedReader(new InputStreamReader(new FileInputStream(new File(arrayOfSpamFiles[j].toString()))));
						String str1=x.readLine();
						while (str1!=null) {
							builder.append(str1);
							str1=x.readLine();
						}
					}
					str[i]=builder.toString();
					str[i]=str[i].replaceAll(" ' ","'");
					str[i]=str[i].replaceAll("[^a-zA-Z']"," ");
					str[i]=str[i].replaceAll("''"," ");
					str[i]=str[i].replaceAll("\\s+", " ");
					str[i]=str[i].trim();
					TreeSet<String> spamSet=new TreeSet<String>();
					String[] temp=str[i].split(" ");
					for (int j = 0; j < temp.length; j++) {
						if(stopText.containsKey(temp[j].toLowerCase()))
							continue;
						boolean b=spamSet.add(temp[j].toLowerCase());
						if(b==true||b==false){
							textc.append(temp[j]+" ");
						}
					}
					System.out.println("Spamset count:"+spamSet.size());
				}
				Iterator<String> iterator=set.iterator();
				Integer[] count=new Integer[set.size()];
				int j=-1;
				while (iterator.hasNext()) {
					j++;
					String itrText=iterator.next();
					list.add(itrText);
					count[j]=textc.toString().split(itrText).length-1;
				}
				iterator=set.iterator();
				j=0;
				int sum=0;
				for (int j2 = 0; j2 < count.length; j2++) {
					sum+=count[j2];
				}
				sum=sum+1;
				while (iterator.hasNext()) {
					totalPossibleProb[j][i]= ((double)(count[j]+1)/(double)(sum+count.length));
					iterator.next();
					j++;
				}

			}
			return totalPossibleProb;
		}
		private static ArrayList<String[]> extractAllTheWords() throws Exception{
			StringBuilder builder=new StringBuilder();
			File sDatafile = new File(stopWordsFile);
			builder=new StringBuilder();
			BufferedReader reader=new BufferedReader(new InputStreamReader(new FileInputStream(sDatafile)));
			String str=reader.readLine();
			while (str!=null) {
				builder.append(str);
				stopText.put(str, str);
				str=reader.readLine();
			}


			File hamfolder = new File(hamDataForTrain);
			File[] hamlistOfFiles = hamfolder.listFiles();
			builder=new StringBuilder(); 
			String[] dataSet=new String[hamlistOfFiles.length];
			for (int i = 0; i < hamlistOfFiles.length; i++) {
				builder=new StringBuilder();
				reader=new BufferedReader(new InputStreamReader(new FileInputStream(new File(hamlistOfFiles[i].toString()))));
				str=reader.readLine();
				while (str!=null) {
					builder.append(str);
					str=reader.readLine();
				}
				dataSet[i]=builder.toString();
			}

			File spamfolder = new File(spamDataForTrain);
			File[] spamlistOfFiles = spamfolder.listFiles();
			String[] resultArray=new String[spamlistOfFiles.length];
			for (int i = 0; i < spamlistOfFiles.length; i++) {
				builder=new StringBuilder();
				reader=new BufferedReader(new InputStreamReader(new FileInputStream(new File(spamlistOfFiles[i].toString()))));
				str=reader.readLine();
				while (str!=null) {
					builder.append(str);
					str=reader.readLine();
				}
				resultArray[i]=builder.toString();
			}
			ArrayList<String[]> listOfResultData=new ArrayList<String[]>();
			listOfResultData.add(dataSet);
			listOfResultData.add(resultArray);
			return listOfResultData;
		}

	}

