package org.nuaa.b730401.plcompiler.util;

import org.nuaa.b730401.plcompiler.entity.JudgeEntity;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @Author: ToMax
 * @Description:
 * @Date: Created in 2019/1/9 10:51
 */
public class JudgeUtil {
    /**
     * 测评文件路径
     */
    public static final String JUDGE_FILE_PATH = "C:/mydata/plcompiler/judge";

    public static List<String> getInput(int id, String mode) throws IOException {
        FileInputStream inputStream = new FileInputStream(JUDGE_FILE_PATH + "/" + id + "." + mode);
        Scanner scanner = new Scanner(inputStream);

        List<String> inputList = new ArrayList<>();
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            if (line.trim().length() > 0) {
                inputList.add(line);
            }
        }

        inputStream.close();
        scanner.close();
        return inputList;
    }
}
