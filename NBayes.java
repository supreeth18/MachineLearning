package hw2;


	import java.io.BufferedReader;
	import java.io.File;
	import java.io.FileInputStream;
	import java.io.FileNotFoundException;
	import java.io.IOException;
	import java.io.InputStreamReader;
	import java.util.ArrayList;
	import java.util.Iterator;
	import java.util.TreeSet;

	public class NBayes {

		static Integer totalSpamCount=0,totalHamCount=0,totalCount=0,category=0;
		static double probability[];
		static ArrayList<String> list = new ArrayList<String>();
		static String[] tHam,tspam;
		static String hamDataForTest,spamDataForTest,hamDataForTrain,spamDataForTrain;
		

		public  static double hamTest(Double[][] priorProbability)
				throws FileNotFoundException, IOException {
			File file = new File(hamDataForTest);
			File[] arrayOfFiles = file.listFiles();
			tHam=new String[arrayOfFiles.length];
			StringBuilder builder=new StringBuilder(); 
			int probabilityOfCount =0;
			for (int i = 0; i < arrayOfFiles.length; i++) {
				builder=new StringBuilder();
				FileInputStream input =new FileInputStream(new File(arrayOfFiles[i].toString()));
				BufferedReader reader=new BufferedReader(new InputStreamReader(new FileInputStream(new File(arrayOfFiles[i].toString()))));
				String readInput =reader.readLine();
				while (readInput!=null) {
					builder.append(readInput);
					readInput=reader.readLine();
				}
				String eVariable =builder.toString().replaceAll("[^a-zA-Z]"," ");
				eVariable=builder.toString().replaceAll("\\s+", " ");
				double arrayCategory[]=new double[category];
				for (int j = 0; j < category; j++) {
					arrayCategory[j]=(double)(-1*Math.log10(probability[j])/Math.log(2));
					String[] str=eVariable.split(" ");
					for (int k = 0; k < str.length; k++) {
						String var =str[k];
						int poutput =list.indexOf(var);
						if(poutput!=-1){
							arrayCategory[j]+=(double)(-1*Math.log10(priorProbability[poutput][j])/Math.log(2));
						}
					}
				}
				if(arrayCategory[0]<arrayCategory[1]){
					tHam[i]="HAM";
					probabilityOfCount++;
				}
				else{
					tHam[i]="SPAM";
				}
			}
			double countPriorProbability=(double)probabilityOfCount/(double)arrayOfFiles.length;
			return countPriorProbability;
		}

		public  static double spamTest(Double[][] priorProbability)
				throws FileNotFoundException, IOException {
			File fileham = new File(spamDataForTest);
			File[] arrayOfFiles = fileham.listFiles();
			tspam=new String[arrayOfFiles.length];
			int probabilityOfCount=0;
			StringBuilder builder=new StringBuilder(); 
			for (int i = 0; i < arrayOfFiles.length; i++) {
				builder=new StringBuilder();
				FileInputStream inputStream=new FileInputStream(new File(arrayOfFiles[i].toString()));
				BufferedReader reader=new BufferedReader(new InputStreamReader(new FileInputStream(new File(arrayOfFiles[i].toString()))));
				String s=reader.readLine();
				while (s!=null) {
					builder.append(s);
					s=reader.readLine();
				}
				String temp=builder.toString().replaceAll("[^a-zA-Z]"," ");
				temp=builder.toString().replaceAll("\\s+", " ");
				double arrayTotal[]=new double[category];
				for (int j = 0; j < category; j++) {
					arrayTotal[j]=(double)(-1*Math.log(probability[j])/Math.log(2));
					String[] tempArray=temp.split(" ");
					for (int k = 0; k < tempArray.length; k++) {

						String t=tempArray[k];
						int value=list.indexOf(t);
						if(value!=-1){
							arrayTotal[j]+=(double)(-1*Math.log(priorProbability[value][j])/Math.log(2));
						}	
					}
				}
				if(arrayTotal[0]<arrayTotal[1]){
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
		public  static Double[][] basicNBayes() throws Exception{
			ArrayList<String[]> words = extractFeatures();
			category=words.size();
			String[] hamWords=words.get(0);
			String[] spamWords=words.get(1);
			TreeSet<String> set =new TreeSet<String>();
			for (int i = 0; i < hamWords.length; i++) {
				hamWords[i]=hamWords[i].replaceAll("[^a-zA-Z]"," ");
				hamWords[i]=hamWords[i].replaceAll("\\s+", " ");
				String temp[]=hamWords[i].split(" ");
				for (int j = 0; j < temp.length; j++) {
					set.add(temp[j]);
				}
			}
			for (int i = 0; i < spamWords.length; i++) {
				spamWords[i]=spamWords[i].replaceAll("[^a-zA-Z]"," ");
				spamWords[i]=spamWords[i].replaceAll("\\s+", " ");

				String temp[]=spamWords[i].split(" ");
				for (int j = 0; j < temp.length; j++) {
					set.add(temp[j]);
				}
			}
			totalSpamCount=spamWords.length;
			totalHamCount=hamWords.length;
			totalCount=totalSpamCount+totalHamCount;
			String[] str=new String[words.size()];
			probability=new double[words.size()];
			StringBuilder textualCount=new StringBuilder();

			Double[][] probabilisticCountOfWords=new Double[set.size()][words.size()]; 
			for (int i = 0; i < words.size(); i++) {
				Integer wordCount=words.get(i).length;
				textualCount=new StringBuilder();
				probability[i]=(double)wordCount/(double)totalCount;
				if(i==0){
					File fileCountOfEachFolder = new File(hamDataForTrain);
					TreeSet<String> hamSet=new TreeSet<String>();
					File[] hamlistOfFiles = fileCountOfEachFolder.listFiles();
					StringBuilder builder=new StringBuilder(); 
					for (int j = 0; j < hamlistOfFiles.length; j++) {
						FileInputStream inputStream=new FileInputStream(new File(hamlistOfFiles[j].toString()));
						BufferedReader readerC=new BufferedReader(new InputStreamReader(new FileInputStream(new File(hamlistOfFiles[j].toString()))));
						String str1=readerC.readLine();
						while (str1!=null) {
							builder.append(str1);
							str1=readerC.readLine();
						}
					}
					str[i]=builder.toString().replaceAll("[^a-zA-Z]"," ");
					str[i]=str[i].replaceAll("\\s+", " ");
					String[] temp=str[i].split(" ");
					for (int j = 0; j < temp.length; j++) {
						boolean b=hamSet.add(temp[j]);
						if(b==true||b==false){
							textualCount.append(temp[j]+" ");
						}
					}
					System.out.println("Hamset count:"+hamSet.size());
				}
				else if(i==1){
					File folder = new File(spamDataForTrain);
					File[] spamlistOfFiles = folder.listFiles();
					StringBuilder builder=new StringBuilder(); 
					for (int j = 0; j < spamlistOfFiles.length; j++) {
						FileInputStream iStream=new FileInputStream(new File(spamlistOfFiles[j].toString()));
						BufferedReader rStream=new BufferedReader(new InputStreamReader(new FileInputStream(new File(spamlistOfFiles[j].toString()))));
						String str1=rStream.readLine();
						while (str1!=null) {
							builder.append(str1);
							str1=rStream.readLine();
						}
					}
					str[i]=builder.toString().replaceAll("[^a-zA-Z]"," ");
					str[i]=str[i].replaceAll("\\s+", " ");
					TreeSet<String> spamSet=new TreeSet<String>();
					String[] temp=str[i].split(" ");
					for (int j = 0; j < temp.length; j++) {
						boolean b=spamSet.add(temp[j]);
						if(b==true||b==false){
							textualCount.append(temp[j]+" ");
						}
					}
					System.out.println("Spamset count:"+spamSet.size());
				}
				Iterator<String> iterator=set.iterator();
				Integer[] count=new Integer[set.size()];
				int j=-1;
				while (iterator.hasNext()) {
					j++;
					String nextWordsFetch=iterator.next();
					list.add(nextWordsFetch);				
					count[j]=textualCount.toString().split(nextWordsFetch).length-1;
				}
				iterator=set.iterator();
				j=0;
				int sum=0;
				for (int j2 = 0; j2 < count.length; j2++) {
					sum+=count[j2];
				}
				sum=sum+1;
				while (iterator.hasNext()) {
					probabilisticCountOfWords[j][i]= ((double)(count[j]+1)/(double)(sum+count.length));
					iterator.next();
					j++;
				}

			}
			return probabilisticCountOfWords;
		}
		private static ArrayList<String[]> extractFeatures() throws Exception{
			File hamData = new File(hamDataForTrain);
			File[] arrayOfFilesForHam = hamData.listFiles();
			StringBuilder builder=new StringBuilder(); 
			String[] totalFileCount=new String[arrayOfFilesForHam.length];
			for (int i = 0; i < arrayOfFilesForHam.length; i++) {
				builder=new StringBuilder();
				FileInputStream input=new FileInputStream(new File(arrayOfFilesForHam[i].toString()));
				BufferedReader readerStream=new BufferedReader(new InputStreamReader(new FileInputStream(new File(arrayOfFilesForHam[i].toString()))));
				String str=readerStream.readLine();
				while (str!=null) {
					builder.append(str);
					str=readerStream.readLine();
				}
				totalFileCount[i]=builder.toString();
			}

			File spamfolder = new File(spamDataForTrain);
			File[] arrayOfSpamFiles = spamfolder.listFiles();
			String[] spamDataListFilesGenerated=new String[arrayOfSpamFiles.length];
			for (int i = 0; i < arrayOfSpamFiles.length; i++) {
				builder=new StringBuilder();
				FileInputStream en=new FileInputStream(new File(arrayOfSpamFiles[i].toString()));
				BufferedReader reader =new BufferedReader(new InputStreamReader(new FileInputStream(new File(arrayOfSpamFiles[i].toString()))));
				String output =reader.readLine();
				while (output!=null) {
					builder.append(output);
					output=reader.readLine();
				}
				spamDataListFilesGenerated[i]=builder.toString();
			}


			ArrayList<String[]> list=new ArrayList<String[]>();
			list.add(totalFileCount);
			list.add(spamDataListFilesGenerated);
			return list;
		}

	}

