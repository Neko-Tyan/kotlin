package org.jetbrains.jet.generators.builtins;

import com.intellij.openapi.util.io.FileUtil;
import com.intellij.testFramework.UsefulTestCase;
import com.intellij.util.Function;
import com.intellij.util.containers.ContainerUtil;
import org.jetbrains.jet.JetTestUtils;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public class BuiltInsSerializerTest extends UsefulTestCase {

    public void testBuiltIns() throws Exception {
        File actual = JetTestUtils.tmpDir("builtins");
        BuiltInsSerializer.serializeToDir(actual, null);

        File expected = new File(BuiltInsSerializer.DEST_DIR);

        List<File> actualFiles = getAllFiles(actual);
        List<File> expectedFiles = getAllFiles(expected);

        Set<String> actualNames = getFileNames(actualFiles);
        Set<String> expectedNames = getFileNames(expectedFiles);

        assertEquals("File name sets differ. Re-run BuiltInsSerializer", expectedNames, actualNames);

        for (File actualFile : actualFiles) {
            if (actualFile.isDirectory()) continue;
            String relativePath = FileUtil.getRelativePath(actual, actualFile);
            assert relativePath != null : "no relative path for " + actualFile;
            File expectedFile = new File(expected, relativePath);
            byte[] expectedBytes = FileUtil.loadFileBytes(expectedFile);
            byte[] actualBytes = FileUtil.loadFileBytes(actualFile);
            assertTrue("File contents differ for " + expectedFile + " and " + actualFile + ". Re-run BuiltInsSerializer",
                       Arrays.equals(expectedBytes, actualBytes));
        }
        System.out.println(actualFiles.size() + " files checked");
    }

    private static List<File> getAllFiles(File actual) {
        return FileUtil.findFilesByMask(Pattern.compile(".*"), actual);
    }

    private static Set<String> getFileNames(List<File> actualFiles) {
        return new HashSet<String>(ContainerUtil.map(actualFiles, new Function<File, String>() {
            @Override
            public String fun(File file) {
                return file.getName();
            }
        }));
    }

}