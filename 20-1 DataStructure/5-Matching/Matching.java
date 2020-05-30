import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.List;

public class Matching {
	public static int TABLE_SIZE = 100;
	public static int HASH_SIZE = 6;
	private static HashTable table;

	public static void main(String args[]) {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		while (true) {
			try {
				String input = br.readLine();
				if (input == null) { break; }
				if (input.compareTo("QUIT") == 0) { break; }
				if (input.length() < 2) { continue; }

				char cmd = input.charAt(0);
				String content = input.substring(2);

				switch (cmd) {
					case '<' :
						loadFile(content);
						break;
					case '@' :
						searchSlot(content);
						break;
					case '?' :
						searchPattern(content);
						break;
				}
			} catch (IOException e) {
				System.err.println("Wrong Input. ERROR : " + e.toString());
			}
		}
	}

	private static void loadFile(String content) throws IOException {
		List<String> lines = Files.readAllLines(Paths.get(content), Charset.forName("US-ASCII"));
		table = new HashTable();

		int line_num = 1;
		for(String l : lines) {
			for(int i = 0; i <= l.length()-HASH_SIZE; ++i) {
				String s = l.substring(i, i+HASH_SIZE);
				table.insert(s, line_num, i+1);
			}
			line_num++;
		}
//		BufferedReader br = new BufferedReader(new FileReader(content));
//
//		String line;
//		int line_num = 1;
//
//		while((line = br.readLine()) != null) {
//			for(int i = 0; i <= line.length()-HASH_SIZE; ++i) {
//				String s = line.substring(i, i+HASH_SIZE);
//				table.insert(s, line_num, i+1);
//			}
//			line_num++;
//		}
//		br.close();
	}

	private static void searchSlot(String content) {
		int slotNum = Integer.parseInt(content.trim());
		table.print(slotNum);
	}

	private static void searchPattern(String content) {
		table.search(content);
	}
}
