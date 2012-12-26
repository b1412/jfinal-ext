package com.jfinal.ext.plugin.tablebind;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.jfinal.core.Controller;
import com.jfinal.ext.kit.BeanKit;
import com.jfinal.log.Logger;

public class ClassSearcher {
	
	protected final static Logger logger = Logger.getLogger(ClassSearcher.class);
	static URL classPathUrl = ClassSearcher.class.getResource("/");
	static String lib = new File(classPathUrl.getFile()).getParent() + "/lib/";

	/**
	 * 递归查找文件
	 * 
	 * @param baseDirName
	 *            查找的文件夹路径
	 * @param targetFileName
	 *            需要查找的文件名
	 */
	private static List<String> findFiles(String baseDirName,String targetFileName) {
		/**
		 * 算法简述： 从某个给定的需查找的文件夹出发，搜索该文件夹的所有子文件夹及文件，
		 * 若为文件，则进行匹配，匹配成功则加入结果集，若为子文件夹，则进队列。 队列不空，重复上述操作，队列为空，程序结束，返回结果。
		 */
		List<String> classFiles = new ArrayList<String>();
		String tempName = null;
		// 判断目录是否存在
		File baseDir = new File(baseDirName);
		if (!baseDir.exists() || !baseDir.isDirectory()) {
			System.err.println("search error：" + baseDirName + "is not a dir！");
		} else {
			String[] filelist = baseDir.list();
			for (int i = 0; i < filelist.length; i++) {
				File readfile = new File(baseDirName + File.separator
						+ filelist[i]);
				if (!readfile.isDirectory()) {
					tempName = readfile.getName();
					if (ClassSearcher.wildcardMatch(targetFileName, tempName)) {
						String classname;
						String tem = readfile.getAbsoluteFile().toString().toString().replaceAll("\\\\", "/");
						classname = tem.substring(tem.indexOf("/classes")+ "/classes".length(), tem.indexOf(".class"));
						if (classname.startsWith("/")) {
							classname = classname.substring(classname.indexOf("/") + 1);
						}
						classname = className(classname, "/classes");
						classFiles.add(classname);
					}
				} else if (readfile.isDirectory()) {
					classFiles.addAll(findFiles(baseDirName + File.separator+ filelist[i], targetFileName));
				}
			}
		}
		return classFiles;
	}

	/**
	 * 查找jar包中的class
	 * 
	 * @param baseDirName
	 *            jar路径
	 * @param includeJars 
	 * @param jarFileURL
	 *            jar文件地址 <a href="http://my.oschina.net/u/556800"
	 *            target="_blank" rel="nofollow">@return</a>
	 */
	public static List<String> findjarFiles(String baseDirName, final List<String> includeJars) {
		List<String> classFiles = new ArrayList<String>();
		try {
			// 判断目录是否存在
			File baseDir = new File(baseDirName);
			if (!baseDir.exists() || !baseDir.isDirectory()) {
				System.out.println("file serach error：" + baseDirName + "is not a dir！");
			} else {
				String[] filelist = baseDir.list(new FilenameFilter() {
					@Override
					public boolean accept(File dir, String name) {
						return includeJars.contains(name);
					}
				});
				for (int i = 0; i < filelist.length; i++) {
					JarFile localJarFile = new JarFile(new File(baseDirName
							+ File.separator + filelist[i]));
					Enumeration<JarEntry> entries = localJarFile.entries();
					while (entries.hasMoreElements()) {
						JarEntry jarEntry = entries.nextElement();
						String entryName = jarEntry.getName();
						if (!jarEntry.isDirectory()&&entryName.endsWith(".class")) {
							String className = entryName.replaceAll("/", ".")
									.substring(0, entryName.length() - 6);
							classFiles.add(className);
						}
					}
					localJarFile.close();
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return classFiles;

	}

	@SuppressWarnings("rawtypes")
	public static List<Class> findInClasspathAndInJars(Class clazz, List<String> includeJars) {
		List<String> classFileList = findFiles(classPathUrl.getFile(),"*.class");
		classFileList.addAll(findjarFiles(lib,includeJars));
		return extraction(clazz,classFileList);
	}

	@SuppressWarnings("rawtypes")
	private static List<Class> extraction(Class clazz,List<String> classFileList) {
		List<Class> classList = new ArrayList<Class>();
		for (String classFile : classFileList) {
			try {
				Class classInFile = Class.forName(classFile);
				if (BeanKit.isSuperclass(classInFile,clazz)) {
					classList.add(classInFile);
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}

		return classList;
	}

	private static String className(String classFile, String pre) {
		String objStr = classFile.replaceAll("\\\\", "/");
		return objStr.replaceAll("/", ".");
	}

	/**
	 * 通配符匹配
	 * 
	 * @param pattern
	 *            通配符模式
	 * @param str
	 *            待匹配的字符串 <a href="http://my.oschina.net/u/556800"
	 *            target="_blank" rel="nofollow">@return</a>
	 *            匹配成功则返回true，否则返回false
	 */
	private static boolean wildcardMatch(String pattern, String str) {
		int patternLength = pattern.length();
		int strLength = str.length();
		int strIndex = 0;
		char ch;
		for (int patternIndex = 0; patternIndex < patternLength; patternIndex++) {
			ch = pattern.charAt(patternIndex);
			if (ch == '*') {
				// 通配符星号*表示可以匹配任意多个字符
				while (strIndex < strLength) {
					if (wildcardMatch(pattern.substring(patternIndex + 1),
							str.substring(strIndex))) {
						return true;
					}
					strIndex++;
				}
			} else if (ch == '?') {
				// 通配符问号?表示匹配任意一个字符
				strIndex++;
				if (strIndex > strLength) {
					// 表示str中已经没有字符匹配?了。
					return false;
				}
			} else {
				if ((strIndex >= strLength) || (ch != str.charAt(strIndex))) {
					return false;
				}
				strIndex++;
			}
		}
		return (strIndex == strLength);
	}

	public static List<Class> findInClasspath(Class<Controller> clazz) {
		List<String> classFileList = findFiles(classPathUrl.getFile(),"*.class");
		return extraction(clazz,classFileList);
	}
}