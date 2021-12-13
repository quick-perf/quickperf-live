/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2021-2021 the original author or authors.
 */
package testgen.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileContentVerifier {

    private final String pathOfFolderContainingGeneratedFiles;

    private final String referenceFolderRelativePath;

    public FileContentVerifier(String pathOfFolderContainingGeneratedFiles, String referenceFolderRelativePath) {
        this.pathOfFolderContainingGeneratedFiles = pathOfFolderContainingGeneratedFiles;
        this.referenceFolderRelativePath = referenceFolderRelativePath;
    }

    public void compareGeneratedFileWithReferenceFile(String fileName) {
        File folderContainingReferenceFiles = findFolderContainingReferenceFiles(referenceFolderRelativePath);
        File testFolder = new File(pathOfFolderContainingGeneratedFiles);
        File generatedSqlFile = new File(testFolder, fileName);
        File expectedSqlFile = new File(folderContainingReferenceFiles, fileName);

        if (fileName.endsWith(".json")){
            assertEqualJsonFiles(expectedSqlFile, generatedSqlFile);
        } else {
            assertThat(generatedSqlFile).hasSameTextualContentAs(expectedSqlFile);
        }
    }

    private File findFolderContainingReferenceFiles(String referenceFolderRelativePath) {
        Path targetDirectory = Paths.get("src");
        String srcFolderPath = targetDirectory.toFile().getAbsolutePath();
        return new File(srcFolderPath,
                     File.separator + "test"
                        + File.separator + "resources"
                        + File.separator + "testgen-expected"
                        + File.separator + referenceFolderRelativePath);
    }

    private void assertEqualJsonFiles(File expectedSqlFile, File generatedSqlFile) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            assertEquals(mapper.readTree(expectedSqlFile), mapper.readTree(generatedSqlFile));
        } catch (IOException e) {
            throw new UncheckedIOException(String.format("Unable to compare json files:\n<%s> \nand:\n<%s>", expectedSqlFile.getAbsoluteFile(), generatedSqlFile.getAbsoluteFile()), e);
        }
    }

}
