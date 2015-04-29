/**   
 * @Title: CreateAction.java 
 * @Package com.tx.extension.tools.create 
 * @author chenxiaojia  
 * @date 2014-7-20 上午9:37:35 
 * @version V1.0   
 */
package sto.common.create;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Arrays;
import java.util.Date;

import org.junit.Test;

import Acme.JPM.Encoders.PpmEncoder;

/**
 * @ClassName: CreateAction
 * @Description:
 * @author chenxiaojia
 * @date 2014-7-20 上午9:37:35
 * 
 */
public class CreateAction {
	private static String FunName = "Role";
	private static String ppackage = "account";

	@Test
	public void create_All(){createAll();}
	@Test
	public void create_JSP(){init();createJSP();}
	@Test
	public void create_Action(){init();createAction();}
	@Test
	public void create_Dao(){init();createDao();}
	@Test
	public void create_Service(){init();createService();}
	@Test
	public void create_Unit(){init();createUnitTest();}
	@Test
	public void create_Entity(){init();createEntity();}
	


	public static void init() {
		String str = CreateAction.class.getResource("").toString();
		String srcPath = str.substring(6, str.indexOf("WebRoot"));
		CurrentPath = str.substring(6, str.length());
		BasePath = srcPath + BasePath;
		//JSPPath = srcPath + JSPPath;
		UnitTeestBasePath = srcPath + UnitTeestBasePath;
	}

	public static void createAll() {
		init();
		createEntity();
		createDao();
		createService();
		createAction();
		createJSP();
		createUnitTest();
	}

	public static void createJSP() {
		File file = createFile(JSPPath);
		if (Fileexists) {
			System.out.println("JSP File already exist ,skip!!!");
			Fileexists = false;
			return;
		}
		writerFile(file, "jsp.txt");
	}

	public static void createAction() {
		File file = createFile(WebPackage);
		if (Fileexists) {
			System.out.println("Action File already exist ,skip!!!");
			Fileexists = false;
			return;
		}
		writerFile(file, "action.txt");
	}

	public static void createDao() {
		File file = createFile(DaoPackage);
		if (Fileexists) {
			System.out.println("Dao File already exist ,skip!!!");
			Fileexists = false;
			return;
		}
		writerFile(file, "dao.txt");
	}

	public static void createService() {
		File file = createFile(ServicePackage);
		if (Fileexists) {
			System.out.println("Service File already exist ,skip!!!");
			Fileexists = false;
			return;
		}
		writerFile(file, "service.txt");
	}

	public static void createEntity() {
		File file = createFile(EntityPackage);
		if (Fileexists) {
			System.out.println("POJO already exist ,skip!!!");
			Fileexists = false;
			return;
		}
		writerFile(file, "entity.txt");
		System.out.println("POJO  OK ...!!!");
	}

	public static void createUnitTest() {
		File file = createFile("UnitTest");
		if (Fileexists) {
			System.out.println("UnitTest File already exist ,skip!!!");
			Fileexists = false;
			return;
		}
		writerFile(file1, "daoUnitTest.txt");
		writerFile(file2, "serviceUnitTest.txt");
		System.out.println("UnitTest  OK ...!!!");
	}

	public static String CastFirstToLow() {
		return FunName.substring(0, 1).toLowerCase()
				+ FunName.substring(1, FunName.length());
	}

	/**
	 * @Description:
	 * @param @return 设定文件
	 * @return File 返回类型
	 * @throws
	 */
	public static File createFile(String str) {
		String dirPath = "";
		if (str.equals(JSPPath))
			dirPath = checkPath(JSPPath);
		else
			dirPath = checkPath(BasePath);
		// 创建目录
		String unitTestDirPath1 = checkPath(UnitTeestBasePath);
		String unitTestDirPath2 = checkPath(UnitTeestBasePath);

		if (str.equals(JSPPath)) {

		} else {
			dirPath += str.replaceAll("\\.", "\\" + File.separator);
		}
		unitTestDirPath1 += DaoPackage.replaceAll("\\.", "\\" + File.separator);
		unitTestDirPath2 += ServicePackage.replaceAll("\\.", "\\"
				+ File.separator);

		String filePath = "";
		if (str.equals(JSPPath)) {
			filePath = checkPath(dirPath) + CastFirstToLow();
		} else {
			filePath = checkPath(dirPath) + FunName;
		}
		String unitFilePath1 = checkPath(unitTestDirPath1) + FunName+DaoSuffix
				+ "Test.java";
		String unitFilePath2 = checkPath(unitTestDirPath2) + FunName+ServiceSuffix
				+ "Test.java";

		if (str.equals(ServicePackage)) {
			filePath += ServiceSuffix;
		} else if (str.equals(WebPackage)) {
			filePath += WebSuffix;
		} else if (str.equals(DaoPackage)) {
			filePath += DaoSuffix;
		} else if (str.equals(JSPPath)) {

		}
		if (str.equals("UnitTest")) {
			createDir(unitTestDirPath1);
			createDir(unitTestDirPath2);
			file1 = crateFile(unitFilePath1);
			file2 = crateFile(unitFilePath2);
			return null;
		}
		if (str.equals(JSPPath)) {
			filePath += ".jsp";
		} else {
			filePath += ".java";
		}
		createDir(dirPath);
		File file = crateFile(filePath);
		return file;
	}

	/**
	 * @Description:
	 * @param @param filePath
	 * @param @return 设定文件
	 * @return File 返回类型
	 * @throws
	 */
	public static File crateFile(String filePath) {
		File file = new File(filePath);
		if (file.exists()) {
			System.out.println("注意：" + file.getName() + " already exist!!");
			Fileexists = true;
		}
		try {
			if (file.createNewFile()) {
				System.out.println("成功:文件" + file.getName() + "创建成功");
			} else {
				System.out.println("ERROR:文件" + file.getName() + "创建失败");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return file;
	}

	/**
	 * @Description:
	 * @param @param file
	 * @param @param fileName 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public static void writerFile(File file, String fileName) {
		try {
			Reader r = new FileReader(new File(CurrentPath + fileName));
			System.out.println(CurrentPath +fileName);
			StringBuffer sb = new StringBuffer();
			char b[] = new char[1];
			while (r.read(b) != -1) {
				sb.append(b);
				// Arrays.fill(b, (char)0);
			}
			r.close();

			String packg = "";
			String className = FunName;
			if ("action.txt".equals(fileName)) {
				packg = WebPackage;
				className += WebSuffix;
			} else if ("entity.txt".equals(fileName)) {
				packg = EntityPackage;
			} else if ("service.txt".equals(fileName)) {
				packg = ServicePackage;
				className += ServiceSuffix;
			} else if ("dao.txt".equals(fileName)) {
				packg = DaoPackage;
				className += DaoSuffix;
			} else if ("daoUnitTest.txt".equals(fileName)) {
				packg = DaoPackage;
				className += (DaoSuffix+"Test");
			} else if ("serviceUnitTest.txt".equals(fileName)) {
				packg = ServicePackage;
				className += (ServiceSuffix+"Test");
			}

			String str = sb.toString();
			str = str.replaceAll("--date", new Date().toLocaleString());
			str = str.replaceAll("--author", System.getProperty("user.name"));
			str = str.replaceAll("--visitName", CastFirstToLow());
			str = str.replaceAll("--className", className);
			str = str.replaceAll("--package", packg);

			str = str.replaceAll("--enitytFullName", EntityPackage + "."+ FunName);
			str = str.replaceAll("--entityClass", FunName);

			str = str.replaceAll("--serviceClassName", CastFirstToLow() + ServiceSuffix);
			str = str.replaceAll("--serviceClass", FunName + ServiceSuffix);
			str = str.replaceAll("--serviceFullName", ServicePackage + "."+ FunName + ServiceSuffix);

			str = str.replaceAll("--daoClassName", CastFirstToLow() + DaoSuffix);
			str = str.replaceAll("--daoClass", FunName + DaoSuffix);
			str = str.replaceAll("--daoFullName", DaoPackage + "." + FunName+ DaoSuffix);
			str = str.replaceAll("--ppackage", ppackage);
			Writer writer = new FileWriter(file);
			writer.append(str);
			writer.flush();
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @Description:
	 * @param @param dirPath 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public static void createDir(String dirPath) {
		File dir = new File(dirPath);
		if (!dir.exists()) {
			dir.mkdirs();
		}
	}

	public static String checkPath(String str) {
		String sep = File.separator;
		if (str.endsWith(sep))
			return str;
		else
			return str + sep;
	}

	// 类的src路径
	private static String BasePath = "src";
	// 单元测试src路径
	private static String UnitTeestBasePath = "test";

	// 类后缀
	private static String WebSuffix = "Action";
	private static String ServiceSuffix = "Service";
	private static String DaoSuffix = "Dao";
	// TestUnit
	private static File file1;
	private static File file2;

	private static String CurrentPath = "";
	private static boolean Fileexists = false;
	// ---------------------------------------------------------------------------------
	private static String JSPPath = "WebRoot\\WEB-INF\\sto_jsp\\" + ppackage;
	private static String WebPackage = "sto.web." + ppackage;
	private static String EntityPackage = "sto.model." + ppackage;
	private static String ServicePackage = "sto.service." + ppackage;
	private static String DaoPackage = "sto.dao." + ppackage;
}
