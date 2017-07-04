package com.sino.scaffold.utils;

import java.io.File;
import java.io.IOException;

import org.nutz.lang.Files;

/**
 * @author kerbores
 *
 */
public class Projects {
	public static void clear(String base) throws IOException {
		Files.cleanAllFolderInSubFolderes(Files.checkFile(base), ".settings");
		Files.cleanAllFolderInSubFolderes(Files.checkFile(base), "target");
		cleanAllFilesInSubFolderes(Files.checkFile(base), ".project");
		cleanAllFilesInSubFolderes(Files.checkFile(base), ".idea");
		cleanAllFilesInSubFolderes(Files.checkFile(base), ".classpath");
	}

	public static void cleanAllFilesInSubFolderes(File dir, String name) throws IOException {
		File[] files = dir.listFiles();
		if (files == null)
			return;
		for (File d : files) {
			if (d.isDirectory()) {
				cleanAllFilesInSubFolderes(d, name);
			} else {
				if (d.getName().equalsIgnoreCase(name))
					Files.deleteFile(d);
			}
		}
	}
}
