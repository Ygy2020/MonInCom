package fileIO;

import java.io.*;

/**
 * This class contain all the methods 
 * to read and write into the source file.
 * 
 * @author Simone Mercatelli
 * @version 1.0
 */
public class MonInFileIO {
	private String fileName;
	
	public MonInFileIO(String fname){
		fileName = fname;		
	}
	
	/**
	 * read the file passed to the class 
	 * and return an array with all the component of the file 
	 * @throws IOException
	 * @return an String array
	 */
	public String[] readSrcFile() throws IOException {
		int ch;
		int countLine= 0;
		String str[];
		String err ="";
		
		str = new String[1];
		
		try(BufferedReader fileRdr = new BufferedReader(new FileReader(fileName)))
		{
			do {
				ch = fileRdr.read();
				if (ch == '#' ) {
					countLine++;
				}
			} while (ch != -1);
		} catch (IOException ex) {
			err = "Error";			
		}	
		try(BufferedReader fileRdr = new BufferedReader(new FileReader(fileName)))
		{
			str = new String[countLine];
			
			int i = 0;
			do {
				ch = fileRdr.read();
				if (ch == '#' ) {
					str[i] = fileRdr.readLine();
				}
				i++;
			}while (ch != -1);
			
		} catch (IOException ex) {
			err = "Error";			
		}		
		if (err != "") {
			str[0] = err;
		}
		return str;
	}
	
	/**
	 * Write on the file the modification from the form
	 * @param what the tag which must been modified
	 * @param value the new value of the tag
	 */
	public boolean WriteFile(String what, String value) throws IOException{
		
		try{
			File file = new File(fileName);
			
			File temp = File.createTempFile("temp", ".txt", file.getParentFile());	
			
			try (BufferedReader fileRdr = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
				 PrintWriter fw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(temp)))) {
								
				for (String str; (str = fileRdr.readLine()) != null;) {
										
					if (str.contains(what)){
						String strApp = str.substring(1);
						str = str.replace(strApp,what+"="+value);
					}
					
					str = str + "\r\n";
					fw.write(str);			
									
				}
			} catch (IOException exc){
				return false;			
			}
			file.delete();
			
			temp.renameTo(file);
		}catch (IOException exc) {
			return false;
		}
		return false;
	}	
}
