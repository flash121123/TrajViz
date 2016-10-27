package base;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import core.frame.Interval;
import edu.gmu.itr.Direction;

public class WriteFile {

	public String name;
	public String path;
	public int index = 0;
	public boolean health = true;

	public WriteFile(String name) {
		super();
		this.name = name;
		Path releaseFolder = Paths.get(name);
		if (Files.exists(releaseFolder)) {
			health = false;
			System.err.println("Error: The folder is existed");
		} else {
			boolean success = (new File("../" + name)).mkdirs();
			if (!success) {
				// Directory creation failed
				System.err.println("Error: Failed to Create Folder");
			}
		}
		path = "../" + name + "/";
	}

	public void write(ArrayList<Double> lon, ArrayList<Double> alt, List<? extends Interval> r) {
		if(!health)
			return;
		int i = 0;
		for (Interval x : r) {
			List<Double> t1 = alt.subList(x.getStart(), x.getEnd());
			List<Double> t2 = lon.subList(x.getStart(), x.getEnd());
			write(t1, t2, i);
			i++;
		}
		index++;
		return;
	}

	private void write(List<Double> t1, List<Double> t2, int inst_index) {
		// TODO Auto-generated method stub
		PrintWriter writer;
		try {
			writer = new PrintWriter(path + "moitf" + index + "_" + inst_index, "UTF-8");
			for (int i = 0; i < t1.size(); i++) {
				String line = t1.get(i) + "," + t2.get(i)+"\n";
				writer.write(line);
			}
			writer.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void write(ArrayList<Double> lon, ArrayList<Double> lat, Direction<Integer> x) {
		// TODO Auto-generated method stub
		PrintWriter writer;
		try {
			writer = new PrintWriter(path + "anomaly" + index, "UTF-8");
			List<Double> t1 = lat.subList(x.start, x.end);
			List<Double> t2 = lon.subList(x.start, x.end);
			for (int i = 0; i < t1.size(); i++) {
				String line = t1.get(i) + "," + t2.get(i)+"\n";
				writer.write(line);
			}
			writer.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		index++;
	}

}
